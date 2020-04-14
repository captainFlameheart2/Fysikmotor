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
package main;

import body.Body;
import java.util.ArrayList;
import body.BodyPair;
import body.CircularBody;
import body.Contact;
import body.PolygonBody;
import convenience.Vector2D;
import static convenience.Vector2D.division;
import static convenience.Vector2D.drawing;
import static convenience.Vector2D.multiplication;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Jonatan Larsson
 */
final class ContactReporter {

    List<Contact> report(ArrayList<BodyPair> bodyPairs) {
        return bodyPairs.stream()
                .map(bodyPair -> contact(bodyPair))
                .filter(contact -> contact != null)
                .collect(Collectors.toList());
    }

    private Contact contact(BodyPair bodyPair) {
        if (bodyPair.bodyA.isStatic && bodyPair.bodyB.isStatic) {
            return null;
        }

        boolean bodyAIsCircular = (bodyPair.bodyA instanceof CircularBody);
        boolean bodyBIsCircular = (bodyPair.bodyB instanceof CircularBody);

        if (bodyAIsCircular && bodyBIsCircular) {
            return circleVSCircleContact(bodyPair);
        } else if (bodyAIsCircular) {
            return circleVSPolygonContact((CircularBody) bodyPair.bodyA, (PolygonBody) bodyPair.bodyB);//contactTemplateUsingOneBodysNormals((PolygonBody) bodyPair.bodyB, bodyPair.bodyA);
        } else if (bodyBIsCircular) {
            return circleVSPolygonContact((CircularBody) bodyPair.bodyB, (PolygonBody) bodyPair.bodyA);
        } else {
            return polygonVSPolygonContact(bodyPair);
        }
    }

    private Contact circleVSCircleContact(BodyPair bodyPair) {
        CircularBody a = (CircularBody) bodyPair.bodyA;
        CircularBody b = (CircularBody) bodyPair.bodyB;

        Vector2D posDiff = drawing(a.position, b.position);
        double distSquared = posDiff.magSquared();
        double radiusSum = a.radius + b.radius;
        if (distSquared >= radiusSum * radiusSum) {
            return null;
        }

        double dist = Math.sqrt(distSquared);
        double contactPenetration = radiusSum - dist;
        Vector2D contactNormal = division(posDiff, dist);
        Vector2D contactPoint = multiplication(contactNormal, -b.radius);
        contactPoint.add(b.position);
        return new Contact(bodyPair, contactNormal, contactPenetration, contactPoint);
    }

    private Contact circleVSPolygonContact(CircularBody circularBody, PolygonBody polygonBody) {
        ContactTemplate contactTemplate = contactTemplate(polygonBody, circularBody);
        if (contactTemplate == null) {
            return null;
        }
        Vector2D normal = polygonBody.normals[contactTemplate.normalIndex];
        Vector2D contactPoint = multiplication(normal, -circularBody.radius);
        contactPoint.add(circularBody.position);
        return new Contact(polygonBody, circularBody, normal, contactTemplate.depth, contactPoint);
    }

    private Contact polygonVSPolygonContact(BodyPair bodyPair) {
        PolygonBody a = (PolygonBody) bodyPair.bodyA, b = (PolygonBody) bodyPair.bodyB;

        ContactTemplate templateA = contactTemplate(a, b);
        if (templateA == null) {
            return null;
        }
        ContactTemplate templateB = contactTemplate(b, a);
        if (templateB == null) {
            return null;
        }

        ContactTemplate bestTemplate, worseTemplate;
        if (templateA.hasLessDepthThan(templateB)) {
            bestTemplate = templateA;
            worseTemplate = templateB;
        } else {
            bestTemplate = templateB;
            worseTemplate = templateA;
        }

        PolygonBody referenceBody = bestTemplate.normalConsideredBody, incidentBody = (PolygonBody) bestTemplate.other;
        int referenceNormalIndex = bestTemplate.normalIndex, incidentNormalIndex = worseTemplate.normalIndex;

        Vector2D referenceVertex0 = referenceBody.vertexLeftOfNormal(referenceNormalIndex);
        Vector2D referenceVertex1 = referenceBody.vertexRightOfNormal(referenceNormalIndex);
        Vector2D incidentVertex0 = incidentBody.vertexLeftOfNormal(incidentNormalIndex);
        Vector2D incidentVertex1 = incidentBody.vertexRightOfNormal(incidentNormalIndex);
        Vector2D referenceNormal = referenceBody.normals[referenceNormalIndex];
        Vector2D[] contactPoints = contactPointsUsingClipping(referenceVertex0, referenceVertex1, incidentVertex0, incidentVertex1, referenceNormal);

        return new Contact(referenceBody, incidentBody, referenceNormal, bestTemplate.depth, contactPoints);
    }

    private ContactTemplate contactTemplate(PolygonBody normalConsideredBody, Body other) {
        double depth = Double.POSITIVE_INFINITY;
        int normalIndex = 0;

        for (int i = 0; i < normalConsideredBody.vertexCount; i++) {
            double edgeCoordinate = normalConsideredBody.maxCoordinateAlongNormal(i);
            double minCoordinate = other.minCoordinateAlong(normalConsideredBody.normals[i]);

            double proposedDepth = edgeCoordinate - minCoordinate;
            if (proposedDepth <= 0) {
                return null;
            }
            if (proposedDepth < depth) {
                depth = proposedDepth;
                normalIndex = i;
            }
        }

        return new ContactTemplate(normalConsideredBody, other, normalIndex, depth);
    }

    private final class ContactTemplate {

        final PolygonBody normalConsideredBody;
        final Body other;
        final int normalIndex;
        final double depth;

        ContactTemplate(PolygonBody normalConsideredBody, Body other, int normalIndex, double depth) {
            this.normalConsideredBody = normalConsideredBody;
            this.other = other;
            this.normalIndex = normalIndex;
            this.depth = depth;
        }

        boolean hasLessDepthThan(ContactTemplate other) {
            return depth < other.depth;
        }

    }

    private Vector2D[] contactPointsUsingClipping(
            Vector2D referenceVertexA, Vector2D referenceVertexB,
            Vector2D incidentVertexA, Vector2D incidentVertexB,
            Vector2D referenceNormal) {

        Vector2D invalidAreaNormal = drawing(referenceVertexA, referenceVertexB);
        invalidAreaNormal.norm();
        Vector2D contactPointB = constrainPoint(incidentVertexB, referenceVertexA, invalidAreaNormal, incidentVertexA);
        invalidAreaNormal.flip();
        Vector2D contactPointA = constrainPoint(incidentVertexA, referenceVertexB, invalidAreaNormal, incidentVertexB);

        Vector2D[] contactPoints = removeOnePointAtMost(contactPointA, contactPointB, referenceVertexA, Vector2D.flip(referenceNormal));
        return contactPoints;
    }

    private Vector2D constrainPoint(Vector2D point, Vector2D invalidAreaEdgePoint, Vector2D invalidAreaNormal, Vector2D pointToConstrainTowards) {
        double invalidAreaEdgeCoord = invalidAreaEdgePoint.dot(invalidAreaNormal);
        double pointCoord = point.dot(invalidAreaNormal);
        double relativePointCoord = pointCoord - invalidAreaEdgeCoord;
        if (relativePointCoord < 0) {
            Vector2D constrainedPoint = drawing(point, pointToConstrainTowards);
            constrainedPoint.mul(relativePointCoord / (relativePointCoord - pointToConstrainTowards.dot(invalidAreaNormal)));
            constrainedPoint.add(point);
            return constrainedPoint;
        }
        return point;
    }

    private Vector2D[] removeOnePointAtMost(Vector2D removablePointA, Vector2D removablePointB, Vector2D invalidAreaEdgePoint, Vector2D invalidAreaNormal) {
        double invalidAreaEdgeCoord = invalidAreaEdgePoint.dot(invalidAreaNormal);

        double coordA = removablePointA.dot(invalidAreaNormal);
        if (coordA < invalidAreaEdgeCoord) {
            return new Vector2D[]{removablePointB};
        }
        double coordB = removablePointB.dot(invalidAreaNormal);
        if (coordB < invalidAreaEdgeCoord) {
            return new Vector2D[]{removablePointA};
        }

        return new Vector2D[]{removablePointA, removablePointB};
    }

}
