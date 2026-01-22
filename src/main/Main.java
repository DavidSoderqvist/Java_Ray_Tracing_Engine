package main;

import math.Ray;
import math.Vec3;
import java.io.File;
import java.io.PrintStream;

/**
 * Main class for the Ray Tracer application.
 * 
 * This class sets up the camera, image dimensions, and handles the rendering loop.
 * It generates a simple gradient background and outputs the result to a PPM file.
 */
public class Main {

    /**
     * Computes the color seen along a given ray.
     *
     * @param ray The ray to evaluate.
     * @return A Vec3 representing the RGB color.
     */
    public static Vec3 rayColor(Ray ray) {
        Vec3 unitDirection = ray.getDirection().normalize();
        double t = 0.5 * (unitDirection.y + 1.0);
        Vec3 white = new Vec3(1.0, 1.0, 1.0);
        Vec3 blue = new Vec3(0.5, 0.7, 1.0);
        return white.scale(1.0 - t).add(blue.scale(t));
    }

    public static void main(String[] args) throws Exception {
        // Camera and image setup
        double aspectRatio = 16.0 / 9.0;
        int imageWidth = 400;
        int imageHeight = (int)(imageWidth / aspectRatio);

        // Viewport setup
        double viewportHeight = 2.0;
        double viewportWidth = aspectRatio * viewportHeight;
        double focalLength = 1.0;

        // Camera origin and viewport corners
        Vec3 origin = new Vec3(0, 0, 0);
        Vec3 horizontal = new Vec3(viewportWidth, 0, 0);
        Vec3 vertical = new Vec3(0, viewportHeight, 0);

        // Lower left corner of the viewport
        Vec3 lowerLeftCorner = origin
                .sub(horizontal.scale(0.5))
                .sub(vertical.scale(0.5))
                .sub(new Vec3(0, 0, focalLength));

        // Rendering loop/ output
        PrintStream fileOut = new PrintStream(new File("image.ppm"));
        
        fileOut.println("P3");
        fileOut.println(imageWidth + " " + imageHeight);
        fileOut.println("255");

        System.out.println("Rendering...");

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
                Vec3 pixelColor = rayColor(ray);

                int ir = (int)(255.999 * pixelColor.x);
                int ig = (int)(255.999 * pixelColor.y);
                int ib = (int)(255.999 * pixelColor.z);

                fileOut.println(ir + " " + ig + " " + ib);
            }
        }

        fileOut.close();
        System.out.println("\nDone.");
    }
}