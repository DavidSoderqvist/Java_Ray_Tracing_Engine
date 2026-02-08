package hittable;

import math.Ray;
import math.Vec3;

/**
 * Axis-Aligned Bounding Box (AABB) class for efficient ray-object intersection tests.
 */
public class AABB {
    public final Vec3 min;
    public final Vec3 max;

    public AABB(Vec3 min, Vec3 max) {
        this.min = min;
        this.max = max;
    }

    /* Method to check if a ray intersects the bounding box. */
    public boolean hit(Ray r, double tMin, double tMax) {
        for (int a = 0; a < 3; a++) {
            double origin = (a == 0) ? r.getOrigin().x : (a == 1) ? r.getOrigin().y : r.getOrigin().z;
            double direction = (a == 0) ? r.getDirection().x : (a == 1) ? r.getDirection().y : r.getDirection().z;
            
            double invD = 1.0 / direction;
            double t0 = ((a == 0 ? min.x : (a == 1 ? min.y : min.z)) - origin) * invD;
            double t1 = ((a == 0 ? max.x : (a == 1 ? max.y : max.z)) - origin) * invD;
            
            if (invD < 0.0) {
                double temp = t0; t0 = t1; t1 = temp;
            }
            
            tMin = t0 > tMin ? t0 : tMin;
            tMax = t1 < tMax ? t1 : tMax;
            
            if (tMax <= tMin) return false;
        }
        return true;
    }
    
    /* Static method to create a bounding box that encompasses two given boxes. */
    public static AABB surroundingBox(AABB box0, AABB box1) {
        Vec3 small = new Vec3(
            Math.min(box0.min.x, box1.min.x),
            Math.min(box0.min.y, box1.min.y),
            Math.min(box0.min.z, box1.min.z)
        );
        Vec3 big = new Vec3(
            Math.max(box0.max.x, box1.max.x),
            Math.max(box0.max.y, box1.max.y),
            Math.max(box0.max.z, box1.max.z)
        );
        return new AABB(small, big);
    }
}