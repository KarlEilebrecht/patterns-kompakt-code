//@formatter:off
/*
 * SecuManga Gateway Client Interface
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
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
package de.calamanari.pk.gateway.client;

/**
 * SecuManga Gateway Client interface, this additional interface helps to create mocks for testing "client-only".
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface SecuMangaGatewayClient {

    /**
     * Scrambles the given text using SecuManga via the gateway
     * 
     * @param text data to be scrambled
     * @return scrambled text
     */
    public String scramble(String text);

    /**
     * Unscrambles the given text using SecuManga via the gateway
     * 
     * @param text data to be restored
     * @return clear text
     */
    public String unscramble(String text);

}
