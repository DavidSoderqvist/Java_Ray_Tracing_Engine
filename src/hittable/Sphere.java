package hittable;

import material.Material;
import math.Ray;
import math.Vec3;

/**
 * Sphere class representing a sphere in the scene.
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
    // Ray-sphere intersection logic
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

    @Override
    // Axis-Aligned Bounding Box for the sphere
    public AABB boundingBox() {
        return new AABB(
            center.sub(new Vec3(radius, radius, radius)),
            center.add(new Vec3(radius, radius, radius))
        );
    }
}