//@formatter:off
/*
 * BloomBoxHeader
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.calamanari.pk.ohbf.BloomFilterConfig;

/**
 * The {@link BloomBoxHeader} is part of the bloom box file format (BBX) and contains the core information to restore a serialized bloom box.<b> Technically
 * (see {@link HeaderUtil}) it is a json-one-liner containing the box version (see {@link BloomBox#VERSION} and the exact settings.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomBoxHeader implements Serializable {

    private static final long serialVersionUID = -4607476887292824628L;

    /**
     * The bloom box version
     */
    private String version;

    /**
     * Date this box was created
     */
    private Date dateCreated;

    /**
     * Comment (optional)
     */
    private String description;

    /**
     * length of the bloom filter vector
     */
    private long requiredNumberOfBitsM;

    /**
     * Expected number of inserts
     */
    private long numberOfInsertedElementsN;

    /**
     * Desired or computed false-positive rate of the filter
     */
    private double falsePositiveRateEpsilon;

    /**
     * Number of hashes (hash functions) required for the filter
     */
    private int numberOfHashesK;

    public BloomBoxHeader() {
        // default constructor
    }

    /**
     * @param version box Major.Minor
     * @param config the bloom filter configuration to be preserved
     * @param dateCreated time the box was created
     * @param description textual description
     */
    public BloomBoxHeader(String version, BloomFilterConfig config, Date dateCreated, String description) {
        this.version = version;
        this.dateCreated = dateCreated;
        this.description = description;
        this.falsePositiveRateEpsilon = config.getFalsePositiveRateEpsilon();
        this.numberOfHashesK = config.getNumberOfHashesK();
        this.numberOfInsertedElementsN = config.getNumberOfInsertedElementsN();
        this.requiredNumberOfBitsM = config.getRequiredNumberOfBitsM();
    }

    /**
     * @return number of bits in a record's bloom filter
     */
    public long getRequiredNumberOfBitsM() {
        return requiredNumberOfBitsM;
    }

    /**
     * @param requiredNumberOfBitsM number of bits in a record's bloom filter
     */
    public void setRequiredNumberOfBitsM(long requiredNumberOfBitsM) {
        this.requiredNumberOfBitsM = requiredNumberOfBitsM;
    }

    /**
     * @return number of fields per record
     */
    public long getNumberOfInsertedElementsN() {
        return numberOfInsertedElementsN;
    }

    /**
     * @param numberOfInsertedElementsN number of fields per record
     */
    public void setNumberOfInsertedElementsN(long numberOfInsertedElementsN) {
        this.numberOfInsertedElementsN = numberOfInsertedElementsN;
    }

    /**
     * @return false-positive rate of the bloom filter
     */
    public double getFalsePositiveRateEpsilon() {
        return falsePositiveRateEpsilon;
    }

    /**
     * @param falsePositiveRateEpsilon false-positive rate of the bloom filter
     */
    public void setFalsePositiveRateEpsilon(double falsePositiveRateEpsilon) {
        this.falsePositiveRateEpsilon = falsePositiveRateEpsilon;
    }

    /**
     * @return number of partitions (hashes) of the bloom filter
     */
    public int getNumberOfHashesK() {
        return numberOfHashesK;
    }

    /**
     * @param numberOfHashesK number of partitions (hashes) of the bloom filter
     */
    public void setNumberOfHashesK(int numberOfHashesK) {
        this.numberOfHashesK = numberOfHashesK;
    }

    /**
     * @return box version Majer.Minor, see {@link BloomBox#VERSION}
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version box version Majer.Minor, see {@link BloomBox#VERSION}
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return comment (optional)
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description optional comment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return date of box creation
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated date of box creation
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        String created = "<N/A>";
        if (dateCreated != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            created = df.format(dateCreated);
        }

        return this.getClass().getSimpleName() + " [version " + version + ", dateCreated=" + created + ", requiredNumberOfBitsM=" + requiredNumberOfBitsM
                + ", numberOfInsertedElementsN=" + numberOfInsertedElementsN + ", falsePositiveRateEpsilon=" + falsePositiveRateEpsilon + ", numberOfHashesK="
                + numberOfHashesK + ", description=" + description + "]";
    }

}
