package testing.blackbox;

import org.junit.jupiter.api.Test;
import titan.math.LinearAlgebra;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinearAlgebraTest {
    @Test public void testDet2x2(){
        LinearAlgebra algebra = new LinearAlgebra();
        double[][] matrix = { { 4, 7 } , { 2, 6 } };
        assertEquals((4.0 * 6.0 - 7.0 * 2.0), algebra.det(matrix));
    }

    @Test public void testDet3x3(){
        LinearAlgebra algebra = new LinearAlgebra();
        double[][] matrix = { { 1, 2, 3 }, { 0, 1, 4 }, { 5, 6, 0 } };
        assertEquals(1.0, algebra.findDet(matrix));
    }

    @Test public void testDetMatrix3x3(){
        LinearAlgebra algebra = new LinearAlgebra();
        double[][] matrix = { { 1, 0, 1 }, { 2, -2, -1 }, { 3, 0, 0 } };
        double[][] detMatrix = algebra.detMatrix(matrix);

        /*
        * Expected matrix:
        *   0   3   6
        *   0  -3   0
        *   2  -3  -2
        * */
        assertEquals(0.0, detMatrix[0][0]);
        assertEquals(3.0, detMatrix[0][1]);
        assertEquals(6.0, detMatrix[0][2]);

        assertEquals(0.0, detMatrix[1][0]);
        assertEquals(-3.0, detMatrix[1][1]);
        assertEquals(0.0, detMatrix[1][2]);

        assertEquals(2.0, detMatrix[2][0]);
        assertEquals(-3.0, detMatrix[2][1]);
        assertEquals(-2.0, detMatrix[2][2]);
    }

    @Test public void testInverse3x3(){
        LinearAlgebra algebra = new LinearAlgebra();
        double[][] matrix = { { 1, 0, 1 }, { 2, -2, -1 }, { 3, 0, 0 } };
        double[][] inverse = algebra.inverse(matrix);

        /*
         * Expected inverse:
         *   0    -0   1/3
         * -1/2 -1/2  1/2
         *   1    -0  -1/3
         * */

        assertEquals(0.0, inverse[0][0]);
        assertEquals(-0.0, inverse[0][1]);
        assertEquals(1.0/3.0, inverse[0][2]);

        assertEquals(-1.0/2.0, inverse[1][0]);
        assertEquals(-1.0/2.0, inverse[1][1]);
        assertEquals(1.0/2.0, inverse[1][2]);

        assertEquals(1.0, inverse[2][0]);
        assertEquals(-0.0, inverse[2][1]);
        assertEquals(-1.0/3.0, inverse[2][2]);


        matrix = new double[][]{ { 1, 2, 3 }, { 0, 1, 4 }, { 5, 6, 0 } };
        inverse = algebra.inverse(matrix);

        /*
        * Expected inverse
        * -24.0 18.0 5.0
        * 20.0 -15.0 -4.0
        * -5.0 4.0 1.0
        * */

        assertEquals(-24.0, inverse[0][0]);
        assertEquals(18.0, inverse[0][1]);
        assertEquals(5.0, inverse[0][2]);

        assertEquals(20.0, inverse[1][0]);
        assertEquals(-15.0, inverse[1][1]);
        assertEquals(-4.0, inverse[1][2]);

        assertEquals(-5.0, inverse[2][0]);
        assertEquals(4.0, inverse[2][1]);
        assertEquals(1.0, inverse[2][2]);

    }

    @Test public void testEquate(){
        LinearAlgebra algebra = new LinearAlgebra();
        double[][] matrix =  { { 1, 3, 4 }, { 2, 1, 6 } };
        double[] x = { 4, 2, -1 };
        double[] b = algebra.equate(matrix, x);
        assertEquals(6, b[0]);
        assertEquals(4, b[1]);
    }
}
