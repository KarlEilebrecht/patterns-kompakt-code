//@formatter:off
/*
 * Customer Manager Server - a remote service handling persistence 
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
package de.calamanari.pk.datatransferobject.server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import de.calamanari.pk.datatransferobject.Customer;
import de.calamanari.pk.datatransferobject.CustomerManager;
import de.calamanari.pk.util.AbstractConsoleServer;
import de.calamanari.pk.util.TimeUtils;

/**
 * Customer Manager Server - a remote service handling persistence
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerManagerServer extends AbstractConsoleServer implements CustomerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerManagerServer.class);

    /**
     * default registry port (our private server, usually this is 1099 for RMI!)
     */
    public static final int DEFAULT_REGISTRY_PORT = 8091;

    /**
     * Time for RMI-registry to come up.
     */
    private static final long REGISTRY_STARTUP_MILLIS = 3000;

    /**
     * simulated expensive request
     */
    private static final long REQUEST_DELAY_MILLIS = 500;

    /**
     * port the RMI server listens
     */
    private volatile int registryPort;

    /**
     * server stub
     */
    // Volatile is sufficient as there is no race condition in this scenario
    @SuppressWarnings("java:S3077")
    private volatile CustomerManager customerManagerStub;

    /**
     * our private RMI-server
     */
    // Volatile is sufficient as there is no race condition in this scenario
    @SuppressWarnings("java:S3077")
    private volatile Process rmiRegistryProcess;

    /**
     * the manager's "database"
     */
    private Map<String, CustomerEntity> database = new ConcurrentHashMap<>();

    /**
     * Creates new Customer Manager Server
     */
    public CustomerManagerServer() {
        super(CustomerManagerServer.class.getSimpleName());
    }

    @Override
    public void addCustomer(String customerId, String lastName, String firstName, String street, String zipCode, String city) throws RemoteException {
        CustomerEntity entity = new CustomerEntity(customerId, lastName, firstName, street, zipCode, city);
        CustomerEntity entityExist = database.get(customerId);
        if (entityExist != null) {

            // without this, we would evtl. be unable to stop RMI later
            try {
                UnicastRemoteObject.unexportObject(entityExist, true);
            }
            catch (NoSuchObjectException ex) {
                // we can ignore that
            }
        }
        this.database.put(customerId, entity);
    }

    @Override
    public Customer findCustomer(String customerId) throws RemoteException {
        LOGGER.debug("{}.findCustomer('{}') called", this.getClass().getSimpleName(), customerId);
        return database.get(customerId);
    }

    @Override
    public Customer findCustomerReturnDto(String customerId) throws RemoteException {
        LOGGER.debug("{}.findCustomerReturnDto('{}') called", this.getClass().getSimpleName(), customerId);
        CustomerEntity entity = database.get(customerId);
        return (entity == null ? null : entity.toDto());
    }

    @Override
    protected void configureInstance(String[] cmdLineArgs) {
        configureRegistryPort(cmdLineArgs);
        startRmiRegistry();
    }

    /**
     * Starts the internal RMI-registry on the configured port.
     */
    private void startRmiRegistry() {
        List<String> args = new ArrayList<>();
        args.add("rmiregistry");
        args.add("" + this.registryPort);

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.environment().put("CLASSPATH", System.getProperties().getProperty("java.class.path", null));
        try {
            LOGGER.info("Starting private RMI-Registry on port {} ...", this.registryPort);
            rmiRegistryProcess = pb.start();
            // give the registry some time to get ready
            Thread.sleep(REGISTRY_STARTUP_MILLIS);
            LOGGER.info("RMI-Registry ready.");
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new CustomerManagerServerException(
                    String.format("Unexpected interruption exception (could not start RMI-Registry on port %d)!", this.registryPort), ex);
        }
        catch (IOException | RuntimeException ex) {
            throw new CustomerManagerServerException(
                    String.format("Unexpected communication exception (could not start RMI-Registry on port %d)!", this.registryPort), ex);
        }
    }

    /**
     * Terminates the internally started RMI-registry process.
     */
    private void shutdownRmiRegistry() {
        try {
            if (rmiRegistryProcess != null) {
                rmiRegistryProcess.destroy();
                LOGGER.info("Private RMI-Registry stopped!");
            }
        }
        catch (RuntimeException t) {
            LOGGER.error("Error during RMI-shutdown!", t);
        }
    }

    /**
     * Reads the RMI-registry port from the command line arguments
     * 
     * @param cmdLineArgs programm arguments
     */
    private void configureRegistryPort(String[] cmdLineArgs) {
        int port = DEFAULT_REGISTRY_PORT;
        if (cmdLineArgs != null && cmdLineArgs.length > 0) {
            try {
                port = Integer.parseInt(cmdLineArgs[0]);
            }
            catch (Exception ex) {
                LOGGER.warn("Error parsing rmi-port='{}', using default={}", cmdLineArgs[0], port, ex);
            }
        }
        this.registryPort = port;
    }

    @Override
    protected void prepare() {
        try {
            this.customerManagerStub = (CustomerManager) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(registryPort);
            registry.bind("CustomerManager", this.customerManagerStub);
        }
        catch (RemoteException | AlreadyBoundException | RuntimeException ex) {
            throw new CustomerManagerServerException("Error during preparation!", ex);
        }
    }

    @Override
    protected String createStartupCompletedMessage() {
        return this.getServerName() + " started!";
    }

    @Override
    protected void doRequestProcessing() {
        while (true) {
            TimeUtils.sleep(Level.WARN, REQUEST_DELAY_MILLIS);
            if (getServerState() != ServerState.ONLINE) {
                break;
            }
        }
    }

    @Override
    protected void initiateShutdown() {
        // nothing to do
    }

    @Override
    protected void cleanUp() {
        try {
            if (this.customerManagerStub != null) {
                Registry registry = LocateRegistry.getRegistry(registryPort);
                registry.unbind("CustomerManager");
                UnicastRemoteObject.unexportObject(this, true);
                for (CustomerEntity entity : database.values()) {
                    unexportCustomerEntity(entity);
                }
            }
        }
        catch (RemoteException | NotBoundException | RuntimeException t) {
            LOGGER.error("Error during clean-up!", t);
        }
        shutdownRmiRegistry();
    }

    private void unexportCustomerEntity(CustomerEntity entity) {
        try {
            UnicastRemoteObject.unexportObject(entity, true);
        }
        catch (NoSuchObjectException ex) {
            LOGGER.trace("Cleanup of non-existing object", ex);
        }
    }

    /**
     * Creates stand-alone console server
     * 
     * @param args first argument may optionally specify the port
     */
    public static void main(String[] args) {
        try {
            (new CustomerManagerServer()).setupAndStart(args);
        }
        catch (RuntimeException ex) {
            LOGGER.error("Server startup failed!", ex);
        }
    }

}
