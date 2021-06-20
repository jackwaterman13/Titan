package titan.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing Linear Algebra, meaning methods, functions related to LA will be here
 */
public class LinearAlgebra {
    /**
     * Calculates the determinant of a 2 x 2 matrix
     *
     * @param matrix - the involved 2 x 2 matrix
     * @return Double representing determinant
     */
    public Double determinant(Double[][] matrix){
        return matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];
    }

    /**
     * Finds the determinant of a n x n matrix
     *
     * @param matrix - the involved n x n matrix
     * @return Double representing the determinant of the n x n matrix
     */
    public Double findDeterminant(Double[][] matrix){
        double det = 0.0;
        if (matrix.length == 2 && matrix[0].length == 2){
            det = determinant(matrix);
        }
        else if (matrix.length < 2 || matrix[0].length < 2){ throw new RuntimeException("Vector detected!"); }
        else{
            for(int i = 0; i < matrix.length; i++){
                for(int j = 0; j < matrix[i].length; j++){
                    Double[][] minorIJ = constructMinorIJ(matrix, i, j);
                    det += findDeterminant(minorIJ);
                }
            }
        }
        return det;
    }

    /**
     * Returns the ij-minor matrix of a matrix
     *
     * @param matrix - involved matrix whose ij minor you require
     * @param r - the row that should not be included in the minor
     * @param c - the column that should not be included in the minor
     * @return Ij-minor matrix which does not contain row r and column c entries
     */
    public Double[][] constructMinorIJ (Double[][] matrix, int r, int c){
        Double[][] minor = new Double[matrix.length - 1][matrix[0].length - 1];
        int x = 0, y = 0;
        for(int i = 0; i < matrix.length; i++){
            if (i == r){ continue; }
            for(int j = 0; j < matrix[i].length; j++){
                if (j == c){ continue; }
                minor[x][y] = matrix[i][j];
                y++;
                if (y == minor[0].length){
                    x++;
                    y = 0;
                }
            }
        }
        return minor;
    }
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
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    /**
     * Method that gets the inverted matrix of a n x n matrix A

     * @param matrix - a n x n Double matrix that needs to be inverted
     * @return Returns the inverted matrix A^-1
     */
    public Double[][] invertedMatrix(Double[][] matrix){
        double det = findDeterminant(matrix);
        Double[][] N = new Double[matrix.length][];
        for(int i = 0; i < matrix.length; i++){
            Double[] row = new Double[matrix[i].length];
            for(int j = 0; j < matrix[i].length; j++){
                row[j] = findDeterminant(constructMinorIJ(matrix, i, j));
            }
            N[i] = row;
        }

        N = transposeMatrix(N);
        for(int i = 0; i < N.length; i++){
            N[i] = scale(N[i], det);
        }
        return N;
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
     *  Method that swaps two rows with each other.
     *
     * @param A - matrix involved in the swap
     * @param swapped - row that will be swapped
     * @param replacement - row that will acts as a replacement for the swapped row at that position
     * @return B where B[i][j] = A[i][j] except B[swapped][j] = A[replacement][j] and B[replacement][j] = A[swapped][j]
     */
    public Double[][] swap(Double[][] A, int swapped, int replacement){
        Double[][] B = new Double[A.length][];
        for(int i = 0; i < A.length; i++){
            Double[] row = new Double[A[i].length];
            for(int j = 0; j < A[i].length; j++){
                if (i == swapped){
                    row[j] = A[replacement][j];
                }
                else if (i == replacement){
                    row[j] = A[swapped][j];
                }
                else{
                    row[j] = A[i][j];
                }
            }
            B[i] = row;
        }
        return B;
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
                b[i] +=  A[i][j] * x[j];
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
        Double[][] B = new Double[A.length][];

        int pivot = 0;
        int lastIndex = -1;
        Double[] base = null; // row we are working with
        List<Double[]> explored = new ArrayList<>();


        while(pivot < n){
            for(int i = 0; i < A.length; i++){
                if (A[i][pivot] != 0.0 && !explored.contains(A[i])){
                    if (i - lastIndex > 1){ swap(A, i - 1, i ); }
                    base = scale(A[i], 1.0 / A[i][pivot]);
                    lastIndex = i;
                }
            }

            for (Double[] row : A) {
                if (row == base) { continue; }
                else if (row[pivot] == 0.0) { continue; }

                if (row[pivot] % base[pivot] == 0) { subtract(base, row); }
                else { subtract(scale(base, row[pivot] / base[pivot]), row); }
                System.out.println(Arrays.toString(row));
            }
            explored.add(base);
            base = null;
            pivot++;

        }
        return B;
    }
}
