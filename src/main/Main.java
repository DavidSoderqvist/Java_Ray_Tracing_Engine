package main;

import hittable.HittableList;
import hittable.Sphere;
import material.Lambertian;
import material.Material;
import math.Vec3;

import java.io.File;
import java.io.PrintStream;

/**
 * Main class to set up the scene and initiate rendering.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // 1. Configuration
        double aspectRatio = 16.0 / 9.0;
        int imageWidth = 400;
        int imageHeight = (int)(imageWidth / aspectRatio);
        int samplesPerPixel = 50; 
        int maxDepth = 50;        

        // 2. Setup World (Scene Graph)
        HittableList world = new HittableList();

        // Define Materials
        Material materialGround = new Lambertian(new Vec3(0.8, 0.8, 0.0));
        Material materialLeft   = new Lambertian(new Vec3(0.5, 0.5, 0.5));
        Material materialCenter = new material.Metal(new Vec3(0.8, 0.8, 0.8));
        Material materialRight  = new material.Metal(new Vec3(0.8, 0.6, 0.2));

        // Add Objects to World
        // Ground (Large sphere underneath)
        world.add(new Sphere(new Vec3(0, -100.5, -1), 100, materialGround));
        
        // The three floating spheres
        world.add(new Sphere(new Vec3(-1.5, 0.5, -1), 0.5, materialLeft));   // Matte Gray
        world.add(new Sphere(new Vec3(0.0, 0.5, -1), 0.5, materialCenter));  // Silver
        world.add(new Sphere(new Vec3(1.5, 0.5, -1), 0.5, materialRight));   // Gold

        // 3. Setup Camera
        // Positioned slightly to the right and above, looking down at the center.
        Vec3 lookFrom = new Vec3(3, 2, 5); 
        Vec3 lookAt = new Vec3(0, 0.5, -1);
        Vec3 vup = new Vec3(0, 1, 0);
        
        // FOV 30 degrees for a narrower, more "portrait-like" view
        Camera cam = new Camera(lookFrom, lookAt, vup, 30, aspectRatio);

        // 4. Execution
        // Initialize the renderer and start the process.
        PrintStream fileOut = new PrintStream(new File("image.ppm"));
        
        Renderer renderer = new Renderer(imageWidth, imageHeight, samplesPerPixel, maxDepth);
        renderer.render(world, cam, fileOut);

        fileOut.close();
    }
}