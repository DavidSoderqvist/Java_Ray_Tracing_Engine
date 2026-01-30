package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

public class Lambertian implements Material {
    private final Vec3 albedo;

    public Lambertian(Vec3 albedo) {
        this.albedo = albedo;
    }

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

    private boolean nearZero(Vec3 v) {
        double s = 1e-8;
        return (Math.abs(v.x) < s) && (Math.abs(v.y) < s) && (Math.abs(v.z) < s);
    }   
}
