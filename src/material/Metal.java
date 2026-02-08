package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

public class Metal implements Material {
    private final Vec3 albedo;
    private final double fuzz;

    public Metal(Vec3 albedo, double fuzz) {
        this.albedo = albedo;
        // Vi begränsar fuzz till max 1.0 så det inte blir tokigt
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }
    
    // En extra konstruktor om du vill skapa perfekt metall utan fuzz
    public Metal(Vec3 albedo) {
        this(albedo, 0.0);
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper) {
        Vec3 reflected = Vec3.reflect(rIn.getDirection().normalize(), rec.normal);
        
        // Här lägger vi till slumpmässigheten (fuzz)
        // Vi behöver randomInUnitSphere() i Vec3. Om du saknar den, se nedan!
        wrapper.scatteredRay = new Ray(rec.p, reflected.add(Vec3.randomInUnitSphere().scale(fuzz)));
        wrapper.attenuation = albedo;
        
        return (wrapper.scatteredRay.getDirection().dot(rec.normal) > 0);
    }
}