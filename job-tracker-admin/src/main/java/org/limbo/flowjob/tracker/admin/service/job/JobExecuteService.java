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

package org.limbo.flowjob.tracker.admin.service.job;

import lombok.extern.slf4j.Slf4j;
import org.limbo.flowjob.tracker.commons.constants.WorkerHeaders;
import org.limbo.flowjob.tracker.commons.constants.enums.JobExecuteResult;
import org.limbo.flowjob.tracker.commons.constants.enums.ScheduleType;
import org.limbo.flowjob.tracker.commons.dto.job.JobExecuteFeedbackDto;
import org.limbo.flowjob.tracker.commons.utils.Symbol;
import org.limbo.flowjob.tracker.core.job.context.JobInstance;
import org.limbo.flowjob.tracker.core.job.context.JobInstanceRepository;
import org.limbo.flowjob.tracker.core.plan.Plan;
import org.limbo.flowjob.tracker.core.plan.PlanInstance;
import org.limbo.flowjob.tracker.core.plan.PlanInstanceRepository;
import org.limbo.flowjob.tracker.core.plan.PlanRepository;
import org.limbo.flowjob.tracker.core.schedule.consumer.ClosedConsumer;
import org.limbo.flowjob.tracker.core.storage.JobInstanceStorage;
import org.limbo.flowjob.tracker.core.tracker.TrackerNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Brozen
 * @since 2021-07-07
 */
@Slf4j
@Service
public class JobExecuteService {

    @Autowired
    private TrackerNode trackerNode;

    @Autowired
    private JobInstanceRepository jobInstanceRepository;

    @Autowired
    private PlanInstanceRepository planInstanceRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private JobInstanceStorage jobInstanceStorage;

    /**
     * ????????????????????????
     *
     * @param mono ????????????
     */
    public Mono<Symbol> feedback(Mono<JobExecuteFeedbackDto> mono) {
        return mono.transformDeferredContextual((feedbackMono, ctx) -> {

            // todo ???context??????workerId ??? ?????????????????????
            String workerId = ctx.getOrDefault(WorkerHeaders.WORKER_ID, "");

            return feedbackMono.map(fb -> {

                // ????????????
                JobInstance jobInstance = jobInstanceRepository.getInstance(fb.getPlanId(), fb.getPlanInstanceId(), fb.getJobId());
                JobExecuteResult result = fb.getResult();

                // ??????????????????
                jobInstance.onClosed().subscribe(new ClosedConsumer(jobInstanceRepository, planInstanceRepository,
                        planRepository, trackerNode, jobInstanceStorage));

                // ????????????
                switch (result) {
                    case SUCCEED:
                        jobInstance.close();
                        break;

                    case FAILED:
                        jobInstance.close(fb.getErrorMsg(), fb.getErrorStackTrace());
                        break;

                    case TERMINATED:
                        throw new UnsupportedOperationException("??????????????????????????????");
                }

                return Symbol.newSymbol();
            });

        });
    }


}
