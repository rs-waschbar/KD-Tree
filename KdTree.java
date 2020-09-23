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
        if (p == null) throw new IllegalArgumentException("Point must not be null");

        root = insert(root, p, null);
    }

    private KdNode insert(KdNode curr, Point2D nextPoint, KdNode parent) {
        if (curr == null) {
            size++;
            return new KdNode(nextPoint, swapDirectionFrom(parent));
        }
        if (nextPoint.equals(curr.point)) {
            return curr;
        }

        int compare = compareSplitDirection(curr, nextPoint);
        if (compare < 0) {
            curr.left = insert(curr.left, nextPoint, curr);
        } else { // if (compare >= 0)
            curr.right = insert(curr.right, nextPoint, curr);
        }

        return curr;
    }

    private Split swapDirectionFrom(KdNode parent) {
        if (parent == null || parent.splitDir == Split.HORIZONTAL) {
            return Split.VERTICAL;
        } else {
            return Split.HORIZONTAL;
        }
    }

    private int compareSplitDirection(KdNode parent, Point2D point) {
        int compare;

        if (parent.splitDir == Split.VERTICAL) {
            compare = Point2D.X_ORDER.compare(point, parent.point);
        } else {
            compare = Point2D.Y_ORDER.compare(point, parent.point);
        }
        return compare;
    }

    // helper method for rectangles, where we can't create explicit points
    // due to the assignment requirements for the size of the used memory
    private int compareSplitDirection(KdNode parent, double x, double y) {
        int compare;

        if (parent.splitDir == Split.VERTICAL) {
            compare = Double.compare(x, parent.point.x());
        } else {
            compare = Double.compare(y, parent.point.y());
        }
        return compare;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point must not be null");
        KdNode node = root;

        while (node != null) {
            if (p.equals(node.point)) {
                return true;
            }
            int compare = compareSplitDirection(node, p);
            if (compare < 0) {
                node = node.left;
            } else { // if (compare >= 0)
                node = node.right;
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

        drawNodePoint(node);
        drawSplitLine(node, parent);

        draw(node.left, parent);
        draw(node.right, parent);
    }


    private void drawNodePoint(KdNode node) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();
    }

    private void drawSplitLine(KdNode node, KdNode parent) {
        if (parent == null) {
            StdDraw.line(node.point.x(), 0, node.point.x(), 1);

        } else {
            int compare = compareSplitDirection(parent, node.point);

            if (compare > 0 && node.splitDir == Split.HORIZONTAL) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(parent.point.x(), node.point.y(), 1, node.point.y());

            } else if (compare < 0 && node.splitDir == Split.HORIZONTAL) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(0, node.point.y(), parent.point.x(), node.point.y());

            } else if (compare > 0 && node.splitDir == Split.VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), parent.point.y(), node.point.x(), 1);

            } else if (compare < 0 && node.splitDir == Split.VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), 0, node.point.x(), parent.point.y());
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle must not be null");

        ArrayList<Point2D> arr = new ArrayList<>();
        searchPointsInRect(root, rect, arr);

        return arr;
    }

    private void searchPointsInRect(KdNode node, RectHV rect, ArrayList<Point2D> result) {
        if (node == null) return;

        if (rect.contains(node.point)) {
            result.add(node.point);
        }

        int compare = compareRectToNode(node, rect);

        if (compare < -1) {
            searchPointsInRect(node.left, rect, result);
        } else if (compare >= 1) {
            searchPointsInRect(node.right, rect, result);
        } else {
            searchPointsInRect(node.left, rect, result);
            searchPointsInRect(node.right, rect, result);
        }
    }

    private int compareRectToNode(KdNode node, RectHV rect) {
        return compareSplitDirection(node, rect.xmin(), rect.ymin())
                + compareSplitDirection(node, rect.xmax(), rect.ymax());
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Target Point must not be null");
        if (root == null) return null;

        KdNode nearest = searchNearest(root, p, root);

        return nearest.point;
    }

    private KdNode searchNearest(KdNode curr, Point2D target, KdNode near) {
        if (curr == null) return near;

        KdNode best = near;

        double currDist = curr.point.distanceSquaredTo(target);
        double minDist = best.point.distanceSquaredTo(target);

        if (currDist < minDist) {
            best = curr;
        }

        int compare = compareSplitDirection(curr, target);
        if (compare < 0) {
            best = searchNearest(curr.left, target, best);
            minDist = near.point.distanceSquaredTo(target);

            if (anotherMayContainNearest(curr, target, minDist)) {
                best = searchNearest(curr.right, target, best);
            }

        } else {
            best = searchNearest(curr.right, target, best);
            minDist = near.point.distanceSquaredTo(target);

            if (anotherMayContainNearest(curr, target, minDist)) {
                best = searchNearest(curr.left, target, best);
            }
        }
        return best;
    }

    private boolean anotherMayContainNearest(KdNode parent, Point2D target, double minDist) {
        if (parent.splitDir == Split.VERTICAL) {
            return distanceSquaredTo(target, parent.point.x(), target.y()) < minDist;
        } else {
            return distanceSquaredTo(target, target.x(), parent.point.y()) < minDist;
        }
    }

    private double distanceSquaredTo(Point2D target, double x, double y) {
        double dx = target.x() - x;
        double dy = target.y() - y;
        return dx*dx + dy*dy;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {


        KdTree kdTree = new KdTree();

        System.out.println(kdTree.size());
        System.out.println(kdTree.root);

        System.out.println("*********");

        Point2D p1 = new Point2D(0.25, 0.3);
        Point2D p2 = new Point2D(0.7, 0.6);

        kdTree.insert(new Point2D(0.5, 0.5));
        kdTree.insert(p1);
        kdTree.insert(new Point2D(0.2, 0.8));
        kdTree.insert(new Point2D(0.4, 0.6));
        kdTree.insert(new Point2D(0.7, 0.1));
        kdTree.insert(new Point2D(0.7, 0.1));
        kdTree.insert(new Point2D(0.7, 0.1));


        Point2D target = new Point2D(0.73, 0.5);
        Point2D target2 = new Point2D(0.3, 0.9);

        System.out.println("contain test true: " + kdTree.contains(p1));
        System.out.println("contain test false: " + kdTree.contains(p2));

        System.out.println("********");

        System.out.println(kdTree.root);
        System.out.println(kdTree.size());

        System.out.println("target: " + target);
        System.out.println(kdTree.nearest(target));

        System.out.println("target2: " + target2);
        System.out.println(kdTree.nearest(target2));


    }
}
