/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

    public PointSET() {

    } // construct an empty set of points
    public boolean isEmpty() {

        return false;
    }                     // is the set empty?

    public int size() {

        return 0;
    } // number of points in the set
    public void insert(Point2D p) {

    } // add the point to the set (if it is not already in the set)
    public boolean contains(Point2D p) {

        return  false;
    }           // does the set contain point p?
    public void draw() {

    }                        // draw all points to standard draw
    public Iterable<Point2D> range(RectHV rect) {

        return null;
    }            // all points that are inside the rectangle (or on the boundary)
    public Point2D nearest(Point2D p) {

        return null;
    }            // a nearest neighbor in the set to point p; null if the set is empty

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
