package algorithm;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class TransitionAlgorithm {

    private int height;
    private int width;
    private int layers;
    private String[][] grid;
    private ArrayList<ProductData> products;
    String jobPath;


    public TransitionAlgorithm(String dataPath, String jobPath) {
        this.products = new ArrayList<>();
        ReadStock(dataPath);
        this.jobPath = jobPath;
    }

    //reading data from grid-x.txt
    public void ReadStock(String filepath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String line = br.readLine();
            String[] dataArr = line.split(" ");
            this.width = Integer.parseInt(dataArr[0]);
            this.height = Integer.parseInt(dataArr[1]);
            this.layers = Integer.parseInt(dataArr[2]);

            this.grid = new String[height][width];

            for (int i = 0; i < height; i++) {
                line = br.readLine();
                for (int j = 0; j < width; j++) {
                    grid[i][j] = String.valueOf(line.charAt(j));
                }
            }
            try {
                line = br.readLine();
                while (line != null) {
                    String[] productArr = line.split(" ");
                    ProductData pd = new ProductData(productArr[0], Integer.parseInt(productArr[2]), Integer.parseInt(productArr[1]), Integer.parseInt(productArr[3]));
                    products.add(pd);
                    line = br.readLine();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JobData readJob(String filepath) {

        int startx = 0, starty = 0, endx = 0, endy = 0;
        String productName = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line = br.readLine();
            String[] dataArr = line.split(" ");
            startx = Integer.parseInt(dataArr[0]);
            starty = Integer.parseInt(dataArr[1]);

            line = br.readLine();
            String[] dataArr2 = line.split(" ");
            endx = Integer.parseInt(dataArr2[0]);
            endy = Integer.parseInt(dataArr2[1]);

            line = br.readLine();
            productName = line;


        } catch (IOException e) {
            e.printStackTrace();
        }
        JobData jobData = new JobData(startx, starty, endx, endy, productName);

        return jobData;
    }


    public GridData CreateWay(int startx, int starty, int endx, int endy) {

        //initializing empty grid to calculate way
        int[][] wayGrid = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == startx && j == starty) {
                    wayGrid[i][j] = -2;
                } else if (grid[i][j].equals("O")) {
                    wayGrid[i][j] = -1;
                } else wayGrid[i][j] = 0;
            }
        }

        //initializing empty weight grid to calculate way weights
        double[][] weightGrid = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j].equals("O")) {
                    weightGrid[i][j] = -1;
                } else weightGrid[i][j] = 0;
            }
        }

        //initializing number of step
        int stepCounter = 0;

        //array with assigned points to step
        List<int[]> stepPoints = new ArrayList<>();

        //adding start point
        stepPoints.add(new int[]{startx, starty});

        //filling wave
        while (wayGrid[endx][endy] == 0 && weightGrid[endx][endy] == 0) {
            stepCounter++;

            //counting start weights
            if (stepCounter == 1) {
                for (int[] stepPoint : stepPoints) {
                    if (stepPoint[0] - 1 >= 0 && wayGrid[stepPoint[0] - 1][stepPoint[1]] == 0) {
                        wayGrid[stepPoint[0] - 1][stepPoint[1]] = stepCounter;
                        weightGrid[stepPoint[0] - 1][stepPoint[1]] = getWeight(grid[stepPoint[0] - 1][stepPoint[1]], grid[stepPoint[0]][stepPoint[1]]) + weightGrid[stepPoint[0]][stepPoint[1]];
                    }
                    if (stepPoint[1] - 1 >= 0 && wayGrid[stepPoint[0]][stepPoint[1] - 1] == 0) {
                        wayGrid[stepPoint[0]][stepPoint[1] - 1] = stepCounter;
                        weightGrid[stepPoint[0]][stepPoint[1] - 1] = getWeight(grid[stepPoint[0]][stepPoint[1] - 1], grid[stepPoint[0]][stepPoint[1]]) + weightGrid[stepPoint[0]][stepPoint[1]];
                    }
                    if (stepPoint[0] + 1 < height && wayGrid[stepPoint[0] + 1][stepPoint[1]] == 0) {
                        wayGrid[stepPoint[0] + 1][stepPoint[1]] = stepCounter;
                        weightGrid[stepPoint[0] + 1][stepPoint[1]] = getWeight(grid[stepPoint[0] + 1][stepPoint[1]], grid[stepPoint[0]][stepPoint[1]]) + weightGrid[stepPoint[0]][stepPoint[1]];
                    }
                    if (stepPoint[1] + 1 < width && wayGrid[stepPoint[0]][stepPoint[1] + 1] == 0) {
                        wayGrid[stepPoint[0]][stepPoint[1] + 1] = stepCounter;
                        weightGrid[stepPoint[0]][stepPoint[1] + 1] = getWeight(grid[stepPoint[0]][stepPoint[1] + 1], grid[stepPoint[0]][stepPoint[1]]) + weightGrid[stepPoint[0]][stepPoint[1]];
                    }

                }
            }

            //counting best weight for step
            else {
                for (int[] stepPoint : stepPoints) {
                    if (stepPoint[0] - 1 >= 0 && wayGrid[stepPoint[0] - 1][stepPoint[1]] == 0) {
                        wayGrid[stepPoint[0] - 1][stepPoint[1]] = stepCounter;
                    }
                    if (stepPoint[1] - 1 >= 0 && wayGrid[stepPoint[0]][stepPoint[1] - 1] == 0) {
                        wayGrid[stepPoint[0]][stepPoint[1] - 1] = stepCounter;
                    }
                    if (stepPoint[0] + 1 < height && wayGrid[stepPoint[0] + 1][stepPoint[1]] == 0) {
                        wayGrid[stepPoint[0] + 1][stepPoint[1]] = stepCounter;
                    }
                    if (stepPoint[1] + 1 < width && wayGrid[stepPoint[0]][stepPoint[1] + 1] == 0) {
                        wayGrid[stepPoint[0]][stepPoint[1] + 1] = stepCounter;
                    }

                    double bestValue = Double.MAX_VALUE;


                    if (stepPoint[0] - 1 >= 0 && wayGrid[stepPoint[0] - 1][stepPoint[1]] == wayGrid[stepPoint[0]][stepPoint[1]] - 1) {

                        if (weightGrid[stepPoint[0] - 1][stepPoint[1]] < bestValue) {
                            bestValue = weightGrid[stepPoint[0] - 1][stepPoint[1]];
                            weightGrid[stepPoint[0]][stepPoint[1]] = getWeight(grid[stepPoint[0] - 1][stepPoint[1]], grid[stepPoint[0]][stepPoint[1]]) + bestValue;
                        }
                    }

                    if (stepPoint[1] - 1 >= 0 && wayGrid[stepPoint[0]][stepPoint[1] - 1] == wayGrid[stepPoint[0]][stepPoint[1]] - 1) {
                        if (weightGrid[stepPoint[0]][stepPoint[1] - 1] < bestValue) {
                            bestValue = weightGrid[stepPoint[0]][stepPoint[1] - 1];
                            weightGrid[stepPoint[0]][stepPoint[1]] = getWeight(grid[stepPoint[0]][stepPoint[1] - 1], grid[stepPoint[0]][stepPoint[1]]) + bestValue;
                        }
                    }

                    if (stepPoint[0] + 1 < height && wayGrid[stepPoint[0] + 1][stepPoint[1]] == wayGrid[stepPoint[0]][stepPoint[1]] - 1) {
                        if (weightGrid[stepPoint[0] + 1][stepPoint[1]] < bestValue) {
                            bestValue = weightGrid[stepPoint[0] + 1][stepPoint[1]];
                            weightGrid[stepPoint[0]][stepPoint[1]] = getWeight(grid[stepPoint[0] + 1][stepPoint[1]], grid[stepPoint[0]][stepPoint[1]]) + bestValue;
                        }
                    }


                    if (stepPoint[1] + 1 < width && wayGrid[stepPoint[0]][stepPoint[1] + 1] == wayGrid[stepPoint[0]][stepPoint[1]] - 1) {
                        if (weightGrid[stepPoint[0]][stepPoint[1] + 1] < bestValue) {
                            bestValue = weightGrid[stepPoint[0]][stepPoint[1] + 1];
                            weightGrid[stepPoint[0]][stepPoint[1]] = getWeight(grid[stepPoint[0]][stepPoint[1] + 1], grid[stepPoint[0]][stepPoint[1]]) + bestValue;
                        }
                    }

                }
            }
            //erasing list of points
            stepPoints.clear();

            //adding new points to next step
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (wayGrid[i][j] == stepCounter) {
                        stepPoints.add(new int[]{i, j});
                    }
                }
            }
        }

        //counting weight for last step
        double bestValue = Double.MAX_VALUE;

        int[] stepPoint = new int[]{endx, endy};
        if (stepPoint[0] - 1 >= 0 && wayGrid[stepPoint[0] - 1][stepPoint[1]] == wayGrid[stepPoint[0]][stepPoint[1]] - 1) {

            if (weightGrid[stepPoint[0] - 1][stepPoint[1]] < bestValue) {
                bestValue = weightGrid[stepPoint[0] - 1][stepPoint[1]];
                weightGrid[stepPoint[0]][stepPoint[1]] = getWeight(grid[stepPoint[0] - 1][stepPoint[1]], grid[stepPoint[0]][stepPoint[1]]) + bestValue;
            }
        }

        if (stepPoint[1] - 1 >= 0 && wayGrid[stepPoint[0]][stepPoint[1] - 1] == wayGrid[stepPoint[0]][stepPoint[1]] - 1) {
            if (weightGrid[stepPoint[0]][stepPoint[1] - 1] < bestValue) {
                bestValue = weightGrid[stepPoint[0]][stepPoint[1] - 1];
                weightGrid[stepPoint[0]][stepPoint[1]] = getWeight(grid[stepPoint[0]][stepPoint[1] - 1], grid[stepPoint[0]][stepPoint[1]]) + bestValue;
            }
        }

        if (stepPoint[0] + 1 < height && wayGrid[stepPoint[0] + 1][stepPoint[1]] == wayGrid[stepPoint[0]][stepPoint[1]] - 1) {
            if (weightGrid[stepPoint[0] + 1][stepPoint[1]] < bestValue) {
                bestValue = weightGrid[stepPoint[0] + 1][stepPoint[1]];
                weightGrid[stepPoint[0]][stepPoint[1]] = getWeight(grid[stepPoint[0] + 1][stepPoint[1]], grid[stepPoint[0]][stepPoint[1]]) + bestValue;
            }
        }


        if (stepPoint[1] + 1 < width && wayGrid[stepPoint[0]][stepPoint[1] + 1] == wayGrid[stepPoint[0]][stepPoint[1]] - 1) {
            if (weightGrid[stepPoint[0]][stepPoint[1] + 1] < bestValue) {
                bestValue = weightGrid[stepPoint[0]][stepPoint[1] + 1];
                weightGrid[stepPoint[0]][stepPoint[1]] = getWeight(grid[stepPoint[0]][stepPoint[1] + 1], grid[stepPoint[0]][stepPoint[1]]) + bestValue;
            }
        }


        //creating structure with data
        return new GridData(wayGrid, weightGrid);
    }

    //gettng weights from enum
    public double getWeight(String nextindex, String startindex) {

        switch (startindex + nextindex) {
            case "SS", "SB", "SH", "BS", "HS" -> {
                return Weights.S.weight;
            }
            case "BB", "BH", "HB" -> {
                return Weights.B.weight;
            }
            case "HH" -> {
                return Weights.H.weight;
            }
            default -> {
                return 0;
            }
        }
    }


    public void printData() {
        JobData jb = readJob(this.jobPath);
        TransitionData td = StartAlgorithm(jb.getProductName(), jb.getStarty(), jb.getStartx(), jb.getEndy(), jb.getEndx());

        System.out.println(td.getEstimatedTime());
        System.out.println(td.getWayLength());
        for (int i = 0; i < td.optimalWay.size(); i++) {
            System.out.println(td.optimalWay.get(i)[0] + " " + td.optimalWay.get(i)[1]);
        }


    }


    public TransitionData CalculateWay(GridData gridData, int startx, int starty, int endx, int endy) {

        //initializing list with final way
        ArrayList<int[]> finalWay = new ArrayList<>();
        double estimatedTime = gridData.getWeightGrid()[endx][endy];
        int stepNumber = gridData.getWayGrid()[endx][endy];
        //where are we current presenting
        int[] currentPos = new int[]{endx, endy};

        //initializing number of step
        int stepCounter = stepNumber;

        //point where we're going in the next step basing on the way weight
        int[] optimalWayPoint = new int[]{currentPos[0], currentPos[1]};

        //initializing optimal way weight
        double optimalWayWeight = gridData.getWeightGrid()[endx][endy];

        //weight of current step
        double currentWayWeight;


        finalWay.add(new int[]{endx, endy});

        //creating way by steps from Data
        while (stepCounter > 1) {

            //searching for optimal way if we have ways with similar steps (4 if)
            if (currentPos[0] - 1 >= 0 && gridData.getWayGrid()[currentPos[0] - 1][currentPos[1]] == stepCounter - 1) {
                currentWayWeight = gridData.getWeightGrid()[currentPos[0] - 1][currentPos[1]];
                if (currentWayWeight < optimalWayWeight) {
                    optimalWayWeight = currentWayWeight;
                    optimalWayPoint[0] = currentPos[0] - 1;
                    optimalWayPoint[1] = currentPos[1];
                }
            }
            if (currentPos[1] - 1 >= 0 && gridData.getWayGrid()[currentPos[0]][currentPos[1] - 1] == stepCounter - 1) {
                currentWayWeight = gridData.getWeightGrid()[currentPos[0]][currentPos[1] - 1];
                if (currentWayWeight < optimalWayWeight) {
                    optimalWayWeight = currentWayWeight;
                    optimalWayPoint[0] = currentPos[0];
                    optimalWayPoint[1] = currentPos[1] - 1;
                }
            }
            if (currentPos[0] + 1 < height && gridData.getWayGrid()[currentPos[0] + 1][currentPos[1]] == stepCounter - 1) {
                currentWayWeight = gridData.getWeightGrid()[currentPos[0] + 1][currentPos[1]];
                if (currentWayWeight < optimalWayWeight) {
                    optimalWayWeight = currentWayWeight;
                    optimalWayPoint[0] = currentPos[0] + 1;
                    optimalWayPoint[1] = currentPos[1];
                }
            }
            if (currentPos[1] + 1 < width && gridData.getWayGrid()[currentPos[0]][currentPos[1] + 1] == stepCounter - 1) {
                currentWayWeight = gridData.getWeightGrid()[currentPos[0]][currentPos[1] + 1];
                if (currentWayWeight < optimalWayWeight) {
                    optimalWayWeight = currentWayWeight;
                    optimalWayPoint[0] = currentPos[0];
                    optimalWayPoint[1] = currentPos[1] + 1;
                }
            }
            currentPos = optimalWayPoint;
            finalWay.add(new int[]{currentPos[0], currentPos[1]});
            stepCounter--;
        }
        //adding start point to way list
        finalWay.add(new int[]{startx, starty});

        //reversing way
        Collections.reverse(finalWay);

        //creating structure with data
        return new TransitionData(finalWay, estimatedTime, stepNumber);


    }


    public double calculateUnloadingSpeed(String index, int layer) {
        switch (index) {
            case "H" -> {
                return (3 * layer) + 4;
            }
            case "B" -> {
                return (2 * layer) + 2;
            }
            case "S" -> {
                return layer + 1;
            }
            default -> {
                return 0;
            }
        }

    }

    public TransitionData StartAlgorithm(String productName, int startx, int starty, int endx, int endy) {

        //searching for products
        ArrayList<ProductData> matchedProducts = new ArrayList<>();
        for (ProductData data : products) {
            if (data.getName().equals(productName)) {
                matchedProducts.add(data);
            }
        }

        //creating full way from 2 calculated ways and unloading speed
        ArrayList<TransitionData> TransitionDataList = new ArrayList<>();
        for (ProductData matchedProduct : matchedProducts) {
            TransitionData td = CalculateWay(CreateWay(startx, starty, matchedProduct.getX(), matchedProduct.getY()), startx, starty, matchedProduct.getX(), matchedProduct.getY());
            td.setEstimatedTime(td.getEstimatedTime() + calculateUnloadingSpeed(grid[matchedProduct.getX()][matchedProduct.getY()], matchedProduct.getLayer()));
            TransitionData td2 = CalculateWay(CreateWay(matchedProduct.getX(), matchedProduct.getY(), endx, endy), matchedProduct.getX(), matchedProduct.getY(), endx, endy);
            td.setEstimatedTime(td.getEstimatedTime() + td2.getEstimatedTime());
            td2.optimalWay.remove(0);
            td.optimalWay.addAll(td2.optimalWay);
            td.setWayLength(td.getWayLength() + td2.getWayLength() - 1);
            TransitionDataList.add(td);
        }


        TransitionData bestWay = TransitionDataList.get(0);
        double bestTime = TransitionDataList.get(0).getEstimatedTime();

        //searching for best way
        for (TransitionData data : TransitionDataList) {
            if (data.getEstimatedTime() < bestTime) {
                bestWay = data;
            }


        }
        return bestWay;
    }


}
