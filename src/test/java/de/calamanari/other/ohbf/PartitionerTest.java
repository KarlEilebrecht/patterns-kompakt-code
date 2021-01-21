package de.calamanari.other.ohbf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class PartitionerTest {

    @Test
    public void testBasics() {
        Partitioner partitioner = new Partitioner();
        // 341504
        partitioner.computePartitions(2_000_070_000, 27);

        int[] partitions = partitioner.computePartitions(40, 11);

        assertEquals(11, partitions.length);

        assertTrue(Partitioner.calculateWaste(partitions, 40) >= 0);
    }

}
