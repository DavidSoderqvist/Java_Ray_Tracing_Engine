package hittable;

import math.Ray;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BVHNode implements Hittable {
    private final Hittable left;
    private final Hittable right;
    private final AABB box;

    public BVHNode(HittableList list) {
        this(list.objects, 0, list.objects.size());
    }

    private BVHNode(List<Hittable> objects, int start, int end) {
        Random random = new Random();
        int axis = random.nextInt(3); // Sortera slumpmässigt på X, Y eller Z axeln

        // Jämförare för sortering
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
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec) {
        if (!box.hit(r, tMin, tMax)) return false;

        boolean hitLeft = left.hit(r, tMin, tMax, rec);
        boolean hitRight = right.hit(r, tMin, hitLeft ? rec.t : tMax, rec);

        return hitLeft || hitRight;
    }

    @Override
    public AABB boundingBox() {
        return box;
    }
}