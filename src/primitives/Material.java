package primitives;

/**
 * Represents the material properties of an object in a 3D scene.
 * This includes diffuse reflection coefficient (kD), specular reflection coefficient (kS),
 * and shininess coefficient (nShininess).
 *
 * @author Hadar Cohen and Einat Mazuz
 * Diffuse reflection coefficient (kD) determines how much of the diffuse light is reflected.
 * Specular reflection coefficient (kS) determines how much of the specular light is reflected.
 * Shininess coefficient (nShininess) determines the size of the specular highlight.
 */
public class Material {
    /**
     * Diffuse reflection coefficient
     */
    public Double3 kD = Double3.ZERO;
    /**
     * Specular reflection coefficient
     */
    public Double3 kS = Double3.ZERO;
    /*
     * Transparency coefficient
     */
    public Double3 kT = Double3.ZERO;
    /*
     * Reflection coefficient
     */
    public Double3 kR = Double3.ZERO;
    /**
     * Shininess coefficient
     */
    public int nShininess = 1;

    /**
     * Sets the diffuse reflection coefficient (kD) of the material.
     *
     * @param kD The new diffuse reflection coefficient.
     * @return This Material object with the updated diffuse reflection coefficient.
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient (kD) of the material using a scalar value,
     * which sets all components of kD to the same value.
     *
     * @param kD The new diffuse reflection coefficient as a scalar value.
     * @return This Material object with the updated diffuse reflection coefficient.
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular reflection coefficient (kS) of the material.
     *
     * @param kS The new specular reflection coefficient.
     * @return This Material object with the updated specular reflection coefficient.
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient (kS) of the material using a scalar value,
     * which sets all components of kS to the same value.
     *
     * @param kS The new specular reflection coefficient as a scalar value.
     * @return This Material object with the updated specular reflection coefficient.
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the transparency coefficient (kT) of the material.
     *
     * @param kT The new transparency coefficient.
     * @return This Material object with the updated transparency coefficient.
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the specular Transparency coefficient (Kt) of the material using a scalar value,
     * which sets all components of Kt to the same value.
     *
     * @param kt The new specular reflection coefficient as a scalar value.
     * @return This Material object with the updated specular reflection coefficient.
     */
    public Material setKt(double kt) {
        this.kT = new Double3(kt);
        return this;
    }

    /*
     * Sets the Reflection coefficient (kT) of the material using a scalar value,
     * which sets all components of kT to the same value.
     *
     * @param kT The new Reflection coefficient as a scalar value.
     * @return This Material object with the updated Reflection coefficient.
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the specular Transparency coefficient (kR) of the material using a scalar value,
     * which sets all components of kR to the same value.
     *
     * @param kR The new specular reflection coefficient as a scalar value.
     * @return This Material object with the updated specular reflection coefficient.
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Sets the shininess coefficient (nShininess) of the material.
     *
     * @param nShininess The new shininess coefficient.
     * @return This Material object with the updated shininess coefficient.
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }


}
