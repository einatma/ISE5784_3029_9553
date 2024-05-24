package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class VectorTests {

    private static final double DELTA = 0.0000001;
    /**
     * Test method for
     * {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v123=new Vector(0,0,1);
        Vector v03M2=new Vector(1,0,0);
        Vector vr = v123.crossProduct(v03M2);
        // TC01: Test that length of cross-product is proper (orthogonal vectors taken
        // for simplicity)
        assertEquals(v123.length() * v03M2.length(), vr.length(), DELTA, "crossProduct() wrong result length");
        // TC02: Test cross-product result orthogonality to its operands
        assertEquals(0, vr.dotProduct(v123), "crossProduct() result is not orthogonal to 1st operand");
        assertEquals(0, vr.dotProduct(v03M2), "crossProduct() result is not orthogonal to 2nd operand");
        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross-product of parallel vectors
        Vector vM2M4M6=new Vector(0,0,25);
        assertThrows(IllegalArgumentException.class, () -> v123.crossProduct(vM2M4M6), //
                "crossProduct() for parallel vectors does not throw an exception");
    }
    @Test
    void testAdd() { //~Ask Eliezer if adding and subtracting vectors in opposite directions is considered a separate equivalence class
        Vector v1         = new Vector(1, 2, 3);
        Vector v1Opposite = new Vector(-1, -2, -3);
        Vector v2         = new Vector(-2, -4, -6);
        // ============ Equivalence Partitions Tests ==============
        //        //TC01: Test that adding a vectors
        assertEquals(v1Opposite, v1.add(v2), "ERROR: Vector + Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test that adding a vector to itself throws an exception
        assertThrows(IllegalArgumentException.class, () ->
            v1.add(v1Opposite),
        "Vector + -itself does not throw an exception");


    }
@Test
void testSubtract() {
    Vector v1         = new Vector(1, 2, 3);
    Vector v1Opposite = new Vector(-1, -2, -3);
    Vector v2         = new Vector(-2, -4, -6);
    //============ Equivalence Partitions Tests ==============
    //TC01: Test that subtracting a vector from a point throws an exception
    assertEquals(v2, v1Opposite.subtract(v1), "ERROR: Vector - Vector does not work correctly");

    // =============== Boundary Values Tests ==================
    //TC11: Test that subtracting a vector from itself throws an exception
    assertThrows(IllegalArgumentException.class, () ->
        v1.subtract(v1),
     "Vector - itself does not throw an exception");



}
@Test
void testLength() {

    //============ Equivalence Partitions Tests =============
    //TC01: Test that ...
    assertEquals(3,new Vector(1, 2, 2).length(),"ERROR: length wrong value");

    // =============== Boundary Values Tests ==================
    // TC11: Test Length result for vector that has negative value
    assertEquals(3,new Vector(-1, 2, 2).length(),"ERROR: length wrong value");
    // TC12: Test Length result for unit vector
    assertEquals(1,new Vector(1, 0, 0).length(),"ERROR: length wrong value");
}
@Test
void testLengthSquared() {

    //============ Equivalence Partitions Tests =============
    //TC01: Test that ...
    assertEquals(9,new Vector(1, 2, 2).lengthSquared(),"ERROR: lengthSquared wrong value");

    // =============== Boundary Values Tests ==================
    // TC11: Test Length result for vector that has negative value
    assertEquals(9,new Vector(-1, 2, 2).lengthSquared(),"ERROR: lengthSquared wrong value");
    // TC12: Test Length result for unit vector
    assertEquals(1,new Vector(1, 0, 0).lengthSquared(),"ERROR: lengthSquared wrong value");
}

@Test
void testDotProduct() {
    Vector v1         = new Vector(0, 0, -3);
    Vector v2         = new Vector(1, 0, 2);
    Vector v3         = new Vector(2, 0, 0);
    Vector v4         = new Vector(1, 0, 0);

    // ============ Equivalence Partitions Tests ==============
    // TC01: Test dot-product result for angle>90 deg.
    assertEquals(-6,v1.dotProduct(v2), "ERROR: dotProduct() does not work correctly");
    // TC02: Test dot-product result for angle<90 deg.
    assertEquals(2,v2.dotProduct(v3), "ERROR: dotProduct() does not work correctly");

    // =============== Boundary Values Tests ==================
    // TC11: Test dot-product result for orthogonal vectors
    assertEquals(0,v1.dotProduct(v3), "ERROR: dotProduct() for orthogonal vectors does not work correctly");
    //TC12: Test dot-product result for unit vector
    assertEquals(1,v2.dotProduct(v4), "ERROR: dotProduct() for unit vector does not work correctly");

}
@Test
void testNormalize()
{
    Vector v = new Vector(1, 2, 3);
    Vector u= v.normalize();
    // ============ Equivalence Partitions Tests ==============
    // TC01: test vector normalization vs vector length and cross-product
    assertEquals(1,u.length(),"ERROR: the normalized vector is not a unit vector");
    assertThrows(IllegalArgumentException.class, () ->
                    v.crossProduct(u),
            "ERROR: the normalized vector is not parallel to the original one");
    assert v.dotProduct(u) < 0:"ERROR: the normalized vector is opposite to the original one";

    // =============== Boundary Values Tests ==================
    //TC01: test vector normalization for a unit vector
    Vector v1 = new Vector(1, 0, 0);
    assertEquals(v1,v1.normalize(),"ERROR: the normalized vector is not a unit vector");
}
@Test
void testConstructor() {
    // ============ Equivalence Partitions Tests ==============
    // TC01: Correct 3 points
    assertDoesNotThrow(() -> new Vector(1, 0, 0),
            "Failed constructing a correct vector");
    // ================== Boundary Values Tests ==================
    // TC11: 3 points that are the same
    assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0),
            "Constructed a vector with 3 identical points");
    // TC12: 3 points on the same line
    assertThrows(IllegalArgumentException.class, () -> new Vector(1, 1, 1),
            "Constructed a vector with 3 points on the same line");


}
@Test
void testScale() {
    Vector v = new Vector(1, 2, 3);
    // ============ Equivalence Partitions Tests ==============
    // TC01: Test that scaling a vector
    assertEquals(new Vector(3, 6, 9), v.scale(3), "ERROR: Vector * scalar does not work correctly");
    // =============== Boundary Values Tests ==================
    // TC11: Test that scaling a vector by 0 throws an exception
    assertThrows(IllegalArgumentException.class, () -> v.scale(0), "ERROR: Vector * 0 does not throw an exception");
}



}