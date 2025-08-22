//@formatter:off
/*
 * MainConfig
 * Copyright 2025 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//@formatter:on

package de.calamanari.pk.fluent;

import java.util.List;
import java.util.Map;

import de.calamanari.pk.fluent.ConfigBuilderInterfaces.MainConfigBuilder;
import de.calamanari.pk.fluent.ConfigBuilderInterfaces.MainConfigSetSystemType;

/**
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public record MainConfig(int id, String systemType, Contact contact, Map<String, String> parameters, List<RegionConfig> regionConfigs) {

    /**
     * @param id configuration id, positive number
     * @return builder
     */
    public static MainConfigSetSystemType withId(int id) {
        return new Builder().withId(id);
    }

    private static class Builder extends AbstractConfigBuilder<Builder> implements MainConfigBuilder {

        @Override
        public MainConfig get() {
            return new MainConfig(id, systemType, createContact(), parameters, regionConfigs);
        }

    }

}
