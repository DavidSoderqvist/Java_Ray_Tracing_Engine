package hittable;
import math.Ray;

public interface Hittable {
    boolean hit(Ray r, double tMin, double tMax, HitRecord rec);
    
    AABB boundingBox();
}