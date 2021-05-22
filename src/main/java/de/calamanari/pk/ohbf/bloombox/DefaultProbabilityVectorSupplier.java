//@formatter:off
/*
 * DefaultProbabilityVectorSupplier
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

/**
 * This default implementation decodes the vector when it is needed and caches it for the time the current row is processed.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DefaultProbabilityVectorSupplier implements ProbabilityVectorSupplier {

    private static final long serialVersionUID = 3522527039631876355L;

    /**
     * compressed version from data store
     */
    private byte[] compressedProbabilityVector = null;

    /**
     * cached vector after decoding
     */
    private float[] probabilityVector = null;

    public void initialize(byte[] compressedProbabilityVector) {
        this.compressedProbabilityVector = compressedProbabilityVector;
        this.probabilityVector = null;
    }

    @Override
    public float[] getProbabilityVector() {
        if (probabilityVector == null) {
            probabilityVector = ProbabilityVectorCodec.getInstance().decode(compressedProbabilityVector);
            this.compressedProbabilityVector = null;
        }
        return this.probabilityVector;
    }

}
