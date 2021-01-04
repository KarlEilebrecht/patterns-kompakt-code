package de.calamanari.pk.muhai;

import java.math.BigInteger;

import org.apfloat.Apfloat;
import org.junit.Test;

public class KeyCollisionTest {

    @Test
    public void testApfloatComputation() {

        Apfloat value = KeyCollisionProcessor.val(Long.MAX_VALUE);

        System.out.println(value.toString());

        System.out.println(MuhaiUtils.toIntString(Long.MIN_VALUE));
        value = KeyCollisionProcessor.val(Long.MIN_VALUE);
        System.out.println(value.toString());

        System.out.println(MuhaiUtils.toIntString(-1));
        value = KeyCollisionProcessor.val(-1);
        System.out.println(value.toString());

        long m = BigInteger.TWO.pow(32).longValue();

        System.out.println(m);

        for (long n = 1; n < 1_000_002; n = n + 5_000) {
            System.out.println("" + n + " --> " + KeyCollisionProcessor.computeExpectedNumberOfCollisions(m, n));
        }

    }

}
