package math;

/**
 * Represents a mathematical 3-dimensional vector (x, y, z).
 * 
 * This class is commonly used to represent 3D positions, directions, 
 * or RGB color values in the Ray Tracer.
 */
public class Vec3 {
    
    /** The x-coordinate (or Red component). */
    public final double x;
    /** The y-coordinate (or Green component). */
    public final double y;
    /** The z-coordinate (or Blue component). */
    public final double z;
    
    /**
     * Constructs a new 3D vector.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
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
     * Returns a string representation of the vector.
     * Format: "(x, y, z)"
     *
     * @return A formatted string containing the coordinates.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
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


}