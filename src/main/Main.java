package main;

import hittable.HitRecord;
import hittable.Hittable;
import hittable.HittableList;
import hittable.Sphere;
import math.Ray;
import math.Vec3;
import java.io.File;
import java.io.PrintStream;
import java.util.Random;

/**
 * Main class for rendering a simple ray-traced scene.
 */
public class Main {
    

    /**
     * Computes the color seen along a ray in the scene.
     *
     * @param ray The ray to trace.
     * @param world The world containing hittable objects.
     * @param depth The remaining recursion depth for reflections.
     * @return The color as a Vec3.
     */
    public static Vec3 rayColor(Ray ray, Hittable world, int depth) {
        HitRecord rec = new HitRecord();

        // 1. SÄKERHETSSPÄRR: Om vi studsat för många gånger, sluta räkna ljus.
        // Returnera svart (inget ljus).
        if (depth <= 0) {
            return new Vec3(0, 0, 0);
        }

        if (world.hit(ray, 0.001, Double.POSITIVE_INFINITY, rec)) {
            // Beräkna målet för den studsande strålen
            Vec3 target = rec.p.add(rec.normal).add(Vec3.randomInUnitSphere());
            
            // Skapa den studsande strålen
            Ray bouncedRay = new Ray(rec.p, target.sub(rec.p));
            
            // Rekursivt räkna färgen för den studsande strålen och dämpa den
            return rayColor(bouncedRay, world, depth - 1).scale(0.5);
        }

        // Himlen (bakgrundsljuset)
        Vec3 unitDirection = ray.getDirection().normalize();
        double t = 0.5 * (unitDirection.y + 1.0);
        Vec3 white = new Vec3(1.0, 1.0, 1.0);
        Vec3 blue = new Vec3(0.5, 0.7, 1.0);
        return white.scale(1.0 - t).add(blue.scale(t));
    }

    /**
     * Main method to set up the scene and render the image.
     *
     * @param args Command line arguments.
     * @throws Exception If an error occurs during file operations.
     */
    public static void main(String[] args) throws Exception {
        // --- INSTÄLLNINGAR ---
        double aspectRatio = 16.0 / 9.0;
        int imageWidth = 400;
        int imageHeight = (int)(imageWidth / aspectRatio);
        int samplesPerPixel = 50; // Antialiasing
        int maxDepth = 50; // Max antal studsar

        // --- OBJEKT ---
        HittableList world = new HittableList();
        world.add(new Sphere(new Vec3(0, 0, -1), 0.5));
        world.add(new Sphere(new Vec3(0, -100.5, -1), 100));

        // --- KAMERA ---
        // Se så rent det blev! Ingen matte här längre.
        Camera cam = new Camera();

        // --- RENDERING ---
        PrintStream fileOut = new PrintStream(new File("image.ppm"));
        fileOut.println("P3");
        fileOut.println(imageWidth + " " + imageHeight);
        fileOut.println("255");

        System.out.println("Rendering cleaned up project...");
        Random random = new Random();

        for (int j = imageHeight - 1; j >= 0; j--) {
            System.out.printf("\rScanlines remaining: %d ", j);
            for (int i = 0; i < imageWidth; i++) {
                
                Vec3 pixelColor = new Vec3(0, 0, 0);

                for (int s = 0; s < samplesPerPixel; s++) {
                    double u = (i + random.nextDouble()) / (imageWidth - 1);
                    double v = (j + random.nextDouble()) / (imageHeight - 1);

                    // Kameran sköter strålarna nu!
                    Ray ray = cam.getRay(u, v);
                    
                    pixelColor = pixelColor.add(rayColor(ray, world, maxDepth));
                }

                // ColorUtil sköter skrivandet
                ColorUtil.writeColor(fileOut, pixelColor, samplesPerPixel);
            }
        }

        fileOut.close();
        System.out.println("\nDone.");
    }
}