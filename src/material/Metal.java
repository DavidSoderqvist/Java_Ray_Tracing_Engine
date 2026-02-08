package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

/**
 * Metal material that reflects rays with some fuzziness.
 */
public class Metal implements Material {
    private final Vec3 albedo;
    private final double fuzz;

    public Metal(Vec3 albedo, double fuzz) {
        this.albedo = albedo;
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }
    
    public Metal(Vec3 albedo) {
        this(albedo, 0.0);
    }

    @Override
    /**
     * Scatter the ray by reflecting it off the surface with some fuzziness. The reflected ray is only valid if it goes in the same general direction as the normal (i.e., it doesn't go inside the surface).
     * @param rIn The incoming ray that hits the surface.
     * @param rec The hit record containing information about the hit point and normal.
     * @param wrapper A wrapper object to store the scattered ray and attenuation (albedo) for color calculation.
     * @return true if the ray is scattered (reflected), false if it is absorbed (e.g., if it goes inside the surface). The wrapper contains the scattered ray and the attenuation (albedo) for the color calculation.
     */
    public boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper) {
        Vec3 reflected = Vec3.reflect(rIn.getDirection().normalize(), rec.normal);
        wrapper.scatteredRay = new Ray(rec.p, reflected.add(Vec3.randomInUnitSphere().scale(fuzz)));
        wrapper.attenuation = albedo;
        
        return (wrapper.scatteredRay.getDirection().dot(rec.normal) > 0);
    }
}