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

/**
 *
 * @author Jonatan Larsson
 */
public final class CircularBody extends Body {

    public final double radius;
    private final double radiusSquared;

    public CircularBody(double radius, double mass, double momentOfInertia, double coefficientOfRestitution) {
        super(mass, momentOfInertia, coefficientOfRestitution);
        this.radius = radius;
        radiusSquared = this.radius * this.radius;
    }

    @Override
    public boolean containsPoint(Vector2D point) {
        return position.distSquared(point) < radiusSquared;
    }

    @Override
    public double minCoordinateAlong(Vector2D axisVec) {
        return positionAlong(axisVec) - radius;
    }

}
