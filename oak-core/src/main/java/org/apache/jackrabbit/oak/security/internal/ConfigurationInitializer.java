/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.security.internal;

import java.util.List;
import org.apache.jackrabbit.oak.plugins.tree.RootProvider;
import org.apache.jackrabbit.oak.plugins.tree.TreeProvider;
import org.apache.jackrabbit.oak.spi.security.CompositeConfiguration;
import org.apache.jackrabbit.oak.spi.security.ConfigurationBase;
import org.apache.jackrabbit.oak.spi.security.ConfigurationParameters;
import org.apache.jackrabbit.oak.spi.security.SecurityConfiguration;
import org.apache.jackrabbit.oak.spi.security.SecurityProvider;
import org.jetbrains.annotations.NotNull;

final class ConfigurationInitializer {

    private ConfigurationInitializer() {}

    @NotNull
    static <T extends SecurityConfiguration> T initializeConfiguration(@NotNull T configuration, @NotNull SecurityProvider securityProvider, @NotNull RootProvider rootProvider, @NotNull TreeProvider treeProvider) {
        return initializeConfiguration(configuration, securityProvider, ConfigurationParameters.EMPTY, rootProvider, treeProvider);
    }

    @NotNull
    static <T extends SecurityConfiguration> T initializeConfiguration(@NotNull T configuration, @NotNull SecurityProvider securityProvider, @NotNull ConfigurationParameters parameters, @NotNull RootProvider rootProvider, @NotNull TreeProvider treeProvider) {
        if (configuration instanceof ConfigurationBase) {
            ConfigurationBase base = (ConfigurationBase) configuration;
            base.setSecurityProvider(securityProvider);
            base.setRootProvider(rootProvider);
            base.setTreeProvider(treeProvider);
            base.setParameters(ConfigurationParameters.of(base.getParameters(), parameters));
        }
        return configuration;
    }

    static <T extends SecurityConfiguration> void initializeConfigurations(@NotNull CompositeConfiguration<T> configuration, @NotNull SecurityProvider securityProvider,
                                         @NotNull ConfigurationParameters parameters,
                                         @NotNull RootProvider rootProvider,
                                         @NotNull TreeProvider treeProvider) {
        configuration.setSecurityProvider(securityProvider);
        configuration.setRootProvider(rootProvider);
        configuration.setTreeProvider(treeProvider);

        List<T> configs = configuration.getConfigurations();
        for (T config : configs) {
            initializeConfiguration(config, securityProvider, parameters, rootProvider, treeProvider);
        }
    }
}
