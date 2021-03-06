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

package org.limbo.flowjob.tracker.admin.service.worker;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.limbo.flowjob.tracker.commons.constants.enums.WorkerProtocol;
import org.limbo.flowjob.tracker.commons.constants.enums.WorkerStatus;
import org.limbo.flowjob.tracker.commons.dto.worker.WorkerExecutorRegisterDto;
import org.limbo.flowjob.tracker.commons.dto.worker.WorkerRegisterOptionDto;
import org.limbo.flowjob.tracker.commons.dto.worker.WorkerRegisterResult;
import org.limbo.flowjob.tracker.core.tracker.TrackerNode;
import org.limbo.flowjob.tracker.core.tracker.worker.HttpWorker;
import org.limbo.flowjob.tracker.core.tracker.worker.Worker;
import org.limbo.flowjob.tracker.core.tracker.worker.WorkerRepository;
import org.limbo.flowjob.tracker.core.tracker.worker.metric.WorkerAvailableResource;
import org.limbo.flowjob.tracker.core.tracker.worker.metric.WorkerExecutor;
import org.limbo.flowjob.tracker.core.tracker.worker.metric.WorkerMetric;
import org.limbo.flowjob.tracker.core.tracker.worker.metric.WorkerMetricRepository;
import org.limbo.flowjob.tracker.core.tracker.worker.statistics.WorkerStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ??????????????????worker????????????
 *
 * @author Brozen
 * @since 2021-06-03
 */
@Slf4j
@Service
public class WorkerRegisterService {

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerMetricRepository metricRepository;

    @Autowired
    private WorkerStatisticsRepository statisticsRepository;

    @Autowired
    private TrackerNode trackerNode;

    /**
     * worker??????
     * @param options ????????????
     * @return ????????????tracker????????????
     */
    @Transactional(rollbackFor = Throwable.class)
    public Mono<WorkerRegisterResult> register(WorkerRegisterOptionDto options) {

        // TODO ????????????

        // ?????? or ?????? worker
        Worker worker = workerRepository.getWorker(options.getId());
        if (worker == null) {

            worker = createNewWorker(options);
            workerRepository.addWorker(worker);

        } else {

            worker.setHost(options.getHost());
            worker.setPort(options.getPort());
            worker.setProtocol(options.getProtocol());
            worker.setStatus(WorkerStatus.RUNNING);
            workerRepository.updateWorker(worker);

        }

        // ??????metric
        WorkerMetric metric = createMetric(options);
        worker.updateMetric(metric);

        log.info("worker registered " + worker);

        // ??????tracker
        WorkerRegisterResult registerResult = new WorkerRegisterResult();
        registerResult.setWorkerId(worker.getWorkerId());
        registerResult.setTrackers(trackerNode.getNodes());
        return Mono.just(registerResult);
    }

    /**
     * ????????????worker??????????????????????????????worker
     * @param options ????????????
     * @return worker????????????
     */
    @Nonnull
    private Worker createNewWorker(WorkerRegisterOptionDto options) {
        // ???????????????HTTP?????????worker
        Worker worker;
        WorkerProtocol protocol = options.getProtocol();
        if (protocol == WorkerProtocol.HTTP) {
            worker = new HttpWorker(httpClient, workerRepository, metricRepository, statisticsRepository);
        } else {
            throw new UnsupportedOperationException("????????????worker?????????" + protocol);
        }

        worker.setWorkerId(options.getId());
        worker.setHost(options.getHost());
        worker.setPort(options.getPort());
        worker.setProtocol(protocol);
        worker.setStatus(WorkerStatus.RUNNING);

        return worker;
    }


    /**
     * ???????????????????????????worker????????????
     * @param options worker????????????
     * @return worker??????????????????
     */
    private WorkerMetric createMetric(WorkerRegisterOptionDto options) {
        WorkerMetric metric = new WorkerMetric();
        metric.setExecutors(convertWorkerExecutor(options));
        metric.setExecutingJobs(Lists.newArrayList()); // TODO ?????????????????????
        metric.setAvailableResource(WorkerAvailableResource.from(options.getAvailableResource()));
        return metric;
    }


    /**
     * {@link WorkerExecutorRegisterDto} => {@link WorkerExecutor} ???????????????????????????????????????id??????workerId
     */
    private List<WorkerExecutor> convertWorkerExecutor(WorkerRegisterOptionDto options) {
        List<WorkerExecutor> executors;
        if (CollectionUtils.isNotEmpty(options.getJobExecutors())) {
            executors = options.getJobExecutors().stream()
                    .map(this::convertWorkerExecutor)
                    .peek(exe -> exe.setWorkerId(options.getId()))
                    .collect(Collectors.toList());
        } else {
            executors = Lists.newArrayList();
        }

        return executors;
    }


    /**
     * {@link WorkerExecutorRegisterDto} => {@link WorkerExecutor}
     */
    private WorkerExecutor convertWorkerExecutor(WorkerExecutorRegisterDto dto) {
        WorkerExecutor executor = new WorkerExecutor();
        executor.setName(dto.getName());
        executor.setDescription(dto.getDescription());
        executor.setType(dto.getType());
        return executor;
    }

}
