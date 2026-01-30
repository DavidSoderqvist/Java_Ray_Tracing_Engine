package main;

import math.Ray;
import math.Vec3;

/**
 * Represents a simple camera in 3D space for ray tracing.
 * The camera is defined by its position, orientation, and viewport size.
 */
public class Camera {
    private Vec3 origin;
    private Vec3 lowerLeftCorner;
    private Vec3 horizontal;
    private Vec3 vertical;

    /**
     * Constructs a Camera object.
     *
     * @param lookFrom    The position of the camera.
     * @param lookAt      The point the camera is looking at.
     * @param vup         The "up" direction for the camera.
     * @param vfov        The vertical field of view in degrees.
     * @param aspectRatio The aspect ratio of the viewport (width/height).
     */
    public Camera(Vec3 lookFrom, Vec3 lookAt, Vec3 vup, double vfov, double aspectRatio) {
        double theta = Math.toRadians(vfov);
        double h = Math.tan(theta / 2.0);
        
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspectRatio * viewportHeight;

        Vec3 w = lookFrom.sub(lookAt).normalize();
        
        Vec3 u = vup.cross(w).normalize();
        
        Vec3 v = w.cross(u);

        this.origin = lookFrom;
        this.horizontal = u.scale(viewportWidth);
        this.vertical = v.scale(viewportHeight);
        
        
        this.lowerLeftCorner = origin
                .sub(horizontal.scale(0.5))
                .sub(vertical.scale(0.5))
                .sub(w);
    }

    /**
     * Generates a ray from the camera through the viewport at normalized coordinates (s, t).
     *
     * @param s The horizontal coordinate in the range [0, 1].
     * @param t The vertical coordinate in the range [0, 1].
     * @return A Ray object originating from the camera through the specified point on the viewport.
     */
    public Ray getRay(double s, double t) {
        // direction = lowerLeft + s*horizontal + t*vertical - origin
        Vec3 direction = lowerLeftCorner
                .add(horizontal.scale(s))
                .add(vertical.scale(t))
                .sub(origin);
        
        return new Ray(origin, direction);
    }
}