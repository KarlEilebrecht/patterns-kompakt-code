//@formatter:off
/*
 * ProbabilityVectorSupplier
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
package de.calamanari.pk.ohbf.bloombox;

import java.io.Serializable;

/**
 * The {@link ProbabilityVectorSupplier} allows delaying the computation of the probability vector for a single row, which might be time consuming.
 * <p>
 * <b>Note:<b> This is by intention not a lambda because lambdas are not serializable and cannot cache data.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface ProbabilityVectorSupplier extends Serializable {

    /**
     * To be returned by dummy implementations of {@link #getProbabilityVector()}
     */
    public static final float[] NONE = new float[0];

    default float[] getProbabilityVector() {
        return NONE;
    }

}
