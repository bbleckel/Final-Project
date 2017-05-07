public class Point {
    int X;
    int Y;
    
    public Point(int x, int y) {
        X = x;
        Y = y;
    }
    
    public void printSelf() {
        System.out.println("(" + X + ", " + Y + ")");
    }
}
