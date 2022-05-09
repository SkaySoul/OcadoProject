package algorithm;

public class ProductData {
    private String name;
    private int x;
    private int y;
    private int layer;

    public ProductData(String name, int x, int y, int layer) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.layer = layer;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLayer() {
        return layer;
    }
}
