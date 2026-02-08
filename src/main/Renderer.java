package main;

import hittable.HitRecord;
import hittable.Hittable;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO; // Important: Make sure Wrapper.java exists in the material package
import material.Wrapper;
import math.Ray;
import math.Vec3;

/**
 * Renderer class responsible for rendering the scene and saving the output image.
 */
public class Renderer {

    private int imageWidth;
    private int imageHeight;
    private int samplesPerPixel;
    private int maxDepth;

    public Renderer(int w, int h, int s, int d) {
        this.imageWidth = w;
        this.imageHeight = h;
        this.samplesPerPixel = s;
        this.maxDepth = d;
    }

    /**
     * Renders the scene and saves it to the specified output file.
     * @param world
     * @param cam
     * @param outputFile
     */
    public void render(Hittable world, Camera cam, File outputFile) {
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        for (int j = imageHeight - 1; j >= 0; j--) {
             System.out.println("Scanlines remaining: " + j);
             for (int i = 0; i < imageWidth; i++) {
                 Vec3 pixelColor = new Vec3(0, 0, 0);
                 for (int s = 0; s < samplesPerPixel; s++) {
                     double u = (i + Math.random()) / (imageWidth - 1);
                     double v = (j + Math.random()) / (imageHeight - 1);
                     Ray r = cam.getRay(u, v);
                     pixelColor = pixelColor.add(rayColor(r, world, maxDepth));
                 }
                 writeColor(image, i, imageHeight - j - 1, pixelColor, samplesPerPixel);
             }
        }
        try {
            ImageIO.write(image, "png", outputFile);
            System.out.println("Done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- SKY AND LIGHTING CALCULATION (GOLDEN HOUR EDITION) ---
    /**
     * Calculates the color seen along a ray, including the new golden hour sky and sun effects.
     * @param r
     * @param world
     * @param depth
     * @return
     */
    private Vec3 rayColor(Ray r, Hittable world, int depth) {
        // Limit recursion
        if (depth <= 0) {
            return new Vec3(0, 0, 0);
        }

        HitRecord rec = new HitRecord();
        
        if (world.hit(r, 0.001, Double.POSITIVE_INFINITY, rec)) {
            Ray scattered = new Ray(new Vec3(0,0,0), new Vec3(0,0,0));
            Vec3 attenuation = new Vec3(0,0,0);
            
            Wrapper wrapper = new Wrapper(scattered, attenuation);
            
            if (rec.material.scatter(r, rec, wrapper)) {
                return wrapper.attenuation.multiply(rayColor(wrapper.scatteredRay, world, depth - 1));
            }
            return new Vec3(0, 0, 0);
        }

        // --- NEW ATMOSPHERE SETTINGS ---
        
        Vec3 unitDirection = r.getDirection().normalize();
        
        // 1. SUN POSITION
        // We lower the Y-value from 0.3 to 0.15. 
        // This puts the sun very close to the horizon (Long shadows, golden light).
        Vec3 sunDir = new Vec3(-1.0, 0.15, -1.0).normalize();
        
        // 2. SUN SIZE AND HAZINESS
        double sunFocus = unitDirection.dot(sunDir);
        
        // Changed from 400 to 100.
        // Lower number = Bigger sun disk on the sky = More "glow" around the sun.
        double sunIntensity = Math.pow(Math.max(0, sunFocus), 100); 
        
        // 3. SUN BRIGHTNESS (THE NUCLEAR OPTION) ☀️
        // Increased from (8, 6, 4) to (50, 35, 20).
        // This is physically impossibly bright, but it creates the "glare" effect
        // on the shiny spheres.
        Vec3 sunColor = new Vec3(100.0, 70.0, 40.0); 

        // 4. BRIGHTER SKY GRADIENT
        double t = 0.5 * (unitDirection.y + 1.0);
        
        // Brighter horizon (White-Gold instead of just Orange)
        Vec3 horizonColor = new Vec3(4, 3, 2); 
        
        // Brighter zenith (Blue but glowing)
        Vec3 zenithColor = new Vec3(0.5, 0.7, 1.0); 

        Vec3 skyColor = horizonColor.scale(1.0 - t).add(zenithColor.scale(t));

        return skyColor.add(sunColor.scale(sunIntensity));
    }

    // --- HELPER METHODS FOR WRITING COLORS (These were missing!) ---

    /**
     * Writes the color to the image, applying gamma correction and scaling by the number of samples.
     * @param image
     * @param x
     * @param y
     * @param pixelColor
     * @param samples
     */
    private void writeColor(BufferedImage image, int x, int y, Vec3 pixelColor, int samples) {
        double r = pixelColor.x;
        double g = pixelColor.y;
        double b = pixelColor.z;

        // Divide the color by the number of samples and gamma-correct for gamma=2.0.
        double scale = 1.0 / samples;
        r = Math.sqrt(scale * r);
        g = Math.sqrt(scale * g);
        b = Math.sqrt(scale * b);

        // Write the translated [0,255] value of each color component.
        int ir = (int)(256 * clamp(r, 0.0, 0.999));
        int ig = (int)(256 * clamp(g, 0.0, 0.999));
        int ib = (int)(256 * clamp(b, 0.0, 0.999));

        int color = (ir << 16) | (ig << 8) | ib;
        image.setRGB(x, y, color);
    }

    /**
     * Clamps a value between a minimum and maximum.
     * @param x
     * @param min
     * @param max
     * @return
     */
    private double clamp(double x, double min, double max) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }
}