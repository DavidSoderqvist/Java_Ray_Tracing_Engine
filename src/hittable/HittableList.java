package hittable;

import java.util.ArrayList;
import java.util.List;
import math.Ray;

/**
 * A class that represents a list of Hittable objects. It implements the Hittable interface itself,
 * allowing it to be treated as a single object in the scene.
 */
public class HittableList implements Hittable {
    public final List<Hittable> objects = new ArrayList<>();
    public HittableList() {}

    /**
     * Adds a Hittable object to the list.
     * @param object The Hittable object to add.
     */
    public void add(Hittable object) {
        objects.add(object);
    }

    @Override
    /**
     * Checks if the ray hits any object in the list. It iterates through all objects and keeps track of the closest hit.
     * @param r The ray to test for intersection.
     * @param tMin The minimum t value for a valid hit.
     * @param tMax The maximum t value for a valid hit.
     * @param rec A HitRecord to store the details of the hit if it occurs.
     * @return true if the ray hits any object, false otherwise.
     */
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

    @Override
    /**
     * Computes the bounding box that contains all objects in the list. It iterates through all objects and combines their bounding boxes.
     * @return An AABB that contains all objects, or null if the list is empty.
     */
    public AABB boundingBox() {
        if (objects.isEmpty()) return null;

        AABB outputBox = objects.get(0).boundingBox();
        for (int i = 1; i < objects.size(); i++) {
            outputBox = AABB.surroundingBox(outputBox, objects.get(i).boundingBox());
        }
        return outputBox;
    }

}