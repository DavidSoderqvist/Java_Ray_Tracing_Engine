package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

/**
 * Dielectric material for simulating transparent materials like glass.
 * Implements refraction and reflection based on the index of refraction.
 */
public class Dielectric implements Material {
    private final double ir;

    public Dielectric(double index) {
        this.ir = index;
    }

    @Override
    /**
     * Scatters the ray based on refraction and reflection. Uses Schlick's approximation for reflectance.
      * @param rIn The incoming ray.
      * @param rec The hit record containing information about the hit point.
      * @param wrapper A wrapper to hold the scattered ray and attenuation.
      * @return true if the ray is scattered, false otherwise.
     */
    public boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper) {
        wrapper.attenuation = new Vec3(1.0, 1.0, 1.0);
        
        // 1. Bestäm brytningsindex (Luft/Glas eller Glas/Luft)
        double refractionRatio = rec.frontFace ? (1.0 / ir) : ir;

        // 2. Normalisera inkommande stråle (VIKTIGT!)
        Vec3 unitDirection = rIn.getDirection().normalize();
        
        // 3. Beräkna Cosinus (vinkel)
        // Math.min säkerställer att vi aldrig får > 1.0
        double cosTheta = Math.min(unitDirection.scale(-1).dot(rec.normal), 1.0);
        
        // 4. Beräkna Sinus
        // VIKTIGT: Math.abs här skyddar mot NaN om cosTheta råkade bli 1.0000001
        double sinTheta = Math.sqrt(Math.abs(1.0 - cosTheta * cosTheta));

        // 5. Kan vi bryta ljuset? (Total Internal Reflection check)
        boolean cannotRefract = refractionRatio * sinTheta > 1.0;
        
        Vec3 direction;
        
        // Schlicks approximation för reflektion vid vinklar
        if (cannotRefract || reflectance(cosTheta, refractionRatio) > Math.random()) {
            direction = Vec3.reflect(unitDirection, rec.normal);
        } else {
            // Här anropar vi Vec3.refract (som vi också ska säkra upp, men Math.abs ovan hjälper)
            direction = Vec3.refract(unitDirection, rec.normal, refractionRatio);
        }

        wrapper.scatteredRay = new Ray(rec.p, direction);
        return true;
    }

    /**
     * Schlick's approximation for reflectance. Ger en enkel formel för att beräkna hur mycket ljus som reflekteras vs bryts baserat på vinkeln och brytningsindex.     
     * @param cosine
     * @param refIdx
     * @return
     */
    private static double reflectance(double cosine, double refIdx) {
        double r0 = (1 - refIdx) / (1 + refIdx);
        r0 = r0 * r0;
        return r0 + (1 - r0) * Math.pow((1 - cosine), 5);
    }
}