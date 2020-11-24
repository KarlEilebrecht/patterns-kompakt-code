//@formatter:off
/*
 * In-Memory Lock Manager - manages locks in the COARSE GRAINED LOCK example 
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
package de.calamanari.pk.coarsegrainedlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In-Memory Lock Manager - manages locks in the COARSE GRAINED LOCK example<br>
 * In non-distributed scenarios it often makes more sense to leverage an in-memory lock management rather than storing lock information in database tables.<br>
 * A big advantage (besides performance) of in-memory management is the fact that the system is always in a clean state after startup (no lock zombies surviving
 * in lock tables).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class InMemoryLockManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryLockManager.class);

    /**
     * this map contains the currently managed locks
     */
    private static final ConcurrentHashMap<String, ElementLock> LOCKS = new ConcurrentHashMap<>();

    /**
     * Utility class, no instances
     */
    private InMemoryLockManager() {
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

        LOGGER.debug("{}: {}.acquireReadLock({}, {}) called", Thread.currentThread().getName(), InMemoryLockManager.class.getSimpleName(), elementId, ownerId);
        ElementLock currentLock = null;
        boolean success = false;
        boolean abort = false;

        do {
            LOGGER.debug("{}: looking up existing lock ...", Thread.currentThread().getName());
            currentLock = LOCKS.get(elementId);
            if (currentLock != null) {
                LOGGER.debug("{}: lock entry found ", Thread.currentThread().getName());

                if (currentLock.lockType == LockType.WRITE_LOCK) {
                    LOGGER.debug("{}: Existing write lock detected - aborting.", Thread.currentThread().getName());
                    // there is a write lock, thus cannot acquire read lock
                    abort = true;
                }
                else {
                    success = updateReadLock(currentLock, elementId, ownerId);
                }
            }
            else {
                success = createNewReadLock(elementId, ownerId);
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

        LOGGER.debug("{}: {}.acquireWriteLock({}, {}) called", Thread.currentThread().getName(), InMemoryLockManager.class.getSimpleName(), elementId, ownerId);
        ElementLock currentLock = null;
        boolean success = false;
        boolean abort = false;

        do {
            LOGGER.debug("{}: looking up existing lock ...", Thread.currentThread().getName());
            currentLock = LOCKS.get(elementId);

            if (currentLock != null) {
                LOGGER.debug("{}: lock entry found ", Thread.currentThread().getName());

                if (currentLock.getLockType() == LockType.WRITE_LOCK) {
                    // if we already have the lock, report success
                    success = checkWriteLockOwner(currentLock, ownerId);
                    // cannot get a write-lock if someone else already holds one
                    abort = !success;
                }
                else {
                    List<String> lockOwnerIds = new ArrayList<>(currentLock.getOwnerIds());
                    // there is a read lock
                    if (lockOwnerIds.size() > 1 || !lockOwnerIds.contains(ownerId)) {
                        LOGGER.debug("{}: Existing read lock (not owned by requestor) detected - aborting.", Thread.currentThread().getName());
                        // someone else also holds the read-lock, cannot upgrade to write lock
                        abort = true;
                    }
                    else {
                        success = upgradeReadLockToWriteLock(currentLock, elementId, ownerId);
                    }
                }
            }
            else {
                success = createNewWriteLock(elementId, ownerId);
            }
        } while (!success && !abort);

        return success;
    }

    /**
     * Releases the lock the given owner has on the specified element
     * 
     * @param elementId global unique identifier of the item to be locked
     * @param ownerId requester of the lock
     * @return true if lock was released, otherwise false (no lock found)
     */
    public static boolean releaseLock(String elementId, String ownerId) {

        LOGGER.debug("{}: {}.releaseLock({}, {}) called", Thread.currentThread().getName(), InMemoryLockManager.class.getSimpleName(), elementId, ownerId);
        ElementLock currentLock = null;

        OpState state;

        do {
            LOGGER.debug("{}: looking up existing lock ...", Thread.currentThread().getName());
            currentLock = LOCKS.get(elementId);

            if (currentLock != null && currentLock.isEngaged()) {
                LOGGER.debug("{}: lock entry found ", Thread.currentThread().getName());
                state = releaseEngagedLock(currentLock, elementId, ownerId);
            }
            else {
                LOGGER.debug("{}: no lock found to be removed - aborting ...", Thread.currentThread().getName());
                // there was no lock
                state = OpState.COMPLETED;
            }
        } while (state == OpState.PENDING);

        return state.isSuccess();
    }

    /**
     * Releases the lock if still present
     * 
     * @param currentLock
     * @param elementId
     * @param ownerId
     * @return {@link OpState#COMPLETED} if the lock was released, {@link OpState#PENDING} to suggest a new attempt and {@link OpState#ABORTED} not possible
     *         (not owned by requestor)
     */
    private static OpState releaseEngagedLock(ElementLock currentLock, String elementId, String ownerId) {
        OpState state = OpState.PENDING;
        if (currentLock.getLockType() == LockType.WRITE_LOCK) {
            if (currentLock.getOwnerIds().contains(ownerId)) {
                if (releaseWriteLock(currentLock, elementId)) {
                    state = OpState.COMPLETED;
                }
            }
            else {
                LOGGER.debug("{}: write lock NOT owned by requestor detected - aborting ...", Thread.currentThread().getName());
                // cannot remove lock, belongs to someone else
                state = OpState.ABORTED;
            }
        }
        else {
            if (currentLock.getOwnerIds().contains(ownerId)) {
                if (releaseReadLock(currentLock, elementId, ownerId)) {
                    state = OpState.COMPLETED;
                }
            }
            else {
                LOGGER.debug("{}: read lock NOT owned by requestor detected - aborting ...", Thread.currentThread().getName());
                state = OpState.ABORTED;
            }
        }
        return state;
    }

    /**
     * This method creates a new read-lock.
     * 
     * @param elementId global unique identifier of the item to be locked
     * @param ownerId requester of the lock
     * @return true on success or false (concurrent modification)
     */
    private static boolean createNewReadLock(String elementId, String ownerId) {
        boolean success = false;

        LOGGER.debug("{}: No lock entry found, creating one ...", Thread.currentThread().getName());
        ElementLock newLock = new ElementLock(LockType.READ_LOCK, elementId, Arrays.asList(ownerId), null);

        boolean haveLock = (LOCKS.putIfAbsent(elementId, newLock) == null);
        if (!haveLock) {
            LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
        }
        else {
            LOGGER.debug("{}: Successfully acquired read lock.", Thread.currentThread().getName());
            success = true;
        }
        return success;
    }

    /**
     * This method updates an existing read-lock (another owner will be added).
     * 
     * @param readLock existing readLock
     * @param elementId global unique identifier of the item to be locked
     * @param ownerId requester of the lock
     * @return true on success or false (concurrent modification)
     */
    private static boolean updateReadLock(ElementLock readLock, String elementId, String ownerId) {
        boolean success = false;
        // there is a read lock
        if (readLock.getOwnerIds().contains(ownerId)) {
            LOGGER.debug("{}: Existing read lock detected, owned by requestor, leaving with success.", Thread.currentThread().getName());
            // we have already the lock, report success
            success = true;
        }
        else {
            LOGGER.debug("{}: Existing read lock detected, adding requestor to owner list.", Thread.currentThread().getName());
            List<String> lockOwnerIds = new ArrayList<>(readLock.getOwnerIds());
            lockOwnerIds.add(ownerId);

            ElementLock newLock = new ElementLock(readLock.getLockType(), elementId, lockOwnerIds, readLock.latch);

            boolean haveLock = LOCKS.replace(elementId, readLock, newLock);
            if (!haveLock) {
                LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
            }
            else {
                LOGGER.debug("{}: Successfully acquired read lock.", Thread.currentThread().getName());
                success = true;
            }
        }
        return success;
    }

    /**
     * Creates a new exclusive write-lock for the given owner.
     * 
     * @param elementId global unique identifier of the item to be locked
     * @param ownerId requester of the lock
     * @return true on success or false (concurrent modification)
     */
    private static boolean createNewWriteLock(String elementId, String ownerId) {
        boolean success = false;
        LOGGER.debug("{}: No lock entry found, creating one ...", Thread.currentThread().getName());

        ElementLock newLock = new ElementLock(LockType.WRITE_LOCK, elementId, Arrays.asList(ownerId), null);

        boolean haveLock = (LOCKS.putIfAbsent(elementId, newLock) == null);
        if (!haveLock) {
            LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
        }
        else {
            LOGGER.debug("{}: Successfully acquired write lock.", Thread.currentThread().getName());
            success = true;
        }
        return success;
    }

    /**
     * Checks whether the given write-lock is owned by the specified owner.
     * 
     * @param writeLock existing write-lock
     * @param ownerId requester of the lock
     * @return true if given lock is owned by the specified owner, otherwise false
     */
    private static boolean checkWriteLockOwner(ElementLock writeLock, String ownerId) {
        boolean success = false;
        if (writeLock.getOwnerIds().contains(ownerId)) {
            LOGGER.debug("{}: Existing write lock detected, owned by requestor, leaving with success.", Thread.currentThread().getName());
            success = true;
        }
        else {
            LOGGER.debug("{}: Existing write lock detected - aborting.", Thread.currentThread().getName());
        }
        return success;
    }

    /**
     * If the current read lock is owned exclusively by the specified owner, we can upgrade it to a write-lock.
     * 
     * @param readLock existing read-lock
     * @param elementId global unique identifier of the item to be locked
     * @param ownerId requester of the lock
     * @return true on success or false on concurrent modification
     */
    private static boolean upgradeReadLockToWriteLock(ElementLock readLock, String elementId, String ownerId) {
        boolean success = false;

        LOGGER.debug("{}: Single read lock owned by requestor detected - switching to WRITE_LOCK.", Thread.currentThread().getName());
        List<String> lockOwnerIds = new ArrayList<>(1);
        lockOwnerIds.add(ownerId);
        ElementLock newLock = new ElementLock(LockType.WRITE_LOCK, elementId, lockOwnerIds, readLock.latch);

        boolean haveLock = LOCKS.replace(elementId, readLock, newLock);
        if (!haveLock) {
            LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
        }
        else {
            LOGGER.debug("{}: Successfully acquired write lock.", Thread.currentThread().getName());
            success = true;
        }
        return success;
    }

    /**
     * Releases the given read-lock for the specified owner. <br>
     * If the owner was the last owner, the lock will be removed otherwise it will continue to exist for the remaining owners.
     * 
     * @param readLock current lock
     * @param elementId global unique identifier of the item to be locked
     * @param ownerId requester of the lock
     * @return true on success or false on concurrent modification
     */
    private static boolean releaseReadLock(ElementLock readLock, String elementId, String ownerId) {
        boolean success = false;
        List<String> lockOwnerIds = new ArrayList<>(readLock.getOwnerIds());
        LOGGER.debug("{}: read lock owned by requestor detected - removing lock ...", Thread.currentThread().getName());
        lockOwnerIds.remove(ownerId);

        boolean haveReleased = false;

        if (!lockOwnerIds.isEmpty()) {
            LOGGER.debug("{}: preserving read locks owned by others", Thread.currentThread().getName());
            ElementLock newLock = new ElementLock(readLock.getLockType(), elementId, lockOwnerIds, readLock.latch);
            haveReleased = LOCKS.replace(elementId, readLock, newLock);
        }
        else {
            haveReleased = LOCKS.remove(elementId, readLock);
            if (haveReleased) {
                LOGGER.debug("{}: Notifying potentially waiting threads about lock release ...", Thread.currentThread().getName());
                readLock.latch.countDown(); // if anyone was waiting he can now proceed
            }
        }

        if (!haveReleased) {
            LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
            // must be concurrent access, try the whole process again
        }
        else {
            LOGGER.debug("{}: Successfully removed read lock.", Thread.currentThread().getName());
            success = true;
        }
        return success;
    }

    /**
     * Releases the given write-lock
     * 
     * @param writeLock current write-lock to be released
     * @param elementId global unique identifier of the item to be locked
     * @return true on success or false (lock not found)
     */
    private static boolean releaseWriteLock(ElementLock writeLock, String elementId) {
        boolean success = false;

        LOGGER.debug("{}: write lock owned by requestor detected - removing lock", Thread.currentThread().getName());
        boolean haveReleased = LOCKS.remove(elementId, writeLock);

        if (!haveReleased) {
            LOGGER.debug("{}: concurrent modification detected - trying again ...", Thread.currentThread().getName());
            // must be concurrent access, try the whole process again
        }
        else {
            LOGGER.debug("{}: notifying potentially waiting threads about lock release ...", Thread.currentThread().getName());
            writeLock.latch.countDown(); // if anyone was waiting he can now proceed
            LOGGER.debug("{}: Successfully removed write lock.", Thread.currentThread().getName());
            success = true;
        }
        return success;
    }

    /**
     * Returns the lock currently associated with the specified element
     * 
     * @param elementId globally unique identifier
     * @return lock meta data object or null if no lock was found
     */
    public static ElementLock getLockInfo(String elementId) {
        return LOCKS.get(elementId);
    }

    /**
     * Implementation of an immutable element lock with metadata and option to wait for release
     */
    public static final class ElementLock {

        /**
         * read or write lock
         */
        private final LockType lockType;

        /**
         * unique key of element
         */
        private final String elementId;

        /**
         * list of lock owners
         */
        private final List<String> ownerIds;

        /**
         * this latch allows to wait for the lock's release
         */
        protected final CountDownLatch latch;

        /**
         * Creates new lock
         * 
         * @param lockType type of lock
         * @param elementId identifier
         * @param ownerIds lock owners
         * @param latch for lock interaction
         */
        protected ElementLock(LockType lockType, String elementId, List<String> ownerIds, CountDownLatch latch) {
            this.lockType = lockType;
            this.elementId = elementId;
            this.ownerIds = Collections.unmodifiableList(new ArrayList<>(ownerIds));
            this.latch = (latch == null ? new CountDownLatch(1) : latch);
        }

        /**
         * Client's may use this method to wait until the related lock was released.<br>
         * Afterwards the client does not own any lock, a new attempt to acquire must follow.
         * 
         * @throws InterruptedException pass-through from waiting
         */
        public void await() throws InterruptedException {
            latch.await();
        }

        /**
         * Client's may use this method to wait until the related lock was released.<br>
         * Afterwards the client does not own any lock, a new attempt to acquire must follow.
         * 
         * @param timeout milliseconds
         * @param unit the unit of measurement
         * @return true if the lock was released before timeout, otherwise false
         * @throws InterruptedException pass-through from waiting
         */
        public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
            return latch.await(timeout, unit);
        }

        /**
         * Determines whether this lock is still engaged
         * 
         * @return true if lock is engaged otherwise false
         */
        public boolean isEngaged() {
            return latch.getCount() > 0;
        }

        /**
         * Returns the lock's type
         * 
         * @return type of the lock
         */
        public LockType getLockType() {
            return lockType;
        }

        /**
         * Returns the id of the locked element
         * 
         * @return elementId
         */
        public String getElementId() {
            return elementId;
        }

        /**
         * Returns the list of lock owners
         * 
         * @return list of lock owners
         */
        public List<String> getOwnerIds() {
            return ownerIds;
        }

        @Override
        public String toString() {
            return ElementLock.class.getSimpleName() + "({lockType=" + lockType + ", elementId='" + elementId + "', ownerIds=" + ownerIds.toString()
                    + ", engaged=" + isEngaged() + "})";
        }

    }

    /**
     * Enumeration of supported lock types
     */
    public enum LockType {
        /**
         * indicates read lock
         */
        READ_LOCK,

        /**
         * indicates write lock
         */
        WRITE_LOCK
    }

    private enum OpState {

        PENDING(false), COMPLETED(true), ABORTED(false);

        private final boolean success;

        OpState(boolean success) {
            this.success = success;
        }

        boolean isSuccess() {
            return success;
        }

    }
}
