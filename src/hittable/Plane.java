package hittable;

import material.Material;
import math.Ray;
import math.Vec3;

/**
 * Represents an infinite plane in the scene.
 */
public class Plane implements Hittable {
    private final Vec3 point;   
    private final Vec3 normal;
    private final Material material;
    
    public Plane(Vec3 point, Vec3 normal, Material material) {
        this.point = point;
        this.normal = normal.normalize();
        this.material = material;
    }

    @Override
    /**
     * Checks if a ray hits the plane. If it does, it fills the HitRecord with the hit information.
     */
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec) {
        double denominator = r.getDirection().dot(normal);

        if (Math.abs(denominator) < 1e-6) {
            return false;
        }

        double t = (point.sub(r.getOrigin())).dot(normal) / denominator;

        if (t < tMin || t > tMax) {
            return false;
        }

        rec.t = t;
        rec.p = r.at(t);
        
        rec.setFaceNormal(r, normal);
        rec.material = material;

        return true;
    }

    @Override
    /**
     * Returns an AABB that encompasses the plane. Since the plane is infinite, we return a very large box.
     */
    public AABB boundingBox() {
        double inf = 1e9; // Ett jättestort tal
        
        return new AABB(
            new Vec3(-inf, point.y - 0.01, -inf),
            new Vec3(inf, point.y + 0.01, inf)
        );
    }
}