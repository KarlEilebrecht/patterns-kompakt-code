//@formatter:off
/*
 * Counter Flyweight - demonstrates a FLYWEIGHT.
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
package de.calamanari.pk.flyweight;

/**
 * Counter Flyweight - interface all concrete FLYWEIGHTs in this example must implement.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface CounterFlyweight {

    /**
     * The flyweight's method to do some operation, here we count something
     * 
     * @param extrinsicState data or context to operate on
     * @return result of flyweight's operation
     */
    public int count(String extrinsicState);

}
