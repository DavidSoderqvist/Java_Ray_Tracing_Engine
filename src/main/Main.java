package main;

import hittable.HittableList;
import hittable.Sphere;
import material.Lambertian;
import material.Material;
import math.Vec3;

import java.io.File;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) throws Exception {
        // 1. KONFIGURATION
        double aspectRatio = 16.0 / 9.0;
        
        // HD-inställningar
        int imageWidth = 800; 
        int imageHeight = (int)(imageWidth / aspectRatio);
        int samplesPerPixel = 100; 
        int maxDepth = 50;        

        // 2. SKAPA VÄRLDEN
        HittableList world = new HittableList();

        Material materialGround = new Lambertian(new Vec3(0.8, 0.8, 0.0));
        
        // Vänster: GLAS (Dielectric)
        Material materialLeft   = new material.Dielectric(1.5);
        
        // Mitten: SILVER (Metal)
        Material materialCenter = new material.Metal(new Vec3(0.8, 0.8, 0.8));
        
        // Höger: GULD (Metal)
        Material materialRight  = new material.Metal(new Vec3(0.8, 0.6, 0.2));

        world.add(new Sphere(new Vec3(0, -100.5, -1), 100, materialGround));
        world.add(new Sphere(new Vec3(-1.5, 0.5, -1), 0.5, materialLeft));
        world.add(new Sphere(new Vec3(0.0, 0.5, -1), 0.5, materialCenter));
        world.add(new Sphere(new Vec3(1.5, 0.5, -1), 0.5, materialRight));

        // 3. KAMERA
        Vec3 lookFrom = new Vec3(3, 2, 5); 
        Vec3 lookAt = new Vec3(0, 0.5, -1);
        Vec3 vup = new Vec3(0, 1, 0);
        Camera cam = new Camera(lookFrom, lookAt, vup, 30, aspectRatio);

        // 4. AUTOMATISK FILHANTERING (NYTT HÄR!)
        
        // Skapa mappen "images" om den inte finns
        String folderName = "images";
        File directory = new File(folderName);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Hitta nästa lediga nummer: image1, image2, image3...
        int counter = 1;
        File outputFile;
        while (true) {
            outputFile = new File(directory, "image" + counter + ".ppm");
            if (!outputFile.exists()) {
                break; // Vi hittade ett ledigt namn!
            }
            counter++;
        }

        System.out.println("Saving image to: " + outputFile.getPath());
        
        // Skapa strömmen till den specifika filen
        PrintStream fileOut = new PrintStream(outputFile);
        
        // 5. RENDERING
        Renderer renderer = new Renderer(imageWidth, imageHeight, samplesPerPixel, maxDepth);
        renderer.render(world, cam, fileOut);

        fileOut.close();
    }
}