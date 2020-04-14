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
public final class PolygonBody extends Body {

    public final int vertexCount;
    private final Vector2D[] relativeVertices, relativeNormals;
    public final Vector2D[] vertices, normals;

    public PolygonBody(Vector2D[] relativeVertices, double mass, double inertiaTensor, double coefficientOfRestitution) {
        super(mass, inertiaTensor, coefficientOfRestitution);

        this.relativeVertices = relativeVertices.clone();
        vertexCount = this.relativeVertices.length;
        relativeNormals = new Vector2D[vertexCount];
        for (int i = 1; i < vertexCount; i++) {
            int previousVertexIndex = i - 1;
            relativeNormals[previousVertexIndex] = Vector2D.drawing(relativeVertices[i], relativeVertices[previousVertexIndex]);
            relativeNormals[previousVertexIndex].rotate90DegreesClockwise();
            relativeNormals[previousVertexIndex].norm();
        }
        relativeNormals[vertexCount - 1] = Vector2D.drawing(relativeVertices[0], relativeVertices[vertexCount - 1]);
        relativeNormals[vertexCount - 1].rotate90DegreesClockwise();
        relativeNormals[vertexCount - 1].norm();

        vertices = new Vector2D[vertexCount];
        normals = new Vector2D[vertexCount];
    }

    public Vector2D[] copyRelativeVertices() {
        Vector2D[] copies = new Vector2D[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            copies[i] = new Vector2D(relativeVertices[i]);
        }
        return copies;
    }

    @Override
    public void integrate(double seconds) {
        super.integrate(seconds);
        convertRelativeShapeAttributes();
    }

    private void convertRelativeShapeAttributes() {
        for (int i = 0; i < vertexCount; i++) {
            vertices[i] = new Vector2D(relativeVertices[i]);
            vertices[i].rotate(angle);
            vertices[i].add(position);

            normals[i] = Vector2D.rotation(relativeNormals[i], angle);
        }
    }

    @Override
    public boolean containsPoint(Vector2D point) {
        for (int i = 0; i < vertexCount; i++) {
            if (point.dot(normals[i]) > maxCoordinateAlongNormal(i)) {
                return false;
            }
        }
        return true;
    }

    public double maxCoordinateAlongNormal(int normalIndex) {
        Vector2D normal = normals[normalIndex];
        Vector2D maxPoint = vertices[normalIndex];
        return maxPoint.dot(normal);
    }

    @Override
    public double minCoordinateAlong(Vector2D axisVec) {
        double minCoordinate = Double.POSITIVE_INFINITY;

        for (Vector2D vertex : vertices) {
            double coordinate = vertex.dot(axisVec);
            if (coordinate < minCoordinate) {
                minCoordinate = coordinate;
            }
        }

        return minCoordinate;
    }
    
    public Vector2D vertexLeftOfNormal(int normalIndex) {
        return vertices[normalIndex];
    }
    
    public Vector2D vertexRightOfNormal(int normalIndex) {
        return vertices[(normalIndex + 1) % vertexCount];
    }

}
