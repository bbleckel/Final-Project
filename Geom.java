import java.util.Vector;

public class Geom {
    /*public static double signedArea(Point a, Point b, Point c) {
        double area = 0.5 * ((b.X - a.X) * (c.Y - a.Y) - (b.Y - a.Y) * (c.X - a.X));
        return area;
    }
    
    public static boolean left(Point a, Point b, Point c) {
        return signedArea(a, b, c) > 0;
    }
    
    public static boolean isWithin(Triangle t, Point p) {
        // returns true if point p is inside triangle t
        boolean left1 = left(t.a, t.b, p);
        boolean left2 = left(t.b, t.c, p);
        boolean left3 = left(t.a, t.c, p);
        
        return left1 == left2 == left3;
    }
    
    public static Point getIntersect(Point a, Point b, Point c, Point d) {
        // returns the point, if any, at which the segment specified by a, b intersects
        // the segment specified by c, d
        double denom = (d.Y - c.Y) * (b.X - a.X) - (d.X - c.X) * (b.Y - a.Y);
        if(denom == 0) {
            return new Point(-1, -1);
        }
        
        double ua = ((d.X - c.X) * (a.Y - c.Y) - (d.Y - c.Y) * (a.X - c.X)) / denom;
        double ub = ((b.X - a.X) * (a.Y - c.Y) - (b.Y - a.Y) * (a.X - c.X)) / denom;
        if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
            // Get the intersection point.
            return new Point((int) (a.X + ua*(b.X - a.X)), (int) (a.Y + ua*(b.Y - a.Y)));
        }
        
        return new Point(-1, -1);
    }*/
    
    public static Point pointOnLine(Point a, Point b, int x) {
        // returns the y coordinate of the point on line a, b at x
        double denom = b.X - a.X;
        if(denom == 0) {
            return new Point(x, Math.max(a.Y, b.Y));
        }
        // return y value that solves line equation between a and b
        return new Point(x, a.Y + (b.Y - a.Y) * (x - a.X) / (b.X - a.X));
    }
    
    public static Vector<Point> getPointsInTriangle(Triangle t, int imgHeight) {
        Point maxX = new Point(0, 0);
        Point midX = new Point(0, 0);
        Point minX = new Point(Integer.MAX_VALUE, 0);
        
        // get bounding X range
        if(t.a.X < minX.X) {
            minX = t.a;
        } else if(t.a.X > maxX.X) {
            maxX = t.a;
        }
        if(t.b.X < minX.X) {
            minX = t.b;
        } else if(t.b.X > maxX.X) {
            maxX = t.b;
        }
        if(t.c.X < minX.X) {
            minX = t.c;
        } else if(t.c.X > maxX.X) {
            maxX = t.c;
        }
        
        // midX is the point that is neither min nor max
        if(minX.X != t.a.X && maxX.X != t.a.X) {
            midX = t.a;
        } else if(minX.X != t.b.X && maxX.X != t.b.X) {
            midX = t.b;
        } else {
            midX = t.c;
        }
        Vector<Point> points = new Vector<Point>();
        
//        System.out.println("minX is " + minX.X + ", midX is " + midX.X + ", maxX is " + maxX.X);
        // get points from min to mid
        for(int x = minX.X; x < midX.X; x++) {
            // for each x, get bounding y's
            Point y1 = pointOnLine(minX, midX, x);
            Point y2 = pointOnLine(minX, maxX, x);
            if(y1.Y >= imgHeight || y2.Y >= imgHeight) {
                continue;
            } else {
            }

            int minY, maxY;
            if(y1.Y > y2.Y) {
                minY = y2.Y;
                maxY = y1.Y;
            } else {
                minY = y1.Y;
                maxY = y2.Y;
            }
            // now that we've found the range, add all points in that range
            for(int y = minY; y <= maxY; y++) {
                // validity checks
                if(y >= imgHeight || y < 0) {
                    continue;
                } else {
                    points.add(new Point(x, y));
                }
            }
        }
        for(int x = midX.X; x < maxX.X; x++) {
            // for each x, get bounding y's
            Point y1 = pointOnLine(midX, maxX, x);
            Point y2 = pointOnLine(minX, maxX, x);
            if(y1.Y >= imgHeight || y2.Y >= imgHeight) {
                continue;
            } else {
            }
            
            int minY, maxY;
            if(y1.Y > y2.Y) {
                minY = y2.Y;
                maxY = y1.Y;
            } else {
                minY = y1.Y;
                maxY = y2.Y;
            }
            // now that we've found the range, add all points in that range
            for(int y = minY; y <= maxY; y++) {
                // validity checks
                if(y >= imgHeight || y < 0) {
                    continue;
                } else {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

}
