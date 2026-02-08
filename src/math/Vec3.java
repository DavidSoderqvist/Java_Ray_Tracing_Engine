package math;

public class Vec3 {
    public final double x, y, z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // --- GRUNDLÄGGANDE MATEMATIK ---

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
        if (len == 0) return new Vec3(0, 0, 0); // Skydd mot division med noll
        return scale(1.0 / len);
    }

    // --- FYSIG-VERKTYG (REFLEKTION & REFRAKTION) ---

    /**
     * Studsar en vektor mot en normal (Spegling).
     */
    public static Vec3 reflect(Vec3 v, Vec3 n) {
        return v.sub(n.scale(2 * v.dot(n)));
    }

    /**
     * Bryter en vektor genom ett material (Glas/Vatten).
     * Inkluderar skydd mot NaN (svarta pixlar).
     */
    public static Vec3 refract(Vec3 uv, Vec3 n, double etaiOverEtat) {
        double cosTheta = Math.min(uv.scale(-1).dot(n), 1.0);
        
        // Räkna ut den vinkelräta delen
        Vec3 rOutPerp = uv.add(n.scale(cosTheta)).scale(etaiOverEtat);
        
        // Räkna ut den parallella delen
        // VIKTIGT: Math.abs här förhindrar att vi drar roten ur ett negativt tal (NaN)
        double rOutParallelVal = -Math.sqrt(Math.abs(1.0 - rOutPerp.lengthSquared()));
        
        Vec3 rOutParallel = n.scale(rOutParallelVal);
        
        return rOutPerp.add(rOutParallel);
    }

    // --- SLUMP-VERKTYG ---

    public static Vec3 random() {
        return new Vec3(Math.random(), Math.random(), Math.random());
    }

    public static Vec3 random(double min, double max) {
        return new Vec3(
            min + (max - min) * Math.random(),
            min + (max - min) * Math.random(),
            min + (max - min) * Math.random()
        );
    }

    public static Vec3 randomInUnitSphere() {
        while (true) {
            Vec3 p = random(-1, 1);
            if (p.lengthSquared() < 1) return p;
        }
    }

    // Returnerar true om vektorn är extremt nära noll i alla riktningar
    public boolean nearZero() {
        double s = 1e-8;
        return (Math.abs(x) < s) && (Math.abs(y) < s) && (Math.abs(z) < s);
    }
}