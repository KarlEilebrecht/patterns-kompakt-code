//@formatter:off
/*
 * AbstractConfigBuilder
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractConfigBuilder<T extends AbstractConfigBuilder<T>> {

    /**
     * builder instance (concrete class is parameterized with itself (T))
     */
    private final T builder;

    protected int id;

    protected String systemType;

    protected String contactName;

    protected String contactEmail;

    protected String contactPhone;

    protected Map<String, String> parameters = new HashMap<>();

    protected List<RegionConfig> regionConfigs = new ArrayList<>();

    protected String regionCode;

    protected String description;

    protected AbstractConfigBuilder() {
        // need explicit cast, otherwise the compiler does not understand
        // that T must be the "type of this"
        @SuppressWarnings("unchecked")
        T instance = (T) this;
        this.builder = instance;
    }

    public T withId(int id) {
        this.id = id;
        return builder;
    }

    public T contact(String name) {
        this.contactName = name;
        return builder;
    }

    public T email(String email) {
        this.contactEmail = email;
        return builder;
    }

    public T phone(String phone) {
        this.contactPhone = phone;
        return builder;
    }

    public T type(String type) {
        this.systemType = type;
        return builder;
    }

    public T parameter(String name, String value) {
        this.parameters.put(name, value);
        return builder;
    }

    public T regionConfig(RegionConfig regionConfig) {
        this.regionConfigs.add(regionConfig);
        return builder;
    }

    public T code(String regionCode) {
        this.regionCode = regionCode;
        return builder;
    }

    public T description(String description) {
        this.description = description;
        return builder;
    }

    protected Contact createContact() {
        return contactName == null ? null : new Contact(contactName, contactEmail, contactPhone);
    }

}
