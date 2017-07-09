package com.algorithms.assg3;

/**
 * Finding collinear points: a brute-force solution
 * examines 4 points at a time and checks whether they all lie on the same line segment,
 * returning all such line segments.
 */
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] lineSegments;  // lineSegments.length > numSegments;
    private int numSegments = 0;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point p: points)
            if (p == null) throw new IllegalArgumentException();

        int n = points.length;
        lineSegments = new LineSegment[n];
        Point[] curSegmentPoints;
        double slopej, slopek, slopel;  // slope to point i

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                slopej = points[j].slopeTo(points[i]);
                if (slopej == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();

                for (int k = j + 1; k < n; k++) {
                    slopek = points[k].slopeTo(points[i]);
                    if (slopej != slopek) continue;

                    for (int l = k + 1; l < n; l++) {
                        slopel = points[l].slopeTo(points[i]);
                        if (slopel != slopej) continue;

                        curSegmentPoints = new Point[]{points[i], points[j], points[k], points[l]};
                        Arrays.sort(curSegmentPoints);
                        lineSegments[numSegments] = new LineSegment(curSegmentPoints[0], curSegmentPoints[3]);
                        numSegments++;
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
        for (int i = 0; i < num; i++)
            System.out.println(segments[i]);
    }
}
