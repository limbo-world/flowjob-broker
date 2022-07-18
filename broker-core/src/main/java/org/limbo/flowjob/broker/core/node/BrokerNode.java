/*
 *
 *  * Copyright 2020-2024 Limbo Team (https://github.com/limbo-world).
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * 	http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.limbo.flowjob.broker.core.node;

/**
 * @author Devil
 * @since 2022/7/15
 */
public abstract class BrokerNode {

    /**
     * 启动节点
     */
    public abstract void start();

    /**
     * 重新平衡调度
     */
    public abstract void rebalance();

    /**
     * 停止
     */
    public abstract void stop();

    /**
     * @return 是否正在运行
     */
    public abstract boolean isRunning();

    /**
     * @return 是否已停止
     */
    public abstract boolean isStopped();

}