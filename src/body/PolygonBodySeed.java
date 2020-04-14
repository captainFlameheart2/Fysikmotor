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
public final class PolygonBodySeed extends BodySeed {

    public Vector2D[] bodyRelativeVertices;

    public void setBodyRelativeVertices(Vector2D... bodyVertices) {
        this.bodyRelativeVertices = bodyVertices;
    }

    @Override
    public void setBodyDensity(double density) {
        bodyMass = 0;
        bodyMomentOfInertia = 0;
        
        int previousIndex = bodyRelativeVertices.length - 1;
        for (int i = 0; i < bodyRelativeVertices.length; i++) {
            Vector2D a = bodyRelativeVertices[previousIndex], b = bodyRelativeVertices[i];
            double crossProduct = a.cross(b);
            bodyMass += crossProduct;
            bodyMomentOfInertia += crossProduct * (a.magSquared() + b.magSquared() + a.dot(b));
            
            previousIndex = i;
        }
        bodyMass *= density / 2;
        bodyMomentOfInertia *= density / 12;
    }

}
