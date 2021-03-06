package org.limbo.flowjob.tracker.core.schedule.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.limbo.flowjob.tracker.commons.constants.enums.JobScheduleStatus;
import org.limbo.flowjob.tracker.commons.constants.enums.PlanScheduleStatus;
import org.limbo.flowjob.tracker.commons.constants.enums.ScheduleType;
import org.limbo.flowjob.tracker.commons.utils.TimeUtil;
import org.limbo.flowjob.tracker.core.job.Job;
import org.limbo.flowjob.tracker.core.job.context.JobInstance;
import org.limbo.flowjob.tracker.core.job.context.JobInstanceRepository;
import org.limbo.flowjob.tracker.core.job.handler.JobFailHandler;
import org.limbo.flowjob.tracker.core.plan.Plan;
import org.limbo.flowjob.tracker.core.plan.PlanInstance;
import org.limbo.flowjob.tracker.core.plan.PlanInstanceRepository;
import org.limbo.flowjob.tracker.core.plan.PlanRepository;
import org.limbo.flowjob.tracker.core.storage.JobInstanceStorage;
import org.limbo.flowjob.tracker.core.tracker.TrackerNode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Devil
 * @since 2021/8/16
 */
@Slf4j
public class ClosedConsumer implements Consumer<JobInstance> {

    private final JobInstanceRepository jobInstanceRepository;

    private final PlanInstanceRepository planInstanceRepository;

    private final PlanRepository planRepository;

    private final TrackerNode trackerNode;

    private final JobInstanceStorage jobInstanceStorage;

    public ClosedConsumer(JobInstanceRepository jobInstanceRepository,
                          PlanInstanceRepository planInstanceRepository,
                          PlanRepository planRepository,
                          TrackerNode trackerNode,
                          JobInstanceStorage jobInstanceStorage) {
        this.jobInstanceRepository = jobInstanceRepository;
        this.planInstanceRepository = planInstanceRepository;
        this.planRepository = planRepository;
        this.trackerNode = trackerNode;
        this.jobInstanceStorage = jobInstanceStorage;
    }

    @Override
    public void accept(JobInstance jobInstance) {
        // ?????? job instance ??????
        if (log.isDebugEnabled()) {
            log.debug(jobInstance.getWorkerId() + " closed " + jobInstance.getId());
        }
        jobInstanceRepository.updateInstance(jobInstance);

        // ??????plan?????????????????????????????????????????????plan?????????
        PlanInstance planInstance = planInstanceRepository.getInstance(jobInstance.getPlanId(), jobInstance.getPlanInstanceId());
        if (PlanScheduleStatus.SUCCEED == planInstance.getState() || PlanScheduleStatus.FAILED == planInstance.getState()) {
            return;
        }

        switch (jobInstance.getState()) {
            case SUCCEED:
                handlerSuccess(planInstance, jobInstance);
                break;

            case FAILED:
                handlerFailed(planInstance, jobInstance);
                break;
            // todo ?????????????????????????????????
        }
    }

    public void handlerSuccess(PlanInstance planInstance, JobInstance jobInstance) {
        Plan plan = planRepository.getPlan(planInstance.getPlanId(), planInstance.getVersion());

        List<Job> subJobs = plan.getDag().getSubJobs(jobInstance.getJobId());
        if (CollectionUtils.isEmpty(subJobs)) {
            // ????????????????????????????????????plan??????
            if (checkPreJobsFinished(plan.getPlanId(), planInstance.getPlanInstanceId(), plan.getDag().getFinalJobs())) {
                LocalDateTime endTime = TimeUtil.nowLocalDateTime();
                // ??????plan
                planInstanceRepository.endInstance(planInstance.getPlanId(), planInstance.getPlanInstanceId(), endTime, PlanScheduleStatus.SUCCEED);

                // ?????? plan ???????????? feedback ?????? FIXED_INTERVAL???????????????????????????????????????????????????????????????????????????????????????????????????
                plan.setLastScheduleAt(planInstance.getStartAt());
                plan.setLastFeedBackAt(TimeUtil.toInstant(endTime));
                if (ScheduleType.FIXED_INTERVAL == plan.getScheduleOption().getScheduleType() && planInstance.isReschedule()) {
                    trackerNode.jobTracker().schedule(plan);
                }
            }
        } else {
            // ??????end?????????????????????
            for (Job job : subJobs) {
                if (checkPreJobsFinished(plan.getPlanId(), planInstance.getPlanInstanceId(), plan.getDag().getPreJobs(job.getJobId()))) {
                    jobInstanceStorage.store(job.newInstance(plan.getPlanId(), planInstance.getPlanInstanceId(), plan.getVersion(), JobScheduleStatus.Scheduling));
                }
            }
        }

    }

    public void handlerFailed(PlanInstance planInstance, JobInstance jobInstance) {
        JobFailHandler failHandler = jobInstance.getFailHandler();

        // ?????????????????????
        failHandler.execute();

        if (failHandler.terminate()) {
            // todo ???????????????????????? ???????????????plan ????????????
            // todo ?????????????????? ??????worker??????????????????????????????

            planInstanceRepository.endInstance(planInstance.getPlanId(), planInstance.getPlanInstanceId(),
                    TimeUtil.nowLocalDateTime(), PlanScheduleStatus.FAILED);
        } else {
            handlerSuccess(planInstance, jobInstance);
        }

    }

    /**
     * ??????????????????????????????
     * @param planId
     * @param planInstanceId
     * @param preJobs
     * @return
     */
    public boolean checkPreJobsFinished(String planId, Long planInstanceId, List<Job> preJobs) {
        // ??????db??? job??????
        List<JobInstance> finalInstances = jobInstanceRepository.getInstances(planId, planInstanceId,
                preJobs.stream().map(Job::getJobId).collect(Collectors.toList()));

        // ?????????????????????????????????
        if (preJobs.size() > finalInstances.size()) {
            return false;
        }

        // ???????????????????????????????????????????????????
        for (JobInstance finalInstance : finalInstances) {
            if (!finalInstance.canTriggerNext()) {
                return false;
            }
        }
        return true;
    }

}
