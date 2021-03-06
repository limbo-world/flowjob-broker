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

package org.limbo.flowjob.tracker.core.dispatcher.strategies;

import org.apache.commons.collections4.CollectionUtils;
import org.limbo.flowjob.tracker.commons.exceptions.JobWorkerException;
import org.limbo.flowjob.tracker.core.job.context.JobInstance;
import org.limbo.flowjob.tracker.core.tracker.worker.Worker;
import org.limbo.flowjob.tracker.core.tracker.worker.metric.WorkerExecutor;
import org.limbo.flowjob.tracker.core.tracker.worker.metric.WorkerMetric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Brozen
 * @since 2021-05-27
 */
public abstract class AbstractJobDispatcher implements JobDispatcher {

    /**
     * {@inheritDoc}
     *
     * @param instance 待下发的作业实例
     * @param workers  可用的worker
     * @param callback 作业执行回调
     */
    @Override
    public void dispatch(JobInstance instance, Collection<Worker> workers, BiConsumer<JobInstance, Worker> callback) {
        if (CollectionUtils.isEmpty(workers)) {
            throw new JobWorkerException(instance.getJobId(), null, "No worker available!");
        }

        List<Worker> availableWorkers = new ArrayList<>();
        // todo 比较 cpu 和 内存 以及下发给对应的执行器 worker没找到怎么处理
        for (Worker worker : workers) {
            WorkerMetric metric = worker.getMetric();
            if (metric == null || CollectionUtils.isEmpty(metric.getExecutors())) {
                continue;
            }
            // 判断是否有对应的执行器
            for (WorkerExecutor executor : metric.getExecutors()) {
                if (executor.getType() == instance.getExecutorOption().getType()
                        && executor.getName().equals(instance.getExecutorOption().getName())) {
                    availableWorkers.add(worker);
                }
            }
        }

        if (CollectionUtils.isEmpty(availableWorkers)) {
            throw new JobWorkerException(instance.getJobId(), null, "No worker available!");
        }

        // todo worker 拒绝后的重试
        Worker worker = selectWorker(instance, availableWorkers);
        if (worker == null) {
            throw new JobWorkerException(instance.getJobId(), null, "No worker available!");
        }
        callback.accept(instance, worker);

    }

    /**
     * 选择一个worker进行作业下发
     *
     * @param context 待下发的作业上下文
     * @param workers 待下发上下文可用的worker
     * @return 需要下发作业上下文的worker
     */
    protected abstract Worker selectWorker(JobInstance context, Collection<Worker> workers);

}
