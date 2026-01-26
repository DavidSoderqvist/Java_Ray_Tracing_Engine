package main;

import hittable.HitRecord;
import hittable.Hittable;
import hittable.HittableList;
import hittable.Sphere;
import math.Ray;
import math.Vec3;
import java.io.File;
import java.io.PrintStream;


public class Main {
    /**
     * Computes the color seen along a ray in the scene.
     *
     * @param ray   The ray to trace.
     * @param world The Hittable world containing objects to intersect with the ray.
     * @return      The color as a Vec3.
     */
    public static Vec3 rayColor(Ray ray, Hittable world) {
        HitRecord rec = new HitRecord();

        if (world.hit(ray, 0.001, Double.POSITIVE_INFINITY, rec)) {
            return new Vec3(rec.normal.x + 1, rec.normal.y + 1, rec.normal.z + 1).scale(0.5);
        }

        Vec3 unitDirection = ray.getDirection().normalize();
        double t = 0.5 * (unitDirection.y + 1.0);
        Vec3 white = new Vec3(1.0, 1.0, 1.0);
        Vec3 blue = new Vec3(0.5, 0.7, 1.0);
        return white.scale(1.0 - t).add(blue.scale(t));
    }

    public static void main(String[] args) throws Exception {
        // --- BILD-INSTÄLLNINGAR ---
        double aspectRatio = 16.0 / 9.0;
        int imageWidth = 400;
        int imageHeight = (int)(imageWidth / aspectRatio);

        // --- VÄRLDEN ---
        HittableList world = new HittableList();
        // Liten boll i mitten
        world.add(new Sphere(new Vec3(0, 0, -1), 0.5));
        // Stor boll under (Marken) - radie 100
        world.add(new Sphere(new Vec3(0, -100.5, -1), 100));

        // --- KAMERA ---
        double viewportHeight = 2.0;
        double viewportWidth = aspectRatio * viewportHeight;
        double focalLength = 1.0;

        Vec3 origin = new Vec3(0, 0, 0);
        Vec3 horizontal = new Vec3(viewportWidth, 0, 0);
        Vec3 vertical = new Vec3(0, viewportHeight, 0);
        Vec3 lowerLeftCorner = origin
                .sub(horizontal.scale(0.5))
                .sub(vertical.scale(0.5))
                .sub(new Vec3(0, 0, focalLength));

        // --- RENDERING ---
        PrintStream fileOut = new PrintStream(new File("image.ppm"));
        fileOut.println("P3");
        fileOut.println(imageWidth + " " + imageHeight);
        fileOut.println("255");

        System.out.println("Rendering World...");

        for (int j = imageHeight - 1; j >= 0; j--) {
            System.out.printf("\rScanlines remaining: %d ", j);
            for (int i = 0; i < imageWidth; i++) {
                double u = (double)i / (imageWidth - 1);
                double v = (double)j / (imageHeight - 1);

                Vec3 direction = lowerLeftCorner
                        .add(horizontal.scale(u))
                        .add(vertical.scale(v))
                        .sub(origin);
                
                Ray ray = new Ray(origin, direction);
                
                // Skicka med 'world' till rayColor
                Vec3 pixelColor = rayColor(ray, world);

                int ir = (int)(255.999 * pixelColor.x);
                int ig = (int)(255.999 * pixelColor.y);
                int ib = (int)(255.999 * pixelColor.z);

                fileOut.println(ir + " " + ig + " " + ib);
            }
        }

        fileOut.close();
        System.out.println("\nDone! Check image.ppm.");
    }

}