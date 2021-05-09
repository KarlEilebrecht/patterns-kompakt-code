//@formatter:off
/*
 * BbxMessage
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enum contains the error and warning message identifiers and provides utility methods to create messages.
 * <p>
 * I did not want to separate codes from messages, so I introduced a scheme that simplifies later parsing. <br>
 * Each error code starts with {@value #ERROR_CODE_PREFIX}, each warning code starts with {@value #WARNING_CODE_PREFIX}, followed by a minus and terminated by a
 * colon, e.g. <code>BBXE-4003: <i>some text message</i></code>.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public enum BbxMessage {

    /**
     * Common technical problem, aka UNKNOWN
     */
    ERR_COMMON("BBXE-4003"),

    /**
     * Common issue with upscaling
     */
    ERR_UPSCALING("BBXE-5000"),

    /**
     * Indicates a configuration problem, an upscaler should be their but could not be created
     */
    ERR_UPSCALER_UNAVAILABLE("BBXE-5001"),

    /**
     * Problem during query preparation
     */
    ERR_PREPARING("BBXE-1000"),

    /**
     * The given query bundle is invalid
     */
    ERR_INVALID_BUNDLE("BBXE-1001"),

    /**
     * The given query bundle is invalid as it does not contain any base query
     */
    ERR_BUNDLE_NO_BASE_QUERY("BBXE-1002"),

    /**
     * The given query bundle is invalid because query types have been mixed, base queries must be separated from post-queries
     */
    ERR_BUNDLE_UNEXPECTED_QUERY_TYPE("BBXE-1003"),

    /**
     * The meta data of a base or post query is incomplete (e.g. name not present)
     */
    ERR_MAIN_QUERY_META_DATA("BBXE-1004"),

    /**
     * The meta data of a sub query is incomplete (e.g. name/label not present)
     */
    ERR_SUB_QUERY_META_DATA("BBXE-1005"),

    /**
     * A query name violates naming convention
     */
    ERR_QUERY_NAMING_CONVENTION("BBXE-1020"),

    /**
     * A referenced query could not be resolved
     */
    ERR_INVALID_QUERY_REFERENCE("BBXE-1021"),

    /**
     * (Most likely) a problem with the quoting in a given query
     */
    ERR_QUERY_SYNTAX_QUOTES("BBXE-1025"),

    /**
     * Name:expression syntax violation
     */
    ERR_QUERY_SYNTAX_EASY_SCRIPT("BBXE-1026"),

    /**
     * The configured population is too small, must be greater than or equal to the number of records in the box
     */
    ERR_CNF_POPULATION_TOO_SMALL("BBXE-1006"),

    /**
     * The configured base scale factor is too small (expected: &gt;0)
     */
    ERR_CNF_BASE_SCALE_FACTOR_TOO_SMALL("BBXE-1007"),

    /**
     * The configured attribute scale factor is too small (expected: &gt;0)
     */
    ERR_CNF_ATTR_SCALE_FACTOR_TOO_SMALL("BBXE-1008"),

    /**
     * The configured attribute value scale factor is too small (expected: &gt;0)
     */
    ERR_CNF_ATTR_VAL_SCALE_FACTOR_TOO_SMALL("BBXE-1009"),

    /**
     * Error during query creation
     */
    ERR_CREATE_QUERY("BBXE-1100"),

    /**
     * Name clash: the name of a query in a bundle must be unique
     */
    ERR_DUPLICATE_QUERY("BBXE-2000"),

    /**
     * Error during query execution
     */
    ERR_QUERY_EXECUTION("BBXE-3000"),

    /**
     * Overscaling (count would have been greater than population) was detected and corrected. <br>
     * This may influence the value of results based on this query.
     */
    WARN_OVERSCALE("BBXW-5001"),

    /**
     * The query optimizer detected that the given query always yields true (all records will match)
     */
    WARN_ALWAYS_TRUE("BBXW-1001"),

    /**
     * The query optimizer detected that the given query always yields false (no record will match)
     */
    WARN_ALWAYS_FALSE("BBXW-1002"),

    /**
     * The query optimizer found parts of the query that will always match or always not match.<br>
     * This indicates a mistake and can lead to problems especially in conjunction with upscaling.
     */
    WARN_ALWAYS_TRUE_OR_ALWAYS_FALSE("BBXW-1003"),

    /**
     * The returned count looks too high, might be an indication for a bad query
     */
    WARN_ALWAYS_MAX_COUNT("BBXW-1004"),

    /**
     * The returned scaled count looks too high (equal to or larger than population)
     */
    WARN_ALWAYS_MAX_SCALED("BBXW-1005")

    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(BbxMessage.class);

    public static final String ERROR_CODE_PREFIX = "BBXE";

    public static final String WARNING_CODE_PREFIX = "BBXW";

    /**
     * reason identifier
     */
    private final String identifier;

    /**
     * @param id reason identifier
     */
    BbxMessage(String id) {
        this.identifier = id;
    }

    /**
     * @param ex some exception
     * @return tests the returned message and either includes it or if unknown prefixes it or if null creates a message with the type of the exception.
     */
    public String format(Throwable ex) {
        String msg = ex.getMessage();
        if (msg == null || msg.isBlank()) {
            msg = "<" + ex.getClass().getSimpleName() + ">";
        }
        if (!msg.startsWith(ERROR_CODE_PREFIX) && !msg.startsWith(WARNING_CODE_PREFIX)) {
            msg = identifier + ": " + msg;
        }
        LOGGER.trace(msg, ex);
        return msg;
    }

    /**
     * Prefixes the message with the identifier followed by a colon and a space
     * 
     * @param message raw message
     * @return prefixed message
     */
    public String format(String message) {
        return identifier + ": " + message;
    }

    /**
     * Creates a message preceded with the identifier, a colon and a space
     * 
     * @param message raw message
     * @param ex message will be appended
     * @return new message
     */
    public String format(String message, Throwable ex) {
        return identifier + ": " + message + ", cause: " + format(ex);
    }

}
