package algorithm;

public class JobData {
    private int startx;
    private int starty;
    private int endx;
    private int endy;
    private String productName;

    public JobData(int startx, int starty, int endx, int endy, String productName) {
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
        this.productName = productName;
    }

    public int getStartx() {
        return startx;
    }

    public int getStarty() {
        return starty;
    }

    public int getEndx() {
        return endx;
    }

    public int getEndy() {
        return endy;
    }

    public String getProductName() {
        return productName;
    }
}
