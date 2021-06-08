import java.lang.Math;
import java.util.Scanner;
class Main {
    static double triangleArea(double a, double b, double c){
        double p = (a + b + c) / 2;
        double p2 = p*((p-a)*(p-b)*(p-c));
        return Math.sqrt(p2);
    }
    static double rectangleArea(double a, double b){
        return a * b;
    }

    static double circleArea(double r){
        return 3.14 * Math.pow(r,2);
    }
    public static void main(String[] args) {
        double a;
        double b;
        double c;
        double r;
        double area = 0;
        // put your code here
        Scanner scanner = new Scanner(System.in);

        String shape = scanner.nextLine();

        switch(shape){
            case "triangle":
                a = scanner.nextInt();
                b = scanner.nextInt();
                c = scanner.nextInt();
                area = triangleArea(a,b,c);
                break;
            case "rectangle":
                a = scanner.nextInt();
                b = scanner.nextInt();
                area = rectangleArea(a,b);
                break;
            case"circle":
                r = scanner.nextInt();
                area = circleArea(r);
                break;
        }
        System.out.print(area);

    }
}