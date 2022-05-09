package algorithm;


//class for store grids data
public class GridData {

    private int [][] wayGrid;
    private double [][] weightGrid;

    public GridData(int[][] emptyGrid, double[][] weightGrid) {
        this.weightGrid = weightGrid;
        this.wayGrid = emptyGrid;
    }

    public int[][] getWayGrid() {
        return wayGrid;
    }

    public double[][] getWeightGrid() {
        return weightGrid;
    }
}
