package math;

/**
 * A simple 3D vector class for representing points, colors, and directions.
 */
public class Vec3 {
    public final double x, y, z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Basic vector operations: addition, subtraction, scaling, dot product, cross product, length, normalization.      
     * @param v
     * @return
     */
    public Vec3 add(Vec3 v) {
        return new Vec3(x + v.x, y + v.y, z + v.z);
    }

    public Vec3 sub(Vec3 v) {
        return new Vec3(x - v.x, y - v.y, z - v.z);
    }

    public Vec3 scale(double t) {
        return new Vec3(x * t, y * t, z * t);
    }

    public double dot(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vec3 cross(Vec3 v) {
        return new Vec3(
            y * v.z - z * v.y,
            z * v.x - x * v.z,
            x * v.y - y * v.x
        );
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vec3 normalize() {
        double len = length();
        if (len == 0) return new Vec3(0, 0, 0);
        return scale(1.0 / len);
    }

    /**
     * Reflection and refraction methods for handling light interactions with materials.
     * @param v
     * @param n
     * @return
     */
    public static Vec3 reflect(Vec3 v, Vec3 n) {
        return v.sub(n.scale(2 * v.dot(n)));
    }

    /**
     * Refraction using Snell's law. Returns the refracted ray direction given an incident ray, surface normal, and the ratio of indices of refraction.
     * @param uv
     * @param n
     * @param etaiOverEtat
     * @return
     */
    public static Vec3 refract(Vec3 uv, Vec3 n, double etaiOverEtat) {
        double cosTheta = Math.min(uv.scale(-1).dot(n), 1.0);
        Vec3 rOutPerp = uv.add(n.scale(cosTheta)).scale(etaiOverEtat);
        double rOutParallelVal = -Math.sqrt(Math.abs(1.0 - rOutPerp.lengthSquared()));
        Vec3 rOutParallel = n.scale(rOutParallelVal);
        return rOutPerp.add(rOutParallel);
    }

    /**
     * Utility methods for generating random vectors, which are useful for diffuse scattering and other effects in the ray tracer.
     * @return
     */
    public static Vec3 random() {
        return new Vec3(Math.random(), Math.random(), Math.random());
    }

    /**
     * Generates a random vector with each component in the range [min, max). This is useful for creating random colors or directions within a specific range.
     * @param min
     * @param max
     * @return
     */
    public static Vec3 random(double min, double max) {
        return new Vec3(
            min + (max - min) * Math.random(),
            min + (max - min) * Math.random(),
            min + (max - min) * Math.random()
        );
    }

    /**
     * Generates a random point inside a unit sphere. This is commonly used for diffuse scattering, where rays are scattered in random directions within a hemisphere.
     * @return
     */
    public static Vec3 randomInUnitSphere() {
        while (true) {
            Vec3 p = random(-1, 1);
            if (p.lengthSquared() < 1) return p;
        }
    }

    /**
     * Checks if the vector is close to zero in all dimensions. This can be useful for avoiding issues with very small vectors that might cause numerical instability in calculations.
     * @return
     */
    public boolean nearZero() {
        double s = 1e-8;
        return (Math.abs(x) < s) && (Math.abs(y) < s) && (Math.abs(z) < s);
    }

    /**
     * Multiplies two vectors component-wise. This is often used for combining colors (e.g., when calculating the color contribution from a light source and a material's albedo).  
     * @param v
     * @return
     */
    public Vec3 multiply(Vec3 v) {
        return new Vec3(this.x * v.x, this.y * v.y, this.z * v.z);
    }
}