//@formatter:off
/*
 * AttributeScalingConfig
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
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link AttributeScalingConfig} descrives how a specific attribute should be scaled to compensate for a bias in the target population.
 * 
 * @see UpScaler
 * @see DefaultUpScaler
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class AttributeScalingConfig implements Serializable {

    private static final long serialVersionUID = -5641308519214800711L;

    /**
     * Scaling factor on attribute level
     */
    private double scalingFactor = 1.0;

    /**
     * Individual settings for attribute values
     */
    private Map<String, Double> valueScalingFactors = new HashMap<>();

    public AttributeScalingConfig() {
        // default constructor
    }

    /**
     * @return scaling factor on attribute level
     */
    public double getScalingFactor() {
        return scalingFactor;
    }

    /**
     * @param scalingFactor factor on attribute level
     */
    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    /**
     * @return map of attribute values to individual scaling factors
     */
    public Map<String, Double> getValueScalingFactors() {
        return valueScalingFactors;
    }

    /**
     * @param valueScalingFactors map of attribute values to individual scaling factors
     */
    public void setValueScalingFactors(Map<String, Double> valueScalingFactors) {
        this.valueScalingFactors = valueScalingFactors;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [scalingFactor=" + scalingFactor + ", valueScalingFactors=" + valueScalingFactors + "]";
    }

}
