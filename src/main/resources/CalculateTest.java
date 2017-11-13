import org.junit.Test;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/*
 * @author Steve Lamont
 * @version 5 Oct 2017
 * Purpose is to run test cases on Students' Calculate Library
 */
public class CalculateTest {
    private static double DELTA = 0.01;

    /* Part 1 Tests */
	@Test public void testSquare() {
        assertEquals("testing square with 5", 25, Calculate.square(5));
        assertEquals("testing square with -2", 4, Calculate.square(-2));
    }

    @Test public void testCube() {
        assertEquals("testing cube with 3", 27, Calculate.cube(3));
    }

    @Test public void testAverage() {
        assertEquals("testing average 2 numbers with 2 & 4", 3.0, Calculate.average(2.0, 4.0), DELTA);
        assertEquals("testing average 2 numbers with -5.0 & 5.0", 0.0, Calculate.average(-5.0, 5.0), DELTA);
        assertEquals("testing average 3 numbers with -5.0, 2.0, 15", 4.0, Calculate.average(-5.0, 2, 15), DELTA);
    }

    @Test public void testToDegrees() {
        assertEquals("testing toDegrees with 5", 286.479, Calculate.toDegrees(5.0), .001);
        assertEquals("testing toDegrees with -3", -171.887, Calculate.toDegrees(-3.0), .001);
    }

    @Test public void testToRadians() {
        assertEquals("testing toRadians with 190 degrees", 3.31613, Calculate.toRadians(190.0), .001);
    }

    @Test public void testDiscriminant() {
        assertEquals("testing discriminant with 5, 4, & 3 ", -44.0, Calculate.discriminant(5.0, 4.0, 3.0), DELTA);
    }

    @Test public void testToImproperFrac() {
        assertEquals("testing toImproperFrac with 3, 1, 4 ", "13/4", Calculate.toImproperFrac(3, 1, 4));
    }

    @Test public void testToMixedNum() {
        assertEquals("testing toMixedNum with 7/2", "3_1/2", Calculate.toMixedNum(7, 2));
		assertEquals("testing toMixedNum with -9/3 (warning if wrong)","-3", Calculate.toMixedNum(-9,3));
    }

    @Test public void testFoil() {
        assertEquals("testing foil with 2,3,6,-7,x", "12x^2 + 4x + -21", Calculate.foil(2, 3, 6, -7, "x"));
    }

    /* Part 2 Tests */
    @Test public void testIsDivisibleBy() {
        assertEquals("testing isDivisibleBy with 9 and 3", true, Calculate.isDivisibleBy(9, 3));
        assertEquals("testing isDivisibleBy with 8 and 3", false, Calculate.isDivisibleBy(8, 3));
    }

    @Test public void testAbsValue() {
        assertEquals("testing absValue with 8", 8.0, Calculate.absValue(8), DELTA);
        assertEquals("testing absValue with 8.0", 8.0, Calculate.absValue(8.0), DELTA);
        assertEquals("testing absValue with -2.1", 2.1, Calculate.absValue(-2.1), DELTA);
        assertEquals("testing absValue with 0", 0, Calculate.absValue(0.0), DELTA);
    }

    @Test public void testMax() {
        assertEquals("testing max with 2 integers 8 & 9", 9, Calculate.max(8, 9), DELTA);
        assertEquals("testing max with 2 integers -5 & -3", -3, Calculate.max(-5, -3), DELTA);
        assertEquals("testing max with 3 doubles -4, 3, & 9", 9.0, Calculate.max(-4.0, 3, 9.0), DELTA);
    }

    @Test public void testMin() {
        assertEquals("testing min with 2 integers -5 & -3", -5, Calculate.min(-5, -3));
    }

    @Test public void testRound2() {
        assertEquals("testing round2 with 85.9876", 85.99, Calculate.round2(85.98765), DELTA);
        assertEquals("testing round2 with 5.5", 5.50, Calculate.round2(5.5), DELTA);
        assertEquals("testing round2 with 5.596", 5.60, Calculate.round2(5.596), DELTA);
    }

    /* Part 3 Tests */
    @Test public void testExponent() {
        assertEquals("testing exponent with 2 to the 3rd", 8.0, Calculate.exponent(2.0, 3), DELTA);
        assertEquals("testing exponent with 1.5 to the 5th", 7.59375, Calculate.exponent(1.5, 5), DELTA);
    }

    @Test public void testFactorial() {
        assertEquals("testing factorial with 5", 120, Calculate.factorial(5));
    }

    @Test public void isPrime() {
        assertEquals("testing isPrime with 7", true, Calculate.isPrime(7));
        assertEquals("testing isPrime with 8", false, Calculate.isPrime(8));
    }

    @Test public void testGcf() {
        assertEquals("testing gcf with 12 and 15", 3, Calculate.gcf(12, 15));
        assertEquals("testing gcf with 39 and 26", 13, Calculate.gcf(39, 26));
    }

    @Test public void testSqrt() {
        assertEquals("testing sqrt with 81, accurate +/- .01", 9.0, Calculate.sqrt(81.0), DELTA);
        assertEquals("testing sqrt with 56, accurate +/- .01", 7.4833, Calculate.sqrt(56.0), DELTA);
    }

    /* Part 4 Test(s) */
    @Test public void testQuadForm() {
        Assert.assertThat("testing quadForm with no real roots -1,0,-5", "no real roots", Matchers.equalToIgnoringCase(Calculate.quadForm(-1, 0, -5)));
        Assert.assertThat("testing quadForm with one real root 1,-3,10 (note this may fail by number of signif digits) \n\t\t", "-2.00", Matchers.equalToIgnoringCase(Calculate.quadForm(1, 4, 4)));
        Assert.assertThat("testing quadForm with two real roots (smaller root should come first) \n\t\t", "-2.00 and 5.00", Matchers.equalToIgnoringCase(Calculate.quadForm(1, -3, -10)));
    }
}
