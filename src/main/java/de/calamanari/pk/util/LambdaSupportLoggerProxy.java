//@formatter:off
/*
 * LambdaSupportLoggerProxy
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * Proxy with support for lambdas as arguments to the common logging methods of SLF4J Logger.
 * <p>
 * <b>Usage:</b>
 * <ul>
 * <li>Wrap the logger: <code>private static final Logger LOGGER = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(DataManager.class));</code></li>
 * <li>Import the defer(...)-method: <code>import static ..LambdaSupportLoggerProxy.defer;</code></li>
 * <li>Use the LOGGER as usual (no interface change).</li>
 * <li>Whenever you need a lambda-expression to be evaluated lately, pass the lambda as an argument using <code>defer(<i>expression</i>)</code>, example:
 * <code>LOGGER.debug("Instance of type {} found: @{}, id={}", type, <b><i>defer</i>(() -> Integer.toHexString(element.hashCode()))</b>, id);</code></li>
 * </ul>
 * <p>
 * <b>Motivation and implications:</b>
 * <p>
 * The journey started with SonaType complaints of the type <code>java:S2629</code> "Invoke method(s) only conditionally.".<br>
 * Some of my logging statements contained computed data. This computation effort should not be spent if the the corresponding log level is disabled. There is
 * an obvious way to deal with this: surround the logging statements with <code>if (LOGGER.isDebugEnabled() {...}</code> which creates two extra lines and is
 * error-prone as the level "debug" is now specified twice, in the condition and in the log-method-name (redundancy).<br>
 * However, for quite a while lambdas are around, so enthusiastically I looked for a newer version of slf4 or so and could not get satisfied.<br>
 * While the top dog Log4j2 has introduced basic lambda support, SLF4J only integrated it in the new fluent-API. The fluent-API may be a great piece of work,
 * however, I found several problems adopting it:
 * <ul>
 * <li>Due to architectural reasons the implementation had to go into the logger implementation, so effective lambda support depends on the adapter - or worse -
 * its precise version. In my case lockback-classic 1.3.0-alpha<b>4</b> with slf4j 2.0.0-alpha1 worked, 1.3.0-alpha<b>5</b> surprisingly did not. Such things
 * make me nervous.<br>
 * &nbsp;</li>
 * <li>The fluent API changes the look of log statements drastically. Example: <code>LOGGER.debug("Processing arguments a={}, b={}, c={}", a, b, c);</code> is
 * very clear to read. Now we want to add <i>d</i> which shall be a lambda.
 * <code>LOGGER.atDebug().addAgument(()->computeD()).log("Processing arguments d={} a={}, b={}, c={}", a, b, c);</code><br>
 * Hmm, but what if <i>d</i> should go last or if I wanted to mix arguments? I would end up with anything like this:
 * <code>LOGGER.atDebug().addArgument(a).addArgument(b).addArgument(c).addAgument(()->computeD()).log("Processing arguments d={} a={}, b={}, c={}");</code><br>
 * For new projects people might get used to this style, for existing projects with many existing classes it is not easy to deal with different styles.</li>
 * </ul>
 * <p>
 * In a moment of hubris I thought it should not be that complicated to quickly change the logger implementation to simply test every Object argument for being
 * a Supplier&lt;?&gt; and evaluate it only if the log-level is active. So, I decided to dig a little bit and read through some discussions and blogs. Soon, I
 * realized all the problems the logger implementors are fighting with. This is indeed a hard nut to crack. The user (means us, the developers) has a seemingly
 * simple request to specify a lambda <i>on demand</i> without any changes to the API of the logger or the style for writing log statements.<br>
 * Thus, the ultimate goal would be writing <code>LOGGER.debug("Processing arguments a={}, b={}, c={}, d={}", a, b, c, ()->computeD());</code>. Unfortunately,
 * this does not work with the current interface due to Java's implementation of lambdas requiring the specification of a Supplier&lt;?&gt;. The first problem
 * to solve is the question how to get the Supplier arguments in without getting too verbose.<br>
 * To achieve the ideal interface (from user perspective) I found no other way than creating <i>argument list permutations</i>. Every argument can be either an
 * Object or a Supplier&lt;?&gt;. A test has shown that we need to add (generate) hundreds of methods when we assume that a reasonable log statement won't
 * require more than 5 log parameters. A Java class can have thousands of methods without any runtime performance impact, but IDEs (code assist) are probably
 * not so happy with such a method variety. You should also be aware that IDE code analysis and SonaType magic may no longer be able to give all the useful
 * hints (like missing parameters) if you change the interface using a logger ADAPTER with an enriched interface. And finally, we do not want all these methods
 * to be part of the Logger <i>interface</i>. Anyway, a test has shown this approach would work and would give the user exactly what she wants, but I don't like
 * the implications.
 * <p>
 * Back to the drawing board ...
 * <p>
 * Hmm, what if we don't make it <i>perfect</i>? Is there any reasonable compromise? I think yes, if we accepted a little bit more code to specify a lambda as
 * an Object argument. Casting is possible but inconvenient:
 * <code>LOGGER.debug("Processing arguments a={}, b={}, c={}, d={}", a, b, c, (Supplier&lt;?&gt;) ()->computeD());</code><br>
 * Default code assist won't help and user-defined IDE-macros are not everybody's favorite. The solution I can live with is the <i>defer()</i>-method, which
 * just performs an implicit cast to Object.
 * <p>
 * Coming back to the original challenge there is a second problem to solve: Where should we evaluate the lambdas? Logger is an interface. So, adding the new
 * behavior would mean adjusting <i>every</i> implementation class. My earlier problem with the logback implementation and the fluent-API shows that this is not
 * trivial. At least for a POC this was too much work. Instead I decided to implement a PROXY (we add behavior and let the interface as-is) that deals with the
 * Supplier&lt;?&gt;-arguments to resolve them if - and only if - the log level is active. Then the PROXY delegates the remaining work to the underlying real
 * logger.
 * <p>
 * <b>Advantages:</b>
 * <p>
 * <ul>
 * <li>Minimally invasive: decision to use it or not can be made per class.</li>
 * <li>Lambdas can be specified on demand.</li>
 * <li>Simple logging statements remain clear and short, lambda-enriched statements have a still acceptable level of complexity.</li>
 * <li>We do not change the interface, we do not change the existing usage paradigm.</li>
 * <li>Code analysis tools (like code helper or SonaType) are not impacted at all.</li>
 * <li>This solution can easily be back-ported for any older slf4j-version.</li>
 * </ul>
 * <p>
 * <b>Limitations:</b>
 * <p>
 * The implementors of Java lambdas had to make some compromises to make the new language feature compatible to the existing Java world. Especially, the
 * requirement to only use <i>final</i> or <i>effectively final</i> variables in the expression can cause headaches. For logging statements this can be a real
 * pain. We want to write short statements and evaluate computed values only conditionally. All the efforts like this class or the fluent-API or the new methods
 * in Log4j2 target these goal. But none of them can eliminate the <i>final</i> or <i>effectively final</i> problem. <br>
 * Example: <code>LOGGER.debug("Instance found: @{}", Integer.toHexString(res.hashCode()));</code><br>
 * SonaType complains that we should do the computation <code>Integer.toHexString(res.hashCode())</code> only conditionally. Makes sense ...<br>
 * <code>LOGGER.debug("Instance found: @{}", defer(() -> Integer.toHexString(res.hashCode())));</code> <b>does not compile!</b><br>
 * The problem is now the variable "res" which is NOT (effectively) final. It is a typical result variable to ensure the method has exactly one return.<br>
 * In this example I could fix this:<br>
 * <code>final var resLog = res;</code><br>
 * <code>LOGGER.debug("Instance found: @{}", Integer.toHexString(resLog.hashCode()));</code><br>
 * This is not beautiful because a line of code has been added which seemingly does not contribute anything to the method's logic.
 * <p>
 * <b>Conclusion:</b>
 * <p>
 * This POC has shown that the idea works. I can use now lambdas for logging with minimal effort. But it seems, there is no ideal solution.<br>
 * Even if you implemented the ADAPTER solution with hundreds of methods and adjusted all code helpers and SonaType rules, the <i>(effectively) final
 * problem</i> won't go away.
 * <p>
 * Whatever logger you use, this is currently the price you have to pay: more and unrelated code to perform the logging with lambdas.
 * <p>
 * Maybe sometimes a simple <code>if (LOGGER.isDebugEnabled()) {...}</code> is just the better deal. <b>:)</b>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class LambdaSupportLoggerProxy implements Logger {

    /**
     * Creates a wrapper for the underlying logger with extended interface
     * @param logger
     * @return wrapper (enriched logger)
     */
    public static LambdaSupportLoggerProxy wrap(Logger logger) {
        return new LambdaSupportLoggerProxy(logger);
    }

    /**
     * This method performs a "cast" of the given supplier lambda expression to java.lang.Object to prepare it for later conditional execution by the logger.
     * @param supplier
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

    public void trace(String format, Object arg) {
        logger.trace(format, eval(format, arg, 1, isTraceEnabled()));
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

    public void trace(Marker marker, String format, Object arg) {
        logger.trace(marker, format, eval(format, arg, 1, isTraceEnabled(marker)));
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

    public void debug(String format, Object arg) {
        logger.debug(format, eval(format, arg, 1, isDebugEnabled()));
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

    public void debug(Marker marker, String format, Object arg) {
        logger.debug(marker, format, eval(format, arg, 1, isDebugEnabled(marker)));
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

    public void info(String format, Object arg) {
        logger.info(format, eval(format, arg, 1, isInfoEnabled()));
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

    public void info(Marker marker, String format, Object arg) {
        logger.info(marker, format, eval(format, arg, 1, isInfoEnabled(marker)));
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

    public void warn(String format, Object arg) {
        logger.warn(format, eval(format, arg, 1, isWarnEnabled()));
    }

    public void warn(String format, Object... arguments) {
        logger.warn(format, eval(format, arguments, 1, isWarnEnabled()));
    }

    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format, eval(format, arg1, 1, isWarnEnabled()), eval(format, arg2, 2, isWarnEnabled()));
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

    public void warn(Marker marker, String format, Object arg) {
        logger.warn(marker, format, eval(format, arg, 1, isWarnEnabled(marker)));
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

    public void error(String format, Object arg) {
        logger.error(format, eval(format, arg, 1, isErrorEnabled()));
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

    public void error(Marker marker, String format, Object arg) {
        logger.error(marker, format, eval(format, arg, 1, isErrorEnabled(marker)));
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
                if (logger.isTraceEnabled()) {
                    logger.trace("Evaluation of Supplier<?> failed at log argument {} for message '{}'", argNumber, format, ex);
                }
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
