package main;

import hittable.HittableList;
import hittable.Sphere;
import java.io.File;
import material.Dielectric; // Don't forget this import!
import material.Lambertian;
import material.Material;
import material.Metal;
import math.Vec3;

/**
 * Main class to set up the scene and start rendering.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // --- CONFIGURATION ---
        int imageWidth = 1200; 
        int samplesPerPixel = 500; // Higher samples for better quality (especially for metal)
        int maxDepth = 50;
        
        double aspectRatio = 16.0 / 9.0;
        int imageHeight = (int)(imageWidth / aspectRatio);

        // --- CREATE THE WORLD ---
        HittableList world = new HittableList();

        // FLOOR (Plane)
        Material woodGround = new material.WoodCheckerMaterial();
        world.add(new hittable.Plane(new Vec3(0, 0, 0), new Vec3(0, 1, 0), woodGround));

        // SMALL SPHERES
        int range = 60;
        for (int a = -range; a < range; a++) {
            for (int b = -range; b < range; b++) {
                double chooseMat = Math.random();
                double radius = 0.2; // Radius
                Vec3 center = new Vec3(a + 0.9 * Math.random(), radius, b + 0.9 * Math.random());

                // Avoid placing small spheres inside the big ones
                if (center.sub(new Vec3(4, 0.2, 0)).length() > 0.9) {
                    Material sphereMaterial;
                    
                    if (chooseMat < 0.8) {
                        // Matte / Diffuse
                        Vec3 albedo = Vec3.random().scale(Math.random());
                        sphereMaterial = new Lambertian(albedo);
                    } else if (chooseMat < 0.95) {
                        // Metal
                        Vec3 albedo = Vec3.random(0.5, 1);
                        double fuzz = Math.random() * 0.5;
                        sphereMaterial = new Metal(albedo, fuzz);
                    } else {
                        // Glass
                        sphereMaterial = new Dielectric(1.5);
                    }
                    world.add(new Sphere(center, radius, sphereMaterial));
                }
            }
        }

        // --- THE THREE BIG SPHERES ---
        
        // 1. Center (Glass)
        Material material1 = new Dielectric(1.5);
        world.add(new Sphere(new Vec3(0, 1, 0), 1.0, material1));

        // 2. Left/Back (NOW: COPPER) 🥉
        // Changed from Lambertian to Metal with a copper color
        // (0.7, 0.3, 0.1) is a good base for copper.
        Material material2 = new Metal(new Vec3(0.7, 0.3, 0.1), 0.1); // A little fuzz for realism
        world.add(new Sphere(new Vec3(-4, 1, 0), 1.0, material2));

        // 3. Right/Front (Gold/Silver-like as before)
        Material material3 = new Metal(new Vec3(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vec3(4, 1, 0), 1.0, material3));

        // BVH (Optimization)
        hittable.BVHNode bvhWorld = new hittable.BVHNode(world);

        // --- CAMERA ---
        // Position: Elevated 3/4 view
        Vec3 lookFrom = new Vec3(13, 4, 3); 
        
        // Target: Looking slightly above the ground (horizon adjustment)
        Vec3 lookAt = new Vec3(0, 0.2, 0);    
        Vec3 vup = new Vec3(0, 1, 0);
        
        // Focus: Set focus distance to the front metal sphere (at 4, 1, 0)
        Vec3 targetBallPosition = new Vec3(4, 1, 0);
        double distToFocus = lookFrom.sub(targetBallPosition).length();
        
        // Aperture (Depth of Field). 0.1 gives a nice blur to the background.
        double aperture = 0.1; 

        Camera cam = new Camera(lookFrom, lookAt, vup, 20, aspectRatio, aperture, distToFocus);

        // --- RENDERING ---
        String folderName = "images";
        File directory = new File(folderName);
        if (!directory.exists()) directory.mkdirs();

        int counter = 1;
        File outputFile;
        while (true) {
            outputFile = new File(directory, "render" + counter + ".png");
            if (!outputFile.exists()) break;
            counter++;
        }

        System.out.println("Starting render! Output target: " + outputFile.getName());
        Renderer renderer = new Renderer(imageWidth, imageHeight, samplesPerPixel, maxDepth);
        renderer.render(bvhWorld, cam, outputFile);
    }
}