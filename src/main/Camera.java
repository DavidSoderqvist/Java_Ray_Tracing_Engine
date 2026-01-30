package main;

import math.Ray;
import math.Vec3;

/**
 * Represents a simple camera in 3D space.
 */
public class Camera {
    private final Vec3 origin;
    private final Vec3 lowerLeftCorner;
    private final Vec3 horizontal;
    private final Vec3 vertical;

    /**
     * Initializes the camera with default parameters.
     */
    public Camera() {
        // Hårdkodade värden för nu (vi kan göra dem flexibla senare)
        double aspectRatio = 16.0 / 9.0;
        double viewportHeight = 2.0;
        double viewportWidth = aspectRatio * viewportHeight;
        double focalLength = 1.0;

        this.origin = new Vec3(0, 0, 0);
        this.horizontal = new Vec3(viewportWidth, 0, 0);
        this.vertical = new Vec3(0, viewportHeight, 0);
        
        // Räkna ut hörnet (samma matte som vi hade i Main förut)
        this.lowerLeftCorner = origin
                .sub(horizontal.scale(0.5))
                .sub(vertical.scale(0.5))
                .sub(new Vec3(0, 0, focalLength));
    }

    /**
     * Generates a ray from the camera through the viewport at normalized coordinates (u, v).
     *
     * @param u Horizontal coordinate in [0, 1].
     * @param v Vertical coordinate in [0, 1].
     * @return  The generated Ray.
     */
    public Ray getRay(double u, double v) {
        // direction = lowerLeft + u*horizontal + v*vertical - origin
        Vec3 direction = lowerLeftCorner
                .add(horizontal.scale(u))
                .add(vertical.scale(v))
                .sub(origin);
        
        return new Ray(origin, direction);
    }
}