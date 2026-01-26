package hittable;

import math.Vec3;
import math.Ray;

/**
 * Represents a sphere in 3D space that can be intersected by rays.
 * 
 * This class implements the Hittable interface to allow ray-sphere intersection tests.
 */
public class Sphere implements Hittable {
    private final Vec3 center;
    private final double radius;
    
    /**
     * Constructs a new Sphere with the specified center and radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Vec3 center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Determines if a ray hits the sphere within a specified range.
     *
     * @param ray    The ray to test for intersection.
     * @param tMin   The minimum t value to consider for a hit.
     * @param tMax   The maximum t value to consider for a hit.
     * @param rec    A HitRecord to store intersection details if a hit occurs.
     * @return       True if the ray hits the sphere, false otherwise.
     */
    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord rec) {
        Vec3 oc = ray.getOrigin().sub(center);
        double a = ray.getDirection().dot(ray.getDirection());
        double b = oc.dot(ray.getDirection());
        double c = oc.dot(oc) - radius * radius;
        
        double discriminant = b * b - 4 * a * c;
    
        if (discriminant > 0) {
            double root = Math.sqrt(discriminant);
        
            double temp = (-b - root) / a;
            if (temp < tMax && temp > tMin) {
                rec.t = temp;
                rec.p = ray.at(rec.t);
                rec.normal = rec.p.sub(center).scale(1.0 / radius);
                return true;
            }

            temp = (-b + root) / a;
            if (temp < tMax && temp > tMin) {
                rec.t = temp;
                rec.p = ray.at(rec.t);
                rec.normal = rec.p.sub(center).scale(1.0 / radius);
                return true;
            }
        }
        return false;
    }


}
