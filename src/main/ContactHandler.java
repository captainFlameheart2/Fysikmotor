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
import body.Contact;
import convenience.Vector2D;
import static convenience.Vector2D.division;
import static convenience.Vector2D.drawing;
import static convenience.Vector2D.flip;
import static convenience.Vector2D.multiplication;
import static convenience.Vector2D.subtraction;
import java.util.List;

/**
 * @author Jonatan Larsson
 */
final class ContactHandler {

    void handleContacts(List<Contact> contactReport) {
        for (Contact contact : contactReport) {
            handleSingleContact(contact);
        }
    }

    private void handleSingleContact(Contact c) {
        computeAndApplyImpulses(c);
        moveApart(c);
    }

    private void computeAndApplyImpulses(Contact c) {
        Body a = c.bodyA, b = c.bodyB;
        Vector2D n = c.normal;

        for (Vector2D contactPoint : c.points) {
            Vector2D perpendicularOffsetA = perpendicularContactPointOffset(contactPoint, a);
            Vector2D perpendicularOffsetB = perpendicularContactPointOffset(contactPoint, b);

            Vector2D pointVelA = pointVelocity(a, perpendicularOffsetA);
            Vector2D pointVelB = pointVelocity(b, perpendicularOffsetB);
            Vector2D relativePointVel = subtraction(pointVelA, pointVelB);

            double smashingSpeed = relativePointVel.dot(n);
            if (smashingSpeed <= 0) {
                continue;
            }

            double coefficientOfRestitution = Math.min(a.getCoefficientOfRestitution(), b.getCoefficientOfRestitution());

            double impulseMagnitude = (1 + coefficientOfRestitution) * smashingSpeed;
            impulseMagnitude /= a.invertedMass + b.invertedMass
                    + square(perpendicularOffsetA.dot(n)) / a.momentOfInertia + square(perpendicularOffsetB.dot(n)) / b.momentOfInertia;

            Vector2D impulseB = multiplication(n, impulseMagnitude);
            double angularImpulseB = angularImpulse(impulseB, perpendicularOffsetB);
            Vector2D impulseA = flip(impulseB);
            double angularImpulseA = angularImpulse(impulseA, perpendicularOffsetA);

            applyImpulse(impulseB, b);
            applyAngularImpulse(angularImpulseB, b);
            applyImpulse(impulseA, a);
            applyAngularImpulse(angularImpulseA, a);
        }
    }

    private Vector2D perpendicularContactPointOffset(Vector2D contactPoint, Body referenceBody) {
        Vector2D offset = drawing(referenceBody.position, contactPoint);
        offset.rotate90DegreesClockwise();
        return offset;
    }

    private Vector2D pointVelocity(Body owner, Vector2D perpendicularPointOffset) {
        Vector2D pointVel = multiplication(perpendicularPointOffset, owner.angularVelocity);
        pointVel.add(owner.velocity);
        return pointVel;
    }

    private double square(double n) {
        return n * n;
    }

    private double angularImpulse(Vector2D linearImpulse, Vector2D perpendicularApplicationPointOffset) {
        return linearImpulse.dot(perpendicularApplicationPointOffset);
    }

    private void applyImpulse(Vector2D impulse, Body affectedBody) {
        affectedBody.addToVelocity(division(impulse, affectedBody.mass));
    }

    private void applyAngularImpulse(double angularImpulse, Body affectedBody) {
        affectedBody.addToAngularVelocity(angularImpulse / affectedBody.momentOfInertia);
    }

    private void moveApart(Contact c) {
        Body a = c.bodyA, b = c.bodyB;
        
        Vector2D positionCorrection = multiplication(c.normal, .5 * c.depth);
        if (a.isStatic) {
            b.position.add(positionCorrection);
        } else if (b.isStatic) {
            a.position.sub(positionCorrection);
        } else {
            double totalMass = a.mass + b.mass;
            double proportionA = b.mass / totalMass;
            double proportionB = a.mass / totalMass;

            b.position.add(multiplication(positionCorrection, proportionB));
            a.position.sub(multiplication(positionCorrection, proportionA));
        }
    }

}
