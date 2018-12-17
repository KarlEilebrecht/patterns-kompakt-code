//@formatter:off
/*
 * Lock Manager - manages locks in this PESSIMISTIC OFFLINE LOCK example 
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
package de.calamanari.pk.pessimisticofflinelock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lock Manager - manages locks in this PESSIMISTIC OFFLINE LOCK example<br>
 * This one uses a global lock table in the database
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class LockManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockManager.class);

    /**
     * Simulates database
     */
    private static final ConcurrentHashMap<String, AtomicReference<String[]>> DATABASE = new ConcurrentHashMap<>();

    /**
     * Utility Class
     */
    private LockManager() {
        // no instances
    }

    /**
     * Creates a read lock for the given owner
     * 
     * @param elementId global unique identifier of the item to be locked
     * @param ownerId requester of the lock
     * @return true if lock was set otherwise false
     */
    public static boolean acquireReadLock(String elementId, String ownerId) {

        LOGGER.debug("{}: {}.acquireReadLock({}, {}) called", Thread.currentThread().getName(), LockManager.class.getSimpleName(), elementId, ownerId);

        LockType currentLockType = LockType.NONE;
        LockType newLockType = LockType.READ_LOCK;
        List<String> lockOwnerIds = new ArrayList<>();

        long version = 0;

        boolean lockEntryExists = false;

        boolean success = false;
        boolean abort = false;

        do {

            LOGGER.debug("{}: " + "looking up existing lock in lock table ...", Thread.currentThread().getName());

            // get existing lock entry (if any)

            // begin TX

            // select LOCK_TYPE, OWNER_IDS, VERSION
            // from LOCK_TABLE
            // where ELEMENT_ID = ${elementId}

            LockInfo lockInfo = doSimulateDatabaseSelectLock(elementId);

            // end TX

            if (lockInfo != null) {
                lockEntryExists = true;
                currentLockType = lockInfo.lockType;
                lockOwnerIds = lockInfo.ownerIds;
                version = lockInfo.version;
            }

            if (lockEntryExists) {
                LOGGER.debug("{}: lock entry found ", Thread.currentThread().getName());

                if (currentLockType == LockType.NONE) {
                    LOGGER.debug("{}: element '{}' currently not locked, updating lock entry with: READ_LOCK ", Thread.currentThread().getName(), elementId);

                    int numberOfUpdatedRows = 0;

                    // begin TX

                    // update LOCK_TABLE
                    // set LOCK_TYPE = ${newLockType}
                    // , OWNER_IDS = ${ownerId}
                    // , VERSION = VERSION + 1
                    // where ELEMENT_ID = ${elementId} and VERSION = ${version}

                    numberOfUpdatedRows = doSimulateDatabaseUpdateLock(elementId, newLockType, Arrays.asList(new String[] { ownerId }), version);

                    // end TX

                    if (numberOfUpdatedRows != 1) {
                        LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
                        // must be concurrent access, try the whole process again
                        continue;
                    }
                    else {
                        LOGGER.debug("{}: Successfully acquired read lock.", Thread.currentThread().getName());
                        success = true;
                    }
                }
                else if (currentLockType == LockType.WRITE_LOCK) {
                    LOGGER.debug("{}: Existing write lock detected - aborting.", Thread.currentThread().getName());
                    // there is a write lock, thus cannot acquire read lock
                    abort = true;
                }
                else {
                    // there is a read lock
                    if (lockOwnerIds.contains(ownerId)) {
                        LOGGER.debug("{}: Existing read lock detected, owned by requestor, leaving with success.", Thread.currentThread().getName());
                        // we have already the lock, show reentrant behavior
                        success = true;
                    }
                    else {
                        LOGGER.debug("{}: Existing read lock detected, adding requestor to owner list.", Thread.currentThread().getName());
                        lockOwnerIds = new ArrayList<>(lockOwnerIds);
                        lockOwnerIds.add(ownerId);

                        int numberOfUpdatedRows = 0;

                        // begin TX

                        // update LOCK_TABLE
                        // set LOCK_TYPE = ${newLockType}
                        // , OWNER_IDS = ${lockOwnerIds}
                        // , VERSION = VERSION + 1
                        // where ELEMENT_ID = ${elementId} and VERSION = ${version}

                        numberOfUpdatedRows = doSimulateDatabaseUpdateLock(elementId, newLockType, lockOwnerIds, version);

                        // end TX

                        if (numberOfUpdatedRows != 1) {
                            LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
                            // must be concurrent access, try the whole process again
                            continue;
                        }
                        else {
                            LOGGER.debug("{}: Successfully acquired read lock.", Thread.currentThread().getName());
                            success = true;
                        }

                    }
                }
            }
            else {

                LOGGER.debug("{}: No lock entry found in lock table, creating one ...", Thread.currentThread().getName());

                // currently there is no entry in the LOCK_TABLE
                // thus we have to create one

                boolean insertSuccessful = false;

                // begin TX

                // insert into LOCK_TABLE (ELEMENT_ID, LOCK_TYPE, OWNER_IDS, VERSION)
                // values (${elementId}, ${newLockType}, ${ownerId}, 0)

                insertSuccessful = doSimulateDatabaseInsertLock(elementId, newLockType, Arrays.asList(new String[] { ownerId }));

                // end TX

                if (!insertSuccessful) {
                    LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
                    // must be concurrent access, try the whole process again
                    continue;
                }
                else {
                    LOGGER.debug("{}: Successfully acquired read lock.", Thread.currentThread().getName());
                    success = true;
                }
            }
        } while (!success && !abort);

        return success;
    }

    /**
     * Creates a write lock for the given owner
     * 
     * @param elementId global unique identifier of the item to be locked
     * @param ownerId requester of the lock
     * @return true if lock was set otherwise false
     */
    public static boolean acquireWriteLock(String elementId, String ownerId) {

        LOGGER.debug("{}: {}.acquireWriteLock({}, {}) called", Thread.currentThread().getName(), LockManager.class.getSimpleName(), elementId, ownerId);

        LockType currentLockType = LockType.NONE;
        LockType newLockType = LockType.WRITE_LOCK;
        List<String> lockOwnerIds = new ArrayList<>();

        long version = 0;

        boolean lockEntryExists = false;

        boolean success = false;
        boolean abort = false;

        do {

            LOGGER.debug("{}: looking up existing lock in lock table ...", Thread.currentThread().getName());

            // get existing lock entry (if any)

            // begin TX

            // select LOCK_TYPE, OWNER_IDS, VERSION
            // from LOCK_TABLE
            // where ELEMENT_ID = ${elementId}

            LockInfo lockInfo = doSimulateDatabaseSelectLock(elementId);

            // end TX

            if (lockInfo != null) {
                lockEntryExists = true;
                currentLockType = lockInfo.lockType;
                lockOwnerIds = lockInfo.ownerIds;
                version = lockInfo.version;
            }

            if (lockEntryExists) {

                LOGGER.debug("{}: lock entry found ", Thread.currentThread().getName());

                if (currentLockType == LockType.NONE) {

                    LOGGER.debug("{}: element '{}' currently not locked, updating lock entry with: READ_LOCK ", Thread.currentThread().getName(), elementId);

                    int numberOfUpdatedRows = 0;

                    // begin TX

                    // update LOCK_TABLE
                    // set LOCK_TYPE = ${newLockType}
                    // , OWNER_IDS = ${ownerId}
                    // , VERSION = VERSION + 1
                    // where ELEMENT_ID = ${elementId} and VERSION = ${version}

                    numberOfUpdatedRows = doSimulateDatabaseUpdateLock(elementId, newLockType, Arrays.asList(new String[] { ownerId }), version);

                    // end TX

                    if (numberOfUpdatedRows != 1) {
                        LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
                        // must be concurrent access, try the whole process again
                        continue;
                    }
                    else {
                        LOGGER.debug("{}: Successfully acquired write lock.", Thread.currentThread().getName());
                        success = true;
                    }
                }
                else if (currentLockType == LockType.WRITE_LOCK) {
                    if (lockOwnerIds.contains(ownerId)) {
                        LOGGER.debug("{}: Existing write lock detected, owned by requestor, leaving with success.", Thread.currentThread().getName());
                        // we have already the lock, show reentrant behavior
                        success = true;
                    }
                    else {
                        LOGGER.debug("{}: Existing write lock detected - aborting.", Thread.currentThread().getName());
                        // there is another write lock, thus cannot acquire one
                        abort = true;
                    }
                }
                else {
                    // there is a read lock
                    if (lockOwnerIds.size() > 1 || !lockOwnerIds.contains(ownerId)) {
                        LOGGER.debug("{}: Existing read lock (not owned by requestor) detected - aborting.", Thread.currentThread().getName());
                        // at least one read lock does not belong to us, cannot set write lock
                        abort = true;
                    }
                    else {
                        LOGGER.debug("{}: Single read lock owned by requestor detected - switching to WRITE_LOCK.", Thread.currentThread().getName());

                        // we have THE ONLY read lock, thus we can turn it into a write lock

                        int numberOfUpdatedRows = 0;

                        // begin TX

                        // update LOCK_TABLE
                        // set LOCK_TYPE = ${newLockType}
                        // , OWNER_IDS = ${lockOwnerIds}
                        // , VERSION = VERSION + 1
                        // where ELEMENT_ID = ${elementId} and VERSION = ${version}

                        numberOfUpdatedRows = doSimulateDatabaseUpdateLock(elementId, newLockType, lockOwnerIds, version);

                        // end TX

                        if (numberOfUpdatedRows != 1) {
                            LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
                            // must be concurrent access, try the whole process again
                            continue;
                        }
                        else {
                            LOGGER.debug("{}: Successfully acquired write lock.", Thread.currentThread().getName());
                            success = true;
                        }

                    }
                }
            }
            else {

                LOGGER.debug("{}: No lock entry found in lock table, creating one ...", Thread.currentThread().getName());

                // currently there is no entry in the LOCK_TABLE
                // thus we have to create one

                boolean insertSuccessful = false;

                // begin TX

                // insert into LOCK_TABLE (ELEMENT_ID, LOCK_TYPE, OWNER_IDS, VERSION)
                // values (${elementId}, ${newLockType}, ${ownerId}, 0)

                insertSuccessful = doSimulateDatabaseInsertLock(elementId, newLockType, Arrays.asList(new String[] { ownerId }));

                // end TX

                if (!insertSuccessful) {
                    LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
                    // must be concurrent access, try the whole process again
                    continue;
                }
                else {
                    LOGGER.debug("{}: Successfully acquired write lock.", Thread.currentThread().getName());
                    success = true;
                }
            }
        } while (!success && !abort);

        return success;
    }

    /**
     * Releases the lock the given owner has on the specified element
     * 
     * @param elementId identifier of the element which was locked
     * @param ownerId owner of the lock to be released
     * @return true if lock was released, otherwise false (no lock found)
     */
    public static boolean releaseLock(String elementId, String ownerId) {

        LOGGER.debug("{}: {}.releaseLock({}, {}) called", Thread.currentThread().getName(), LockManager.class.getSimpleName(), elementId, ownerId);

        LockType currentLockType = LockType.NONE;
        LockType newLockType = LockType.NONE;
        List<String> lockOwnerIds = new ArrayList<>();

        long version = 0;

        boolean lockEntryExists = false;

        boolean success = false;
        boolean abort = false;

        do {

            LOGGER.debug("{}: looking up existing lock in lock table ...", Thread.currentThread().getName());

            // get existing lock entry (if any)

            // begin TX

            // select LOCK_TYPE, OWNER_IDS, VERSION
            // from LOCK_TABLE
            // where ELEMENT_ID = ${elementId}

            LockInfo lockInfo = doSimulateDatabaseSelectLock(elementId);

            // end TX

            if (lockInfo != null) {
                lockEntryExists = true;
                currentLockType = lockInfo.lockType;
                lockOwnerIds = lockInfo.ownerIds;
                version = lockInfo.version;
            }

            if (lockEntryExists) {

                LOGGER.debug("{}: lock entry found ", Thread.currentThread().getName());

                if (currentLockType == LockType.NONE) {
                    LOGGER.debug("{}: element '{}' currently not locked aborting ...", Thread.currentThread().getName(), elementId);

                    // there was no lock
                    abort = true;
                }
                else if (currentLockType == LockType.WRITE_LOCK) {

                    if (lockOwnerIds.contains(ownerId)) {

                        LOGGER.debug("{}: write lock owned by requestor detected - removing lock", Thread.currentThread().getName());

                        lockOwnerIds = new ArrayList<>();

                        int numberOfUpdatedRows = 0;

                        // begin TX

                        // update LOCK_TABLE
                        // set LOCK_TYPE = ${newLockType}
                        // , OWNER_IDS = ${ownerIds}
                        // , VERSION = VERSION + 1
                        // where ELEMENT_ID = ${elementId} and VERSION = ${version}

                        numberOfUpdatedRows = doSimulateDatabaseUpdateLock(elementId, newLockType, lockOwnerIds, version);

                        // end TX

                        if (numberOfUpdatedRows != 1) {
                            LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
                            // must be concurrent access, try the whole process again
                            continue;
                        }
                        else {
                            LOGGER.debug("{}: Successfully removed write lock.", Thread.currentThread().getName());
                            success = true;
                        }

                    }
                    else {
                        LOGGER.debug("{}: write lock NOT owned by requestor detected - aborting ...", Thread.currentThread().getName());
                        // cannot remove lock, belongs to someone else
                        abort = true;
                    }
                }
                else {
                    // there is a read lock
                    if (!lockOwnerIds.contains(ownerId)) {
                        LOGGER.debug("{}: read lock NOT owned by requestor detected - aborting ...", Thread.currentThread().getName());
                        // nothing to be removed
                        abort = true;
                    }
                    else {
                        LOGGER.debug("{}: read lock owned by requestor detected - removing lock ...", Thread.currentThread().getName());
                        lockOwnerIds = new ArrayList<>(lockOwnerIds);
                        lockOwnerIds.remove(ownerId);

                        if (lockOwnerIds.size() > 0) {
                            LOGGER.debug("{}: preserving read locks owned by others", Thread.currentThread().getName());
                            // keep element locked
                            newLockType = currentLockType;
                        }

                        int numberOfUpdatedRows = 0;

                        // begin TX

                        // update LOCK_TABLE
                        // set LOCK_TYPE = ${newLockType}
                        // , OWNER_IDS = ${lockOwnerIds}
                        // , VERSION = VERSION + 1
                        // where ELEMENT_ID = ${elementId} and VERSION = ${version}

                        numberOfUpdatedRows = doSimulateDatabaseUpdateLock(elementId, newLockType, lockOwnerIds, version);

                        // end TX

                        if (numberOfUpdatedRows != 1) {
                            LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
                            // must be concurrent access, try the whole process again
                            continue;
                        }
                        else {
                            LOGGER.debug("{}: Successfully removed read lock.", Thread.currentThread().getName());
                            success = true;
                        }
                    }
                }
            }
            else {
                LOGGER.debug("{}: no lock found to be removed - aborting ...", Thread.currentThread().getName());
                // there was no lock
                success = true;
            }
        } while (!success && !abort);

        // What to do with old lock entries in the lock table?
        // As you can see above, we currently do not delete any rows,
        // thus the table would grow infinitely probably causing performance problems.
        // The naive approach was to delete rows as soon as they reach
        // the status LockType.NONE. I wouldn't recommend this since
        // this causes heavy stress to the RDMBS' index management.
        // My advice is to leave the entries in the table and to perform
        // clean-ups from time to time (i.e. setup a "housekeeper" job).

        return success;

    }

    /**
     * Returns the current lock information related to the given element
     * 
     * @param elementId identifier of an element which might be locked
     * @return lock info
     */
    public static LockInfo getLockInfo(String elementId) {

        // get existing lock entry (if any)

        LockInfo res = null;

        // begin TX

        // select LOCK_TYPE, OWNER_IDS, VERSION
        // from LOCK_TABLE
        // where ELEMENT_ID = ${elementId}

        res = doSimulateDatabaseSelectLock(elementId);

        // end TX

        if (res == null) {
            List<String> lockOwnerIds = Collections.EMPTY_LIST;
            res = new LockInfo(elementId, LockType.NONE, lockOwnerIds, 0);
        }

        return res;

    }

    /**
     * Selects the corresponding lock information for the given element
     * 
     * @param elementId identifier of an element where a lock entry might exist
     * @return lock info or null if not found
     */
    private static LockInfo doSimulateDatabaseSelectLock(String elementId) {
        LockInfo res = null;
        AtomicReference<String[]> record = DATABASE.get(elementId);
        String[] recordData = (record == null ? null : record.get());
        if (recordData != null) {
            LockType lockType = LockType.valueOf(recordData[0]);
            List<String> lockOwnerIds = Collections.EMPTY_LIST;
            if (recordData[1].length() > 0) {
                lockOwnerIds = Arrays.asList(recordData[1].split(","));
            }
            long version = 0;
            try {
                version = Long.parseLong(recordData[2]);
            }
            catch (NumberFormatException ex) {
                // won't happen
                throw new RuntimeException(ex);
            }
            res = new LockInfo(elementId, lockType, lockOwnerIds, version);
        }
        return res;
    }

    /**
     * Inserts the corresponding lock entry for the given element
     * 
     * @param elementId identifier of an element, a lock entry shall be created
     * @param lockType type of the lock to be created
     * @param ownerIds list of lock-owner-ids
     * @return true on success, otherwise false (insert failed)
     */
    private static boolean doSimulateDatabaseInsertLock(String elementId, LockType lockType, List<String> ownerIds) {
        boolean success = false;
        String[] recordData = new String[] { lockType.toString(), ownerIdListToCommaSeparatedString(ownerIds), "0" };
        AtomicReference<String[]> record = new AtomicReference<>(recordData);
        success = (DATABASE.putIfAbsent(elementId, record) == null);
        return success;
    }

    /**
     * Updates the corresponding lock entry for the given element
     * 
     * @param elementId identifier of an element, the corresponding lock shall be updated
     * @param lockType new type of the lock
     * @param ownerIds list of lock-owner-ids
     * @param expectedVersion only update if the entry was not updated in the meantime
     * @return number of updated rows
     */
    private static int doSimulateDatabaseUpdateLock(String elementId, LockType lockType, List<String> ownerIds, long expectedVersion) {
        int numberOfUpdatedRows = 0;
        AtomicReference<String[]> record = DATABASE.get(elementId);

        String[] recordData = (record == null ? null : record.get());
        if (recordData != null) {
            long currentVersion = 0;
            try {
                currentVersion = Long.parseLong(recordData[2]);
            }
            catch (NumberFormatException ex) {
                // won't happen
                throw new RuntimeException(ex);
            }
            if (currentVersion == expectedVersion) {
                String[] newRecordData = new String[] { lockType.toString(), ownerIdListToCommaSeparatedString(ownerIds), "" + (currentVersion + 1) };
                boolean success = record.compareAndSet(recordData, newRecordData);
                if (success) {
                    numberOfUpdatedRows = 1;
                }
            }
        }
        return numberOfUpdatedRows;
    }

    /**
     * Utility method to put all the owner-ids in one string<br>
     * This is not very elegant, a database column should contain atomic data but for this example this greatly simplifies work assuming that ownerIds never
     * contain commas.
     * 
     * @param ownerIds list of owners
     * @return stringified list
     */
    private static String ownerIdListToCommaSeparatedString(List<String> ownerIds) {
        String res = "";
        int len = ownerIds.size();
        if (len > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(ownerIds.get(i));
            }
            res = sb.toString();
        }
        return res;
    }

    /**
     * In-memory representation of lock data
     */
    public static class LockInfo {

        /**
         * identifies the locked element
         */
        public final String elementId;

        /**
         * type of lock
         */
        public final LockType lockType;

        /**
         * list of current lock owners
         */
        public final List<String> ownerIds;

        /**
         * version of lock
         */
        public final long version;

        /**
         * Creates new lock info
         * 
         * @param elementId identifier of an element, this lock is assigned
         * @param lockType type of the lock
         * @param ownerIds list of current lock owners
         * @param version record version of the lock entry
         */
        public LockInfo(String elementId, LockType lockType, List<String> ownerIds, long version) {
            this.elementId = elementId;
            this.lockType = lockType;
            this.ownerIds = Collections.unmodifiableList(ownerIds);
            this.version = version;
        }

        @Override
        public String toString() {
            return LockInfo.class.getSimpleName() + "({elementId='" + elementId + "', lockType='" + lockType + "', ownerIds=" + ownerIds + "})";
        }

    }

    /**
     * Enumeration of supported lock types
     */
    public static enum LockType {
        /**
         * no lock / idle
         */
        NONE,

        /**
         * read lock
         */
        READ_LOCK,

        /**
         * write lock
         */
        WRITE_LOCK
    }
}
