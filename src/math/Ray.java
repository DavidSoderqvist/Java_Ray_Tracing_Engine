package math;

/**
 * Represents a ray in 3D space, defined by an origin point and a direction vector.
 * 
 * A ray is commonly used in ray tracing to model the path of light or sight lines.
 */
public class Ray {

    private final Vec3 origin;
    private final Vec3 direction;

    /**
     * Constructs a new Ray with the specified origin and direction.
     *
     * @param origin    The starting point of the ray.
     * @param direction The direction vector of the ray.
     */
    public Ray (Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }
    
    /**
     * Gets the origin point of the ray.
     *
     * @return The origin Vec3.
     */
    public Vec3 getOrigin() {
        return origin;
    }

    /**
     * Gets the direction vector of the ray.
     *
     * @return The direction Vec3.
     */
    public Vec3 getDirection() {
        return direction;
    }

    /**
     * Computes a point along the ray at parameter t.
     *
     * @param t The parameter value along the ray.
     * @return A Vec3 representing the point at distance t along the ray.
     */
    public Vec3 at(double t) {
        return origin.add(direction.scale(t));
    }
}
