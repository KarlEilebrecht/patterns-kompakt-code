//@formatter:off
/*
 * ConfigBuilderInterfaces
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

/**
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
// suppressed linter warning: the interfaces defined here are "methods" and should be kept together in one context
@SuppressWarnings("java:S6539")
public class ConfigBuilderInterfaces {

    public interface DocSetId {

        /**
         * Specifies the identifier of this configuration
         * 
         * @param id identifier &gt; 0
         * @return builder
         */
        Object withId(int id);

    }

    public interface DocSetContact {

        /**
         * Sets the name of the contact, usually first name and last name
         * 
         * @param name the name of the contact person, not null
         * @return builder
         */
        Object contact(String name);

    }

    public interface DocSetEmail {

        /**
         * Email address of the contact
         * 
         * @param email of the contact person, mandatory
         * @return builder
         */
        Object email(String email);

    }

    public interface DocSetPhone {

        /**
         * Phone number of the contact
         * 
         * @param phone contact person's phone number, optional
         * @return builder
         */
        Object phone(String phone);
    }

    public interface DocAddParameter {

        /**
         * Defines a parameter for the current configuration.
         * 
         * @param name parameter identifier, not null
         * @param value to be assigned to the given parameter id
         * @return builder
         */
        Object parameter(String name, String value);
    }

    public interface DocExit {

        /**
         * Exits the builder and returns the newly created configuration object
         * 
         * @return the created and validated configuration
         */
        Object get();
    }

    public interface MainConfigSetId extends DocSetId {

        @Override
        MainConfigSetSystemType withId(int id);

    }

    public interface MainConfigSetSystemType {

        /**
         * Sets the system type
         * 
         * @param systemType standard identifier, not null
         * @return builder
         */
        MainConfigSetContactOrAddParameterOrAddRegionConfigOrExit type(String systemType);

    }

    public interface MainConfigSetContact extends DocSetContact {

        @Override
        MainConfigContactSetEmail contact(String name);

    }

    public interface MainConfigContactSetEmail extends DocSetEmail {

        @Override
        MainConfigContactSetPhoneOrAddParameterOrAddRegionConfigOrExit email(String email);

    }

    public interface MainConfigContactSetPhone extends DocSetPhone {

        @Override
        MainConfigAddParameterOrAddRegionConfigOrExit phone(String phone);
    }

    public interface MainConfigAddParameter extends DocAddParameter {

        @Override
        MainConfigAddParameterOrAddRegionConfigOrExit parameter(String name, String value);
    }

    public interface MainConfigAddRegionConfig {

        /**
         * Adds a region configuration to this main configuration
         * 
         * @param regionConfig to be added
         * @return builder
         */
        MainConfigAddRegionConfigOrExit regionConfig(RegionConfig regionConfig);
    }

    public interface MainConfigExit extends DocExit {

        @Override
        MainConfig get();
    }

    public interface MainConfigAddRegionConfigOrExit extends MainConfigAddRegionConfig, MainConfigExit {
        // combined
    }

    public interface MainConfigAddParameterOrAddRegionConfigOrExit extends MainConfigAddParameter, MainConfigAddRegionConfig, MainConfigExit {
        // combined
    }

    public interface MainConfigContactSetPhoneOrAddParameterOrAddRegionConfigOrExit
            extends MainConfigContactSetPhone, MainConfigAddParameter, MainConfigAddRegionConfig, MainConfigExit {
        // combined
    }

    public interface MainConfigSetContactOrAddParameterOrAddRegionConfigOrExit
            extends MainConfigSetContact, MainConfigAddParameter, MainConfigAddRegionConfig, MainConfigExit {
        // combined
    }

    /**
     * This is the only interface to be implemented by the concrete main configuration builder
     */
    public interface MainConfigBuilder extends MainConfigSetContactOrAddParameterOrAddRegionConfigOrExit,
            MainConfigContactSetPhoneOrAddParameterOrAddRegionConfigOrExit, MainConfigAddParameterOrAddRegionConfigOrExit, MainConfigAddRegionConfigOrExit,
            MainConfigExit, MainConfigSetContact, MainConfigContactSetEmail, MainConfigSetSystemType, MainConfigSetId {
        // combined
    }

    public interface RegionConfigSetId extends DocSetId {

        @Override
        RegionConfigSetRegionCode withId(int id);

    }

    public interface RegionConfigSetRegionCode {

        /**
         * Sets the mandatory region identifier for this region configuration
         * 
         * @param regionCode not null
         * @return builder
         */
        RegionConfigSetDescriptionOrContactAddParameterOrExit code(String regionCode);

    }

    public interface RegionConfigSetDescription {

        /**
         * @param description optional description of the region
         * @return builder
         */
        RegionConfigSetContactAddParameterOrExit description(String description);

    }

    public interface RegionConfigSetContact extends DocSetContact {

        @Override
        RegionConfigContactSetEmail contact(String name);

    }

    public interface RegionConfigContactSetEmail extends DocSetEmail {

        @Override
        RegionConfigContactSetPhoneOrAddParameterOrExit email(String email);

    }

    public interface RegionConfigContactSetPhone extends DocSetPhone {

        @Override
        RegionConfigAddParameterOrExit phone(String phone);
    }

    public interface RegionConfigAddParameter extends DocAddParameter {

        @Override
        RegionConfigAddParameterOrExit parameter(String name, String value);
    }

    public interface RegionConfigExit extends DocExit {

        @Override
        RegionConfig get();
    }

    public interface RegionConfigAddParameterOrExit extends RegionConfigAddParameter, RegionConfigExit {
        // combined
    }

    public interface RegionConfigContactSetPhoneOrAddParameterOrExit extends RegionConfigContactSetPhone, RegionConfigAddParameter, RegionConfigExit {
        // combined
    }

    public interface RegionConfigSetContactAddParameterOrExit extends RegionConfigSetContact, RegionConfigAddParameter, RegionConfigExit {
        // combined
    }

    public interface RegionConfigSetDescriptionOrContactAddParameterOrExit
            extends RegionConfigSetDescription, RegionConfigSetContact, RegionConfigAddParameter, RegionConfigExit {
        // combined
    }

    /**
     * This is the only interface to be implemented by the concrete region configuration builder
     */
    public interface RegionConfigBuilder extends RegionConfigSetDescriptionOrContactAddParameterOrExit, RegionConfigSetContactAddParameterOrExit,
            RegionConfigContactSetPhoneOrAddParameterOrExit, RegionConfigAddParameterOrExit, RegionConfigExit, RegionConfigSetRegionCode,
            RegionConfigContactSetEmail, RegionConfigSetId {
        // combined
    }

    private ConfigBuilderInterfaces() {
        // no instances
    }
}
