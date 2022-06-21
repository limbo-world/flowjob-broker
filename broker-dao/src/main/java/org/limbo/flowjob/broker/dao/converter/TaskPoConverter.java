package org.limbo.flowjob.broker.dao.converter;

import com.google.common.base.Converter;
import org.limbo.flowjob.broker.core.plan.job.context.Task;
import org.limbo.flowjob.broker.core.repositories.PlanInstanceRepository;
import org.limbo.flowjob.broker.dao.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Devil
 * @since 2021/7/24
 */
@Component
public class TaskPoConverter extends Converter<Task, TaskEntity> {

    @Autowired
    private PlanInstanceRepository planInstanceRepository;

    @Override
    protected TaskEntity doForward(Task task) {
        // todo
        return null;
//        TaskEntity po = new TaskEntity();
//        Task.ID taskId = task.getId();
//        po.setPlanId(taskId.planId);
//        po.setPlanRecordId(taskId.planRecordId);
//        po.setPlanInstanceId(taskId.planInstanceId);
//        po.setJobId(taskId.jobId);
//        po.setJobInstanceId(taskId.jobInstanceId);
//        po.setTaskId(taskId.taskId);
//        po.setState(task.getState().status);
//        po.setResult(task.getResult().result);
//        po.setWorkerId(task.getWorkerId());
//        po.setType(task.getType().type);
//        po.setAttributes(task.getAttributes().toString());
//        po.setErrorMsg(task.getErrorMsg());
//        po.setErrorStackTrace(task.getErrorStackTrace());
//        po.setStartAt(TimeUtil.toLocalDateTime(task.getStartAt()));
//        po.setEndAt(TimeUtil.toLocalDateTime(task.getEndAt()));
//        return po;
    }

    @Override
    protected Task doBackward(TaskEntity po) {
        // todo
        return null;
//        PlanInstance planInstance = planInstanceRepository.get(po.getPlanInstanceId());
//        Job job = planInstance.getDag().getJob(po.getJobId());
//
//        Task task = new Task();
//        Task.ID taskId = new Task.ID(
//                po.getPlanId(),
//                po.getPlanRecordId(),
//                po.getPlanInstanceId(),
//                po.getJobId(),
//                po.getJobInstanceId(),
//                po.getTaskId()
//        );
//        task.setId(taskId);
//        task.setState(TaskScheduleStatus.parse(po.getState()));
//        task.setResult(TaskResult.parse(po.getResult()));
//        task.setDispatchOption(job.getDispatchOption());
//        task.setExecutorOption(job.getExecutorOption());
//        task.setWorkerId(po.getWorkerId());
//        task.setType(TaskType.parse(po.getType()));
//        task.setAttributes(new Attributes(StringUtils.isBlank(po.getAttributes()) ? new HashMap<>() : JacksonUtils.parseObject(po.getAttributes(), new TypeReference<Map<String, List<String>>>() {
//        })));
//        task.setErrorMsg(po.getErrorMsg());
//        task.setErrorStackTrace(po.getErrorStackTrace());
//        task.setStartAt(TimeUtil.toInstant(po.getStartAt()));
//        task.setEndAt(TimeUtil.toInstant(po.getEndAt()));
//        return task;
    }

}
