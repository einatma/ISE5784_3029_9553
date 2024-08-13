package primitives;

/**
 * Represents a 3D vector with floating-point coordinates.
 */
public class Vector3 {
    float x, y, z;

    /**
     * Constructs a new Vector3 object with the specified x, y, and z components.
     *
     * @param x The x component of the vector.
     * @param y The y component of the vector.
     * @param z The z component of the vector.
     */
    Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns a new Vector3 that represents the element-wise minimum of this vector and another vector.
     * <p>
     * This method creates a new vector where each component is the minimum of the corresponding components of
     * this vector and the provided vector.
     *
     * @param other The vector to compare with.
     * @return A new Vector3 representing the element-wise minimum of this vector and the other vector.
     */
    Vector3 min(Vector3 other) {
        return new Vector3(
                Math.min(x, other.x),
                Math.min(y, other.y),
                Math.min(z, other.z)
        );
    }

    /**
     * Returns a new Vector3 that represents the element-wise maximum of this vector and another vector.
     * <p>
     * This method creates a new vector where each component is the maximum of the corresponding components of
     * this vector and the provided vector.
     *
     * @param other The vector to compare with.
     * @return A new Vector3 representing the element-wise maximum of this vector and the other vector.
     */
    Vector3 max(Vector3 other) {
        return new Vector3(
                Math.max(x, other.x),
                Math.max(y, other.y),
                Math.max(z, other.z)
        );
    }

    /**
     * Subtracts another vector from this vector and returns the result.
     * <p>
     * This method creates a new vector that represents the difference between this vector and the provided vector.
     *
     * @param other The vector to subtract from this vector.
     * @return A new Vector3 representing the result of the subtraction.
     */
    Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Adds another vector to this vector and returns the result.
     * <p>
     * This method creates a new vector that represents the sum of this vector and the provided vector.
     *
     * @param other The vector to add to this vector.
     * @return A new Vector3 representing the result of the addition.
     */
    Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Multiplies this vector by a scalar and returns the result.
     * <p>
     * This method creates a new vector where each component is the product of the corresponding component of this
     * vector and the provided scalar.
     *
     * @param scalar The scalar value to multiply with.
     * @return A new Vector3 representing the result of the multiplication.
     */
    Vector3 multiply(float scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Computes the dot product of this vector with another vector.
     * <p>
     * This method calculates the dot product, which is a scalar value representing the magnitude of the projection of
     * this vector onto the other vector.
     *
     * @param other The vector to compute the dot product with.
     * @return The dot product of this vector and the other vector.
     */
    float dot(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Computes the cross product of this vector with another vector.
     * <p>
     * This method creates a new vector that is perpendicular to both this vector and the provided vector.
     *
     * @param other The vector to compute the cross product with.
     * @return A new Vector3 representing the result of the cross product.
     */
    Vector3 cross(Vector3 other) {
        return new Vector3(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    /**
     * Computes the length (magnitude) of this vector.
     * <p>
     * This method calculates the Euclidean norm of the vector, which is the square root of the sum of the squares
     * of its components.
     *
     * @return The length of this vector.
     */
    float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalizes this vector and returns the result.
     * <p>
     * This method creates a new vector with the same direction as this vector but with a length of 1.
     *
     * @return A new Vector3 representing the normalized vector.
     */
    Vector3 normalize() {
        float len = length();
        return new Vector3(x / len, y / len, z / len);
    }
}
