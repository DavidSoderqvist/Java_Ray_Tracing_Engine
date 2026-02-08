package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

public class MarbleTiles implements Material {
    private final Vec3 whiteColor;
    private final Vec3 greyColor;
    private final double fuzz;

    public MarbleTiles() {
        // Vi hårdkodar snygga marmorfärger här
        this.whiteColor = new Vec3(0.95, 0.95, 0.95); // Nästan vit
        this.greyColor = new Vec3(0.85, 0.85, 0.85);  // Ljusgrå
        this.fuzz = 0.02; // Liten oskärpa för realism
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, Wrapper wrapper) {
        // 1. Beräkna reflektion (samma som Metal)
        Vec3 reflected = Vec3.reflect(rIn.getDirection().normalize(), rec.normal);
        
        // 2. Skjut iväg strålen (med lite suddighet/fuzz)
        wrapper.scatteredRay = new Ray(rec.p, reflected.add(Vec3.randomInUnitSphere().scale(fuzz)));
        
        // 3. Välj färg baserat på position (samma som Checker)
        // Vi använder 5.0 här för lite större marmorplattor
        double sines = Math.sin(5.0 * rec.p.x) * Math.sin(5.0 * rec.p.z);
        
        if (sines < 0) {
            wrapper.attenuation = whiteColor;
        } else {
            wrapper.attenuation = greyColor;
        }

        // Returnera sant om strålen inte studsar inåt i marken
        return (wrapper.scatteredRay.getDirection().dot(rec.normal) > 0);
    }
}