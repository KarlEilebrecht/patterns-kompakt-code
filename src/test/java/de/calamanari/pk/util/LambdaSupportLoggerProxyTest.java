//@formatter:off
/*
 * LambdaSupportLoggerProxyTest
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

public class LambdaSupportLoggerProxyTest {

    @Test
    public void testDefer() {

        assertNull(LambdaSupportLoggerProxy.defer(null));
        Supplier<?> supplier = () -> "something";
        assertEquals(supplier, LambdaSupportLoggerProxy.defer(supplier));

    }

    @Test
    public void testEval() {

        LambdaSupportLoggerProxy logger = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(LambdaSupportLoggerProxyTest.class));

        String format = "FORMAT";

        Supplier<?> arg = () -> "goodCase";
        int argNumber = 1;

        assertEquals("goodCase", logger.eval(format, arg, argNumber, true));
        assertEquals(arg, logger.eval(format, arg, argNumber, false));

        Supplier<?> argInvalid = () -> {
            throw new RuntimeException("badCase");
        };
        assertEquals(argInvalid, logger.eval(format, argInvalid, argNumber, false));
        assertEquals("SUPPLIER_EVAL_ERROR_ARG_" + argNumber, logger.eval(format, argInvalid, argNumber, true));

    }

    @Test
    public void testEvalArray() {

        LambdaSupportLoggerProxy logger = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(LambdaSupportLoggerProxyTest.class));

        String format = "FORMAT";

        Supplier<?> arg = () -> "goodCase";
        int argNumber = 1;

        assertArrayEquals(new Object[] { "goodCase" }, logger.eval(format, new Object[] { arg }, argNumber, true));
        assertArrayEquals(new Object[] { arg }, logger.eval(format, new Object[] { arg }, argNumber, false));

        Supplier<?> argInvalid = () -> {
            throw new RuntimeException("badCase");
        };
        assertArrayEquals(new Object[] { arg, argInvalid }, logger.eval(format, new Object[] { arg, argInvalid }, argNumber, false));
        assertArrayEquals(new Object[] { "goodCase", "SUPPLIER_EVAL_ERROR_ARG_" + (argNumber + 1) },
                logger.eval(format, new Object[] { arg, argInvalid }, argNumber, true));

    }

    @Test
    public void testBasicDelegateGets() {
        Logger loggerMock;
        Logger logger;

        for (Level level : Level.values()) {
            String name = "Test" + level.name() + "NoMarker";
            loggerMock = prepareLoggerMock(name, level, null);
            logger = LambdaSupportLoggerProxy.wrap(loggerMock);

            assertEquals(name, logger.getName());
            assertLevelSet(logger, level);

            assertNotNull(logger.atError());
            assertSame(loggerMock.atError(), logger.atError());
            assertNotNull(logger.atWarn());
            assertSame(loggerMock.atWarn(), logger.atWarn());
            assertNotNull(logger.atInfo());
            assertSame(loggerMock.atInfo(), logger.atInfo());
            assertNotNull(logger.atDebug());
            assertSame(loggerMock.atDebug(), logger.atDebug());
            assertNotNull(logger.atTrace());
            assertSame(loggerMock.atTrace(), logger.atTrace());
            assertNotNull(logger.makeLoggingEventBuilder(level));
            assertSame(loggerMock.makeLoggingEventBuilder(level), logger.makeLoggingEventBuilder(level));

        }

        Marker marker = createMarker("correctMarker");
        Marker wrongMarker = createMarker("wrongMarker");

        for (Level level : Level.values()) {
            String name = "Test" + level.name() + "Marker" + marker.getName();
            loggerMock = prepareLoggerMock(name, level, marker);
            logger = LambdaSupportLoggerProxy.wrap(loggerMock);

            assertEquals(name, logger.getName());
            assertLevelSet(logger, level, marker, wrongMarker);
        }

    }

    @Test
    public void testErrorPassThrough() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughError";
        Logger loggerMock = prepareLoggerMock(name, Level.ERROR, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.error("msg1");
        Mockito.verify(loggerMock).error(eq("msg1"));

        Exception ex = new Exception();
        logger.error("msg1e", ex);
        Mockito.verify(loggerMock).error(eq("msg1e"), eq(ex));

        logger.error("msg2", "foo");
        Mockito.verify(loggerMock).error(eq("msg2"), eq("foo"));

        logger.error("msg3", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).error(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.error("msg4", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).error(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        logger.error(marker, "msg1_M");
        Mockito.verify(loggerMock).error(eq(marker), eq("msg1_M"));

        logger.error(marker, "msg1eM", ex);
        Mockito.verify(loggerMock).error(eq(marker), eq("msg1eM"), eq(ex));

        logger.error(marker, "msg2_M", "foo");
        Mockito.verify(loggerMock).error(eq(marker), eq("msg2_M"), eq("foo"));

        logger.error(marker, "msg3_M", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).error(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.error(marker, "msg4_M", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).error(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        Mockito.verify(loggerMock, Mockito.times(4)).isErrorEnabled();
        Mockito.verify(loggerMock, Mockito.times(4)).isErrorEnabled(marker);

    }

    @Test
    public void testWarnPassThrough() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughWarn";
        Logger loggerMock = prepareLoggerMock(name, Level.WARN, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.warn("msg1");
        Mockito.verify(loggerMock).warn(eq("msg1"));

        Exception ex = new Exception();
        logger.warn("msg1e", ex);
        Mockito.verify(loggerMock).warn(eq("msg1e"), eq(ex));

        logger.warn("msg2", "foo");
        Mockito.verify(loggerMock).warn(eq("msg2"), eq("foo"));

        logger.warn("msg3", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).warn(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.warn("msg4", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).warn(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        logger.warn(marker, "msg1_M");
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg1_M"));

        logger.warn(marker, "msg1eM", ex);
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg1eM"), eq(ex));

        logger.warn(marker, "msg2_M", "foo");
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg2_M"), eq("foo"));

        logger.warn(marker, "msg3_M", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.warn(marker, "msg4_M", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        Mockito.verify(loggerMock, Mockito.times(4)).isWarnEnabled();
        Mockito.verify(loggerMock, Mockito.times(4)).isWarnEnabled(marker);

    }

    @Test
    public void testInfoPassThrough() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughinfo";
        Logger loggerMock = prepareLoggerMock(name, Level.INFO, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.info("msg1");
        Mockito.verify(loggerMock).info(eq("msg1"));

        Exception ex = new Exception();
        logger.info("msg1e", ex);
        Mockito.verify(loggerMock).info(eq("msg1e"), eq(ex));

        logger.info("msg2", "foo");
        Mockito.verify(loggerMock).info(eq("msg2"), eq("foo"));

        logger.info("msg3", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).info(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.info("msg4", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).info(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        logger.info(marker, "msg1_M");
        Mockito.verify(loggerMock).info(eq(marker), eq("msg1_M"));

        logger.info(marker, "msg1eM", ex);
        Mockito.verify(loggerMock).info(eq(marker), eq("msg1eM"), eq(ex));

        logger.info(marker, "msg2_M", "foo");
        Mockito.verify(loggerMock).info(eq(marker), eq("msg2_M"), eq("foo"));

        logger.info(marker, "msg3_M", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).info(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.info(marker, "msg4_M", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).info(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        Mockito.verify(loggerMock, Mockito.times(4)).isInfoEnabled();
        Mockito.verify(loggerMock, Mockito.times(4)).isInfoEnabled(marker);

    }

    @Test
    public void testDebugPassThrough() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughdebug";
        Logger loggerMock = prepareLoggerMock(name, Level.DEBUG, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.debug("msg1");
        Mockito.verify(loggerMock).debug(eq("msg1"));

        Exception ex = new Exception();
        logger.debug("msg1e", ex);
        Mockito.verify(loggerMock).debug(eq("msg1e"), eq(ex));

        logger.debug("msg2", "foo");
        Mockito.verify(loggerMock).debug(eq("msg2"), eq("foo"));

        logger.debug("msg3", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).debug(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.debug("msg4", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).debug(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        logger.debug(marker, "msg1_M");
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg1_M"));

        logger.debug(marker, "msg1eM", ex);
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg1eM"), eq(ex));

        logger.debug(marker, "msg2_M", "foo");
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg2_M"), eq("foo"));

        logger.debug(marker, "msg3_M", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.debug(marker, "msg4_M", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        Mockito.verify(loggerMock, Mockito.times(4)).isDebugEnabled();
        Mockito.verify(loggerMock, Mockito.times(4)).isDebugEnabled(marker);

    }

    @Test
    public void testTracePassThrough() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughtrace";
        Logger loggerMock = prepareLoggerMock(name, Level.TRACE, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.trace("msg1");
        Mockito.verify(loggerMock).trace(eq("msg1"));

        Exception ex = new Exception();
        logger.trace("msg1e", ex);
        Mockito.verify(loggerMock).trace(eq("msg1e"), eq(ex));

        logger.trace("msg2", "foo");
        Mockito.verify(loggerMock).trace(eq("msg2"), eq("foo"));

        logger.trace("msg3", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).trace(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.trace("msg4", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).trace(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        logger.trace(marker, "msg1_M");
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg1_M"));

        logger.trace(marker, "msg1eM", ex);
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg1eM"), eq(ex));

        logger.trace(marker, "msg2_M", "foo");
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg2_M"), eq("foo"));

        logger.trace(marker, "msg3_M", "foo", Integer.valueOf(6));
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.trace(marker, "msg4_M", "foo", Integer.valueOf(7), "bar");
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        Mockito.verify(loggerMock, Mockito.times(4)).isTraceEnabled();
        Mockito.verify(loggerMock, Mockito.times(4)).isTraceEnabled(marker);

    }

    @Test
    public void testErrorEval() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughError";
        Logger loggerMock = prepareLoggerMock(name, Level.ERROR, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        Supplier<?> foo = () -> "foo";
        logger.error("msg2", foo);
        Mockito.verify(loggerMock).error(eq("msg2"), eq("foo"));

        // warn is off, expect no eval
        logger.warn("msg2", foo);
        Mockito.verify(loggerMock).warn(eq("msg2"), eq(foo));

        Supplier<?> int6 = () -> Integer.valueOf(6);
        logger.error("msg3", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).error(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.error("msg3", "foo+", int6);
        Mockito.verify(loggerMock).error(eq("msg3"), eq("foo+"), eq(Integer.valueOf(6)));

        // warn is off, expect no eval
        logger.warn("msg3", foo, int6);
        Mockito.verify(loggerMock).warn(eq("msg3"), eq(foo), eq(int6));

        Supplier<?> int7 = () -> Integer.valueOf(7);
        Supplier<?> bar = () -> "bar";
        logger.error("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).error(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // warn is off, expect no eval
        logger.warn("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).warn(eq("msg4"), eq("foo"), eq(int7), eq(bar));

        loggerMock = prepareLoggerMock(name, Level.ERROR, marker);
        logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.error(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).error(eq(marker), eq("msg2_M"), eq("foo"));

        // warn is off, expect no eval
        logger.warn(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg2_M"), eq(foo));

        logger.error(marker, "msg3_M", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).error(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.error(marker, "msg3_M", "foo+", int6);
        Mockito.verify(loggerMock).error(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        // warn is off, expect no eval
        logger.warn(marker, "msg3_M", foo, int6);
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg3_M"), eq(foo), eq(int6));

        logger.error(marker, "msg4_M", "foo", int7, "bar");
        Mockito.verify(loggerMock).error(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // warn is off, expect no eval
        logger.warn(marker, "msg4_M", foo, int7, "bar");
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg4_M"), eq(foo), eq(int7), eq("bar"));

        Supplier<?> argInvalid = () -> {
            throw new RuntimeException("BadSupplier");
        };

        logger.error(marker, "msg4_M", "foo", int7, "bar", argInvalid);
        Mockito.verify(loggerMock).error(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"), eq("SUPPLIER_EVAL_ERROR_ARG_4"));

    }

    @Test
    public void testWarnEval() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughWarn";
        Logger loggerMock = prepareLoggerMock(name, Level.WARN, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        Supplier<?> foo = () -> "foo";
        logger.warn("msg2", foo);
        Mockito.verify(loggerMock).warn(eq("msg2"), eq("foo"));

        // info is off, expect no eval
        logger.info("msg2", foo);
        Mockito.verify(loggerMock).info(eq("msg2"), eq(foo));

        Supplier<?> int6 = () -> Integer.valueOf(6);
        logger.warn("msg3", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).warn(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.warn("msg3", "foo+", int6);
        Mockito.verify(loggerMock).warn(eq("msg3"), eq("foo+"), eq(Integer.valueOf(6)));

        // info is off, expect no eval
        logger.info("msg3", foo, int6);
        Mockito.verify(loggerMock).info(eq("msg3"), eq(foo), eq(int6));

        Supplier<?> int7 = () -> Integer.valueOf(7);
        Supplier<?> bar = () -> "bar";
        logger.warn("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).warn(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // info is off, expect no eval
        logger.info("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).info(eq("msg4"), eq("foo"), eq(int7), eq(bar));

        loggerMock = prepareLoggerMock(name, Level.WARN, marker);
        logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.warn(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg2_M"), eq("foo"));

        // info is off, expect no eval
        logger.info(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).info(eq(marker), eq("msg2_M"), eq(foo));

        logger.warn(marker, "msg3_M", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.warn(marker, "msg3_M", "foo+", int6);
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        // info is off, expect no eval
        logger.info(marker, "msg3_M", foo, int6);
        Mockito.verify(loggerMock).info(eq(marker), eq("msg3_M"), eq(foo), eq(int6));

        logger.warn(marker, "msg4_M", "foo", int7, "bar");
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // info is off, expect no eval
        logger.info(marker, "msg4_M", foo, int7, "bar");
        Mockito.verify(loggerMock).info(eq(marker), eq("msg4_M"), eq(foo), eq(int7), eq("bar"));

        Supplier<?> argInvalid = () -> {
            throw new RuntimeException("BadSupplier");
        };

        logger.warn(marker, "msg4_M", "foo", int7, "bar", argInvalid);
        Mockito.verify(loggerMock).warn(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"), eq("SUPPLIER_EVAL_ERROR_ARG_4"));

    }

    @Test
    public void testInfoEval() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughinfo";
        Logger loggerMock = prepareLoggerMock(name, Level.INFO, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        Supplier<?> foo = () -> "foo";
        logger.info("msg2", foo);
        Mockito.verify(loggerMock).info(eq("msg2"), eq("foo"));

        // debug is off, expect no eval
        logger.debug("msg2", foo);
        Mockito.verify(loggerMock).debug(eq("msg2"), eq(foo));

        Supplier<?> int6 = () -> Integer.valueOf(6);
        logger.info("msg3", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).info(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.info("msg3", "foo+", int6);
        Mockito.verify(loggerMock).info(eq("msg3"), eq("foo+"), eq(Integer.valueOf(6)));

        // debug is off, expect no eval
        logger.debug("msg3", foo, int6);
        Mockito.verify(loggerMock).debug(eq("msg3"), eq(foo), eq(int6));

        Supplier<?> int7 = () -> Integer.valueOf(7);
        Supplier<?> bar = () -> "bar";
        logger.info("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).info(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // debug is off, expect no eval
        logger.debug("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).debug(eq("msg4"), eq("foo"), eq(int7), eq(bar));

        loggerMock = prepareLoggerMock(name, Level.INFO, marker);
        logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.info(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).info(eq(marker), eq("msg2_M"), eq("foo"));

        // debug is off, expect no eval
        logger.debug(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg2_M"), eq(foo));

        logger.info(marker, "msg3_M", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).info(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.info(marker, "msg3_M", "foo+", int6);
        Mockito.verify(loggerMock).info(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        // debug is off, expect no eval
        logger.debug(marker, "msg3_M", foo, int6);
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg3_M"), eq(foo), eq(int6));

        logger.info(marker, "msg4_M", "foo", int7, "bar");
        Mockito.verify(loggerMock).info(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // debug is off, expect no eval
        logger.debug(marker, "msg4_M", foo, int7, "bar");
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg4_M"), eq(foo), eq(int7), eq("bar"));

        Supplier<?> argInvalid = () -> {
            throw new RuntimeException("BadSupplier");
        };

        logger.info(marker, "msg4_M", "foo", int7, "bar", argInvalid);
        Mockito.verify(loggerMock).info(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"), eq("SUPPLIER_EVAL_ERROR_ARG_4"));

    }

    @Test
    public void testDebugEval() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughdebug";
        Logger loggerMock = prepareLoggerMock(name, Level.DEBUG, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        Supplier<?> foo = () -> "foo";
        logger.debug("msg2", foo);
        Mockito.verify(loggerMock).debug(eq("msg2"), eq("foo"));

        // trace is off, expect no eval
        logger.trace("msg2", foo);
        Mockito.verify(loggerMock).trace(eq("msg2"), eq(foo));

        Supplier<?> int6 = () -> Integer.valueOf(6);
        logger.debug("msg3", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).debug(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.debug("msg3", "foo+", int6);
        Mockito.verify(loggerMock).debug(eq("msg3"), eq("foo+"), eq(Integer.valueOf(6)));

        // trace is off, expect no eval
        logger.trace("msg3", foo, int6);
        Mockito.verify(loggerMock).trace(eq("msg3"), eq(foo), eq(int6));

        Supplier<?> int7 = () -> Integer.valueOf(7);
        Supplier<?> bar = () -> "bar";
        logger.debug("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).debug(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // trace is off, expect no eval
        logger.trace("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).trace(eq("msg4"), eq("foo"), eq(int7), eq(bar));

        loggerMock = prepareLoggerMock(name, Level.DEBUG, marker);
        logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.debug(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg2_M"), eq("foo"));

        // trace is off, expect no eval
        logger.trace(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg2_M"), eq(foo));

        logger.debug(marker, "msg3_M", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.debug(marker, "msg3_M", "foo+", int6);
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        // trace is off, expect no eval
        logger.trace(marker, "msg3_M", foo, int6);
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg3_M"), eq(foo), eq(int6));

        logger.debug(marker, "msg4_M", "foo", int7, "bar");
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // trace is off, expect no eval
        logger.trace(marker, "msg4_M", foo, int7, "bar");
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg4_M"), eq(foo), eq(int7), eq("bar"));

        Supplier<?> argInvalid = () -> {
            throw new RuntimeException("BadSupplier");
        };

        logger.debug(marker, "msg4_M", "foo", int7, "bar", argInvalid);
        Mockito.verify(loggerMock).debug(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"), eq("SUPPLIER_EVAL_ERROR_ARG_4"));

    }

    @Test
    public void testTraceEval() {

        // we just expect the exact same arguments to be passed to the underlying logger

        Marker marker = createMarker("anyMarker");

        String name = "TestPassThroughtrace";
        Logger loggerMock = prepareLoggerMock(name, Level.TRACE, null);
        Logger logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        Supplier<?> foo = () -> "foo";
        logger.trace("msg2", foo);
        Mockito.verify(loggerMock).trace(eq("msg2"), eq("foo"));

        // error is off, expect no eval
        logger.error("msg2", foo);
        Mockito.verify(loggerMock).error(eq("msg2"), eq(foo));

        Supplier<?> int6 = () -> Integer.valueOf(6);
        logger.trace("msg3", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).trace(eq("msg3"), eq("foo"), eq(Integer.valueOf(6)));

        logger.trace("msg3", "foo+", int6);
        Mockito.verify(loggerMock).trace(eq("msg3"), eq("foo+"), eq(Integer.valueOf(6)));

        // error is off, expect no eval
        logger.error("msg3", foo, int6);
        Mockito.verify(loggerMock).error(eq("msg3"), eq(foo), eq(int6));

        Supplier<?> int7 = () -> Integer.valueOf(7);
        Supplier<?> bar = () -> "bar";
        logger.trace("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).trace(eq("msg4"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // error is off, expect no eval
        logger.error("msg4", "foo", int7, bar);
        Mockito.verify(loggerMock).error(eq("msg4"), eq("foo"), eq(int7), eq(bar));

        loggerMock = prepareLoggerMock(name, Level.TRACE, marker);
        logger = LambdaSupportLoggerProxy.wrap(loggerMock);

        logger.trace(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg2_M"), eq("foo"));

        // error is off, expect no eval
        logger.error(marker, "msg2_M", foo);
        Mockito.verify(loggerMock).error(eq(marker), eq("msg2_M"), eq(foo));

        logger.trace(marker, "msg3_M", foo, Integer.valueOf(6));
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        logger.trace(marker, "msg3_M", "foo+", int6);
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg3_M"), eq("foo"), eq(Integer.valueOf(6)));

        // error is off, expect no eval
        logger.error(marker, "msg3_M", foo, int6);
        Mockito.verify(loggerMock).error(eq(marker), eq("msg3_M"), eq(foo), eq(int6));

        logger.trace(marker, "msg4_M", "foo", int7, "bar");
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"));

        // error is off, expect no eval
        logger.error(marker, "msg4_M", foo, int7, "bar");
        Mockito.verify(loggerMock).error(eq(marker), eq("msg4_M"), eq(foo), eq(int7), eq("bar"));

        Supplier<?> argInvalid = () -> {
            throw new RuntimeException("BadSupplier");
        };

        logger.trace(marker, "msg4_M", "foo", int7, "bar", argInvalid);
        Mockito.verify(loggerMock).trace(eq(marker), eq("msg4_M"), eq("foo"), eq(Integer.valueOf(7)), eq("bar"), eq("SUPPLIER_EVAL_ERROR_ARG_4"));

    }

    private void assertLevelSet(Logger logger, Level checkLevel) {
        for (Level level : Level.values()) {

            if (level == Level.ERROR) {
                if (checkLevel == level) {
                    assertTrue(logger.isErrorEnabled());
                }
                else {
                    assertFalse(logger.isErrorEnabled());
                }
            }
            else if (level == Level.WARN) {
                if (checkLevel == level) {
                    assertTrue(logger.isWarnEnabled());
                }
                else {
                    assertFalse(logger.isWarnEnabled());
                }
            }
            else if (level == Level.INFO) {
                if (checkLevel == level) {
                    assertTrue(logger.isInfoEnabled());
                }
                else {
                    assertFalse(logger.isInfoEnabled());
                }
            }
            else if (level == Level.DEBUG) {
                if (checkLevel == level) {
                    assertTrue(logger.isDebugEnabled());
                }
                else {
                    assertFalse(logger.isDebugEnabled());
                }
            }
            else if (level == Level.TRACE) {
                if (checkLevel == level) {
                    assertTrue(logger.isTraceEnabled());
                }
                else {
                    assertFalse(logger.isTraceEnabled());
                }
            }

        }
    }

    private void assertLevelSet(Logger logger, Level checkLevel, Marker marker, Marker wrongMarker) {
        for (Level level : Level.values()) {

            if (level == Level.ERROR) {
                if (checkLevel == level) {
                    assertTrue(logger.isErrorEnabled(marker));
                    assertFalse(logger.isErrorEnabled(wrongMarker));
                }
                else {
                    assertFalse(logger.isErrorEnabled(marker));
                    assertFalse(logger.isErrorEnabled(wrongMarker));
                }
            }
            else if (level == Level.WARN) {
                if (checkLevel == level) {
                    assertTrue(logger.isWarnEnabled(marker));
                    assertFalse(logger.isWarnEnabled(wrongMarker));
                }
                else {
                    assertFalse(logger.isWarnEnabled(marker));
                    assertFalse(logger.isWarnEnabled(wrongMarker));
                }
            }
            else if (level == Level.INFO) {
                if (checkLevel == level) {
                    assertTrue(logger.isInfoEnabled(marker));
                    assertFalse(logger.isInfoEnabled(wrongMarker));
                }
                else {
                    assertFalse(logger.isInfoEnabled(marker));
                    assertFalse(logger.isInfoEnabled(wrongMarker));
                }
            }
            else if (level == Level.DEBUG) {
                if (checkLevel == level) {
                    assertTrue(logger.isDebugEnabled(marker));
                    assertFalse(logger.isDebugEnabled(wrongMarker));
                }
                else {
                    assertFalse(logger.isDebugEnabled(marker));
                    assertFalse(logger.isDebugEnabled(wrongMarker));
                }
            }
            else if (level == Level.TRACE) {
                if (checkLevel == level) {
                    assertTrue(logger.isTraceEnabled(marker));
                    assertFalse(logger.isTraceEnabled(wrongMarker));
                }
                else {
                    assertFalse(logger.isTraceEnabled(marker));
                    assertFalse(logger.isTraceEnabled(wrongMarker));
                }
            }

        }
    }

    private Marker createMarker(String markerName) {
        Marker marker = null;
        if (markerName != null) {
            marker = Mockito.mock(Marker.class);
            when(marker.getName()).thenReturn(markerName);
        }
        return marker;
    }

    private Logger prepareLoggerMock(String loggerName, Level level, Marker marker) {

        Logger loggerMock = Mockito.mock(Logger.class);

        when(loggerMock.getName()).thenReturn(loggerName);

        LoggingEventBuilder lebError = Mockito.mock(LoggingEventBuilder.class);
        when(loggerMock.makeLoggingEventBuilder(Level.ERROR)).thenReturn(lebError);
        when(loggerMock.atError()).thenReturn(lebError);
        LoggingEventBuilder lebWarn = Mockito.mock(LoggingEventBuilder.class);
        when(loggerMock.makeLoggingEventBuilder(Level.WARN)).thenReturn(lebWarn);
        when(loggerMock.atWarn()).thenReturn(lebWarn);
        LoggingEventBuilder lebInfo = Mockito.mock(LoggingEventBuilder.class);
        when(loggerMock.makeLoggingEventBuilder(Level.INFO)).thenReturn(lebInfo);
        when(loggerMock.atInfo()).thenReturn(lebInfo);
        LoggingEventBuilder lebDebug = Mockito.mock(LoggingEventBuilder.class);
        when(loggerMock.makeLoggingEventBuilder(Level.DEBUG)).thenReturn(lebDebug);
        when(loggerMock.atDebug()).thenReturn(lebDebug);
        LoggingEventBuilder lebTrace = Mockito.mock(LoggingEventBuilder.class);
        when(loggerMock.makeLoggingEventBuilder(Level.TRACE)).thenReturn(lebTrace);
        when(loggerMock.atTrace()).thenReturn(lebTrace);

        if (level == Level.ERROR) {
            if (marker != null) {
                when(loggerMock.isErrorEnabled(marker)).thenReturn(true);
            }
            else {
                when(loggerMock.isErrorEnabled()).thenReturn(true);
            }
        }
        else if (level == Level.WARN) {
            if (marker != null) {
                when(loggerMock.isWarnEnabled(marker)).thenReturn(true);
            }
            else {
                when(loggerMock.isWarnEnabled()).thenReturn(true);
            }
        }
        else if (level == Level.INFO) {
            if (marker != null) {
                when(loggerMock.isInfoEnabled(marker)).thenReturn(true);
            }
            else {
                when(loggerMock.isInfoEnabled()).thenReturn(true);
            }
        }
        else if (level == Level.DEBUG) {
            if (marker != null) {
                when(loggerMock.isDebugEnabled(marker)).thenReturn(true);
            }
            else {
                when(loggerMock.isDebugEnabled()).thenReturn(true);
            }
        }
        else if (level == Level.TRACE) {
            if (marker != null) {
                when(loggerMock.isTraceEnabled(marker)).thenReturn(true);
            }
            else {
                when(loggerMock.isTraceEnabled()).thenReturn(true);
            }
        }

        return loggerMock;

    }

}
