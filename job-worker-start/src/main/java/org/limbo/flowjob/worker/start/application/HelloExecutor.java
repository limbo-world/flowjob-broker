package org.limbo.flowjob.worker.start.application;

import org.limbo.flowjob.tracker.commons.constants.enums.JobExecuteType;
import org.limbo.flowjob.worker.core.domain.Job;
import org.limbo.flowjob.worker.core.infrastructure.JobExecutor;

/**
 * @author Devil
 * @since 2021/7/28
 */
public class HelloExecutor implements JobExecutor {

    @Override
    public String run(Job job) {
        System.out.println("hello " + job.getId());
        return null;
    }

    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JobExecuteType getType() {
        return JobExecuteType.FUNCTION;
    }
}
