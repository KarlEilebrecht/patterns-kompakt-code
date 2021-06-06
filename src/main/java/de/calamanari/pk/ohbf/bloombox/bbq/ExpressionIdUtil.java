//@formatter:off
/*
 * ExpressionIdUtil
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

package de.calamanari.pk.ohbf.bloombox.bbq;

import de.calamanari.pk.muhai.LongPrefix;
import de.calamanari.pk.muhai.MuhaiGenerator;

/**
 * For faster dealing with expressions each {@link BbqExpression} has a unique ID of type long that is derived from its content using a {@link MuhaiGenerator}.
 * This way we not only get a lean and convenient ID, also similar expressions within a query bundle can take advantage of result caching as they will get the
 * same ids if they represent the same expression. The id is stable and independent from the execution environment.
 * <p>
 * Expression-ids all have the same length (19 digits), see {@link LongPrefix#STRAIGHT}.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class ExpressionIdUtil {

    /**
     * generator for id-generation
     */
    private static final MuhaiGenerator ID_GENERATOR = new MuhaiGenerator(LongPrefix.STRAIGHT);

    /**
     * The first 2^16 ids are reserved
     */
    public static final int MIN_GENERATED_DATA_POINT_ID = 65536;

    /**
     * Creates a new unique id based on the given inputs
     * 
     * @param type identifies the type of expression to be keyed, to distinguish different expression with the same members
     * @param subExpressionIds member-ids
     * @return unique id
     */
    public static long createExpressionId(String type, long... subExpressionIds) {

        Object[] inputs = new Object[subExpressionIds.length + 1];

        inputs[0] = type;
        for (int i = 0; i < subExpressionIds.length; i++) {
            inputs[i + 1] = subExpressionIds[i];
        }

        return ID_GENERATOR.createKey(inputs);
    }

    /**
     * Creates high-precision unique data point id (length/format follow the rules defined above for expression-ids).
     * 
     * @param argName column name, <b>case-sensitive</b>
     * @param argValue column value, <b>case-sensitive</b>
     * @return globally unique id for this key/value combination (aka data point)
     */
    public static long createDataPointId(String argName, Object argValue) {
        return ID_GENERATOR.createKey("dp", argName, argValue);
    }

    /**
     * Creates a 31-bit positive integer identifying the given key/value pair (local, low-precision data point id) from the high-precision globally unique data
     * point id
     * 
     * @param dataPointId id created by {@link #createDataPointId(String, Object)}
     * @return identifier EXCLUDING the range <code>[ 0 .. ({@value #MIN_GENERATED_DATA_POINT_ID} - 1) ]</code>
     */
    public static int createLpDataPointId(long dataPointId) {

        int res = (int) (((dataPointId << 33L) >>> 33L) + MIN_GENERATED_DATA_POINT_ID);
        if (res < 0) {
            // overflow, move it into the valid range
            res = ((res + Integer.MAX_VALUE) + 1) + MIN_GENERATED_DATA_POINT_ID;
        }
        // now we have a random-like 31-bit positive integer greater than the minimum
        return res;
    }

    /**
     * Creates a 31-bit positive integer identifying the given key/value pair (local, low-precision data point id).
     * 
     * @param argName column name
     * @param argValue column value
     * @return identifier EXCLUDING the range <code>[ 0 .. ({@value #MIN_GENERATED_DATA_POINT_ID} - 1) ]</code>
     */
    public static int createLpDataPointId(String argName, Object argValue) {
        return createLpDataPointId(createDataPointId(argName, argValue));
    }

    private ExpressionIdUtil() {
        // no instances
    }
}
