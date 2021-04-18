package com.bindingofnewton.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        /*
        System.out.println("Collision detected!");
        System.out.println(contact.getFixtureA().getFriction());
        System.out.println(contact.getFixtureB().getFriction());

         */
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
