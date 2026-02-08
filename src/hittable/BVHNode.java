package hittable;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import math.Ray;

/**
 * BVHNode class for efficient ray-object intersection using a Bounding Volume Hierarchy.
 */
public class BVHNode implements Hittable {
    private final Hittable left;
    private final Hittable right;
    private final AABB box;

    // Constructor that builds the BVH from a list of Hittable objects
    public BVHNode(HittableList list) {
        this(list.objects, 0, list.objects.size());
    }

    /**
     * Recursive constructor to build the BVH tree.
     * @param objects List of Hittable objects to build the BVH from.
     * @param start Starting index of the current subset of objects.
     * @param end Ending index of the current subset of objects.
     */
    private BVHNode(List<Hittable> objects, int start, int end) {
        Random random = new Random();
        int axis = random.nextInt(3); // Sortera slumpmässigt på X, Y eller Z axeln

        
        Comparator<Hittable> comparator = (a, b) -> {
            AABB boxA = a.boundingBox();
            AABB boxB = b.boundingBox();
            double valA = (axis == 0) ? boxA.min.x : (axis == 1) ? boxA.min.y : boxA.min.z;
            double valB = (axis == 0) ? boxB.min.x : (axis == 1) ? boxB.min.y : boxB.min.z;
            return Double.compare(valA, valB);
        };

        int span = end - start;
        if (span == 1) {
            left = right = objects.get(start);
        } else if (span == 2) {
            if (comparator.compare(objects.get(start), objects.get(start + 1)) < 0) {
                left = objects.get(start);
                right = objects.get(start + 1);
            } else {
                left = objects.get(start + 1);
                right = objects.get(start);
            }
        } else {
            objects.subList(start, end).sort(comparator);
            int mid = start + span / 2;
            left = new BVHNode(objects, start, mid);
            right = new BVHNode(objects, mid, end);
        }

        AABB boxLeft = left.boundingBox();
        AABB boxRight = right.boundingBox();
        box = AABB.surroundingBox(boxLeft, boxRight);
    }

    @Override
    /**
     * Checks if a ray hits any object in the BVH node.
     * @param r The ray to test for intersection.
     * @param tMin Minimum t value for valid intersections.
     * @param tMax Maximum t value for valid intersections.
     * @param rec HitRecord to store intersection details if a hit occurs.
     * @return True if the ray hits an object, false otherwise.
     */
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec) {
        if (!box.hit(r, tMin, tMax)) return false;

        boolean hitLeft = left.hit(r, tMin, tMax, rec);
        boolean hitRight = right.hit(r, tMin, hitLeft ? rec.t : tMax, rec);

        return hitLeft || hitRight;
    }

    @Override
    /**
     * Returns the bounding box of the BVH node.
     * @return The AABB representing the bounding box of this BVH node.
     */
    public AABB boundingBox() {
        return box;
    }
}