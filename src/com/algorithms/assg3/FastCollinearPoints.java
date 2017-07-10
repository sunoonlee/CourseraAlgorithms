package com.algorithms.assg3;

/**
 * Finding collinear points: a faster, sort-based solution
 */
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;  // lineSegments.length > numSegments;
    private int numSegments = 0;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point p: points)
            if (p == null) throw new IllegalArgumentException();

        int n = points.length;
        lineSegments = new LineSegment[n];

        Point[] otherPts;
        Point[] curSegPoints;
        int equalSlopeCount = 1;

        for (int i = 0; i < n; i++) {
            Point p = points[i];
            otherPts = Arrays.copyOfRange(points, i + 1, n);
            Arrays.sort(otherPts, p.slopeOrder());

            for (int j = 1; j < otherPts.length + 1; j++) {
                if (j != otherPts.length && otherPts[j].slopeTo(p) == otherPts[j - 1].slopeTo(p))
                    equalSlopeCount += 1;
                else {
                    if (equalSlopeCount >= 3) {
                        curSegPoints = new Point[equalSlopeCount + 1];
                        curSegPoints[0] = p;
                        for (int k = 1; k < curSegPoints.length; k++)
                            curSegPoints[k] = otherPts[j - k];  // indices of collinear points: [j-equalSlopeCount, j-1]
                        Arrays.sort(curSegPoints);
                        lineSegments[numSegments++] = new LineSegment(curSegPoints[0], curSegPoints[curSegPoints.length - 1]);
                    }
                    equalSlopeCount = 1;  // restart counting
                }
            }
        }
    }

    public int numberOfSegments() {
        return numSegments;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, numSegments);
    }

    public static void main(String[] args) {
        Point[] points = {new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0),
                          new Point(1, 1), new Point(2, 2), new Point(3, 3)};
        BruteCollinearPoints collinearPoints = new BruteCollinearPoints(points);
        int num = collinearPoints.numberOfSegments();
        assert num == 2;
        LineSegment[] segments = collinearPoints.segments();
        // for (int i = 0; i < num; i++)
        //    System.out.println(segments[i]);
    }
}
