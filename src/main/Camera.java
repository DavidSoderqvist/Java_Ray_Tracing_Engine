package main;

import math.Ray;
import math.Vec3;

public class Camera {
    private Vec3 origin;
    private Vec3 lowerLeftCorner;
    private Vec3 horizontal;
    private Vec3 vertical;
    private Vec3 u, v, w;
    private double lensRadius;

    /**
     * @param vfov Vertical Field of View in degrees
     * @param aperture Bländarstorlek (0.0 = perfekt skärpa, 0.1-0.5 = suddig bakgrund)
     * @param focusDist Avståndet till det objekt du vill ha i fokus
     */
    public Camera(Vec3 lookFrom, Vec3 lookAt, Vec3 vup, double vfov, double aspectRatio, double aperture, double focusDist) {
        double theta = Math.toRadians(vfov);
        double h = Math.tan(theta / 2.0);
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspectRatio * viewportHeight;

        this.w = lookFrom.sub(lookAt).normalize();
        this.u = vup.cross(w).normalize();
        this.v = w.cross(u);

        this.origin = lookFrom;
        
        // Vi skalar viewporten med fokus-avståndet för att strålarna ska mötas exakt där
        this.horizontal = u.scale(viewportWidth * focusDist);
        this.vertical = v.scale(viewportHeight * focusDist);
        
        this.lowerLeftCorner = origin
                .sub(horizontal.scale(0.5))
                .sub(vertical.scale(0.5))
                .sub(w.scale(focusDist));

        this.lensRadius = aperture / 2.0;
    }

    public Ray getRay(double s, double t) {
        // För Depth of Field: Starta strålen från en slumpmässig punkt på linsen,
        // inte bara från mitten.
        Vec3 rd = randomInUnitDisk().scale(lensRadius);
        Vec3 offset = u.scale(rd.x).add(v.scale(rd.y));

        Vec3 rayOrigin = origin.add(offset);
        
        Vec3 direction = lowerLeftCorner
                .add(horizontal.scale(s))
                .add(vertical.scale(t))
                .sub(origin)
                .sub(offset); // Justera riktningen så den träffar fokusplanet
                
        return new Ray(rayOrigin, direction);
    }

    // Hjälpmetod för att hitta en punkt på en platt lins
    private Vec3 randomInUnitDisk() {
        while (true) {
            Vec3 p = new Vec3(Math.random() * 2 - 1, Math.random() * 2 - 1, 0);
            if (p.lengthSquared() < 1) return p;
        }
    }
}