/** *****************************************************************************
 *  Name: Ruslan Zhdanov
 *  Date: 09/20/2020
 *  Description: Coursera Princeton Algorithms course part 1
 *               week 5 assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Bruteforce implementation that represents
 * a set of points in the unit square.
 * Implementing by using a red–black BST:
 */
public class PointSET {
    private final TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point must not be null");
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point must not be null");
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle must not be null");

        ArrayList<Point2D> arr = new ArrayList<>();

        for (Point2D p : points) {
            if (rect.contains(p)) {
                arr.add(p);
            }
        }
        return arr;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Target Point must not be null");
        if (points.isEmpty()) return null;

        Point2D nearest = points.first();
        double minDist = nearest.distanceTo(p);

        for (Point2D curr : points) {
            double currDist = curr.distanceTo(p);
            if (currDist < minDist) {
                nearest = curr;
                minDist = currDist;
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
