package main;

import math.Vec3;
import java.io.PrintStream;

/**
 * Utility class for color operations.
 */
public class ColorUtil {

    /**
     * Writes the color to the output stream after scaling and clamping.
     *
     * @param out             The output PrintStream.
     * @param pixelColor      The color of the pixel as a Vec3.
     * @param samplesPerPixel The number of samples per pixel for averaging.
     */
    public static void writeColor(PrintStream out, Vec3 pixelColor, int samplesPerPixel) {
        double r = pixelColor.x;
        double g = pixelColor.y;
        double b = pixelColor.z;

        double scale = 1.0 / samplesPerPixel;
        r *= scale;
        g *= scale;
        b *= scale;

        int ir = (int)(256 * clamp(r, 0.0, 0.999));
        int ig = (int)(256 * clamp(g, 0.0, 0.999));
        int ib = (int)(256 * clamp(b, 0.0, 0.999));

        out.println(ir + " " + ig + " " + ib);
    }

    /**
     * Clamps a value between a minimum and maximum.
     *
     * @param x   The value to clamp.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return    The clamped value.
     */
    public static double clamp(double x, double min, double max) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }
}