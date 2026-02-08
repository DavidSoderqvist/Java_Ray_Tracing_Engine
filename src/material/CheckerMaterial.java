package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;


public class CheckerMaterial implements Material {
    private final Vec3 oddColor;
    private final Vec3 evenColor;

    public CheckerMaterial(Vec3 c1, Vec3 c2) {
        this.oddColor = c1;
        this.evenColor = c2;
    }

    @Override
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