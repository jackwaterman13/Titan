package titan.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing Linear Algebra, meaning methods, functions related to LA will be here
 */
public class LinearAlgebra {
    /**
     * Method that can get the transpose of a n x n matrix.
     *
     * @param matrix - a n x n Double matrix that needs to be transposed
     * @return Returns the n x n transposed matrix whose row i column j entry is the row j column i entry of the initial matrix
     */
    public Double[][] transposeMatrix(Double[][] matrix){
        Double[][] transposed = new Double[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                /*  a b c         a d g
                 *  d e f    ->   b e h
                 *  g h i         c f i
                 */
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    /**
     * Method that gets the inverted matrix of a n x n matrix A
     * It uses an augmented matrix A | I to find I | A^-1
     * So its main focus is on transforming A to I
     *
     * Example:
     *          a b c
     * Say A =  d e f  , where a, e, i are non-zero
     *          g h i
     *
     * Operations required to transform A -> I:
     *          a b c    1 b c    1 b c
     *          d e f => d e f => 0 e f
     *          g h i    g h i    0 h i
     *
     *          1 b c    1 b c    1 b c
     *          0 e f => 0 1 f => 0 1 f
     *          0 h i    0 h i    0 0 i
     *
     *          1 b c    1 b c    1 b 0
     *          0 1 f => 0 1 f => 0 1 0
     *          0 0 i    0 0 1    0 0 1
     *
     *          1 b 0    1 0 0
     *          0 1 0 => 0 1 0
     *          0 0 1    0 0 1
     *
     *
     * @param matrix - a n x n Double matrix that needs to be inverted
     * @return Returns the inverted matrix |
     */
    public Double[][] invertedMatrix(Double[][] matrix){
        Double[][] A = augment(matrix, identityMatrix(matrix.length));
        solve(A, matrix.length);
        return separate(A, matrix[0].length)[1];
    }


    /**
     * Creates an identity matrix of a size n x n
     *
     * @param n - size n
     * @return In
     */
    public Double[][] identityMatrix(int n){
        Double[][] identityMatrix = new Double[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if (i == j){ identityMatrix[i][j] = 1.0; }
                else { identityMatrix[i][j] = 0.0; }
            }
        }
        return identityMatrix;
    }

    /**
     * Performs a subtracting row operation between two rows
     *
     * @param subtractor - the row that is used in subtraction step
     * @param subtracted - the row that gets subtracted
     * @return Row [e0, ..., en], where ei = subtracted[i] - subtractor[i] with i = 0, ..., n
     */
    public Double[] subtract (Double[] subtractor, Double[] subtracted){
        Double[] result = new Double[subtractor.length];
        for(int i = 0; i < subtractor.length; i++){
            result[i] = subtracted[i] - subtractor[i];
        }
        return result;
    }

    /**
     * Performs a scaling row operation
     *
     * @param row - the row involved in the multiplication step
     * @param weight - the weight used in the multiplication step
     * @return Row [e0, ..., en], where ei = weight * subtracted[i] with i = 0, ..., n
     */
    public Double[] scale(Double[] row, double weight){
        Double[] result = new Double[row.length];
        for(int i = 0; i < row.length; i++){
            result[i] = weight * row[i];
        }
        return result;
    }

    /**
     * Mutator method that swaps two rows with each other.
     *
     * @param A - matrix involved in the swap
     * @param swapped - row that will be swapped
     * @param replacement - row that will acts as a replacement for the swapped row at that position
     * @return mutated A where A[swapped] = A[replacement] and A[replacement] = A[swapped]
     */
    public Double[][] swap(Double[][] A, int swapped, int replacement){
        Double[] holder = A[swapped];
        A[swapped] = A[replacement];
        A[replacement] = holder;
        return A;
    }

    /**
     * Augments matrix B into A
     *
     * @param A - base matrix
     * @param B - matrix that gets augmented to
     * @return New matrix representing matrix A | B
     */
    public Double[][] augment(Double[][] A, Double[][] B){
        Double[][] M = new Double[A.length][];
        for(int i = 0; i < A.length; i++){
            Double[] row = new Double[A.length + B.length];
            for(int j = 0; j < A.length; j++){
                row[j] = A[i][j];
            }
            for(int j = A.length; j < A.length + B.length; j++){
                row[j] = B[i][j - A.length];
            }
            M[i] = row;
        }
        return M;
    }

    /**
     * Performs a matrix equation Ax = b
     * @param A - the involved matrix in the equation
     * @param x - the involved x
     * @return Vector b representing the result of Ax = b
     */
    public Double[] equate(Double[][] A, Double[] x){
        if (A[0].length != x.length){
            throw new RuntimeException("Mismatched sizes in matrix equation Ax = b!");
        }
        Double[] b = new Double[A[0].length];
        Arrays.fill(b, 0.0);
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A[i].length; j++){
                b[i] +=  A[i][j] * x[i];
            }
        }
        return b;
    }

    /**
     * Solves the the augmented matrix A
     *
     * @param A - the involved augmented matrix A
     * @param n - the number of pivots if a were a square matrix
     * @return
     */
    public Double[][] solve(Double[][] A, int n){
        int pivot = 0;
        int lastIndex = -1;
        Double[] base = null; // row we are working with
        List<Double[]> explored = new ArrayList<>();
        while(pivot < n){
            if (base == null){
                for(int i = 0; i < A.length; i++){
                    if (A[i][pivot] != 0.0 && !explored.contains(A[i])){
                        if (i - lastIndex > 1){ swap(A, i - 1, i ); }
                        base = scale(A[i], 1.0 / A[i][pivot]);
                        lastIndex = i;
                    }
                }
            }

            for (Double[] row : A) {
                if (row == base) { continue; }
                else if (row[pivot] == 0.0) { continue; }

                if (row[pivot] % base[pivot] == 0) { subtract(base, row); }
                else { subtract(scale(base, row[pivot] / base[pivot]), row); }
            }
            explored.add(base);
            base = null;
            pivot++;

        }
        return A;
    }

    /**
     * Separtes an augmented matrix A | B into 2 sub matrices depending on the number of columns the first matrix should have
     *
     * @param A - the involved augmented matrix A
     * @param n - the number of columns first sub-matrix should have
     * @return Array containing the split augmented matrix.
     */
    public Double[][][] separate(Double[][] A, int n){
        Double[][][] matrices = new Double[2][][];
        Double[][] matrix1 = new Double[A.length][];
        Double[][] matrix2 = new Double[A.length][];

        for(int i = 0; i < A.length; i++){
            Double[] subRow1 = new Double[n];
            Double[] subRow2 = new Double[A[0].length - n];
            if (n >= 0) System.arraycopy(A[i], 0, subRow1, 0, n);
            if (A[i].length - n >= 0) System.arraycopy(A[i], n, subRow2, 0, A[i].length - n);

            matrix1[i] = subRow1;
            matrix2[i] = subRow2;
        }
        matrices[0] = matrix1;
        matrices[1] = matrix2;
        return matrices;
    }
}
