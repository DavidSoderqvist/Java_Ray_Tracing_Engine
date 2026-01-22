package main;

import java.io.File;
import java.io.PrintStream;

/**
 * Main class for rendering a simple gradient image and saving it as a PPM file.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        int imageWidth = 256;
        int imageHeight = 256;

        PrintStream fileOut = new PrintStream(new File("image.ppm"));
        fileOut.println("P3");
        fileOut.println(imageWidth + " " + imageHeight);
        fileOut.println("255");

        System.out.println("Start rendering...");

        for (int y = 0; y < imageHeight; y++) {
            
            System.out.println("Scanlines remaining: " + (imageHeight - y));
            
            for (int x = 0; x < imageWidth; x++) {
            double r = (double)x / (imageWidth - 1);
            double g = (double)y / (imageHeight - 1);
            double b = 0.25;

            int ir = (int)(255.999 * r);
            int ig = (int)(255.999 * g);
            int ib = (int)(255.999 * b);

            fileOut.println(ir + " " + ig + " " + ib);
            }
        }
        
        fileOut.close();
        System.out.println("Rendering complete! Image saved to image.ppm");
    }
    
}
