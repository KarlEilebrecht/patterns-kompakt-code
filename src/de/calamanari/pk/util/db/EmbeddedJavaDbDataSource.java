/*
 * EmbeddedJavaDbDataSource
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
package de.calamanari.pk.util.db;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Pseudo-{@link DataSource} (SINGLETON) to access embedded Java DB.
 * <p>
 * <b>Important:</b><br>
 * The way we access the <i>EmbeddedDriver</i> here is a hack, which avoids changing the class-path before running the
 * example tests. However, to cut corners in this way (digging out the derby.jar) is only acceptable for demonstration
 * purposes but never ever for production code!
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class EmbeddedJavaDbDataSource implements DataSource {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(EmbeddedJavaDbDataSource.class.getName());

    /**
     * Driver class name
     */
    public static final String EMBEDDED_JAVADB_DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";

    /**
     * Temp-directory
     */
    private static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));

    /**
     * Temporary directory hosting derby stuff for pk
     */
    private static final File DERBY_DIR = new File(TEMP_DIR, "pk-derby");
    static {
        DERBY_DIR.mkdir();
        System.setProperty("derby.stream.error.file", new File(DERBY_DIR, "derby.log").toString());
        LOGGER.info("Storage folder for embedded derby database: " + DERBY_DIR);
    }

    /**
     * JDBC-URL for the embedded derby database
     */
    public static final String EMBEDDED_JAVADB_JDBC_URL = "jdbc:derby:" + new File(DERBY_DIR, "pkdb").toString()
            + ";create=true";

    /**
     * By default the derby.jar is not on the class path, this method uses a hack to find it anyway.
     * @return URL to derby.jar
     * @throws DerbyJarNotFoundException derby.jar not found
     */
    private static final URL findDerbyJar() throws DerbyJarNotFoundException {
        URL res = null;
        URL url = ClassLoader.getSystemClassLoader().getResource("java/lang/String.class");
        if (url != null) {
            String s = null;
            try {
                s = url.getPath();
                int afterJarPos = s.lastIndexOf("!/java/lang/String.class");
                if (afterJarPos < 0) {
                    throw new DerbyJarNotFoundException("Unexpected JDK structure: " + url);
                }
                s = s.substring(0, afterJarPos);
                int basePos = s.lastIndexOf("/jre/lib");
                if (basePos < 0) {
                    throw new DerbyJarNotFoundException("Unexpected JDK structure: " + url);
                }
                s = s.substring(0, basePos) + "/db/lib/derby.jar";
                res = new URL(s);
                File file = new File(URLDecoder.decode(res.getFile(), "UTF-8"));
                if (!file.exists()) {
                    throw new DerbyJarNotFoundException("Could not find derby.jar: " + url + " - no JDK?");
                }
            }
            catch (MalformedURLException | UnsupportedEncodingException ex) {
                throw new DerbyJarNotFoundException("Unable to create URL for derby.jar: " + s, ex);
            }
        }
        else {
            throw new DerbyJarNotFoundException("Unexpected JDK structure: String class not found.");
        }
        return res;
    }

    /**
     * singleton instance
     */
    private static EmbeddedJavaDbDataSource instance = null;

    /**
     * Returns the only instance, a SINGLETON
     * @return datasource instance
     */
    public static synchronized EmbeddedJavaDbDataSource getInstance() {
        if (instance == null) {
            instance = new EmbeddedJavaDbDataSource();
        }
        return instance;
    }

    /**
     * Creates the only instance on demand
     */
    private EmbeddedJavaDbDataSource() {
        try {
            ClassLoader loader = URLClassLoader.newInstance(new URL[] { findDerbyJar() });
            final Driver driver = (Driver) Class.forName(EMBEDDED_JAVADB_DRIVER_NAME, true, loader).newInstance();

            // DriverManager does not register any drivers from from outside the class path
            // Thus we create a little proxy / delegate, the resulting anonymous class is automatically
            // located in the class path and can thus be loaded by the DriverManager - as long as no SecurityManager
            // comes into play ;-)
            DriverManager.registerDriver(new Driver() {

                @Override
                public Connection connect(String url, Properties info) throws SQLException {
                    return driver.connect(url, info);
                }

                @Override
                public boolean acceptsURL(String url) throws SQLException {
                    return driver.acceptsURL(url);
                }

                @Override
                public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
                    return driver.getPropertyInfo(url, info);
                }

                @Override
                public int getMajorVersion() {
                    return driver.getMajorVersion();
                }

                @Override
                public int getMinorVersion() {
                    return driver.getMinorVersion();
                }

                @Override
                public boolean jdbcCompliant() {
                    return driver.jdbcCompliant();
                }

                @Override
                public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                    return driver.getParentLogger();
                }

            });
        }
        catch (Exception ex) {
            throw new RuntimeException("Unable to load: " + EMBEDDED_JAVADB_DRIVER_NAME, ex);
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        LOGGER.warning("setLogWriter(..) not supported - IGNORED.");
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        LOGGER.warning("setLoginTimeout(..) not supported - IGNORED.");
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return LOGGER;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("Not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public Connection getConnection() throws SQLException {
        LOGGER.finest("Creating connection to '" + EMBEDDED_JAVADB_JDBC_URL + "' using Driver: "
                + EMBEDDED_JAVADB_DRIVER_NAME);
        Connection con = DriverManager.getConnection(EMBEDDED_JAVADB_JDBC_URL);
        LOGGER.finest("Created connection.");
        return con;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

}
