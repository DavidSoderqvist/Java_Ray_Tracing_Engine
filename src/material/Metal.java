package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

/**
 * Represents a metallic material that reflects rays.
 */
public class Metal implements Material {
    private final Vec3 albedo;
    
    public Metal(Vec3 albedo) {
        this.albedo = albedo;
    }

    @Override
    /**
     * Scatters the incoming ray by reflecting it off the surface.
     *
     * @param rIn The incoming ray.
     * @param rec The hit record containing intersection details.
     * @param wrapper A wrapper to hold the scattered ray and attenuation.
     * @return True if the ray is scattered, false otherwise.
     */
    public boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper) {
        Vec3 reflected = Vec3.reflect(rIn.getDirection().normalize(), rec.normal);
        wrapper.scatteredRay = new Ray(rec.p, reflected);
        wrapper.attenuation = albedo;
        return (wrapper.scatteredRay.getDirection().dot(rec.normal) > 0);
    }

}
