
public class Triangle {
    Point a;
    Point b;
    Point c;
    int color;
    
    public Triangle(Point a, Point b, Point c, int color) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.color = color;
    }
    
    public void printSelf() {
        System.out.println("Triangle points:");
        a.printSelf();
        b.printSelf();
        c.printSelf();
        System.out.println("Color " + color);
    }
}
