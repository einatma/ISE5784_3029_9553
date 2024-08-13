package primitives;

public class Vector3 {
    float x, y, z;

    Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector3 min(Vector3 other) {
        return new Vector3(
                Math.min(x, other.x),
                Math.min(y, other.y),
                Math.min(z, other.z)
        );
    }

    Vector3 max(Vector3 other) {
        return new Vector3(
                Math.max(x, other.x),
                Math.max(y, other.y),
                Math.max(z, other.z)
        );
    }

    Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    Vector3 multiply(float scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    float dot(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    Vector3 cross(Vector3 other) {
        return new Vector3(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    Vector3 normalize() {
        float len = length();
        return new Vector3(x / len, y / len, z / len);
    }
}