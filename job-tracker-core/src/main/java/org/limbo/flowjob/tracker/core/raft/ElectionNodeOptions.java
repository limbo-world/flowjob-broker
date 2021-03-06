/*
 * Copyright 2020-2024 Limbo Team (https://github.com/limbo-world).
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   	http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.limbo.flowjob.tracker.core.raft;

import com.alipay.sofa.jraft.option.NodeOptions;

/**
 * @author jiachun.fjc
 */
public class ElectionNodeOptions {

    private String dataPath;
    // raft group id
    private String groupId;
    // ip:port
    private String serverAddress;
    // ip:port,ip:port,ip:port
    private String serverAddressList;
    // raft node options
    private NodeOptions nodeOptions;

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerAddressList() {
        return serverAddressList;
    }

    public void setServerAddressList(String serverAddressList) {
        this.serverAddressList = serverAddressList;
    }

    public NodeOptions getNodeOptions() {
        return nodeOptions;
    }

    public void setNodeOptions(NodeOptions nodeOptions) {
        this.nodeOptions = nodeOptions;
    }

    @Override
    public String toString() {
        return "ElectionNodeOptions{" + "dataPath='" + dataPath + '\'' + ", groupId='" + groupId + '\''
                + ", serverAddress='" + serverAddress + '\'' + ", initialServerAddressList='" + serverAddressList
                + '\'' + ", nodeOptions=" + nodeOptions + '}';
    }
}
