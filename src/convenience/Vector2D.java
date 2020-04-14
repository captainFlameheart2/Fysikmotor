package convenience;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.random;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * This class defines a vector that has an x and a y component. These components
 * can be altered either directly or by calling some of the non-static methods
 * within this class. Other non-static methods can compute and return typically
 * useful information.
 *
 * This class contains static methods that compute and return vectors generated
 * from given vectors that remain unaffected. Other types of static methods also
 * exist.
 *
 * This class utilizes the {@link Math} class for many of it's methods.
 *
 * @author Jonatan Larsson
 */
public final class Vector2D {

    /**
     * The x component.
     */
    public double x;

    /**
     * The y component.
     */
    public double y;

    private static final double CIRCUIT_ANGLE = 2 * Math.PI;

    /**
     * Constructs a new {@code Vector2D} object.
     *
     * Initializes the components with the default value zero.
     */
    public Vector2D() {
    }

    /**
     * Constructs a new {@code Vector2D} object.
     *
     * Initializes the components with the given values.
     *
     * @param x the value used to initialize the x-component
     * @param y the value used to initialize the y-component
     */
    public Vector2D(double x, double y) {
        set(x, y);
    }

    /**
     * Constructs a new {@code Vector2D} object.
     *
     * Initializes the components with the corresponding components of the given
     * vector. Hence, copying is performed.
     *
     * @param v the vector to be copied
     */
    public Vector2D(Vector2D v) {
        set(v);
    }

    /**
     * Sets the components with the default value zero.
     */
    public void set() {
        x = 0;
        y = 0;
    }

    /**
     * Sets the components with the given values.
     *
     * @param x the value used to set the x-component
     * @param y the value used to set the y-component
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the components with the corresponding components of the given
     * vector. Hence, copying is performed.
     *
     * @param v the vector to be copied
     */
    public void set(Vector2D v) {
        set(v.x, v.y);
    }

    /**
     * Adds the given vector by adding its components to the current vector's
     * corresponding components. This follows the concept of "Tip-to-Tail"
     * vector addition.
     *
     * @param v the vector to be added
     */
    public void add(Vector2D v) {
        x += v.x;
        y += v.y;
    }

    /**
     * Subtracts a given vector by subtracting its components to the current
     * vector's corresponding components.
     *
     * @param v the vector to be subtracted
     */
    public void sub(Vector2D v) {
        x -= v.x;
        y -= v.y;
    }

    /**
     * Multiplies the vector with the given value by multiplying both of the
     * vector's components with the given value. Hence, scaling is performed.
     *
     * @param f the value to multiply with
     */
    public void mul(double f) {
        x *= f;
        y *= f;
    }

    /**
     * Divides the vector with the given value by dividing both of the vector's
     * components with the given value. Hence, inverted scaling is performed.
     *
     * @param d the value to divide with
     */
    public void div(double d) {
        x /= d;
        y /= d;
    }

    /**
     * Normalizes the vector, making it a unit vector. This is achieved by
     * dividing the vector using its own magnitude.
     *
     * Note that, if the vector's magnitude is known with absolute certainty,
     * exclusively using the {@link #div(double) } method with the known
     * magnitude is preferred. Otherwise speed will be sacrificed.
     *
     * @see #div(double)
     */
    public void norm() {
        div(mag());
    }

    /**
     * Sets the magnitude with the given value. This is achieved by first
     * normalizing the vector and then multiplying it with the given value.
     *
     * Note that, if the vector can be concluded to be a unit vector with
     * absolute certainty, exlusively using the {@link #mul(double) } method is
     * preferred for this task. Otherwise speed will be sacrificed.
     *
     * @param mag the value used to set the magnitude
     * @see #norm()
     * @see #mul(double)
     */
    public void setMag(double mag) {
        norm();
        mul(mag);
    }

    /**
     * Adds the given value to the magnitude. This is achieved by computing the
     * new magnitude and then using it as an argument for the
     * {@link #setMag(double) } method.
     *
     * Note that, if the vector can be concluded to be a unit vector with
     * absolute certainty, exlusively using the {@link #mul(double) } method is
     * preferred for this task. Otherwise speed will be sacrificed.
     *
     * @param magDiff the value to add to the magnitude
     */
    public void addMag(double magDiff) {
        setMag(mag() + magDiff);
    }

    /**
     * Makes the vector twice as big. This is done by multiplying the vector
     * with the value 2.
     *
     * @see #mul(double)
     */
    public void doubleMag() {
        mul(2);
    }

    /**
     * Makes the vector half as big. This is done by dividing the vector with
     * the value 2.
     *
     * @see #div(double)
     */
    public void halveMag() {
        div(2);
    }

    /**
     * Makes sure that the vector's magnitude is not greater than the given
     * value. This is achieved by utilizing {@link #magSquared() } and {@link #setMag(double)
     * }.
     *
     * @param maxMag the maximum magnitude the vector should have
     */
    public void limitMag(double maxMag) {
        if (magSquared() > maxMag * maxMag) {
            setMag(maxMag);
        }
    }

    /**
     * Flips the vector. This is done by multiplying the vector with the value
     * -1.
     *
     * @see #mul(double)
     */
    public void flip() {
        mul(-1);
    }

    /**
     * Sets the angle of the vector. The vector's magnitude remains the same.
     * The methods {@link #mag() }, {@link #cos(double) } and {@link #sin(double)
     * }
     * are all used once each.
     *
     * @param angle the value used to set the angle (in radians)
     */
    public void setAngle(double angle) {
        double mag = mag();
        set(mag * cos(angle), mag * sin(angle));
    }

    /**
     * Adds the given value to the vector's angle. This is achieved by computing
     * the new angle and then using it as an argument for the
     * {@link #setAngle(double) } method.
     *
     * @param angleDiff the difference in radians between the new and old angle
     */
    public void rotate(double angleDiff) {
        setAngle(angle() + angleDiff);
    }

    /**
     * Rotates the vector 90 degrees clockwise. This yields better performance
     * than passing the radian value pi / 2 inside the {@link #rotate(double) }
     * method.
     */
    public void rotate90DegreesClockwise() {
        set(-y, x);
    }

    /**
     * Rotates the vector 90 degrees counterclockwise. This yields better
     * performance than passing the radian value -pi / 2 inside the {@link #rotate(double)
     * }
     * method.
     */
    public void rotate90DegreesCounterclockwise() {
        set(y, -x);
    }

    /**
     * Computes and returns the vector's magnitude squared. Pythagoras Theorem
     * is utilized.
     *
     * @return the magnitude squared
     */
    public double magSquared() {
        return (x * x + y * y);
    }

    /**
     * Computes and returns the vector's magnitude by utilizing Pythagoras
     * Theorem. This is done by passing in the result of {@link #magSquared() }
     * inside the {@link #sqrt(double) } method. Note that, when possible, only
     * using the {@link #magSquared() } method is preferred due to performance
     * reasons.
     *
     * @return the magnitude
     */
    public double mag() {
        return sqrt(magSquared());
    }

    /**
     * Computes and returns the vector's angle. This is achieved by using the
     * {@link #atan2(double, double) } method.
     *
     * @return the angle of the vector in radians.
     */
    public double angle() {
        return atan2(y, x);
    }

    /**
     * Computes and returns the distance squared to the given vector. This is
     * done from the perspective that the components of this vector and the
     * given vector represent 2D coordinates.
     *
     * @param v the vector to check the distance to
     * @return the distance squared to the given vector
     */
    public double distSquared(Vector2D v) {
        return Vector2D.drawing(this, v).magSquared();
    }

    /**
     * Computes and returns the distance to the given vector. This is done from
     * the perspective that the components of this vector and the given vector
     * represent 2D coordinates. This is more inefficient than the 
     * {@link #distSquared(Vector2D) } method due to the presens of the 
     * {@link #sqrt(double) } method. Thus, the {@link #distSquared(Vector2D) }
     * is preferred when possible.
     *
     * @param v the vector to check the distance to
     * @return the distance to the given vector
     */
    public double dist(Vector2D v) {
        return Vector2D.drawing(this, v).mag();
    }

    /**
     * Computes and returns the dot product between this vector and the given
     * vector. This is used to get a number on how similar this vector is to the
     * given vector. In the case of one of the vectors being a unit vector, the
     * returned value will be the other vector's projection along the direction
     * of the unit vector.
     *
     * @param v the vector to perform the dot product with
     * @return the dot product
     */
    public double dot(Vector2D v) {
        return x * v.x + y * v.y;
    }

    /**
     * Computes and returns the cross product between this vector and the given
     * vector. In 3D the cross product would be a 3D vector that is
     * perpendicular to both the given 3D vectors and normal to the plane
     * containing them. In 2D however, there is no z component. So if 2D vectors
     * were to be converted to 3D, their z components would implicitly be zero.
     * Crossing those 3D vectors would then generate a vector perpendicular to
     * the z-axis. That vector would thus only have one of its components not
     * equal to zero: the z component, which is what this 2D implementation
     * computes and returns.
     *
     * @param v the vector to cross with
     * @return the cross product
     */
    public double cross(Vector2D v) {
        return x * v.y - y * v.x;
    }

    /**
     * Computes and returns a new vector that is the sum of the two given
     * vectors, without altering any of the given vectors.
     *
     * @param vA one of the two "term" vectors to be added
     * @param vB one of the two "term" vectors to be added
     * @return the sum of the two vectors
     * @see #add(Vector2D) ¨
     */
    public static Vector2D addition(Vector2D vA, Vector2D vB) {
        Vector2D v = new Vector2D(vA);
        v.add(vB);
        return v;
    }

    /**
     * Computes and returns a new vector that is the difference between the two
     * given vectors, without altering any of the given vectors.
     *
     * @param vA the "minuend" vector (remains unaffected)
     * @param vB the "subtrahend" vector
     * @return the difference of the two vectors
     * @see #sub(Vector2D)
     */
    public static Vector2D subtraction(Vector2D vA, Vector2D vB) {
        Vector2D v = new Vector2D(vA);
        v.sub(vB);
        return v;
    }

    /**
     * Computes and returns a new vector that is the product of the given vector
     * and the given value, without altering the given vector.
     *
     * @param v the "factor" vector to be multiplied (remains unaffected)
     * @param n the value to multiply with
     * @return the product of the two vectors
     * @see #mul(double)
     */
    public static Vector2D multiplication(Vector2D v, double n) {
        Vector2D vNew = new Vector2D(v);
        vNew.mul(n);
        return vNew;
    }

    /**
     * Computes and returns a new vector that is the fraction between the given
     * vector and the given value, without altering the given vector.
     *
     * @param v the "numerator" vector (remains unaffected)
     * @param n the value to divide with
     * @return the sum of the two vectors
     * @see #div(double)
     */
    public static Vector2D division(Vector2D v, double n) {
        Vector2D vNew = new Vector2D(v);
        vNew.div(n);
        return vNew;
    }

    /**
     * A convenience method for generating a vector that has the same direction
     * and magnitude as the "arrow" that starts at the first given 2D point and
     * ends at the other given 2D point. Simply imagine drawing a vector from
     * the first 2D point to the other 2D point.
     *
     * @param start represents the 2D coordinates of the arrow's start point.
     * @param end represents the 2D coordinates of the arrow's end point.
     * @return the vector represented by the "drawn arrow"
     */
    public static final Vector2D drawing(Vector2D start, Vector2D end) {
        return subtraction(end, start);
    }

    /**
     * Computes and returns a new normalized version of the given vector without
     * altering the given vector.
     *
     * @param v the vector generating the normalized version
     * @return the normalized version of the given vector
     * @see #norm()
     */
    public static Vector2D normalization(Vector2D v) {
        Vector2D vNew = new Vector2D(v);
        vNew.norm();
        return vNew;
    }

    /**
     * Computes and returns the result of setting the magnitude of the given
     * vector with the given value, without altering the given vector.
     *
     * @param v the vector generating the new vector
     * @param mag the magnitude the new vector has
     * @return the new vector with the given magnitude
     * @see #setMag(double)
     */
    public static final Vector2D magSetting(Vector2D v, double mag) {
        Vector2D vNew = new Vector2D(v);
        vNew.setMag(mag);
        return vNew;
    }

    /**
     * Computes and returns the result of adding the given value to the given
     * vector's magnitude, without altering the given vector.
     *
     * @param v the vector generating the new vector
     * @param magDiff the difference in magnitude the new vector has compared to
     * the given vector
     * @return the new vector with the given difference in magnitude
     * @see #addMag(double)
     */
    public static final Vector2D magAddition(Vector2D v, double magDiff) {
        Vector2D vNew = new Vector2D(v);
        vNew.addMag(magDiff);
        return vNew;
    }

    /**
     * Computes and returns the result of doubling the given vector's magnitude,
     * without altering the given vector.
     *
     * @param v the vector generating the new vector
     * @return the new vector that is twice as big as the given vector
     * @see #addMag(double)
     */
    public static final Vector2D magDoubling(Vector2D v) {
        Vector2D vNew = new Vector2D(v);
        vNew.doubleMag();
        return vNew;
    }

    /**
     * Computes and returns the result of making the given vector half as big,
     * without altering the given vector.
     *
     * @param v the vector generating the new vector
     * @return the new vector that is twice as big as the given vector
     * @see #halveMag()
     */
    public static final Vector2D magHalving(Vector2D v) {
        Vector2D vNew = new Vector2D(v);
        vNew.halveMag();
        return vNew;
    }

    /**
     * Computes and returns the result of limiting the given vector's magnitude
     * with the given value, without altering the given vector.
     *
     * @param v the vector generating the new vector
     * @param maxMag the maximum magnitude the new vector should have
     * @return the new vector that is the magnitude-limited version of the given
     * vector
     * @see #limitMag(double)
     */
    public static final Vector2D magLimiting(Vector2D v, double maxMag) {
        Vector2D vNew = new Vector2D(v);
        vNew.limitMag(maxMag);
        return vNew;
    }

    /**
     * Computes and returns a new vector that is a flipped version of the given
     * vector, without altering the given vector.
     *
     * @param v the vector generating the new vector
     * @return the new flipped vector
     * @see #flip()
     */
    public static Vector2D flip(Vector2D v) {
        Vector2D vNew = new Vector2D(v);
        vNew.flip();
        return vNew;
    }

    /**
     * Computes and returns the result of setting the angle of the given vector
     * with the given value, without altering the given vector.
     *
     * @param v the vector generating the new vector
     * @param angle the angle the new vector has
     * @return the new vector with the given angle
     * @see #setAngle(double)
     */
    public static Vector2D angleSetting(Vector2D v, double angle) {
        Vector2D vNew = new Vector2D(v);
        vNew.rotate(angle);
        return vNew;
    }

    /**
     * Computes and returns a new vector that is a rotated version of the given
     * vector using the given angle difference, without altering the given
     * vector.
     *
     * @param v the vector generating the new vector
     * @param angleDiff the difference in angle the new vector has compared to
     * the given vector
     * @return the new vector with the given difference in angle
     * @see #rotate(double)
     */
    public static Vector2D rotation(Vector2D v, double angleDiff) {
        Vector2D vNew = new Vector2D(v);
        vNew.rotate(angleDiff);
        return vNew;
    }

    /**
     * Computes and returns a new vector that is a 90 degree rotated version of
     * the given vector, without altering the given vector. This yields better
     * performance than passing in the radian value pi / 2 as the angle
     * difference in the {@link #rotation(convenience.Vector2D, double) }
     * method.
     *
     * @param v the vector generating the new vector
     * @return the new 90 degree rotated vector
     * @see #rotate90DegreesClockwise()
     */
    public static Vector2D rotation90DegreesClockwise(Vector2D v) {
        Vector2D vNew = new Vector2D(v);
        vNew.rotate90DegreesClockwise();
        return vNew;
    }

    /**
     * Computes and returns a new vector that is a 90 degree rotated version of
     * the given vector in counterclockwise direction, without altering the
     * given vector. This yields better performance than passing in the radian
     * value pi / 2 as the angle difference in the {@link #rotation(convenience.Vector2D, double)
     * }
     * method.
     *
     * @param v the vector generating the new vector
     * @return the new 90 degree rotated vector (counterclockwise)
     * @see #rotate90DegreesCounterclockwise()
     */
    public static Vector2D rotation90DegreesCounterclockwise(Vector2D v) {
        Vector2D vNew = new Vector2D(v);
        vNew.rotate90DegreesCounterclockwise();
        return vNew;
    }

    /**
     * Computes and returns a unit vector with the given angle.
     *
     * @param angle the angle of the unitvector
     * @return a unitvector with the given angle
     */
    public static Vector2D fromAngle(double angle) {
        Vector2D v = new Vector2D(1, 0);
        v.setAngle(angle);
        return v;
    }

    /**
     * Computes and returns a unit vector with a random angle
     *
     * @return a unitvector with a random angle
     * @see #fromAngle(double)
     * @see #random()
     */
    public static Vector2D fromRandAngle() {
        return Vector2D.fromAngle(random() * CIRCUIT_ANGLE);
    }

    /**
     * Computes and returns a vector with the given angle and magnitude.
     *
     * @param angle the vector's angle
     * @param mag the vector's magnitude
     * @return a vector with the given angle and magnitude.
     * @see #fromAngle(double)
     * @see #mul(double)
     */
    public static Vector2D fromAngle(double angle, double mag) {
        return Vector2D.multiplication(Vector2D.fromAngle(angle), mag);
    }

    /**
     * Computes and returns a vector with a random angle and the given magnitude
     *
     * @param mag the vector's magnitude
     * @return a vector with a random direction and the given magnitude
     * @see #mul(double)
     * @see #fromRandAngle()
     */
    public static Vector2D fromRandAngle(double mag) {
        return Vector2D.multiplication(Vector2D.fromRandAngle(), mag);
    }

    /**
     * Computes and returns a vector with a random angle and magnitude within
     * the given interval.
     *
     * @param minMag the minimum magnitude the vector can have
     * @param maxMag the maximum magnitude the vector can have
     * @return a randomized vector who's magnitude is within the given interval
     * @see #fromRandAngle(double)
     */
    public static Vector2D rand(int minMag, int maxMag) {
        double diff = maxMag - minMag;
        double mag = minMag + Math.random() * diff;
        return Vector2D.fromRandAngle(mag);
    }

    /**
     * Returns the distance squared between the given vectors. This is done from
     * the perspective that the components of these vectors represent 2D
     * coordinates.
     *
     * @param vA one of the vectors to be used for distance check
     * @param vB one of the vectors to be used for distance check
     * @return the distance squared to the given vector
     * @see #distSquared(Vector2D)
     */
    public static double distSquared(Vector2D vA, Vector2D vB) {
        return vA.distSquared(vB);
    }

    /**
     * Returns the distance between the given vectors. This is done from the
     * perspective that the components of these vectors represent 2D
     * coordinates.
     *
     * @param vA one of the vectors to be used for distance check
     * @param vB one of the vectors to be used for distance check
     * @return the distance to the given vector
     * @see #dist(Vector2D)
     */
    public static double dist(Vector2D vA, Vector2D vB) {
        return vA.dist(vB);
    }

    /**
     * Returns the dot product of the two given vectors by adding the products
     * of their corresponding components.
     *
     * @param vA one of the vectors who's components will be used
     * @param vB one of the vectors who's components will be used
     * @return the dot product of the two vectors
     * @see #dot(Vector2D)
     */
    public static double dotProduct(Vector2D vA, Vector2D vB) {
        return vA.dot(vB);
    }

    /**
     * Computes and returns the cross product between the two given vectors. In
     * 3D the cross product would be a 3D vector that is perpendicular to both
     * the given 3D vectors and normal to the plane containing them. In 2D
     * however, there is no z component. So if 2D vectors were to be converted
     * to 3D, their z components would implicitly be zero. Crossing those 3D
     * vectors would then generate a vector perpendicular to the z-axis. That
     * vector would thus only have one of its components not equal to zero: the
     * z component, which is what this 2D implementation computes and returns.
     *
     * @param vA the vector on the left-hand side of the cross operator
     * @param vB the vector on the right-hand side of the cross operator
     * @return the cross product
     */
    public static double crossProduct(Vector2D vA, Vector2D vB) {
        return vA.cross(vB);
    }

}
