class Circle implements Measurable {
    private double radius;
    final double PI = Math.PI;

    public Circle(double radius) {
        this.radius = radius;
    }
    public double area() {
        return PI * Math.pow(this.radius, 2);
    }
}

// do not change the interface
interface Measurable {
    double area();
}
