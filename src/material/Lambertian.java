package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

/**
 * Represents a Lambertian (diffuse) material that scatters rays uniformly in all directions.
 */
public class Lambertian implements Material {
    private final Vec3 albedo;

    public Lambertian(Vec3 albedo) {
        this.albedo = albedo;
    }

    /**
     * Scatters the incoming ray in a random direction within a unit sphere around the hit point.
     *
     * @param rayIn The incoming ray.
     * @param rec The hit record containing intersection details.
     * @param wrapper A wrapper to hold the scattered ray and attenuation.
     * @return True if the ray is scattered, false otherwise.
     */
    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Wrapper wrapper) {
        Vec3 scatterDirection = rec.normal.add(Vec3.randomInUnitSphere());
        if (nearZero(scatterDirection)) {
            scatterDirection = rec.normal;
        }
        wrapper.scatteredRay = new Ray(rec.p, scatterDirection);
        wrapper.attenuation = albedo;
        return true;
    }

    /**
     * Checks if a vector is close to zero in all dimensions.
     *
     * @param v The vector to check.
     * @return True if the vector is near zero, false otherwise.
     */
    private boolean nearZero(Vec3 v) {
        double s = 1e-8;
        return (Math.abs(v.x) < s) && (Math.abs(v.y) < s) && (Math.abs(v.z) < s);
    }   
}
