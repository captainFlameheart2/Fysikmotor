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

import static java.lang.Math.PI;

/**
 *
 * @author Jonatan Larsson
 */
public final class CircularBodySeed extends BodySeed {

    public double bodyRadius;

    public void setBodyRadius(double bodyRadius) {
        this.bodyRadius = bodyRadius;
    }

    @Override
    public void setBodyDensity(double density) {
        double area = bodyRadius * bodyRadius * PI;
        bodyMass = density * area;
        bodyMomentOfInertia = bodyMass * bodyRadius * bodyRadius / 2;
    }

}
