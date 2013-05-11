/*
 * All Tests - Test suite for all pattern tests.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.calamanari.pk.abstractfactory.test.AbstractFactoryTest;
import de.calamanari.pk.activeobject.test.ActiveObjectTest;
import de.calamanari.pk.adapter.test.AdapterTest;
import de.calamanari.pk.bridge.test.BridgeTest;
import de.calamanari.pk.builder.test.BuilderTest;
import de.calamanari.pk.coarsegrainedlock.test.CoarseGrainedLockTest;
import de.calamanari.pk.combinedmethod.test.CombinedMethodTest;
import de.calamanari.pk.command.test.CommandTest;
import de.calamanari.pk.commandprocessor.test.CommandProcessorTest;
import de.calamanari.pk.composite.test.CompositeTest;
import de.calamanari.pk.datatransferobject.test.DataTransferObjectTest;
import de.calamanari.pk.decorator.test.DecoratorTest;
import de.calamanari.pk.dependencyinjection.test.DependencyInjectionTest;
import de.calamanari.pk.facade.test.FacadeTest;
import de.calamanari.pk.factorymethod.test.FactoryMethodTest;
import de.calamanari.pk.flyweight.test.FlyweightTest;
import de.calamanari.pk.gateway.test.GatewayTest;
import de.calamanari.pk.identityfield.test.IdentityFieldTest;
import de.calamanari.pk.identitymap.test.IdentityMapTest;
import de.calamanari.pk.iterator.test.IteratorTest;
import de.calamanari.pk.lazyload.test.LazyLoadTest;
import de.calamanari.pk.mapper.test.MapperTest;
import de.calamanari.pk.masterslave.test.MasterSlaveTest;
import de.calamanari.pk.modelviewcontroller.test.ModelViewControllerTest;
import de.calamanari.pk.money.test.MoneyTest;
import de.calamanari.pk.nullobject.test.NullObjectTest;
import de.calamanari.pk.objectpool.test.ObjectPoolTest;
import de.calamanari.pk.objectpool.test.SimpleThreadPoolTest;
import de.calamanari.pk.observer.test.ObserverTest;
import de.calamanari.pk.optimisticofflinelock.test.OptimisticOfflineLockTest;
import de.calamanari.pk.pessimisticofflinelock.test.PessimisticOfflineLockTest;
import de.calamanari.pk.plugin.test.PluginTest;
import de.calamanari.pk.proxy.test.ProxyTest;
import de.calamanari.pk.registry.test.RegistryTest;
import de.calamanari.pk.sequenceblock.test.SequenceBlockTest;
import de.calamanari.pk.servicestub.test.ServiceStubTest;
import de.calamanari.pk.singleton.test.SingletonTest;
import de.calamanari.pk.strategy.test.StrategyTest;
import de.calamanari.pk.templatemethod.test.TemplateMethodTest;
import de.calamanari.pk.transferobjectassembler.test.TransferObjectAssemblerTest;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;
import de.calamanari.pk.util.test.IndexedTextFileAccessorTest;
import de.calamanari.pk.util.test.ParallelFileInputStreamTest;
import de.calamanari.pk.uuid.test.UUIDTest;
import de.calamanari.pk.valueobject.test.ValueObjectTest;
import de.calamanari.pk.visitor.test.VisitorTest;
import de.calamanari.pk.wrapper.test.WrapperTest;

/**
 * All Tests - Test suite for all pattern tests.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
@RunWith(Suite.class)
@SuiteClasses({
//@formatter:off
	AbstractFactoryTest.class,
	ActiveObjectTest.class,
	AdapterTest.class,
	BridgeTest.class,
	BuilderTest.class,
	CoarseGrainedLockTest.class,
	CombinedMethodTest.class,
	CommandProcessorTest.class,
	CommandTest.class,
	CompositeTest.class,
	DataTransferObjectTest.class,
	DecoratorTest.class,
	DependencyInjectionTest.class,
	FacadeTest.class,
	FactoryMethodTest.class,
	FlyweightTest.class,
	GatewayTest.class,
	IdentityFieldTest.class,
	IdentityMapTest.class,
	IndexedTextFileAccessorTest.class,
	IteratorTest.class,
	LazyLoadTest.class,
	MapperTest.class,
	MasterSlaveTest.class,
	ModelViewControllerTest.class,
	MoneyTest.class,
	NullObjectTest.class,
	ObjectPoolTest.class,
	ObserverTest.class,
	OptimisticOfflineLockTest.class,
	ParallelFileInputStreamTest.class,
	PessimisticOfflineLockTest.class,
	PluginTest.class,
	ProxyTest.class,
	RegistryTest.class,
	SequenceBlockTest.class,
	ServiceStubTest.class,
	SimpleThreadPoolTest.class,
	SingletonTest.class,
	StrategyTest.class,
	TemplateMethodTest.class,
	TransferObjectAssemblerTest.class,
	UUIDTest.class,
	ValueObjectTest.class,
	VisitorTest.class,
	WrapperTest.class
	//@formatter:on
})
public final class AllTests {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(AllTests.class.getName());

    /**
     * Time tests began
     */
    private static long startTimeNanos;

    /**
     * No instance required
     */
    private AllTests() {
        // not required
    }

    /**
     * configure loggers before tests
     */
    @BeforeClass
    public static void beforeAll() {
        LogUtils.setConsoleHandlerLogLevel(Level.INFO);
        LogUtils.setLogLevel(Level.INFO, AllTests.class);
        startTimeNanos = System.nanoTime();
        LOGGER.info("Running AllTests ...");
    }

    /**
     * write finish message after tests
     */
    @AfterClass
    public static void afterAll() {
        LOGGER.info("AllTests completed. Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}
