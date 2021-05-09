//@formatter:off
/*
 * UpScalerFactory
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
 * {@link UpScaler}s are stateful. Thus the {@link BloomBoxQueryRunner} needs a way to get a fresh instance per execution. An {@link UpScalerFactory} creates
 * these instances based on the configuration and statistics of the current query.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface UpScalerFactory extends Serializable {

    /**
     * @param runner the caller
     * @param config upscaling configuration
     * @return new upscaler
     */
    public UpScaler createUpScaler(BloomBoxQueryRunner runner, UpScalingConfig config);

}
