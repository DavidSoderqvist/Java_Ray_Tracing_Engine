package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

public class WoodCheckerMaterial implements Material {
    private final Vec3 lightWood;
    private final Vec3 darkWood;
    private final double fuzz;

    public WoodCheckerMaterial() {
        // HÄR ÄR DE NYA MÖRKARE FÄRGERNA:
        
        // Färg 1: Mörk Valnöt (Djup brun)
        this.lightWood = new Vec3(0.20, 0.10, 0.05); 
        
        // Färg 2: Ebenholts (Nästan svart, mycket mörkt trä)
        this.darkWood = new Vec3(0.05, 0.05, 0.05);
        
        // Lite blankare än förut (0.05) för en "nypolerad" look
        this.fuzz = 0.05; 
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper) {
        // 1. Beräkna reflektion
        Vec3 reflected = Vec3.reflect(rIn.getDirection().normalize(), rec.normal);
        
        // 2. Lägg på lite suddighet (fuzz)
        wrapper.scatteredRay = new Ray(rec.p, reflected.add(Vec3.randomInUnitSphere().scale(fuzz)));
        
        // 3. Schackrutorna
        double sines = Math.sin(4.0 * rec.p.x) * Math.sin(4.0 * rec.p.z);
        
        if (sines < 0) {
            wrapper.attenuation = lightWood;
        } else {
            wrapper.attenuation = darkWood;
        }

        // Returnera sant om strålen studsar uppåt
        return (wrapper.scatteredRay.getDirection().dot(rec.normal) > 0);
    }
}