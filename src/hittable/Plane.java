package hittable;

import material.Material;
import math.Ray;
import math.Vec3;

public class Plane implements Hittable {
    private final Vec3 point;   // En punkt som planet går genom (t.ex. 0,0,0)
    private final Vec3 normal;  // Planets riktning (t.ex. rakt upp: 0,1,0)
    private final Material material;
    
    public Plane(Vec3 point, Vec3 normal, Material material) {
        this.point = point;
        this.normal = normal.normalize(); // Viktigt att normalisera riktningen!
        this.material = material;
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec) {
        // Vi använder skalärprodukten (dot product) för att se om strålen
        // är parallell med planet.
        double denominator = r.getDirection().dot(normal);

        // Om denominator är nära 0 betyder det att strålen går parallellt 
        // med marken och aldrig kommer träffa (eller träffa oändligt långt bort).
        if (Math.abs(denominator) < 1e-6) {
            return false;
        }

        // Matematisk formel för att hitta avståndet 't' till träffpunkten
        // t = (point - rayOrigin) . normal / (rayDirection . normal)
        double t = (point.sub(r.getOrigin())).dot(normal) / denominator;

        // Kolla om träffen ligger inom det giltiga intervallet (tMin < t < tMax)
        if (t < tMin || t > tMax) {
            return false;
        }

        // --- VI HAR EN TRÄFF! ---
        rec.t = t;
        rec.p = r.at(t);
        
        // Sätt normalen så att den pekar mot strålen (viktigt för reflektioner)
        rec.setFaceNormal(r, normal);
        rec.material = material;

        return true;
    }

    @Override
    public AABB boundingBox() {
        // Ett oändligt plan är svårt att lägga i en låda (BVH).
        // Vi skapar en "fejkad" jättestor låda så att BVH-trädet inte kraschar.
        // Den sträcker sig oändligt i X och Z, men är tunn i Y-led (höjden).
        
        double inf = 1e9; // Ett jättestort tal
        
        // Justera boxen lite beroende på planets position, men gör den enorm i sidled.
        return new AABB(
            new Vec3(-inf, point.y - 0.01, -inf),
            new Vec3(inf, point.y + 0.01, inf)
        );
    }
}