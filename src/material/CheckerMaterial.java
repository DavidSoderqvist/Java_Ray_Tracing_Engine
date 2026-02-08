package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

/**
 * A simple checkerboard material that alternates between two colors based on the position.
 */
public class CheckerMaterial implements Material {
    private final Vec3 oddColor;
    private final Vec3 evenColor;

    public CheckerMaterial(Vec3 c1, Vec3 c2) {
        this.oddColor = c1;
        this.evenColor = c2;
    }

    @Override
    /**
     * Determines how the material scatters rays. For the checkerboard, we simply return a color based on the position of the hit point.
     * We also create a scattered ray that reflects diffusely from the surface.
     * @param rIn The incoming ray.
     * @param rec The hit record containing information about the hit point.
     * @param wrapper A wrapper object to hold the scattered ray and attenuation color.
     * @return true if the ray is scattered, false otherwise. 
     */
    public boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper) {
        wrapper.attenuation = color(rec.p);
        
        // Reflektera diffus (matt)
        Vec3 scatterDirection = rec.normal.add(Vec3.randomInUnitSphere()).normalize();
        
        // Fånga degenererade strålar
        if (scatterDirection.nearZero()) {
            scatterDirection = rec.normal;
        }

        wrapper.scatteredRay = new Ray(rec.p, scatterDirection);
        return true;
    }

    /**
     * Determines the color of the material at a given point. We use a sine function to create a checkerboard pattern.
     * @param p The point to determine the color for.
     * @return The color at the given point.
     */
    private Vec3 color(Vec3 p) {
        // Använd sinus för att skapa rutnätet. 10 styr storleken på rutorna.
        double sines = Math.sin(10 * p.x) * Math.sin(10 * p.z);
        if (sines < 0) {
            return oddColor;
        } else {
            return evenColor;
        }
    }
}