package hittable;

import math.Ray;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of Hittable objects that can be tested for ray intersections.
 */
public class HittableList implements Hittable {
    public final List<Hittable> objects = new ArrayList<>();

    public HittableList() {}

    public void add(Hittable object) {
        objects.add(object);
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec) {
        HitRecord tempRec = new HitRecord();
        boolean hitAnything = false;
        double closestSoFar = tMax;

        for (Hittable object : objects) {
            if (object.hit(r, tMin, closestSoFar, tempRec)) {
                hitAnything = true;
                closestSoFar = tempRec.t;
                
                rec.t = tempRec.t;
                rec.p = tempRec.p;
                rec.normal = tempRec.normal;
                
                rec.material = tempRec.material;
            }
        }

        return hitAnything;
    }
}