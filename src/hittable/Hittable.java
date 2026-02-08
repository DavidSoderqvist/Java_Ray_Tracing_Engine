package hittable;
import math.Ray;

/**
 * Hittable interface for objects that can be hit by rays.
 */
public interface Hittable {
    /**
     * Checks if a ray hits the object within a certain range.
     * @param r The ray to test.
     * @param tMin Minimum t value (closest hit).
     * @param tMax Maximum t value (farthest hit).
     * @param rec HitRecord to store hit information if a hit occurs.
     * @return true if the ray hits the object, false otherwise.
     */
    boolean hit(Ray r, double tMin, double tMax, HitRecord rec);
    
    /**
     * Computes the axis-aligned bounding box (AABB) for the object.
     * @return The AABB of the object.
     */
    AABB boundingBox();
}