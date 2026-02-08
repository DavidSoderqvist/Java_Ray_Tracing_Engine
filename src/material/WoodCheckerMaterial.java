package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

/**
 * A material that creates a wood-like checkerboard pattern on surfaces.
 * It uses two colors to create the checker effect and has a slight fuzziness for reflections.
 */
public class WoodCheckerMaterial implements Material {
    private final Vec3 lightWood;
    private final Vec3 darkWood;
    private final double fuzz;

    public WoodCheckerMaterial() {
        this.lightWood = new Vec3(0.20, 0.10, 0.05);
        this.darkWood = new Vec3(0.05, 0.05, 0.05);
        this.fuzz = 0.05; 
    }

    @Override
    /**
     * Determines how the material scatters rays. It creates a checkerboard pattern based on the position of the hit point.
     * The scattered ray is reflected with a slight fuzziness, and the attenuation is determined by the checker pattern.
     * @param rIn The incoming ray.
     * @param rec The hit record containing information about the hit point.
     * @param wrapper A wrapper object to hold the scattered ray and attenuation.
     * @return true if the ray is scattered, false otherwise.
     */
    public boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper) {
        Vec3 reflected = Vec3.reflect(rIn.getDirection().normalize(), rec.normal);
        wrapper.scatteredRay = new Ray(rec.p, reflected.add(Vec3.randomInUnitSphere().scale(fuzz)));
        double sines = Math.sin(4.0 * rec.p.x) * Math.sin(4.0 * rec.p.z);
        
        if (sines < 0) {
            wrapper.attenuation = lightWood;
        } else {
            wrapper.attenuation = darkWood;
        }

        return (wrapper.scatteredRay.getDirection().dot(rec.normal) > 0);
    }
}