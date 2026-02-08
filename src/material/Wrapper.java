package material;

import math.Ray;
import math.Vec3;

/**
 * A simple wrapper class to hold the scattered ray and its attenuation.
 * This is used to return both values from the scatter method in Material.
 */
public class Wrapper {
    public Ray scatteredRay;
    public Vec3 attenuation;

    public Wrapper(Ray scatteredRay, Vec3 attenuation) {
        this.scatteredRay = scatteredRay;
        this.attenuation = attenuation;
    }
}