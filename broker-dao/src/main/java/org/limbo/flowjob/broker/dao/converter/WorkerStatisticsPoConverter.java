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

package org.limbo.flowjob.broker.dao.converter;

import com.google.common.base.Converter;
import org.limbo.flowjob.broker.core.worker.statistics.WorkerStatistics;
import org.limbo.flowjob.broker.dao.entity.WorkerStatisticsEntity;
import org.limbo.utils.reflection.EnhancedBeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author Brozen
 * @since 2021-06-03
 */
@Component
public class WorkerStatisticsPoConverter extends Converter<WorkerStatistics, WorkerStatisticsEntity> {

    /**
     * 将{@link WorkerStatistics}值对象转换为{@link WorkerStatisticsEntity}持久化对象
     * @param vo {@link WorkerStatistics}值对象
     * @return {@link WorkerStatisticsEntity}持久化对象
     */
    @Override
    protected WorkerStatisticsEntity doForward(WorkerStatistics vo) {
        return EnhancedBeanUtils.createAndCopy(vo, WorkerStatisticsEntity.class);
    }

    /**
     * 将{@link WorkerStatisticsEntity}持久化对象转换为{@link WorkerStatistics}值对象
     * @param po {@link WorkerStatisticsEntity}持久化对象
     * @return {@link WorkerStatistics}值对象
     */
    @Override
    protected WorkerStatistics doBackward(WorkerStatisticsEntity po) {
        return EnhancedBeanUtils.createAndCopy(po, WorkerStatistics.class);
    }
}