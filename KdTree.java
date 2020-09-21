/** *****************************************************************************
 *  Name: Ruslan Zhdanov
 *  Date: 09/20/2020
 *  Description: Coursera Princeton Algorithms course part 1
 *               week 5 assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

/**
 * 2d-tree implementation. A mutable data type that uses
 * a 2d-tree to implement the same API (but replace PointSET with KdTree).
 * A 2d-tree is a generalization of a BST to two-dimensional keys.
 *
 * The idea is to build a BST with points in the nodes,
 * using the x- and y-coordinates of the points as keys
 * in strictly alternating sequence.
 */
public class KdTree {
    private int size;
    private KdNode root;

    public KdTree() {
        root = null;
        size = 0;
    }

    private static class KdNode {
        Point2D point;
        RectHV rect;
        KdNode left;
        KdNode right;
        Split splitDir;

        public KdNode(Point2D point, Split split) {
            this.point = point;
            this.splitDir = split;
        }
    }

    private enum Split {
        VERTICAL,
        HORIZONTAL;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }


    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        root = insert(root, p, null);
    }

    private KdNode insert(KdNode curr, Point2D nextPoint, KdNode parent) {
        if (curr == null) return new KdNode(nextPoint, swapDirectionFrom(parent));
        int compare = compareTroughDirection(curr, nextPoint);

        if (compare < 0) {
            curr.left = insert(curr.left, nextPoint, curr);
        } else if (compare > 0) {
            curr.right = insert(curr.right, nextPoint, curr);
        }

        return curr;
    }

    private Split swapDirectionFrom(KdNode prevNode) {
        if (prevNode == null || prevNode.splitDir == Split.HORIZONTAL) {
            return Split.VERTICAL;
        } else {
            return Split.HORIZONTAL;
        }
    }

    private int compareTroughDirection(KdNode node, Point2D nextPoint) {
        int compare;

        if (node.splitDir == Split.VERTICAL) {
            compare = Point2D.X_ORDER.compare(nextPoint, node.point);
        } else {
            compare = Point2D.Y_ORDER.compare(nextPoint, node.point);
        }
        return compare;
    }


    // TODO
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point must not be null");

        KdNode node = root;

        while (node != null) {
            int compare = compareTroughDirection(node, p);

            if (compare < 0) {
                node = node.left;
            } else if (compare > 0) {
                node = node.right;
            } else {
                return true;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, null);
    }

    private void draw(KdNode node, KdNode parent) {
        if (node == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();


        if (parent == null) {
            StdDraw.line(node.point.x(), 0, node.point.x(), 1);
        } else {
            int compare = compareTroughDirection(parent, node.point);

            if (compare > 0 && node.splitDir == Split.HORIZONTAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(parent.point.x(), node.point.y(), 1, node.point.y());

            } else if (compare < 0 && node.splitDir == Split.HORIZONTAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(0, node.point.y(), parent.point.x(), node.point.y());

            } else if (compare > 0 && node.splitDir == Split.VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), parent.point.y(), node.point.x(), 1);

            } else if (compare < 0 && node.splitDir == Split.VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), 0, node.point.x(), parent.point.y());
            }
        }

        draw(node.left, parent);
        draw(node.right, parent);
    }


    // TODO
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle must not be null");

        ArrayList<Point2D> arr = new ArrayList<>();

        // ???

        return arr;
    }

    /*
    private void draw(KdNode node, Iterable<Point2D> list) {
        if (node == null) return;

        node.point.draw();

        if (node.left != null) {
            draw(node.left);
        } else if (node.right != null) {
            draw(node.right);
        }
    }
    */


    // TODO
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Target Point must not be null");
        if (root == null) return null;


        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
