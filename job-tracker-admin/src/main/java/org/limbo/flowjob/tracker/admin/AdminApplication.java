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

package org.limbo.flowjob.tracker.admin;

import org.limbo.flowjob.tracker.admin.adapter.config.HttpWorkerMessagingConfiguration;
import org.limbo.flowjob.tracker.admin.adapter.config.TrackerConfiguration;
import org.limbo.flowjob.tracker.admin.adapter.config.MyBatisConfiguration;
import org.limbo.flowjob.tracker.admin.adapter.config.RSocketWorkerMessagingConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

/**
 * @author Brozen
 * @since 2021-06-01
 */
@SpringBootApplication
@Import({
        TrackerConfiguration.class,
        MyBatisConfiguration.class,
        HttpWorkerMessagingConfiguration.class,
        RSocketWorkerMessagingConfiguration.class,
})
public class AdminApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .web(WebApplicationType.REACTIVE)
                .sources(AdminApplication.class)
                .build()
                .run(args);
    }

}
