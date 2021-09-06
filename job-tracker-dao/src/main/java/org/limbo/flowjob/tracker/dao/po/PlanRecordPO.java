package org.limbo.flowjob.tracker.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * plan的一次执行
 *
 * @author Devil
 * @since 2021/9/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flowjob_plan_record")
public class PlanRecordPO extends PO {

    private static final long serialVersionUID = -8999288394853231265L;
    /**
     * DB自增序列ID，并不是唯一标识
     */
    private Long serialId;

    /**
     * 计划ID
     */
    private String planId;

    /**
     * 从 1 开始增加 planId + recordId 全局唯一
     */
    private Long planRecordId;

    /**
     * 计划的版本
     */
    private Integer version;

    /**
     * 状态
     */
    private Byte state;

    /**
     * 已经重试的次数 todo 可以不要这个字段，直接从db获取instance个数   不管用不用这个字段，可能存在worker重复反馈导致数据问题
     */
    private Integer retry;

    /**
     * 是否需要重新调度 目前只有 FIXED_INTERVAL 类型在任务执行完成后才会需要重新调度
     */
    private Boolean reschedule;

    /**
     * 开始时间
     */
    private LocalDateTime startAt;

    /**
     * 结束时间
     */
    private LocalDateTime endAt;
}