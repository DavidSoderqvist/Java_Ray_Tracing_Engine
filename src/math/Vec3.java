package math;

/**
 * Represents a mathematical 3-dimensional vector (x, y, z).
 * 
 * This class is commonly used to represent 3D positions, directions, 
 * or RGB color values in the Ray Tracer.
 */
public class Vec3 {
    
    public final double x;
    public final double y;
    public final double z;
    
    /**
     * Constructs a new 3D vector.
     *
     * @param x The x-coordinate or Red component.
     * @param y The y-coordinate or Green component.
     * @param z The z-coordinate or Blue component.
     */
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Adds another vector to this vector.
     *
     * @param other The vector to add to this one.
     * @return A new Vec3 representing the sum of the two vectors.
     */
    public Vec3 add(Vec3 other) {
        return new Vec3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Scales the vector by a scalar value (multiplication).
     *
     * @param t The factor to multiply each coordinate by.
     * @return A new Vec3 where each component has been multiplied by t.
     */
    public Vec3 scale(double t) {
        return new Vec3(this.x * t, this.y * t, this.z * t);
    }

    /**
     * Computes the length (magnitude) of the vector.
     *
     * @return The length of the vector.
     */
    public double length() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Normalizes the vector to have a length of 1.
     *
     * @return A new Vec3 that is the normalized version of this vector.
     */
    public Vec3 normalize() {
        double len = length();
        return new Vec3(x / len, y / len, z / len);
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param other The vector to subtract from this one.
     * @return A new Vec3 representing the difference of the two vectors.
     */
    public Vec3 sub(Vec3 other) {
        return new Vec3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Computes the dot product of this vector with another vector.
     *
     * @param other The other vector to compute the dot product with.
     * @return The dot product as a double.
     */
    public double dot(Vec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Generates a random vector with each component in the range [0, 1).
     *
     * @return A new Vec3 with random x, y, z components.
     */
    public static Vec3 random() {
        return new Vec3(Math.random(), Math.random(), Math.random());
    }

    /**
     * Generates a random vector with each component in the specified range [min, max).
     *
     * @param min The minimum value for each component.
     * @param max The maximum value for each component.
     * @return A new Vec3 with random x, y, z components within the specified range.
     */
    public static Vec3 random(double min, double max) {
        return new Vec3(min + (max - min) * Math.random(),
                        min + (max - min) * Math.random(),
                        min + (max - min) * Math.random());
    }   

    /**
     * Generates a random vector inside a unit sphere.
     *
     * @return A new Vec3 representing a random point inside a unit sphere.
     */
    public static Vec3 randomInUnitSphere() {
        while (true) {
            Vec3 p = Vec3.random(-1, 1);
            if (p.length() * p.length() >= 1) continue;
            return p;
        }
    }

    /**
     * Reflects a vector off a surface with the given normal.
     *
     * @param v The incoming vector to reflect.
     * @param n The normal vector of the surface.
     * @return A new Vec3 representing the reflected vector.
     */
    public static Vec3 reflect(Vec3 v, Vec3 n) {
        return v.sub(n.scale(2 * v.dot(n)));
    }


}