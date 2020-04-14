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
import body.CircularBody;
import body.BodyPair;
import body.BodySeed;
import body.CircularBodySeed;
import body.Contact;
import body.PolygonBody;
import body.PolygonBodySeed;
import java.util.List;

/**
 *
 * @author Jonatan Larsson
 */
public final class Farm {

    private final ArrayList<Body> bodies;
    private final ArrayList<BodyPair> bodyPairs;

    private final Integrator integrator;
    private final ContactReporter contactReporter;
    private final ContactHandler contactHandler;

    public Farm() {
        bodies = new ArrayList<>();
        bodyPairs = new ArrayList<>();

        integrator = new Integrator();
        contactReporter = new ContactReporter();
        contactHandler = new ContactHandler();
    }

    public CircularBody growCircularBody(CircularBodySeed bodySeed) {
        CircularBody grownBody = new CircularBody(
                bodySeed.bodyRadius,
                bodySeed.getBodyMass(),
                bodySeed.getBodyMomentOfInertia(),
                bodySeed.bodyCoefficientOfRestitution
        );
        initializeBody(grownBody, bodySeed);
        return grownBody;
    }

    public PolygonBody growPolygonBody(PolygonBodySeed bodySeed) {
        PolygonBody grownBody = new PolygonBody(
                bodySeed.bodyRelativeVertices,
                bodySeed.getBodyMass(),
                bodySeed.getBodyMomentOfInertia(),
                bodySeed.bodyCoefficientOfRestitution
        );
        initializeBody(grownBody, bodySeed);
        return grownBody;
    }

    private void initializeBody(Body body, BodySeed bodySeed) {
        body.setPosition(bodySeed.bodyPosition);
        body.setVelocity(bodySeed.bodyVelocity);
        body.setAngle(bodySeed.bodyAngle);
        body.setAngularVelocity(bodySeed.bodyAngularVelocity);

        for (Body other : bodies) {
            bodyPairs.add(new BodyPair(body, other));
        }
        bodies.add(body);
    }

    public void destroyBody(Body body) {
        bodies.remove(body);

        ArrayList<BodyPair> bodyPairsToRemove = new ArrayList<>();
        for (BodyPair bodyPair : bodyPairs) {
            if (bodyPair.contains(body)) {
                bodyPairsToRemove.add(bodyPair);
            }
        }
        bodyPairs.removeAll(bodyPairsToRemove);
    }

    public void update(double seconds) {
        integrator.integrate(bodies, seconds);
        List<Contact> contactReport = contactReporter.report(bodyPairs);
        contactHandler.handleContacts(contactReport);
    }

}
