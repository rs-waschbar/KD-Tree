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

    private class KdNode {
        Point2D point;
        RectHV rect;
        KdNode left;
        KdNode right;
        Split splitDir;

        public KdNode(Point2D point, Split split, RectHV rect) {
            this.point = point;
            this.splitDir = split;
            this.rect = rect;
            size++;
        }

        public String toString() {
            return "KdNode{" +
                    "point=" + point +
                    ", rect=" + rect +
                    ", splitDir=" + splitDir +
                    // ", \n   left=" + left +
                    // ", \n   right=" + right +
                    '}';
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
        if (root == null) {
            root = new KdNode(p, Split.VERTICAL, new RectHV(0, 0, 1, 1));
            return;
        }
        root = insert(root, p, null);
    }

    private KdNode insert(KdNode curr, Point2D nextPoint, KdNode parent) {
        if (curr == null) return createKdNode(nextPoint, parent);

        int compare = compareTroughDirection(curr, nextPoint);
        if (compare < 0) {
            curr.left = insert(curr.left, nextPoint, curr);
        } else { // if (compare >= 0)
            curr.right = insert(curr.right, nextPoint, curr);
        }

        return curr;
    }

    private KdNode createKdNode(Point2D point, KdNode parent) {
        KdNode curr = new KdNode(point,
                                 swapDirectionFrom(parent),
                                 getRect(point, parent));
        return curr;
    }

    private Split swapDirectionFrom(KdNode parent) {
        if (parent == null || parent.splitDir == Split.HORIZONTAL) {
            return Split.VERTICAL;
        } else {
            return Split.HORIZONTAL;
        }
    }

    private RectHV getRect(Point2D point, KdNode parent) {
        if (parent.splitDir == Split.VERTICAL && isSmaller(point, parent)) {
            return new RectHV(parent.rect.xmin(),
                              parent.rect.ymin(),
                              parent.point.x(),
                              parent.rect.ymax());

        } else if (parent.splitDir == Split.VERTICAL && !isSmaller(point, parent)) {
            return new RectHV(parent.point.x(),
                              parent.rect.ymin(),
                              parent.rect.xmax(),
                              parent.rect.ymax());

        } else if (parent.splitDir == Split.HORIZONTAL && isSmaller(point, parent)) {
            return new RectHV(parent.rect.xmin(),
                              parent.rect.ymin(),
                              parent.rect.xmax(),
                              parent.point.y());
        } else {
            return new RectHV(parent.rect.xmin(),
                              parent.point.y(),
                              parent.rect.xmax(),
                              parent.rect.ymax());
        }
    }

    private boolean isSmaller(Point2D point, KdNode parent) {
        return compareTroughDirection(parent, point) < 0;
    }

    private int compareTroughDirection(KdNode parent, Point2D point) {
        int compare;

        if (parent.splitDir == Split.VERTICAL) {
            compare = Point2D.X_ORDER.compare(point, parent.point);
        } else {
            compare = Point2D.Y_ORDER.compare(point, parent.point);
        }
        return compare;
    }

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
            int compare = compareTroughDirection(parent, node.point);

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


    // TODO
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle must not be null");

        ArrayList<Point2D> arr = new ArrayList<>();

        // ???

        return arr;
    }


    // TODO
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Target Point must not be null");
        if (root == null) return null;

        KdNode nearest = searchNearest(root, p, root);

        return nearest.point;
    }

    private KdNode searchNearest(KdNode curr, Point2D target, KdNode near) {
        if (curr == null) return near;
        //KdNode temp;


        System.out.println("** now here -> " + curr.point + " --- near point " + near.point);

        double currDist = curr.point.distanceSquaredTo(target);
        double minDist = near.point.distanceSquaredTo(target);

        if (minDist < currDist) {
            near = curr;
        }

        int compare = compareTroughDirection(curr, target);
        if (compare < 0) {
            near = searchNearest(curr.left, target, near);
            minDist = near.point.distanceSquaredTo(target);

            if (anotherMayContainNearest(curr, target, minDist)) {
                near = searchNearest(curr.right, target, near);
            }

        } else {
            near = searchNearest(curr.right, target, near);
            minDist = near.point.distanceSquaredTo(target);

            if (anotherMayContainNearest(curr, target, minDist)) {
                near = searchNearest(curr.left, target, near);
            }
        }
        return near;
    }

    private boolean anotherMayContainNearest(KdNode parent, Point2D target, double minDist) {
        if (parent.splitDir == Split.VERTICAL) {
            return target.distanceSquaredTo(new Point2D(parent.point.x(), target.y())) < minDist;
        } else {
            return target.distanceSquaredTo(new Point2D(target.x(), parent.point.y())) < minDist;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();

        System.out.println(kdTree.size());
        System.out.println(kdTree.root);

        System.out.println("*********");

        Point2D target = new Point2D(0.95, 0.1);

        kdTree.insert(new Point2D(0.5, 0.5));
        kdTree.insert(new Point2D(0.25, 0.3));
        kdTree.insert(new Point2D(0.7, 0.6));
        kdTree.insert(new Point2D(0.9, 0.2));
        kdTree.insert(new Point2D(0.2, 0.8));

        System.out.println(kdTree.root);
        System.out.println(kdTree.size());

        System.out.println(kdTree.nearest(target));


    }
}
