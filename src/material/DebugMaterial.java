package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

public class DebugMaterial implements Material {
    
    @Override
    public boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper) {
        // Skicka strålen vidare men ändra färgen baserat på frontFace
        
        // Utsida (Rätt) = BLÅ (0, 0, 1)
        // Insida (Fel)  = RÖD (1, 0, 0)
        wrapper.attenuation = rec.frontFace ? new Vec3(0.0, 0.0, 1.0) : new Vec3(1.0, 0.0, 0.0);
        
        // Studsa slumpmässigt (som lera) bara för att vi ska se färgen
        Vec3 scatterDirection = rec.normal.add(Vec3.randomInUnitSphere()).normalize();
        wrapper.scatteredRay = new Ray(rec.p, scatterDirection);
        
        return true;
    }
}