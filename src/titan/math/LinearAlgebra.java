package titan.math;

public class LinearAlgebra {
    /**
     * Tries to find the inverse of a square matrix
     *
     * @param matrix - the involved n x n matrix
     * @return The inverse of the matrix
     */
    public double[][] inverse(double[][] matrix){
        if (matrix.length != matrix[0].length){
            throw new RuntimeException("Matrix is not a square matrix!");
        }
        double[][] A = detMatrix(matrix);
        A = sign(A);
        A = transpose(A);

        double det = findDet(matrix);
        if (det == 0.0 || det == -0.0){
            throw new RuntimeException("Matrix does not have an inverse!");
        }
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A.length; j++){
                A[i][j] = A[i][j] / det;
            }
        }
        return A;
    }

    /**
     * Utility method that supports the inverse(...) method
     *
     * @param matrix - the determinant matrix whose entries need to be multiplied with +/- signs
     * @return The signed determinant matrix
     */
    private double[][] sign(double[][] matrix){
        double[][] A = new double[matrix.length][matrix.length];
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A.length; j++){
                if ( (i + j + 1) % 2 == 0){
                    A[i][j] = -matrix[i][j];
                }
                else{
                    A[i][j] = matrix[i][j];
                }
            }
        }
        return A;
    }

    /**
     * Transposes a matrix
     *
     * @param matrix - the involved m x n matrix that needs to be transposed
     * @return The transpose of the involved matrix
     */
    public double[][] transpose(double[][] matrix){
        double[][] A = new double[matrix[0].length][matrix.length];
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A[0].length; j++){
                A[i][j] = matrix[j][i];
            }
        }
        return A;
    }

    /**
     * Finds the rc-minor of a matrix
     *
     * @param matrix - the involved m x n matrix whose rc-minor we need
     * @param r - the row that should not be included in the minor
     * @param c - the column that should not be included in the minor
     * @return The rc-minor of the matrix
     */
    public double[][] minorIJ (double[][] matrix, int r, int c){
        int size = matrix.length - 1, m = 0, n = 0;
        double[][] minor = new double[size][size];
        for(int i = 0; i < matrix.length; i++){
            if (i == r){ continue; }
            for(int j = 0; j < matrix.length; j++){
                if (j == c){ continue; }
                minor[m][n] = matrix[i][j];
                n++;
                if (n == size){
                    n = 0;
                    m++;
                }
            }
        }
        return minor;
    }

    /**
     * Finds the determinant matrix of a square matrix
     *
     * @param matrix - the involved n x n matrix
     * @return The determinant matrix of the n x n matrix
     */
    public double[][] detMatrix(double[][] matrix){
        if (matrix.length != matrix[0].length){
            throw new RuntimeException("Matrix is not a square matrix!");
        }
        else if (matrix.length == 2){
            throw new RuntimeException("Matrix is too small to form a determinant matrix N");
        }

        double[][] A = new double[matrix.length][matrix.length];
        if (matrix.length > 3){
            for(int i = 0; i < matrix.length; i++){
                for(int j = 0; j < matrix.length; j++){
                    A[i][j] = findDet(minorIJ(matrix, i, j));
                }
            }
        }
        else{
            for(int i = 0; i < matrix.length; i++){
                for(int j = 0; j < matrix.length; j++){
                    A[i][j] = det(minorIJ(matrix, i, j));
                }
            }
        }
        return A;
    }

    /**
     * Method that finds the determinant of the entire square matrix
     *
     * @param matrix - the involved n x n matrix
     * @return The determinant of the entire matrix
     */
    public double findDet(double[][] matrix){
        if (matrix.length != matrix[0].length){
            throw new RuntimeException("Matrix is a not square matrix!");
        }

        if (matrix.length > 2){
            double det = 0.0;
            for(int i = 0; i < matrix.length; i++){
                for(int j = 0; j < matrix.length; j++){
                    double[][] minor = minorIJ(matrix, i, j);
                    if ((j + 1) % 2 == 0){
                        det -= matrix[i][j] * findDet(minor);
                    }
                    else{
                        det +=  matrix[i][j] * findDet(minor);
                    }
                }
            }
            return det;
        }
        else{
            return det(matrix);
        }
    }

    /**
     * Calculates the determinant of a 2 x 2 matrix
     *
     * @param matrix - the involved 2 x 2 matrix
     * @return the determinant of the 2 x 2 matrix
     */
    public double det(double[][] matrix){
        return (matrix[0][0] * matrix[1][1]) - (matrix[1][0] * matrix[0][1]);
    }

    /**
     * Performs the matrix equation Ax = b
     * @param matrix - the matrix A involved in the equation
     * @param vector - the vector x involved in the equation
     * @return Returns the resulting vector b from the matrix equation Ax = b
     */
    public double[] equate(double[][] matrix, double[] vector){
        if (matrix[0].length != vector.length){
            throw new RuntimeException("Number of matrix columns do not match vector entries!");
        }

        double[] b = new double[matrix.length];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                b[i] += matrix[i][j] * vector[j];
            }
        }
        return b;
    }
}
