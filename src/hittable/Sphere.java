package hittable;

import math.Ray;
import math.Vec3;
import material.Material;

/**
 * Represents a sphere in 3D space that can be hit by rays.
 */
public class Sphere implements Hittable {
    private final Vec3 center;
    private final double radius;
    private final Material mat;

    public Sphere(Vec3 center, double radius, Material mat) {
        this.center = center;
        this.radius = radius;
        this.mat = mat;
    }

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord rec) {
        Vec3 oc = ray.getOrigin().sub(center);
        
        double a = ray.getDirection().lengthSquared();
        double half_b = oc.dot(ray.getDirection());
        double c = oc.dot(oc) - radius * radius;
        double discriminant = half_b * half_b - a * c;
    
        if (discriminant > 0) {
            double root = Math.sqrt(discriminant);
            double temp = (-half_b - root) / a;

            if (temp < tMax && temp > tMin) {
                rec.t = temp;
                rec.p = ray.at(rec.t);
                
                Vec3 outwardNormal = rec.p.sub(center).scale(1.0 / radius);

                rec.setFaceNormal(ray, outwardNormal);
                
                rec.material = this.mat;
                return true;
            }

            temp = (-half_b + root) / a;
            if (temp < tMax && temp > tMin) {
                rec.t = temp;
                rec.p = ray.at(rec.t);
                Vec3 outwardNormal = rec.p.sub(center).scale(1.0 / radius);
                
                rec.setFaceNormal(ray, outwardNormal);
                
                rec.material = this.mat;
                return true;
            }
        }
        return false;
    }
}