//@formatter:off
/*
 * LambdaSupportLoggerProxy
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
package de.calamanari.pk.util;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

/**
 * {@link LambdaSupportLoggerProxy} is a PROXY to easier support lambda expressions as arguments to the SLF4J-Logger's logging methods.
 * <p>
 * <b>Usage:</b>
 * <ul>
 * <li>Import the defer(...)-method: <code>import static {@link de.calamanari.pk.util.LambdaSupportLoggerProxy#defer(Supplier)};</code></li>
 * <li>Wrap your logger: <code>private static final Logger LOGGER = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(DataManager.class));</code></li>
 * <li>Use the LOGGER as usual (no interface change).</li>
 * <li>Whenever you need a lambda-expression to be evaluated lately, pass the lambda as an argument using <code>defer(<i>expression</i>)</code>, example:
 * <code>LOGGER.debug("Instance of type {} found: @{}, id={}", type, <b><i>defer</i>(() -> Integer.toHexString(element.hashCode()))</b>, id);</code></li>
 * </ul>
 * <p>
 * <b>Side Notes:</b>
 * <p>
 * This work was part of a POC after reading through various discussions and blog-posts, for example:
 * <ul>
 * <li><a href=
 * "https://stackoverflow.com/questions/41255503/lambda-support-for-slf4j-api">https://stackoverflow.com/questions/41255503/lambda-support-for-slf4j-api</a></li>
 * <li><a href= "https://jira.qos.ch/browse/SLF4J-371">https://jira.qos.ch/browse/SLF4J-371</a></li>
 * </ul>
 * <p>
 * I read between the lines that the fluent-API is a decent solution but many users (including me) were looking for any simpler
 * <i>drop-in-and-be-happy-solution</i>.
 * <p>
 * This implementation is the best compromise I could find:
 * <ul>
 * <li>Minimally invasive: decision to use it or not can be made per class.</li>
 * <li>Lambdas can be specified on demand.</li>
 * <li>Simple logging statements remain clear and short, lambda-enriched statements have a still acceptable level of complexity.</li>
 * <li>We do not change the interface of the Logger, we do not change the existing logger usage paradigm.</li>
 * <li>Code analysis tools (like code helper or SonarLint) are not impacted at all.</li>
 * <li>The caller depth output (i.e. <a href="https://logback.qos.ch/manual/layouts.html">https://logback.qos.ch/manual/layouts.html</a>) may change (+1)
 * because the call to the real logger now comes from inside the PROXY.</li>
 * <li>The thin proxy layer adds minimal extra effort for testing all log arguments. Potentially, this may have an impact on scenarios with enabled logger and
 * super-high throughput.</li>
 * <li>This solution is a trade-off between 3 factors:
 * <ul>
 * <li><b>usability:</b> The original request was <i>simply specifying a lambda expressions as a log argument on demand</i>. This implementation almost
 * satisfies this requirement as the <i>defer()</i>-method might be perceived as a penalty.</li>
 * <li><b>continuity:</b> Except from the impact on the caller depth this solution is close to perfect: the user needs to do a minimal code adjustment when
 * declaring the logger.</li>
 * <li><b>effort:</b> Implementation effort was rather low. We accept some additional general runtime effort for argument testing.</li>
 * </ul>
 * Another POC has shown that better <b>usability</b> (eliminate need for the <i>defer()</i>-method can be achieved by implementing an ADAPTER (generated code
 * with a few hundred methods using argument permutation), but this would come at a high price: The implementation effort would be quite high, and we would
 * definitely break <b>continuity</b>. Tests with such an adapter indicated that IDE code assist and analysis features (SonaLint) out-of-the-box won't work with
 * the adapter in the same way they do with the plain logger. It is also questionable how IDEs react on classes with hundreds of methods. However, the runtime
 * (evaluation) effort would be smaller because unlike the PROXY-solution the ADAPTER-implementation with argument permutation won't require any argument
 * testing.
 * </ul>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class LambdaSupportLoggerProxy implements Logger {

    /**
     * Creates a wrapper for the underlying logger with extended interface
     * @param logger underlying logger for delegation
     * @return wrapper (enriched logger)
     */
    public static LambdaSupportLoggerProxy wrap(Logger logger) {
        return new LambdaSupportLoggerProxy(logger);
    }

    /**
     * This method performs a "cast" of the given supplier lambda expression to java.lang.Object to prepare it for later conditional execution by the logger.
     * @param supplier lambda expression (null will be handled gracefully)
     * @return supplier
     */
    public static final Object defer(Supplier<?> supplier) {
        return supplier;
    }

    private final Logger logger;

    private LambdaSupportLoggerProxy(Logger logger) {
        this.logger = logger;
    }

    // delegate all existing methods to the original logger

    public String getName() {
        return logger.getName();
    }

    @Override
    public LoggingEventBuilder makeLoggingEventBuilder(Level level) {
        return logger.makeLoggingEventBuilder(level);
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public void trace(String msg) {
        logger.trace(msg);
    }

    public void trace(String format, Object arg1) {
        logger.trace(format, eval(format, arg1, 1, isTraceEnabled()));
    }

    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format, eval(format, arg1, 1, isTraceEnabled()), eval(format, arg2, 2, isTraceEnabled()));
    }

    public void trace(String format, Object... arguments) {
        logger.trace(format, eval(format, arguments, 1, isTraceEnabled()));
    }

    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public LoggingEventBuilder atTrace() {
        return logger.atTrace();
    }

    public void trace(Marker marker, String msg) {
        logger.trace(marker, msg);
    }

    public void trace(Marker marker, String format, Object arg1) {
        logger.trace(marker, format, eval(format, arg1, 1, isTraceEnabled(marker)));
    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        logger.trace(marker, format, eval(format, arg1, 1, isTraceEnabled(marker)), eval(format, arg2, 2, isTraceEnabled(marker)));
    }

    public void trace(Marker marker, String format, Object... argArray) {
        logger.trace(marker, format, eval(format, argArray, 1, isTraceEnabled(marker)));
    }

    public void trace(Marker marker, String msg, Throwable t) {
        logger.trace(marker, msg, t);
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void debug(String format, Object arg1) {
        logger.debug(format, eval(format, arg1, 1, isDebugEnabled()));
    }

    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format, eval(format, arg1, 1, isDebugEnabled()), eval(format, arg2, 2, isDebugEnabled()));
    }

    public void debug(String format, Object... arguments) {
        logger.debug(format, eval(format, arguments, 1, isDebugEnabled()));
    }

    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    public void debug(Marker marker, String msg) {
        logger.debug(marker, msg);
    }

    public void debug(Marker marker, String format, Object arg1) {
        logger.debug(marker, format, eval(format, arg1, 1, isDebugEnabled(marker)));
    }

    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(marker, format, eval(format, arg1, 1, isDebugEnabled(marker)), eval(format, arg2, 2, isDebugEnabled(marker)));
    }

    public void debug(Marker marker, String format, Object... arguments) {
        logger.debug(marker, format, eval(format, arguments, 1, isDebugEnabled(marker)));
    }

    public void debug(Marker marker, String msg, Throwable t) {
        logger.debug(marker, msg, t);
    }

    @Override
    public LoggingEventBuilder atDebug() {
        return logger.atDebug();
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(String format, Object arg1) {
        logger.info(format, eval(format, arg1, 1, isInfoEnabled()));
    }

    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, eval(format, arg1, 1, isInfoEnabled()), eval(format, arg2, 2, isInfoEnabled()));
    }

    public void info(String format, Object... arguments) {
        logger.info(format, eval(format, arguments, 1, isInfoEnabled()));
    }

    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    public void info(Marker marker, String msg) {
        logger.info(marker, msg);
    }

    public void info(Marker marker, String format, Object arg1) {
        logger.info(marker, format, eval(format, arg1, 1, isInfoEnabled(marker)));
    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {
        logger.info(marker, format, eval(format, arg1, 1, isInfoEnabled(marker)), eval(format, arg2, 2, isInfoEnabled(marker)));
    }

    public void info(Marker marker, String format, Object... arguments) {
        logger.info(marker, format, eval(format, arguments, 1, isInfoEnabled(marker)));
    }

    public void info(Marker marker, String msg, Throwable t) {
        logger.info(marker, msg, t);
    }

    @Override
    public LoggingEventBuilder atInfo() {
        return logger.atInfo();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void warn(String format, Object arg1) {
        logger.warn(format, eval(format, arg1, 1, isWarnEnabled()));
    }

    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format, eval(format, arg1, 1, isWarnEnabled()), eval(format, arg2, 2, isWarnEnabled()));
    }

    public void warn(String format, Object... arguments) {
        logger.warn(format, eval(format, arguments, 1, isWarnEnabled()));
    }

    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    public void warn(Marker marker, String msg) {
        logger.warn(marker, msg);
    }

    public void warn(Marker marker, String format, Object arg1) {
        logger.warn(marker, format, eval(format, arg1, 1, isWarnEnabled(marker)));
    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        logger.warn(marker, format, eval(format, arg1, 1, isWarnEnabled(marker)), eval(format, arg2, 2, isWarnEnabled(marker)));
    }

    public void warn(Marker marker, String format, Object... arguments) {
        logger.warn(marker, format, eval(format, arguments, 1, isWarnEnabled(marker)));
    }

    public void warn(Marker marker, String msg, Throwable t) {
        logger.warn(marker, msg, t);
    }

    @Override
    public LoggingEventBuilder atWarn() {
        return logger.atWarn();
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public void error(String msg) {
        logger.error(msg);
    }

    public void error(String format, Object arg1) {
        logger.error(format, eval(format, arg1, 1, isErrorEnabled()));
    }

    public void error(String format, Object arg1, Object arg2) {
        logger.error(format, eval(format, arg1, 1, isErrorEnabled()), eval(format, arg2, 2, isErrorEnabled()));
    }

    public void error(String format, Object... arguments) {
        logger.error(format, eval(format, arguments, 1, isErrorEnabled()));
    }

    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    public void error(Marker marker, String msg) {
        logger.error(marker, msg);
    }

    public void error(Marker marker, String format, Object arg1) {
        logger.error(marker, format, eval(format, arg1, 1, isErrorEnabled(marker)));
    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {
        logger.error(marker, format, eval(format, arg1, 1, isErrorEnabled(marker)), eval(format, arg2, 2, isErrorEnabled(marker)));
    }

    public void error(Marker marker, String format, Object... arguments) {
        logger.error(marker, format, eval(format, arguments, 1, isErrorEnabled(marker)));
    }

    public void error(Marker marker, String msg, Throwable t) {
        logger.error(marker, msg, t);
    }

    @Override
    public LoggingEventBuilder atError() {
        return logger.atError();
    }

    // END: delegate all existing methods to the original logger

    /**
     * Tests the given object. If it is a supplier, it will be evaluated to return the result.
     * @param format original message for debugging
     * @param arg the possible supplier to be evaluated
     * @param argNumber position of the argument in the method call for debugging
     * @param convert if true (log level enabled) the method performs the conversion, otherwise not
     * @return evaluation result or arg (unchanged)
     */
    Object eval(String format, Object arg, int argNumber, boolean convert) {
        Object res = arg;
        if (convert && arg instanceof Supplier<?>) {
            // logger should not cause exceptions in case of bad supplier code
            try {
                res = ((Supplier<?>) arg).get();
            }
            catch (RuntimeException ex) {
                res = "SUPPLIER_EVAL_ERROR_ARG_" + argNumber;
                // potential supplier complexity requires option to "debug the logging"
                logger.trace("Evaluation of Supplier<?> failed for log method argument {} (after format) for message '{}'", argNumber, format, ex);
            }
        }
        return res;
    }

    /**
     * Tests all given array elements (non-recursive) and calls {@link #eval(String, Object, int, boolean)} to potentially convert them.
     * @param format original message for debugging
     * @param args array with arguments, will be modified if a supplier needed to be evaluated
     * @param argNumber position of the arguments array in the method call for debugging
     * @param convert if true (log level enabled) the method performs the conversion, otherwise not
     * @return args possibly modified array (supplier elements converted)
     */
    Object[] eval(String format, Object[] args, int argNumber, boolean convert) {
        int len = (args == null ? 0 : args.length);
        if (convert && len > 0) {
            for (int i = 0; i < len; i++) {
                args[i] = eval(format, args[i], argNumber + i, convert);
            }
        }
        return args;
    }

}
