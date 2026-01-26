package hittable;

import math.Ray;
import java.util.ArrayList;
import java.util.List;  

/**
 * Represents a list of Hittable objects that can be intersected by rays.
 * 
 * This class implements the Hittable interface to allow ray intersection tests
 * against multiple objects.
 */
public class HittableList implements Hittable{
    public final List<Hittable> objects = new ArrayList<>();
    
    /**
     * Constructs an empty HittableList.
     */
    public HittableList() {
    }

    /**
     * Adds a Hittable object to the list.
     *
     * @param object The Hittable object to add.
     */
    public void add(Hittable object) {
        objects.add(object);
    }

    /**
     * Determines if a ray hits any object in the list within a specified range.
     *
     * @param ray    The ray to test for intersection.
     * @param tMin   The minimum t value to consider for a hit.
     * @param tMax   The maximum t value to consider for a hit.
     * @param rec    A HitRecord to store intersection details if a hit occurs.
     * @return       True if the ray hits any object, false otherwise.
     */
    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord rec) {
        HitRecord tempRec = new HitRecord();
        boolean hitAnything = false;
        double closestSoFar = tMax;

        for (Hittable object : objects) {
            if (object.hit(ray, tMin, closestSoFar, tempRec)) {
                hitAnything = true;
                closestSoFar = tempRec.t;
                rec.t = tempRec.t;
                rec.p = tempRec.p;
                rec.normal = tempRec.normal;
            }
        } 
        return hitAnything;
    }
}
