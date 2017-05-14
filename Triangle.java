
public class Triangle {
    Point a;
    Point b;
    Point c;
    int[] color;
    float alpha;

    public Triangle(Point a, Point b, Point c, int[] color, float alpha) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.color = new int[3];
        this.color = color;
        this.alpha = alpha;
    }

    public void printSelf() {
        System.out.println("Triangle points:");
        a.printSelf();
        b.printSelf();
        c.printSelf();
        System.out.println("Color " + color[0] + color[1] + color[2]);
    }
}
