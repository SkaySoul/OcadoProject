package algorithm;

import java.util.ArrayList;

public class TransitionData {


    public ArrayList <int[]> optimalWay;
    private double EstimatedTime;
    private int wayLength;



    public TransitionData(ArrayList<int[]> optimalWay, double estimatedTime, int wayLength) {
        this.optimalWay = optimalWay;
        this.EstimatedTime = estimatedTime;
        this.wayLength = wayLength;
    }



    public double getEstimatedTime() {
        return EstimatedTime;
    }


    public void setEstimatedTime(double estimatedTime) {
        EstimatedTime = estimatedTime;
    }

    public int getWayLength() {
        return wayLength;
    }

    public void setWayLength(int wayLength) {
        this.wayLength = wayLength;
    }

}
