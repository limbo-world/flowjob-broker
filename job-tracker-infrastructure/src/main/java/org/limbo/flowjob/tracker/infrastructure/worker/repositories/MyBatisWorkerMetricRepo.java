/*
 * Copyright 2020-2024 Limbo Team (https://github.com/limbo-world).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.limbo.flowjob.tracker.infrastructure.worker.repositories;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.limbo.flowjob.tracker.core.tracker.worker.metric.WorkerExecutor;
import org.limbo.flowjob.tracker.core.tracker.worker.metric.WorkerMetric;
import org.limbo.flowjob.tracker.core.tracker.worker.metric.WorkerMetricRepository;
import org.limbo.flowjob.tracker.dao.mybatis.WorkerExecutorMapper;
import org.limbo.flowjob.tracker.dao.mybatis.WorkerMetricMapper;
import org.limbo.flowjob.tracker.dao.po.WorkerExecutorPO;
import org.limbo.flowjob.tracker.dao.po.WorkerMetricPO;
import org.limbo.flowjob.tracker.infrastructure.worker.converters.WorkerExecutorPoConverter;
import org.limbo.flowjob.tracker.infrastructure.worker.converters.WorkerMetricPoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Brozen
 * @since 2021-06-03
 */
@Repository
public class MyBatisWorkerMetricRepo implements WorkerMetricRepository {

    @Autowired
    private WorkerMetricMapper mapper;

    @Autowired
    private WorkerMetricPoConverter converter;

    @Autowired
    private WorkerExecutorMapper workerExecutorMapper;

    @Autowired
    private WorkerExecutorPoConverter workerExecutorPoConverter;

    /**
     * {@inheritDoc}
     *
     * @param metric worker????????????
     */
    @Override
    public void updateMetric(WorkerMetric metric) {
        WorkerMetricPO po = converter.convert(metric);
        Objects.requireNonNull(po);

        // ???????????????worker??????
        int effected = mapper.update(po, Wrappers.<WorkerMetricPO>lambdaUpdate()
                .eq(WorkerMetricPO::getWorkerId, po.getWorkerId()));
        if (effected <= 0) {

            effected = mapper.insertIgnore(po);
            if (effected != 1) {
                throw new IllegalStateException(String.format("Update worker error, effected %s rows", effected));
            }
        }

        // ??????worker?????????
        workerExecutorMapper.delete(Wrappers.<WorkerExecutorPO>lambdaQuery()
                .eq(WorkerExecutorPO::getWorkerId, metric.getWorkerId()));
        List<WorkerExecutor> executors = metric.getExecutors();
        if (CollectionUtils.isNotEmpty(executors)) {
            workerExecutorMapper.batchInsert(executors.stream()
                    .map(workerExecutor -> {
                        if (StringUtils.isBlank(workerExecutor.getDescription())) {
                            workerExecutor.setDescription(StringUtils.EMPTY);
                        }
                        return workerExecutorPoConverter.convert(workerExecutor);
                    })
                    .collect(Collectors.toList()));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param workerId workerId
     * @return
     */
    @Override
    public WorkerMetric getMetric(String workerId) {
        // ??????metric
        WorkerMetricPO metricPo = mapper.selectById(workerId);
        WorkerMetric metric = converter.reverse().convert(metricPo);
        if (metric == null) {
            return null;
        }

        // ???????????????
        List<WorkerExecutor> executors;
        List<WorkerExecutorPO> executorPos = workerExecutorMapper.findByWorker(workerId);
        if (CollectionUtils.isNotEmpty(executorPos)) {
            executors = executorPos.stream()
                    .map(po -> workerExecutorPoConverter.reverse().convert(po))
                    .collect(Collectors.toList());
        } else {
            executors = Lists.newArrayList();
        }
        metric.setExecutors(executors);

        return metric;
    }

}
