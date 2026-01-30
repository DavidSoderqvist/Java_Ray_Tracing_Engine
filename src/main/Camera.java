package main;

import math.Ray;
import math.Vec3;


public class Camera {
    private final Vec3 origin;
    private final Vec3 lowerLeftCorner;
    private final Vec3 horizontal;
    private final Vec3 vertical;

    public Camera() {
        double aspectRatio = 16.0 / 9.0;
        double viewportHeight = 2.0;
        double viewportWidth = aspectRatio * viewportHeight;
        double focalLength = 1.0;

        this.lowerLeftCorner = origin
                .sub(horizontal.scale(0.5))
                .sub(vertical.scale(0.5))
                .sub(new Vec3(0, 0, focalLength));
        
        public Ray getRay(double u, double v) {
            Vec3 direction = lowerLeftCorner
                    .add(horizontal.scale(u))
                    .add(vertical.scale(v))
                    .sub(origin);
            return new Ray(origin, direction); 

    }
}
