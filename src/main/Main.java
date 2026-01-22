package main;

import math.Ray;
import math.Vec3;
import java.io.File;
import java.io.PrintStream;

public class Main {

    public static Vec3 rayColor(Ray ray) {
        // RÄTTELSE 1: Metoden heter .direction() (inte getDirection)
        Vec3 unitDirection = ray.getDirection().normalize();
        
        double t = 0.5 * (unitDirection.y + 1.0);
        
        Vec3 white = new Vec3(1.0, 1.0, 1.0);
        Vec3 blue = new Vec3(0.5, 0.7, 1.0);
        
        return white.scale(1.0 - t).add(blue.scale(t));
    }

    public static void main(String[] args) throws Exception {
        double aspectRatio = 16.0 / 9.0;
        int imageWidth = 400;
        int imageHeight = (int)(imageWidth / aspectRatio);

        double viewportHeight = 2.0;
        double viewportWidth = aspectRatio * viewportHeight;
        double focalLength = 1.0;

        Vec3 origin = new Vec3(0, 0, 0);
        Vec3 horizontal = new Vec3(viewportWidth, 0, 0);
        Vec3 vertical = new Vec3(0, viewportHeight, 0);

        // RÄTTELSE 2: Metoden heter .sub() (inte subtract) i din Vec3-klass
        Vec3 lowerLeftCorner = origin
                .sub(horizontal.scale(0.5))
                .sub(vertical.scale(0.5))
                .sub(new Vec3(0, 0, focalLength));

        // RÄTTELSE 3: Vi döper variabeln till fileOut direkt så det matchar resten
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