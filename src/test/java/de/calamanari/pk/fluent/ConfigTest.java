//@formatter:off
/*
 * ConfigTest
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

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
class ConfigTest {

    @Test
    void testFluentBuilderRegionConfig() {

        // @formatter:off
        RegionConfig regionConf01 = RegionConfig.withId(313)
                                                    .code("R313")
                                                    .description("Some region")
                                                    .contact("Harry Miller")
                                                        .email("Harry.Miller@neversend.com")
                                                    .parameter("f123", "8273")
                                                    .parameter("f456", "ok")
                                                    .parameter("active", "no")
                                                .get();

        RegionConfig regionConf02 = RegionConfig.withId(987)
                                                    .code("R987")
                                                    .contact("Laura Giller")
                                                        .email("Laura.Giller@neversend.com")
                                                        .phone("+1555-62372384")
                                                    .parameter("op4", "yes")
                                                    .parameter("proactive", "yes")
                                                .get();

        RegionConfig regionConf03 = RegionConfig.withId(101)
                                                    .code("R101")
                                                    .description("Test")
                                                .get();
        
        // @formatter:on

        assertEquals("Harry Miller", regionConf01.contact().name());
        assertEquals("Laura Giller", regionConf02.contact().name());
        assertNull(regionConf03.contact());

    }

    @Test
    void testFluentBuilderMainConfig() {

        // @formatter:off
        
        MainConfig mainConf01 = MainConfig.withId(1007)
                                              .type("ZULUFIX")
                                              .contact("Ronda Ruthless")
                                                  .email("Ronda.Ruthless@neversend.com")
                                              .parameter("q8", "true")
                                              .parameter("global", "yes")
                                          .get();

        MainConfig mainConf02 = MainConfig.withId(1009)
                                              .type("TETRA")
                                              .contact("Biff Tannen")
                                              .email("Biff.Tannen@neversend.com")
                                              .parameter("delay", "8173")
                                              .parameter("wipe", "no")
                                              .regionConfig(
                                                      RegionConfig.withId(987)
                                                                      .code("R987")
                                                                      .contact("Laura Giller")
                                                                          .email("Laura.Giller@neversend.com")
                                                                          .phone("+1555-62372384")
                                                                      .parameter("op4", "yes")
                                                                      .parameter("proactive", "yes")
                                                                  .get())
                                          .get();

        MainConfig mainConf03 = MainConfig.withId(9999)
                                              .type("TEST")
                                          .get();
        
        // @formatter:on

        assertEquals("Ronda Ruthless", mainConf01.contact().name());
        assertEquals(0, mainConf01.regionConfigs().size());
        assertEquals("Biff Tannen", mainConf02.contact().name());
        assertEquals("Laura Giller", mainConf02.regionConfigs().get(0).contact().name());
        assertEquals(9999, mainConf03.id());
        assertNull(mainConf03.contact());
    }

}
