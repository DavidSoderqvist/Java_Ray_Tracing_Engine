package main;

import hittable.HitRecord;
import hittable.Hittable;
import hittable.HittableList;
import hittable.Sphere;
import material.Lambertian;
import material.Material;
import math.Ray;
import math.Vec3;

import java.io.File;
import java.io.PrintStream;
import java.util.Random;

public class Main {

    /**
     * Beräknar färgen för en stråle.
     * Nu använder den Material-systemet för att avgöra studs och färg!
     */
    public static Vec3 rayColor(Ray ray, Hittable world, int depth) {
        HitRecord rec = new HitRecord();

        // 1. SÄKERHETSSPÄRR: Om strålen studsat för många gånger (depth), sluta.
        if (depth <= 0) {
            return new Vec3(0, 0, 0); // Svart (inget ljus kommer tillbaka)
        }

        // 2. TRÄFFADE VI NÅGOT?
        if (world.hit(ray, 0.001, Double.POSITIVE_INFINITY, rec)) {
            
            // Vi skapar en "låda" (wrapper) för att ta emot svaret från materialet
            Material.Wrapper wrapper = new Material.Wrapper();

            // 3. FRÅGA MATERIALET: "Hur studsar ljuset på dig?"
            // Vi kollar rec.mat (materialet på objektet vi träffade)
            if (rec.material.scatter(ray, rec, wrapper)) {
                // Materialet gav oss en dämpningsfärg (attenuation) och en ny stråle (scatteredRay)
                Vec3 attenuation = wrapper.attenuation;
                
                // Skjut iväg den nya strålen rekursivt (minska depth med 1)
                Vec3 colorFromNextBounce = rayColor(wrapper.scatteredRay, world, depth - 1);

                // Multiplicera materialets färg med ljuset som kom tillbaka
                // (Vi gör detta manuellt x*x, y*y, z*z eftersom Vec3 saknar en metod för det)
                return new Vec3(
                    attenuation.x * colorFromNextBounce.x,
                    attenuation.y * colorFromNextBounce.y,
                    attenuation.z * colorFromNextBounce.z
                );
            }
            
            // Om materialet absorberade allt ljus (t.ex. svart hål), returnera svart
            return new Vec3(0, 0, 0);
        }

        // 4. HIMLEN (BAKGRUND)
        Vec3 unitDirection = ray.getDirection().normalize();
        double t = 0.5 * (unitDirection.y + 1.0);
        Vec3 white = new Vec3(1.0, 1.0, 1.0);
        Vec3 blue = new Vec3(0.5, 0.7, 1.0);
        return white.scale(1.0 - t).add(blue.scale(t));
    }

    public static void main(String[] args) throws Exception {
        // --- INSTÄLLNINGAR ---
        double aspectRatio = 16.0 / 9.0;
        int imageWidth = 400;
        int imageHeight = (int)(imageWidth / aspectRatio);
        int samplesPerPixel = 50; // Antialiasing (fler strålar = mjukare bild)
        int maxDepth = 50;        // Max antal studsar (rekursionsdjup)

        // --- VÄRLDEN & MATERIAL ---
        HittableList world = new HittableList();

        // Skapa två olika material
        // Marken: En gul-grön matt färg
        Material materialGround = new Lambertian(new Vec3(0.8, 0.8, 0.0));
        // Mittenbollen: En röd-brun matt färg (eller lila om du vill ändra siffrorna!)
        Material materialCenter = new Lambertian(new Vec3(0.7, 0.3, 0.3));

        // Lägg till bollar och KOPPLA DEM till materialen
        // Marken (Jättestor boll under oss)
        world.add(new Sphere(new Vec3(0, -100.5, -1), 100, materialGround));
        // Bollen i mitten
        world.add(new Sphere(new Vec3(0, 0, -1), 0.5, materialCenter));

        // --- KAMERA ---
        Camera cam = new Camera();

        // --- RENDERING ---
        PrintStream fileOut = new PrintStream(new File("image.ppm"));
        fileOut.println("P3");
        fileOut.println(imageWidth + " " + imageHeight);
        fileOut.println("255");

        System.out.println("Rendering with Materials...");
        Random random = new Random();

        for (int j = imageHeight - 1; j >= 0; j--) {
            System.out.printf("\rScanlines remaining: %d ", j);
            for (int i = 0; i < imageWidth; i++) {
                
                Vec3 pixelColor = new Vec3(0, 0, 0);

                // Multisampling-loop (Antialiasing)
                for (int s = 0; s < samplesPerPixel; s++) {
                    double u = (i + random.nextDouble()) / (imageWidth - 1);
                    double v = (j + random.nextDouble()) / (imageHeight - 1);

                    Ray ray = cam.getRay(u, v);
                    
                    // Anropa rayColor med maxDepth
                    pixelColor = pixelColor.add(rayColor(ray, world, maxDepth));
                }

                // Skriv färgen till filen via vår hjälpklass
                ColorUtil.writeColor(fileOut, pixelColor, samplesPerPixel);
            }
        }

        fileOut.close();
        System.out.println("\nDone! Open image.ppm to see your colored world.");
    }
}