//@formatter:off
/*
 * PartitionerTest
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
package de.calamanari.other.ohbf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Test coverage for the Partitioner
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PartitionerTest {

    @Test
    public void testBasics() {
        Partitioner partitioner = new Partitioner();
        partitioner.computePartitions(2_000_070_000, 27);

        int[] partitions = partitioner.computePartitions(40, 11);

        assertEquals(11, partitions.length);

        assertTrue(Partitioner.calculateWaste(partitions, 40) >= 0);
    }

    private long computeTotalWaste(int[] partitions, long m) {
        long pWaste = Partitioner.calculateWaste(partitions, m);
        long effectiveM = ((m + pWaste + 63) / 64) * 64;
        return effectiveM - m;
    }

    @Test
    public void testWaste() throws Exception {
        Partitioner partitioner = new Partitioner();

        List<String> lines = new ArrayList<>();
        lines.add("k;m;mp;waste");
        StringBuilder sb = new StringBuilder();

        for (int m = 1000; m < 10_000_000; m = m + 999) {
            long n = (m / 100) * ((m % 37) * 2);

            BloomFilterConfig cnf = new BloomFilterConfig(m, n);
            int[] partitions = partitioner.computePartitions(cnf.getRequiredNumberOfBitsM(), cnf.getNumberOfHashesK());
            long waste = computeTotalWaste(partitions, cnf.getRequiredNumberOfBitsM());
            assertTrue(waste <= ((cnf.getRequiredNumberOfBitsM() / cnf.getNumberOfHashesK()) - 1 + 63));

            sb.setLength(0);
            sb.append(cnf.getNumberOfHashesK());
            sb.append(";");
            sb.append(cnf.getRequiredNumberOfBitsM());
            sb.append(";");

            sb.append((cnf.getRequiredNumberOfBitsM() + waste));
            sb.append(";");
            sb.append(waste);
            lines.add(sb.toString());
        }

        // Files.write(new File("/home/minty/wasteTest.csv").toPath(), lines);

    }

    @Test
    public void testReduce() {
        long max = (long) Math.pow(2, 31);

        for (int i = 0; i < 100; i++) {
            long reduced = ((i) * max) >>> 32;
            System.out.println("i=" + i + ", N=" + max + ", reduced=" + reduced);
        }
        for (int i = Integer.MAX_VALUE - 101; i < Integer.MAX_VALUE; i++) {
            long reduced = ((i) * max) >>> 32;
            System.out.println("i=" + i + ", N=" + max + ", reduced=" + reduced);
        }

        max = 90;

        int[] counters = new int[90];

        for (long l = Integer.MIN_VALUE; l <= Integer.MAX_VALUE; l++) {

            long x = l & 0x00000000ffffffffL;

            long reduced = x % max;// (x * max) >>> 32L;
            counters[(int) reduced]++;

            if (x % 100_000 == 0) {
                System.out.println(x);
            }
        }

        System.out.println(Arrays.toString(counters));
    }

    @Test
    public void testShingle() {

        int offset = 0;
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                offset = 0;
            }
            else {
                offset = offset + 16;
            }

            int calcOffset = i * 16;
            System.out.println("i=" + i + ", offset=" + offset + " (" + calcOffset + ")");
        }

    }
}
