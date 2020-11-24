//@formatter:off
/*
 * Simple Access Manager
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Simple Access Manager is a trivial supplementary class simulating a kind of security system.<br>
 * <b>Important:</b><br>
 * You should NEVER EVER implement your own security system, there are APIs for that which have been audited well.<br>
 * However in the current example there is a central flaw.<br>
 * Anyone getting a reference to the permissionsMap (via reflection) using setAccessible(true) can do just everything. In a correct scenario security in a JVM
 * cannot be implemented without a SecurityManager, but this is cumbersome :-).<br>
 * Additionally this manager cannot differentiate between methods with the same name but different parameters (unaware of overloading).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class SimpleAccessManager {

    private static final String MSG_SECURITY_PROBLEM_ACCESS_DENIED = "Security problem: access denied";

    /**
     * For simplicity this is implemented as a singleton.
     */
    private static SimpleAccessManager instance;

    /**
     * The thread creating the singleton is the only one who can manage permissions.
     */
    private static final String ROOT_PERMISSIONS = "*";

    /**
     * a map for storing permissions<br>
     */
    private final Map<Thread, Set<String>> permissionsMap = Collections.synchronizedMap(new WeakHashMap<Thread, Set<String>>());

    /**
     * Returns the access manager instance
     * 
     * @return access manager
     */
    public static synchronized SimpleAccessManager getInstance() {
        if (instance == null) {
            instance = new SimpleAccessManager();
        }
        return instance;
    }

    /**
     * Creates new manager
     */
    private SimpleAccessManager() {
        Set<String> permissions = new HashSet<>();
        permissions.add(ROOT_PERMISSIONS);
        this.permissionsMap.put(Thread.currentThread(), permissions);
    }

    /**
     * Checks whether the current thread has the permission to do the current operation
     * 
     * @return true if allowed otherwise false
     */
    public boolean checkPermission() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        String operationName = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
        Set<String> permissions = getInstance().permissionsMap.get(Thread.currentThread());
        return (permissions != null && (permissions.contains(ROOT_PERMISSIONS) || permissions.contains(operationName)));
    }

    /**
     * Allows the given operation.
     * 
     * @param thread the thread permission shall be granted
     * @param operationName full class name + dot + method name
     */
    public void allow(Thread thread, String operationName) {
        if (checkPermission()) {
            Set<String> permissions = permissionsMap.computeIfAbsent(thread, t -> new HashSet<>());
            permissions.add(operationName);
        }
        else {
            throw new SimpleAccessException(MSG_SECURITY_PROBLEM_ACCESS_DENIED);
        }
    }

    /**
     * Disallows the given operation.
     * 
     * @param thread thread permission shall be revoked
     * @param operationName full class name + dot + method name
     */
    public void disallow(Thread thread, String operationName) {
        if (checkPermission()) {
            Set<String> permissions = permissionsMap.get(thread);
            if (permissions != null) {
                permissions.remove(operationName);
            }
        }
        else {
            throw new SimpleAccessException(MSG_SECURITY_PROBLEM_ACCESS_DENIED);
        }
    }

    /**
     * Clears all permissions for the thread.
     * 
     * @param thread the tread to clear assigned permissions
     */
    public void clearPermissions(Thread thread) {
        if (checkPermission()) {
            permissionsMap.remove(thread);
        }
        else {
            throw new SimpleAccessException(MSG_SECURITY_PROBLEM_ACCESS_DENIED);
        }
    }

}
