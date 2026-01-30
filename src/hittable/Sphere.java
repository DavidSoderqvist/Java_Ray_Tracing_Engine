package hittable;

import math.Ray;
import math.Vec3;
import material.Material; // <-- Se till att denna är med

/**
 * Represents a sphere that can be hit by rays.
 */
public class Sphere implements Hittable {
    private final Vec3 center;
    private final double radius;
    private final Material mat; // Materialet som den här specifika bollen har

    /**
     * Constructs a Sphere with the given center, radius, and material.
     */
    public Sphere(Vec3 center, double radius, Material mat) {
        this.center = center;
        this.radius = radius;
        this.mat = mat;
    }

    /**
     * Checks if a ray hits the sphere between tMin and tMax.
     * If it does, fills in the HitRecord with the hit details.
     */
    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord rec) {
        Vec3 oc = ray.getOrigin().sub(center);
        
        double a = ray.getDirection().dot(ray.getDirection());
        double half_b = oc.dot(ray.getDirection());
        double c = oc.dot(oc) - radius * radius;
        
        double discriminant = half_b * half_b - a * c;
    
        if (discriminant > 0) {
            double root = Math.sqrt(discriminant);
        
            double temp = (-half_b - root) / a;
            if (temp < tMax && temp > tMin) {
                rec.t = temp;
                rec.p = ray.at(rec.t);
                rec.normal = rec.p.sub(center).scale(1.0 / radius);
                
                // --- HÄR ÄR RADEN SOM SAKNADES! ---
                // Vi måste berätta för rapporten vilket material bollen har
                rec.material = this.mat; 
                
                return true;
            }

            temp = (-half_b + root) / a;
            if (temp < tMax && temp > tMin) {
                rec.t = temp;
                rec.p = ray.at(rec.t);
                rec.normal = rec.p.sub(center).scale(1.0 / radius);
                
                // --- OCH HÄR OCKSÅ ---
                rec.material = this.mat;
                
                return true;
            }
        }
        return false;
    }
}