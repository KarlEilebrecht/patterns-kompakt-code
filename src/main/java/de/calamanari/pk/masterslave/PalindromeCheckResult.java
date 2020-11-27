//@formatter:off
/*
 * Palindrome Check Result - demonstrates MASTER SLAVE
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
package de.calamanari.pk.masterslave;

/**
 * Palindrome Check Result - represents result of palindrome check
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class PalindromeCheckResult {

    /**
     * This result may be used after check completed successfully, palindrome was confirmed
     */
    public static final PalindromeCheckResult CONFIRMED = new PalindromeCheckResult(true, -1, -1);

    /**
     * This result may be used if check is still in progress
     */
    public static final PalindromeCheckResult UNKNOWN = new PalindromeCheckResult(false, -1, -1);

    /**
     * This result may be used by a slave if something went wrong
     */
    public static final PalindromeCheckResult ERROR = new PalindromeCheckResult(false, -999, -999);

    /**
     * true if the check confirmed a palindrome
     */
    private final boolean palindromeConfirmed;

    /**
     * position of difference on the left
     */
    private final long errorPositionLeft;

    /**
     * position of difference on the right
     */
    private final long errorPositionRight;

    /**
     * Creates new result
     * 
     * @param confirmed if true, palindrome-nature was confirmed
     * @param errorPositionLeft in case the source is no palindrome, this is the problematic character on the left
     * @param errorPositionRight in case the source is no palindrome, this is the problematic character on the right
     */
    private PalindromeCheckResult(boolean confirmed, long errorPositionLeft, long errorPositionRight) {
        this.palindromeConfirmed = confirmed;
        this.errorPositionLeft = errorPositionLeft;
        this.errorPositionRight = errorPositionRight;
    }

    /**
     * Creates new failed result
     * 
     * @param errorPositionLeft position of difference on the left
     * @param errorPositionRight position of difference on the right
     * @return check result for failure
     */
    public static PalindromeCheckResult createFailedResult(long errorPositionLeft, long errorPositionRight) {
        return new PalindromeCheckResult(false, errorPositionLeft, errorPositionRight);
    }

    /**
     * Returns whether the checked source was a palindrome
     * 
     * @return true if palindrome was confirmed
     */
    public boolean isPalindromeConfirmed() {
        return palindromeConfirmed;
    }

    /**
     * Returns difference position
     * 
     * @return position of difference on the left, -1 means no position
     */
    public long getErrorPositionLeft() {
        return errorPositionLeft;
    }

    /**
     * Returns difference position
     * 
     * @return position of difference on the right, -1 means no position
     */
    public long getErrorPositionRight() {
        return errorPositionRight;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PalindromeCheckResult) && this.toString().equals(obj.toString());
    }

    @Override
    public String toString() {
        return PalindromeCheckResult.class.getSimpleName() + "(confirmed=" + palindromeConfirmed + ", errorPositionLeft=" + errorPositionLeft
                + ", errorPositionRight=" + errorPositionRight + ")";
    }

}
