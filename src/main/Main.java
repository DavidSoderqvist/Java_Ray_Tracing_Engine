package main;

import hittable.HittableList;
import hittable.Sphere;
import java.io.File;
import material.Dielectric; // Glöm inte denna import!
import material.Lambertian;
import material.Material;
import material.Metal;
import math.Vec3;

public class Main {

    public static void main(String[] args) throws Exception {
        // --- KONFIGURATION ---
        int imageWidth = 1200; 
        int samplesPerPixel = 500; // Kör lite fler samples nu för metallens skull
        int maxDepth = 50;
        
        double aspectRatio = 16.0 / 9.0;
        int imageHeight = (int)(imageWidth / aspectRatio);

        // --- SKAPA VÄRLDEN ---
        HittableList world = new HittableList();

        // GOLVET (Plan)
        Material woodGround = new material.WoodCheckerMaterial();
        world.add(new hittable.Plane(new Vec3(0, 0, 0), new Vec3(0, 1, 0), woodGround));

        // SMÅ BOLLAR
        int range = 60;
        for (int a = -range; a < range; a++) {
            for (int b = -range; b < range; b++) {
                double chooseMat = Math.random();
                double radius = 0.2; // Radie
                Vec3 center = new Vec3(a + 0.9 * Math.random(), radius, b + 0.9 * Math.random());

                if (center.sub(new Vec3(4, 0.2, 0)).length() > 0.9) {
                    Material sphereMaterial;
                    if (chooseMat < 0.8) {
                        Vec3 albedo = Vec3.random().scale(Math.random());
                        sphereMaterial = new Lambertian(albedo);
                    } else if (chooseMat < 0.95) {
                        Vec3 albedo = Vec3.random(0.5, 1);
                        double fuzz = Math.random() * 0.5;
                        sphereMaterial = new Metal(albedo, fuzz);
                    } else {
                        sphereMaterial = new Dielectric(1.5);
                    }
                    world.add(new Sphere(center, radius, sphereMaterial));
                }
            }
        }

        // --- DE TRE STORA BOLLARNA ---
        
        // 1. Mitten (Glas)
        Material material1 = new Dielectric(1.5);
        world.add(new Sphere(new Vec3(0, 1, 0), 1.0, material1));

        // 2. Vänster/Bortre (NU: KOPPAR) 🥉
        // Vi ändrar denna från Lambertian till Metal med kopparfärg
        // (0.7, 0.3, 0.1) är en bra bas för koppar.
        Material material2 = new Metal(new Vec3(0.7, 0.3, 0.1), 0.1); // Lite fuzz för realism
        world.add(new Sphere(new Vec3(-4, 1, 0), 1.0, material2));

        // 3. Höger/Främre (Guld/Silver-aktig som förut)
        Material material3 = new Metal(new Vec3(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vec3(4, 1, 0), 1.0, material3));

        // BVH
        hittable.BVHNode bvhWorld = new hittable.BVHNode(world);

        // --- KAMERA (HORIZON LINE SHOT) ---
        // För att horisonten ska skära genom mitten på bollarna (som är 1.0 höga)
        // måste kameran vara på samma höjd: Y = 1.0.
        
        Vec3 lookFrom = new Vec3(13, 4, 3); // Y=1.0 är nyckeln här!
        Vec3 lookAt = new Vec3(0, 0.2, 0);    // Titta rakt fram på bollarnas höjd
        Vec3 vup = new Vec3(0, 1, 0);
        
        // Fokusera på glasbollen i mitten
        Vec3 targetBallPosition = new Vec3(4, 1, 0);
        double distToFocus = lookFrom.sub(targetBallPosition).length();
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