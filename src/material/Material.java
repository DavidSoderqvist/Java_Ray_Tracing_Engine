package material;

import hittable.HitRecord;
import math.Ray;

public interface Material {
    /**
     * Scatters a ray hitting the material.
     * * @param rIn The incoming ray.
     * @param rec The hit record containing details about the hit point.
     * @param wrapper A container to store the scattered ray and attenuation (color).
     * @return true if the ray scattered, false if it was absorbed.
     */
    boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper);
}