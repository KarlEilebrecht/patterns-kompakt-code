/*
 * Abstract Mapper - mappers in this example extend this base class
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package de.calamanari.pk.mapper;

/**
 * Abstract Mapper - mappers in this example extend this base class
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractMapper {

    /**
     * Maps data from the first subsystem's representation to the second one
     */
    public abstract void map();

    /**
     * Maps data from the second subsystem's representation back to the first one
     */
    public abstract void mapBack();

}
