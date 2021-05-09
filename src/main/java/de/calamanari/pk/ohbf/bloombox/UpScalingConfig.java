//@formatter:off
/*
 * UpScalingConfig
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
 * An {@link UpScalingConfig} can be used to specify one factor (linear scaling) or multiple factors (attribute or even attribute level) to extrapolate the raw
 * counts to a target population.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class UpScalingConfig implements Serializable {

    private static final long serialVersionUID = 9022051827276834843L;

    /**
     * Size of the population we want to extrapolate to
     */
    private long targetPopulationSize;

    /**
     * Scaling factor to be used as a basis, if it is the only factor we will do linear scaling assuming a representative sample.
     */
    private double baseScalingFactor = 1.0;

    /**
     * Optional scaling configuration per attribute
     */
    private Map<String, AttributeScalingConfig> attributeScalingFactors = new HashMap<>();

    public UpScalingConfig() {
        // default constructor
    }

    /**
     * @return size of the population we want to extrapolate to
     */
    public long getTargetPopulationSize() {
        return targetPopulationSize;
    }

    /**
     * @param targetPopulationSize size of the population we want to extrapolate to
     */
    public void setTargetPopulationSize(long targetPopulationSize) {
        this.targetPopulationSize = targetPopulationSize;
    }

    /**
     * @return Scaling factor to be used as a basis, if it is the only factor we will do linear scaling assuming a representative sample.
     */
    public double getBaseScalingFactor() {
        return baseScalingFactor;
    }

    /**
     * @param baseScalingFactor Scaling factor to be used as a basis, if it is the only factor we will do linear scaling assuming a representative sample.
     */
    public void setBaseScalingFactor(double baseScalingFactor) {
        this.baseScalingFactor = baseScalingFactor;
    }

    /**
     * @return mapping of attribute names to scaling configs. Attributes not present here will implicitly fall back to the {@link #getBaseScalingFactor()}
     */
    public Map<String, AttributeScalingConfig> getAttributeScalingFactors() {
        return attributeScalingFactors;
    }

    /**
     * @param attributeScalingFactors mapping of attribute names to scaling configs. Attributes not present here will implicitly fall back to the
     *            {@link #getBaseScalingFactor()}
     */
    public void setAttributeScalingFactors(Map<String, AttributeScalingConfig> attributeScalingFactors) {
        this.attributeScalingFactors = attributeScalingFactors;
    }

    /**
     * Method to be called by the execution framework to detect obvious problems early
     * 
     * @param numberOfRows capacity of the store, number of records in the sample
     * @throws BasicValidationException on rule violations
     */
    public void validateSettings(long numberOfRows) {
        if (targetPopulationSize < numberOfRows) {
            throw new BasicValidationException(BbxMessage.ERR_CNF_POPULATION_TOO_SMALL.format(String.format(
                    "targetPopulationSize must not be smaller than the numberOfRows in the BloomBox, given: numberOfRows=%d, config: %s", numberOfRows, this)));
        }
        else if (baseScalingFactor < 1.0) {
            throw new BasicValidationException(BbxMessage.ERR_CNF_BASE_SCALE_FACTOR_TOO_SMALL.format(

                    String.format("baseScalingFactor must be >= 1.0, given: config: %s", this)));
        }

        if (attributeScalingFactors == null) {
            attributeScalingFactors = new HashMap<>();
        }
        else {
            validateAttributeSettings();
        }
    }

    /**
     * Additional validation on attribute and value settings
     * 
     * @throws BasicValidationException on rule violations
     */
    private void validateAttributeSettings() {
        for (Map.Entry<String, AttributeScalingConfig> attributeEntry : attributeScalingFactors.entrySet()) {
            AttributeScalingConfig attrCfg = attributeEntry.getValue();
            if (attrCfg.getScalingFactor() <= 0) {
                throw new BasicValidationException(BbxMessage.ERR_CNF_ATTR_SCALE_FACTOR_TOO_SMALL
                        .format(String.format("scalingFactor for attribute %s must be > 0, given: config: %s", attributeEntry.getKey(), this)));
            }
            if (attrCfg.getValueScalingFactors() == null) {
                attrCfg.setValueScalingFactors(new HashMap<>());
            }
            else {
                for (Map.Entry<String, Double> valueEntry : attrCfg.getValueScalingFactors().entrySet()) {
                    if (valueEntry.getValue() <= 0) {
                        throw new BasicValidationException(BbxMessage.ERR_CNF_ATTR_VAL_SCALE_FACTOR_TOO_SMALL
                                .format(String.format("scalingFactor for (attribute %s, value %s) must be > 0, given: config: %s", attributeEntry.getKey(),
                                        valueEntry.getKey(), this)));
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [targetPopulationSize=" + targetPopulationSize + ", baseScalingFactor=" + baseScalingFactor
                + ", attributeScalingFactors=" + attributeScalingFactors + "]";
    }

}
