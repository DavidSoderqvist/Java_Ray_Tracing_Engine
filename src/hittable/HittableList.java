package hittable;

import java.util.ArrayList;
import java.util.List;
import math.Ray;

/**
 * A list of Hittable objects.
 */
public class HittableList implements Hittable {
    public final List<Hittable> objects = new ArrayList<>();

    public HittableList() {}

    public void add(Hittable object) {
        objects.add(object);
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec) {
        HitRecord tempRec = new HitRecord();
        boolean hitAnything = false;
        double closestSoFar = tMax;

        for (Hittable object : objects) {
            if (object.hit(r, tMin, closestSoFar, tempRec)) {
                hitAnything = true;
                closestSoFar = tempRec.t;
                
                // KOPIERA ALL DATA:
                rec.t = tempRec.t;
                rec.p = tempRec.p;
                rec.normal = tempRec.normal;
                rec.material = tempRec.material;
                
                // --- HÄR SAKNADES DET! ---
                // Utan denna rad tror Main alltid att frontFace är false (RÖD)
                rec.frontFace = tempRec.frontFace; 
            }
        }

        return hitAnything;
    }

    // Lägg till denna metod längst ner i HittableList:
    @Override
    public AABB boundingBox() {
        if (objects.isEmpty()) return null;

        AABB outputBox = objects.get(0).boundingBox();
        for (int i = 1; i < objects.size(); i++) {
            outputBox = AABB.surroundingBox(outputBox, objects.get(i).boundingBox());
        }
        return outputBox;
    }

}