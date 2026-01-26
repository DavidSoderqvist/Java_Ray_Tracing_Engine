package hittable;
import math.Vec3;

/**
 * Records information about a ray-object intersection.
 * 
 * This class stores the point of intersection, the normal at that point,
 * and the parameter t along the ray where the intersection occurs.
 */
public class HitRecord {
    public Vec3 p;
    public Vec3 normal;
    public double t;

    public HitRecord() {}
}
