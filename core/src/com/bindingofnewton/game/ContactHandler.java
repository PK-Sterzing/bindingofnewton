package com.bindingofnewton.game;

import com.badlogic.gdx.physics.box2d.*;

public class ContactHandler implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        removeFixture(fixtureA);
        removeFixture(fixtureB);
    }

    private void removeFixture(Fixture fixtureA) {
        if(fixtureA.getUserData() != null) {
            if (fixtureA.getUserData().equals("bullet")) {
                for (int i = 0; i < BindingOfNewton.level.getCurrentRoom().getBullets().size(); i++) {
                    if (BindingOfNewton.level.getCurrentRoom().getBullets().get(i).getBody().equals(fixtureA.getBody())) {
                        // Setting remove flag
                        BindingOfNewton.level.getCurrentRoom().getBullets().get(i).setRemove(true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
