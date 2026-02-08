package main;

import hittable.HittableList;
import hittable.Sphere;
import java.io.File;
import material.Dielectric;
import material.Lambertian;
import material.Material;
import material.Metal;
import math.Vec3;

public class Main {

    public static void main(String[] args) throws Exception {
        // --- KONFIGURATION (THE COVER SHOT) ---
        // För en snabb test-bild: Sätt width till 400 och samples till 10.
        // För CV-bilden: Kör 1200 width och 100-500 samples (Tar tid!).
        
        int imageWidth = 1200;  // Testa med 400 om det går trögt
        int samplesPerPixel = 500; // Testa med 20 om det går trögt
        int maxDepth = 50;
        
        double aspectRatio = 16.0 / 9.0;
        int imageHeight = (int)(imageWidth / aspectRatio);

        // --- SKAPA EN SLUMPGENERERAD VÄRLD ---
        HittableList world = new HittableList();

        // 1. Marken
        Material groundMaterial = new Lambertian(new Vec3(0.5, 0.5, 0.5));
        world.add(new Sphere(new Vec3(0, -1000, 0), 1000, groundMaterial));

        // 2. Små slumpmässiga bollar
        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double chooseMat = Math.random();
                Vec3 center = new Vec3(a + 0.9 * Math.random(), 0.2, b + 0.9 * Math.random());

                // Kolla så att bollen inte hamnar inuti de stora bollarna
                if (center.sub(new Vec3(4, 0.2, 0)).length() > 0.9) {
                    Material sphereMaterial;

                    if (chooseMat < 0.8) {
                        // Matt (Diffus)
                        Vec3 albedo = Vec3.random().scale(Math.random()); // random * random för snyggare färger
                        sphereMaterial = new Lambertian(albedo);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else if (chooseMat < 0.95) {
                        // Metall
                        Vec3 albedo = Vec3.random(0.5, 1);
                        sphereMaterial = new Metal(albedo);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else {
                        // Glas
                        sphereMaterial = new Dielectric(1.5);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    }
                }
            }
        }

        // 3. De tre stora "Hjälte-bollarna"
        Material material1 = new Dielectric(1.5);
        world.add(new Sphere(new Vec3(0, 1, 0), 1.0, material1));

        Material material2 = new Lambertian(new Vec3(0.4, 0.2, 0.1));
        world.add(new Sphere(new Vec3(-4, 1, 0), 1.0, material2));

        Material material3 = new Metal(new Vec3(0.7, 0.6, 0.5));
        world.add(new Sphere(new Vec3(4, 1, 0), 1.0, material3));

        // Slå in hela listan i en BVH-struktur för att snabba upp sökningen!
        hittable.BVHNode bvhWorld = new hittable.BVHNode(world);

        // --- KAMERA ---
        // Vi backar rejält och zoomar in lite för en snygg "teleobjektiv"-effekt
        Vec3 lookFrom = new Vec3(13, 2, 3);
        Vec3 lookAt = new Vec3(0, 0, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        
        // 20 graders FOV och fokus på origo
        Camera cam = new Camera(lookFrom, lookAt, vup, 20, aspectRatio);

        // --- RENDERING ---
        
        // Automatisk filhantering
        String folderName = "images";
        File directory = new File(folderName);
        if (!directory.exists()) directory.mkdirs();

        int counter = 1;
        File outputFile;
        while (true) {
            outputFile = new File(directory, "final_scene_" + counter + ".png");
            if (!outputFile.exists()) break;
            counter++;
        }

        System.out.println("Starting render! Output target: " + outputFile.getName());
        
        Renderer renderer = new Renderer(imageWidth, imageHeight, samplesPerPixel, maxDepth);
        
        // Skicka in File-objektet istället för PrintStream
        renderer.render(bvhWorld, cam, outputFile);
        
        // Ta bort fileOut.close() om du hade det kvar, det behövs inte längre.
    }
}