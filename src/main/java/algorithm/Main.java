package algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {


        Scanner in = new Scanner(System.in);
        System.out.println("Please insert data of stock: ");
        String dataPath = in.nextLine();

        System.out.println("Please insert data of job: ");
        String jobPath = in.nextLine();
        TransitionAlgorithm tr = new TransitionAlgorithm(dataPath, jobPath);

        tr.printData();
    }
}
