//@formatter:off
/*
 * Customer Manager Server - a remote service handling persistence 
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
package de.calamanari.pk.datatransferobject.server;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.calamanari.pk.datatransferobject.Customer;
import de.calamanari.pk.datatransferobject.CustomerManager;
import de.calamanari.pk.util.AbstractConsoleServer;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Customer Manager Server - a remote service handling persistence
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerManagerServer extends AbstractConsoleServer implements CustomerManager {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(CustomerManagerServer.class.getName());

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
    private volatile CustomerManager customerManagerStub;

    /**
     * our private RMI-server
     */
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
        LOGGER.fine(this.getClass().getSimpleName() + ".findCustomer('" + customerId + "') called");
        return database.get(customerId);
    }

    @Override
    public Customer findCustomerReturnDto(String customerId) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".findCustomerReturnDto('" + customerId + "') called");
        CustomerEntity entity = database.get(customerId);
        return (entity == null ? null : entity.toDto());
    }

    @Override
    protected void configureInstance(String[] cmdLineArgs) {
        configureLogging(cmdLineArgs);
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
            LOGGER.info("Starting private RMI-Registry on port " + this.registryPort + " ...");
            rmiRegistryProcess = pb.start();
            // give the registry some time to get ready
            Thread.sleep(REGISTRY_STARTUP_MILLIS);
            LOGGER.info("RMI-Registry ready.");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
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
        catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Error during RMI-shutdown!", t);
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
                LOGGER.log(Level.WARNING, "Error parsing rmi-port='" + cmdLineArgs[0] + "', using default=" + port, ex);
            }
        }
        this.registryPort = port;
    }

    /**
     * Sets the log-level according the command line arguments
     * 
     * @param cmdLineArgs program arguments
     */
    private void configureLogging(String[] cmdLineArgs) {
        if (cmdLineArgs != null && cmdLineArgs.length > 1 && "logfine".equalsIgnoreCase(cmdLineArgs[1])) {
            LogUtils.setConsoleHandlerLogLevel(Level.FINE);
            LogUtils.setLogLevel(Level.FINE, CustomerManagerServer.class);
        }
        else {
            LogUtils.setConsoleHandlerLogLevel(Level.INFO);
        }
    }

    @Override
    protected void prepare() {
        try {
            this.customerManagerStub = (CustomerManager) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(registryPort);
            registry.bind("CustomerManager", this.customerManagerStub);
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during preparation!", ex);
        }
    }

    @Override
    protected String createStartupCompletedMessage() {
        return this.getServerName() + " started!";
    }

    @Override
    protected void doRequestProcessing() {
        while (true) {
            MiscUtils.sleep(Level.WARNING, REQUEST_DELAY_MILLIS);
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
                    try {
                        UnicastRemoteObject.unexportObject(entity, true);
                    }
                    catch (NoSuchObjectException ex) {
                        // we can ignore that
                    }
                }
            }
        }
        catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Error during clean-up!", t);
        }
        shutdownRmiRegistry();
    }

    /**
     * Creates stand-alone console server
     * 
     * @param args first argument may optionally specify the port
     */
    public static void main(String[] args) {
        (new CustomerManagerServer()).setupAndStart(args);
    }

}
