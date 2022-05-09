package algorithm;

enum Weights {
    H(0.5), B(1), S(2);

    final double weight;

    Weights(double weight){
        this.weight = weight;
    }
}
