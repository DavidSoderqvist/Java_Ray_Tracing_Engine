package main;

import hittable.HitRecord;
import hittable.Hittable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import material.Material;
import math.Ray;
import math.Vec3;

/**
 * Renderer class responsible for rendering the scene.
 * Now saves directly to PNG format using Java's ImageIO.
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
     * Renders the scene and saves it as a PNG file.
     */
    public void render(Hittable world, Camera cam, File outputFile) {
        // 1. Skapa en tom bild i RAM-minnet
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        System.out.println("Starting render...");
        Random random = new Random();
        long startTime = System.currentTimeMillis();

        // Loopa igenom pixlarna
        for (int j = imageHeight - 1; j >= 0; j--) {
            // Visa framsteg i procent
            if (j % 10 == 0) {
                int progress = (int) ((1.0 - (double) j / imageHeight) * 100);
                System.out.print("\rProgress: " + progress + "%");
            }

            for (int i = 0; i < imageWidth; i++) {
                Vec3 pixelColor = new Vec3(0, 0, 0);

                // Multisampling (Antialiasing)
                for (int s = 0; s < samplesPerPixel; s++) {
                    double u = (i + random.nextDouble()) / (imageWidth - 1);
                    double v = (j + random.nextDouble()) / (imageHeight - 1);

                    Ray ray = cam.getRay(u, v);
                    pixelColor = pixelColor.add(rayColor(ray, world, maxDepth));
                }

                // --- FÄRG-KONVERTERING (Direkt till PNG-format) ---
                
                // A. Skala ner färgen baserat på antalet samples
                double r = pixelColor.x / samplesPerPixel;
                double g = pixelColor.y / samplesPerPixel;
                double b = pixelColor.z / samplesPerPixel;

                // B. Gamma-korrigering (Gamma 2.0 = roten ur)
                // Detta gör bilden ljusare och mer korrekt för ögat.
                r = Math.sqrt(r);
                g = Math.sqrt(g);
                b = Math.sqrt(b);

                // C. Konvertera 0.0-1.0 till 0-255
                int ir = (int) (256 * clamp(r, 0.0, 0.999));
                int ig = (int) (256 * clamp(g, 0.0, 0.999));
                int ib = (int) (256 * clamp(b, 0.0, 0.999));

                // D. Packa ihop RGB till ett enda heltal (Bit-shifting)
                // Java hanterar färger som 0xRRGGBB
                int rgb = (ir << 16) | (ig << 8) | ib;

                // E. Spara pixel i bilden
                // OBS: BufferedImage har (0,0) uppe till vänster, men vår loop
                // börjar med j=högst (nedre vänstra hörnet i 3D-världen).
                // Vi måste vända på Y-axeln: (imageHeight - 1 - j)
                image.setRGB(i, imageHeight - 1 - j, rgb);
            }
        }

        System.out.println("\nRendering finished via ImageIO.");

        // Spara bilden som PNG
        try {
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image saved to: " + outputFile.getAbsolutePath());
            
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken: " + (endTime - startTime) / 1000.0 + "s");
            
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }

    // Hjälpmetod för att hålla värden mellan min och max
    private double clamp(double x, double min, double max) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }

    // --- RAY COLOR LOGIK (Samma som innan) ---
    private Vec3 rayColor(Ray ray, Hittable world, int depth) {
        if (depth <= 0) return new Vec3(0, 0, 0);

        HitRecord rec = new HitRecord();
        
        if (world.hit(ray, 0.001, Double.POSITIVE_INFINITY, rec)) {
            Material.Wrapper wrapper = new Material.Wrapper();
            if (rec.material.scatter(ray, rec, wrapper)) {
                
                // Rekursion
                Vec3 colorFromNext = rayColor(wrapper.scatteredRay, world, depth - 1);

                return new Vec3(
                    wrapper.attenuation.x * colorFromNext.x,
                    wrapper.attenuation.y * colorFromNext.y,
                    wrapper.attenuation.z * colorFromNext.z
                );
            }
            return new Vec3(0, 0, 0);
        }

        // Bakgrund
        Vec3 unitDirection = ray.getDirection().normalize();
        double t = 0.5 * (unitDirection.y + 1.0);
        Vec3 white = new Vec3(1.0, 1.0, 1.0);
        Vec3 blue = new Vec3(0.5, 0.7, 1.0);
        return white.scale(1.0 - t).add(blue.scale(t));
    }
}