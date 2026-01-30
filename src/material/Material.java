package material;

import hittable.HitRecord;
import math.Ray;
import math.Vec3;

/**
 * Interface representing a material that can scatter rays.
 */
public interface Material {
    boolean scatter(Ray rayIn, HitRecord rec, Wrapper wrapper);

    class Wrapper {
        public Ray scatteredRay;
        public Vec3 attenuation;
    }
}
