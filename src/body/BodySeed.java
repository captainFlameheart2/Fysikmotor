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
public abstract class BodySeed {

    public final Vector2D bodyPosition = new Vector2D(), bodyVelocity = new Vector2D();
    public double bodyAngle, bodyAngularVelocity;
        
    double bodyMass, bodyMomentOfInertia;
    
    public double bodyCoefficientOfRestitution = .5;

    public final void setBodyPosition(Vector2D position) {
        bodyPosition.set(position);
    }

    public final void setBodyPosition(double x, double y) {
        bodyPosition.set(x, y);
    }

    public final void setBodyVelocity(Vector2D velocity) {
        bodyVelocity.set(velocity);
    }

    public final void setBodyVelocity(double vx, double vy) {
        bodyVelocity.set(vx, vy);
    }

    public final void setBodyAngle(double angle) {
        bodyAngle = angle;
    }

    public final void setBodyAngularVelocity(double angularVelocity) {
        bodyAngularVelocity = angularVelocity;
    }

    public abstract void setBodyDensity(double density);

    public final void setDefaultBodyDensity() {
        setBodyDensity(1);
    }
    
    public final void makeBodyStatic() {
        bodyMass = POSITIVE_INFINITY;
        bodyMomentOfInertia = POSITIVE_INFINITY;
    }
    
    public final double getBodyMass() {
        return bodyMass;
    }
    
    public final double getBodyMomentOfInertia() {
        return bodyMomentOfInertia;
    }
    
    public final void setBodyCoefficientOfRestitution(double coefficientOfRestitution) {
        bodyCoefficientOfRestitution = coefficientOfRestitution;
    }

}
