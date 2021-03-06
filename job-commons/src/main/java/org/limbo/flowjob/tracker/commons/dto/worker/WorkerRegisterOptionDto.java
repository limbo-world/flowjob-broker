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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.limbo.flowjob.tracker.commons.constants.enums.WorkerProtocol;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * worker注册时的参数
 *
 * @author Brozen
 * @since 2021-06-10
 */
@Data
@Schema(title = "worker注册参数")
public class WorkerRegisterOptionDto implements Serializable {

    private static final long serialVersionUID = 4234037520144789567L;

    /**
     * worker id
     */
    @NotBlank(message = "worker id can't be blank")
    @Schema(description = "worker id")
    private String id;

    /**
     * worker服务使用的通信协议，默认为Http协议。
     */
    @Schema(description = "worker服务使用的通信协议，默认为Http协议", implementation = Integer.class)
    private WorkerProtocol protocol;

    /**
     * worker服务的通信IP
     */
    @NotBlank(message = "worker host can't be blank")
    @Schema(description = "worker服务的通信主机名")
    private String host;

    /**
     * worker服务的通信端口
     */
    @NotNull(message = "worker port can't be blank")
    @Schema(description = "worker服务的通信端口")
    private Integer port;

    /**
     * worker可用的资源
     */
    @Schema(description = "worker可用的资源")
    private WorkerResourceDto availableResource;

    /**
     * 执行器
     */
    @NotEmpty(message = "worker job executor can't be empty")
    @Schema(description = "job 执行器")
    private List<WorkerExecutorRegisterDto> jobExecutors;

    /**
     * worker所属租户信息
     */
    @Schema(description = "worker所属租户信息")
    private WorkerTenantDto tenant;

}
