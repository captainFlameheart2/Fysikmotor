/*
 * Copyright (C) 2019 Jonatan Larsson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package body;

import convenience.Vector2D;
import static java.lang.Double.POSITIVE_INFINITY;

/**
 *
 * @author Jonatan Larsson
 */
public abstract class Body {

    public final Vector2D position = new Vector2D(), velocity = new Vector2D(), acceleration = new Vector2D();
    public double angle, angularVelocity, angularAcceleration;

    public final double mass, invertedMass;
    public final double momentOfInertia;
    public final boolean isStatic;
    
    private final double coefficientOfRestitution;

    Body(double mass, double momentOfInertia, double coefficientOfRestitution) {
        this.mass = mass;
        invertedMass = 1 / this.mass;
        this.momentOfInertia = momentOfInertia;
        this.isStatic = (mass == POSITIVE_INFINITY);

        this.coefficientOfRestitution = coefficientOfRestitution;
    }

    public final void setPosition(Vector2D position) {
        this.position.set(position);
    }

    public final void setVelocity(Vector2D velocity) {
        this.velocity.set(velocity);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void addToVelocity(Vector2D velChange) {
        velocity.add(velChange);
    }

    public void addToAngularVelocity(double angVelChange) {
        angularVelocity += angVelChange;
    }

    public final void applyForce(Vector2D force) {
        acceleration.add(Vector2D.division(force, mass));
    }

    public void integrate(double seconds) {
        velocity.add(Vector2D.multiplication(acceleration, seconds));
        acceleration.set();
        position.add(Vector2D.multiplication(velocity, seconds));

        angularVelocity += angularAcceleration * seconds;
        angularAcceleration = 0;
        angle += angularVelocity * seconds;
    }

    public abstract boolean containsPoint(Vector2D point);

    public abstract double minCoordinateAlong(Vector2D vector2D);

    double positionAlong(Vector2D axisVec) {
        return position.dot(axisVec);
    }

    public double getCoefficientOfRestitution() {
        return coefficientOfRestitution;
    }

}
