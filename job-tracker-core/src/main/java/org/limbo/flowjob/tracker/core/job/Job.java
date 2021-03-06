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

package org.limbo.flowjob.tracker.core.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.limbo.flowjob.tracker.commons.constants.enums.JobScheduleStatus;
import org.limbo.flowjob.tracker.commons.constants.enums.ScheduleType;
import org.limbo.flowjob.tracker.commons.utils.strategies.StrategyFactory;
import org.limbo.flowjob.tracker.core.job.context.JobInstance;
import org.limbo.flowjob.tracker.core.plan.Plan;
import org.limbo.flowjob.tracker.core.plan.ScheduleOption;
import org.limbo.flowjob.tracker.core.schedule.Schedulable;
import org.limbo.flowjob.tracker.core.schedule.ScheduleCalculator;
import org.limbo.flowjob.tracker.core.schedule.executor.Executor;
import org.limbo.utils.UUIDUtils;
import org.limbo.utils.verifies.Verifies;

import java.util.List;
import java.util.Set;

/**
 * 作业的抽象。主要定义了作业领域的的行为方法，属性的访问操作在{@link Job}轻量级领域对象中。
 *
 * todo
 * 允许失败的任务，失败后继续执行下面节点
 * 任务失败后处理情况，1. 整个plan失败 2. 执行另一个A分支（成功的话执行B分支）
 *
 * @author Brozen
 * @since 2021-05-14
 */
@Getter
@Setter
@ToString
public class Job {

    /**
     * 作业ID
     */
    private String jobId;

    /**
     * 作业描述
     */
    private String description;

    private Set<String> childrenIds;

    /**
     * 作业分发配置参数
     */
    private DispatchOption dispatchOption;

    /**
     * 作业执行器配置参数
     */
    private ExecutorOption executorOption;

    /**
     * 生成新的作业实例
     * @return 实例
     */
    public JobInstance newInstance(String planId, Long planInstanceId, Integer version, JobScheduleStatus state) {
        JobInstance instance = new JobInstance();
        instance.setPlanId(planId);
        instance.setPlanInstanceId(planInstanceId);
        instance.setJobId(jobId);
        instance.setVersion(version);
        instance.setState(state);
        instance.setDispatchOption(dispatchOption);
        instance.setExecutorOption(executorOption);
        instance.setJobAttributes(null); // todo
        return instance;
    }

}
