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

package org.limbo.flowjob.tracker.commons.dto.worker;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.limbo.flowjob.tracker.commons.dto.ResponseDto;
import org.limbo.utils.JacksonUtils;

import java.io.Serializable;

/**
 * 向worker发送作业后，worker的返回数据
 *
 * @author Brozen
 * @since 2021-05-17
 */
@Data
public class JobReceiveResult implements Serializable {

    private static final long serialVersionUID = 5938197072123607724L;

    /**
     * 作业ID
     */
    private String jobId;

    /**
     * worker是否成功接收作业，返回true表明worker接下来会开始执行此作业
     */
    private Boolean accepted;

}
