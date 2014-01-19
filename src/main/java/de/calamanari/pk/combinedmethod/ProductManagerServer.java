/*
 * Product Manager Server - the server side
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
package de.calamanari.pk.combinedmethod;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.calamanari.pk.util.AbstractConsoleServer;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Product Manager Server - the server side
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ProductManagerServer extends AbstractConsoleServer implements ProductManager {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ProductManagerServer.class.getName());

    /**
     * default registry port (our private server, usually this is 1099 for RMI!)
     */
    public static final int DEFAULT_REGISTRY_PORT = 8091;

    /**
     * product-ids will start at {@value} + 1
     */
    private static final int START_PRODUCT_ID = 1000;
    
    /**
     * Time for RMI-registry to get ready.
     */
    private static final long REGISTRY_STARTUP_MILLIS = 3000;
    
    /**
     * some milliseconds to simulate expensive request
     */
    private static final long REQUEST_DELAY_MILLIS = 500;
    
    /**
     * server-side ID-sequence
     */
    private final AtomicInteger lastId = new AtomicInteger(START_PRODUCT_ID);

    /**
     * Unused IDs
     */
    private final Queue<String> lostAndFoundIds = new ConcurrentLinkedQueue<>();

    /**
     * server-side database
     */
    private final Map<String, Product> database = new ConcurrentHashMap<>();

    /**
     * port the RMI server listens
     */
    private volatile int registryPort;

    /**
     * server stub
     */
    private volatile ProductManager productManagerStub;

    /**
     * our private RMI-server
     */
    private volatile Process rmiRegistryProcess;

    /**
     * flag for testing
     */
    private volatile boolean nextProductRegistrationMustFail = false;

    /**
     * Creates new Product Manager Server
     */
    public ProductManagerServer() {
        super(ProductManagerServer.class.getSimpleName());
    }

    @Override
    public Product findProductById(String id) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".findProductById('" + id + "') called");
        return database.get(id);
    }

    @Override
    public String acquireProductId() throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".acquireProductId() called");
        return "ID" + lastId.incrementAndGet();
    }

    @Override
    public void registerProduct(Product product) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".registerProduct(" + product + ") called");
        if (nextProductRegistrationMustFail) {
            LOGGER.fine("Simulating some error");
            nextProductRegistrationMustFail = false;
            throw new RuntimeException("Some error!");
        }
        String id = product.getProductId();
        if (id != null) {
            database.put(id, product);
            LOGGER.fine("Product registration successful.");
        }
    }

    @Override
    public void setNextProductRegistrationMustFail() throws RemoteException {
        this.nextProductRegistrationMustFail = true;
    }

    @Override
    public Product combinedCreateAndRegisterProduct(Product product) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".combinedCreateAndRegisterProduct(" + product
                + ") called - The COMBINED METHOD");
        String productId = obtainProductId();
        LOGGER.fine("Assigning ID='" + productId + "' to product");
        product.setProductId(productId);
        try {
            registerProduct(product);
        }
        catch (Exception ex) {
            // "rollback"
            LOGGER.fine("Handling error during registration, preserving ID='" + productId + "' for reuse");
            lostAndFoundIds.add(productId);
            if ((ex instanceof RemoteException) || (ex instanceof RuntimeException)) {
                throw ex;
            }
            throw new RuntimeException(ex);
        }
        return product;
    }

    /**
     * This method returns a free product-id, either from lost+found pool or newly acquired
     * @return fresh product-id
     * @throws RemoteException on remoting error
     */
    private String obtainProductId() throws RemoteException {
        LOGGER.fine("Checking for lost IDs ...");
        String productId = lostAndFoundIds.poll();
        if (productId == null) {
            productId = acquireProductId();
        }
        else {
            LOGGER.fine("Reusing lost ID '" + productId + "'");
        }
        return productId;
    }

    @Override
    public void reset() throws RemoteException {
        this.lastId.set(START_PRODUCT_ID);
        this.database.clear();
        this.lostAndFoundIds.clear();
    }

    @Override
    protected void configureInstance(String[] cmdLineArgs) {
        configureLogging(cmdLineArgs);
        configureRegistryPort(cmdLineArgs);
        startRmiRegistry();
    }

    /**
     * Starts the RMI-registry according to this instance's settings.
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
     * Parses the registry port from the command line arguments
     * @param cmdLineArgs program arguments
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
     * Configures log-level according to command line settings
     * @param cmdLineArgs program arguments
     */
    private void configureLogging(String[] cmdLineArgs) {
        if (cmdLineArgs != null && cmdLineArgs.length > 1 && "logfine".equalsIgnoreCase(cmdLineArgs[1])) {
            LogUtils.setConsoleHandlerLogLevel(Level.FINE);
            LogUtils.setLogLevel(Level.FINE, ProductManagerServer.class);
        }
        else {
            LogUtils.setConsoleHandlerLogLevel(Level.INFO);
        }
    }

    @Override
    protected void prepare() {
        try {
            this.productManagerStub = (ProductManager) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(registryPort);
            registry.bind("ProductManager", this.productManagerStub);
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
            if (this.productManagerStub != null) {
                Registry registry = LocateRegistry.getRegistry(registryPort);
                registry.unbind("ProductManager");
                UnicastRemoteObject.unexportObject(this, true);
            }
        }
        catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Error during clean-up!", t);
        }
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
     * Creates stand-alone console server
     * @param args first argument may optionally specify the port
     */
    public static void main(String[] args) {
        (new ProductManagerServer()).setupAndStart(args);
    }

}
