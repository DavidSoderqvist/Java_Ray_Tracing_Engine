package main;

import hittable.HitRecord;
import hittable.Hittable;
import material.Material;
import math.Ray;
import math.Vec3;

import java.io.PrintStream;
import java.util.Random;

/**
 * Renderer class responsible for rendering the scene.
 * Handles pixel iteration, color calculation, and output.
 */
public class Renderer {
    private final int imageWidth;
    private final int imageHeight;
    private final int samplesPerPixel;
    private final int maxDepth;

    public Renderer(int imageWidth, int imageHeight, int samplesPerPixel, int maxDepth) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.samplesPerPixel = samplesPerPixel;
        this.maxDepth = maxDepth;
    }

    /**
     * Renders the scene and outputs the result in PPM format.
     */
    public void render(Hittable world, Camera cam, PrintStream out) {
        // PPM Header
        out.println("P3");
        out.println(imageWidth + " " + imageHeight);
        out.println("255");

        System.out.println("Starting render...");
        Random random = new Random();

        for (int j = imageHeight - 1; j >= 0; j--) {
            System.out.printf("\rScanlines remaining: %d ", j);
            for (int i = 0; i < imageWidth; i++) {
                
                Vec3 pixelColor = new Vec3(0, 0, 0);

                // Multisampling for Antialiasing
                for (int s = 0; s < samplesPerPixel; s++) {
                    double u = (i + random.nextDouble()) / (imageWidth - 1);
                    double v = (j + random.nextDouble()) / (imageHeight - 1);

                    Ray ray = cam.getRay(u, v);
                    pixelColor = pixelColor.add(rayColor(ray, world, maxDepth));
                }

                // Delegate color processing (gamma correction/clamping) to utility class
                ColorUtil.writeColor(out, pixelColor, samplesPerPixel);
            }
        }
        System.out.println("\nDone!");
    }

    /**
     * Computes the color seen along a ray by recursively tracing it through the scene.
     * 
     * @param ray The ray to trace.
     * @param world The scene containing hittable objects.
     * @param depth The remaining recursion depth.
     * @return The color as a Vec3.
     */
    private Vec3 rayColor(Ray ray, Hittable world, int depth) {
        HitRecord rec = new HitRecord();

        // 1. Recursion limit / Safety guard
        // If we've bounced too many times, no more light is gathered.
        if (depth <= 0) {
            return new Vec3(0, 0, 0);
        }

        // 2. Intersection test
        if (world.hit(ray, 0.001, Double.POSITIVE_INFINITY, rec)) {
            Material.Wrapper wrapper = new Material.Wrapper();
            
            // 3. Material scattering
            // Ask the material how it affects the ray.
            if (rec.material.scatter(ray, rec, wrapper)) {
                Vec3 attenuation = wrapper.attenuation;
                Vec3 colorFromNextBounce = rayColor(wrapper.scatteredRay, world, depth - 1);
                
                // Combine the material color with the light from the next bounce
                return new Vec3(
                    attenuation.x * colorFromNextBounce.x,
                    attenuation.y * colorFromNextBounce.y,
                    attenuation.z * colorFromNextBounce.z
                );
            }
            // If the ray was absorbed (e.g., by a black body), return black.
            return new Vec3(0, 0, 0);
        }

        // 4. Background (Sky)
        // Linear interpolation (Lerp) between white and blue based on Y direction.
        Vec3 unitDirection = ray.getDirection().normalize();
        double t = 0.5 * (unitDirection.y + 1.0);
        Vec3 white = new Vec3(1.0, 1.0, 1.0);
        Vec3 blue = new Vec3(0.5, 0.7, 1.0);
        return white.scale(1.0 - t).add(blue.scale(t));
    }
}