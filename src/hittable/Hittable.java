package hittable;
import math.Ray;

public interface Hittable {
    /**
     * Determines if a ray hits the object within a specified range.
     *
     * @param r      The ray to test for intersection.
     * @param tMin   The minimum t value to consider for a hit.
     * @param tMax   The maximum t value to consider for a hit.
     * @param rec    A HitRecord to store intersection details if a hit occurs.
     * @return       True if the ray hits the object, false otherwise.
     */
    boolean hit(Ray ray, double tMin, double tMax, HitRecord rec);
}
