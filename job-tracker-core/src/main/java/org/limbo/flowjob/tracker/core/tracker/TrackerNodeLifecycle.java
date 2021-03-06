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

package org.limbo.flowjob.tracker.core.tracker;

import reactor.core.publisher.Mono;

/**
 * TrackerNode 生命周期函数
 *
 * @author Brozen
 * @since 2021-05-17
 */
public interface TrackerNodeLifecycle {

    /**
     * 启动前。
     * @return start之前触发的Mono，可通过{@link DisposableTrackerNode}阻止启动
     */
    Mono<DisposableTrackerNode> beforeStart();

    /**
     * 启动成功后。
     * @return start之后触发的Mono，可通过{@link DisposableTrackerNode}关闭JobTracker
     */
    Mono<DisposableTrackerNode> afterStart();

    /**
     * 停止前。
     * @return 停止前触发的Mono，Mono触发时，将阻塞停止流程
     */
    Mono<JobTracker> beforeStop();

    /**
     * 停止后。
     * @return 停止后触发的Mono
     */
    Mono<JobTracker> afterStop();


    /**
     * TrackerNode 生命周期事件类型
     */
    enum TrackerNodeLifecycleEventType {

        BEFORE_START,
        AFTER_START,
        BEFORE_STOP,
        AFTER_STOP

    }
}
