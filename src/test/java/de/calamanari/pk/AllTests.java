//@formatter:off
/*
 * All Tests - Test suite for all pattern tests.
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
package de.calamanari.pk;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.abstractfactory.AbstractFactoryTest;
import de.calamanari.pk.activeobject.ActiveObjectTest;
import de.calamanari.pk.adapter.AdapterTest;
import de.calamanari.pk.bridge.BridgeTest;
import de.calamanari.pk.builder.BuilderTest;
import de.calamanari.pk.coarsegrainedlock.CoarseGrainedLockTest;
import de.calamanari.pk.combinedmethod.CombinedMethodTest;
import de.calamanari.pk.command.CommandTest;
import de.calamanari.pk.commandprocessor.CommandProcessorTest;
import de.calamanari.pk.composite.CompositeTest;
import de.calamanari.pk.datatransferobject.DataTransferObjectTest;
import de.calamanari.pk.decorator.DecoratorTest;
import de.calamanari.pk.dependencyinjection.DependencyInjectionTest;
import de.calamanari.pk.facade.FacadeTest;
import de.calamanari.pk.factorymethod.FactoryMethodTest;
import de.calamanari.pk.flyweight.FlyweightTest;
import de.calamanari.pk.gateway.GatewayTest;
import de.calamanari.pk.identityfield.IdentityFieldTest;
import de.calamanari.pk.identitymap.IdentityMapTest;
import de.calamanari.pk.iterator.IteratorTest;
import de.calamanari.pk.lazyload.LazyLoadTest;
import de.calamanari.pk.mapper.MapperTest;
import de.calamanari.pk.masterslave.MasterSlaveTest;
import de.calamanari.pk.modelviewcontroller.ModelViewControllerTest;
import de.calamanari.pk.money.MoneyTest;
import de.calamanari.pk.nullobject.NullObjectTest;
import de.calamanari.pk.objectpool.ObjectPoolTest;
import de.calamanari.pk.objectpool.SimpleThreadPoolTest;
import de.calamanari.pk.observer.ObserverTest;
import de.calamanari.pk.optimisticofflinelock.OptimisticOfflineLockTest;
import de.calamanari.pk.pessimisticofflinelock.PessimisticOfflineLockTest;
import de.calamanari.pk.plugin.PluginTest;
import de.calamanari.pk.proxy.ProxyTest;
import de.calamanari.pk.registry.RegistryTest;
import de.calamanari.pk.sequenceblock.SequenceBlockTest;
import de.calamanari.pk.servicestub.ServiceStubTest;
import de.calamanari.pk.singleton.SingletonTest;
import de.calamanari.pk.strategy.StrategyTest;
import de.calamanari.pk.templatemethod.TemplateMethodTest;
import de.calamanari.pk.transferobjectassembler.TransferObjectAssemblerTest;
import de.calamanari.pk.util.IndexedTextFileAccessorTest;
import de.calamanari.pk.util.ParallelFileInputStreamTest;
import de.calamanari.pk.util.TimeUtils;
import de.calamanari.pk.uuid.UUIDTest;
import de.calamanari.pk.valueobject.ValueObjectTest;
import de.calamanari.pk.visitor.VisitorTest;
import de.calamanari.pk.wrapper.WrapperTest;

/**
 * All Tests - Test suite for all pattern tests.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AllTests.class);

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
        startTimeNanos = System.nanoTime();
        LOGGER.info("Running AllTests ...");
    }

    /**
     * write finish message after tests
     */
    @AfterClass
    public static void afterAll() {
        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("AllTests completed. Elapsed time: {} s", elapsedTimeString);
    }

}
