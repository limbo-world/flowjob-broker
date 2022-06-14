package org.limbo.flowjob.broker.cluster.single;

import org.limbo.flowjob.broker.core.schedule.Schedulable;
import org.limbo.flowjob.broker.core.schedule.scheduler.Scheduler;
import org.limbo.flowjob.broker.core.broker.JobTracker;

/**
 * 当前节点处理所有的需求
 *
 * @author Devil
 * @since 2021/8/9
 */
public class SingleJobTracker implements JobTracker {

    private final Scheduler scheduler;

    public SingleJobTracker(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void schedule(Schedulable schedulable) {
        scheduler.schedule(schedulable);
    }

    @Override
    public void unschedule(String id) {
        scheduler.unschedule(id);
    }

    @Override
    public boolean isScheduling(String id) {
        return scheduler.isScheduling(id);
    }

}