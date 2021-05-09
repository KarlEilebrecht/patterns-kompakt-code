//@formatter:off
/*
 * ExpressionBuilderException
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

import de.calamanari.pk.ohbf.bloombox.BloomBoxException;

/**
 * Exception to be thrown if the {@link IntermediateExpressionBuilder} detected a problem while parsing a BBQ-query.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class ExpressionBuilderException extends BloomBoxException {

    private static final long serialVersionUID = 3365199621035578057L;

    /**
     * source line
     */
    private final int line;

    /**
     * position in source line
     */
    private final int charPositionInLine;

    /**
     * @param line the line number in the source
     * @param charPositionInLine position in source line closest to the problem
     * @param msg explanation
     * @param cause reason or null
     */
    public ExpressionBuilderException(int line, int charPositionInLine, String msg, Throwable cause) {
        super(msg, cause);
        this.line = line;
        this.charPositionInLine = charPositionInLine;
    }

    /**
     * @return the line number in the source
     */
    public int getLine() {
        return line;
    }

    /**
     * @return position in source line closest to the problem
     */
    public int getCharPositionInLine() {
        return charPositionInLine;
    }

}
