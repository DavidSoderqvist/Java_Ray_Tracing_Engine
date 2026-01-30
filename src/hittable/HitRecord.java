package hittable;

import math.Vec3;
import math.Ray;
import material.Material;

public class HitRecord {
    public Vec3 p;
    public Vec3 normal;
    public double t;
    public Material material;
    public boolean frontFace;

    public HitRecord() {}

    public void setFaceNormal(Ray r, Vec3 outwardNormal) {
        frontFace = r.getDirection().dot(outwardNormal) < 0;
        
        normal = frontFace ? outwardNormal : outwardNormal.scale(-1);
    }
}