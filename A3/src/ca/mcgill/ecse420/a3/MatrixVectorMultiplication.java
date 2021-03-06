package ca.mcgill.ecse420.a3;

import java.util.Date;

public class MatrixVectorMultiplication {

    private static final int NUMBER_THREADS = 8;
    private static final int MATRIX_SIZE = 10000;


    public static void main(String[] args) {

        // Generate two random matrices, same size
        double[][] M = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
        double[] v = generateRandomVector(MATRIX_SIZE);

        Date start, end;

        start = new Date();
        double[] out_seq = SequentialMultiplier.multiply(M, v);
        end = new Date();
        System.out.println("\nsequential multiplication (milli seconds): " + (end.getTime() - start.getTime()));

        start = new Date();
        double[] out_par = ParallelMultiplier.multiply(M, v);
        end = new Date();
        System.out.println("\nparallel multiplication (milli seconds): " + (end.getTime() - start.getTime()));

        start = new Date();
        double[] out_prac_par = PracticalParallelMultiplier.multiply(M, v, NUMBER_THREADS);
        end = new Date();
        System.out.println("\npractical parallel multiplication (milli seconds): " + (end.getTime() - start.getTime()));

    }

    /**
     * Populates a matrix of given size with randomly generated integers between 0-10.
     *
     * @param numRows number of rows
     * @param numCols number of cols
     * @return matrix
     */
    private static double[][] generateRandomMatrix(int numRows, int numCols) {
        double matrix[][] = new double[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                matrix[row][col] = (double) ((int) (Math.random() * 10.0));
            }
        }
        return matrix;
    }

    /**
     * Populates a vector of given size with randomly generated integers between 0-10.
     *
     * @param num number of elements
     * @return vector
     */
    private static double[] generateRandomVector(int num) {
        double vector[] = new double[num];
        for (int i = 0; i < num; i++) {
            vector[i] = (double) ((int) (Math.random() * 10.0));
        }
        return vector;
    }

}
