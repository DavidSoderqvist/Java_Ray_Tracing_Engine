package hittable;

import material.Material;
import math.Ray;
import math.Vec3;

/**
 * A class to store information about a ray-object intersection.
 */
public class HitRecord {
    public Vec3 p;
    public Vec3 normal;
    public double t;
    public Material material;
    public boolean frontFace;

    /**
     * Default constructor.
     */
    public HitRecord() {}

    /**
     * Set the face normal and determine if the hit was on the front face.
     * @param r The ray that hit the object.
     * @param outwardNormal The normal vector pointing outward from the surface.
     */
    public void setFaceNormal(Ray r, Vec3 outwardNormal) {
        frontFace = r.getDirection().dot(outwardNormal) < 0;
        
        normal = frontFace ? outwardNormal : outwardNormal.scale(-1);
    }
}