package com.bindingofnewton.game;

import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.character.Player;

import javax.naming.Binding;

public class ContactHandler implements ContactListener {

    private long fireLastContact = 0;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();


        if (fixtureA.getUserData() instanceof Bullet){
            if (fixtureB.getUserData() instanceof Player) {
                ((Player) fixtureB.getUserData()).setHealth(-0.5f);
            }

            removeBulletFixture((Bullet) fixtureA.getUserData());
        }else if (fixtureB.getUserData() instanceof Bullet) {
            if (fixtureA.getUserData() instanceof Player) {
                ((Player) fixtureA.getUserData()).setHealth(-0.5f);
            }

            removeBulletFixture((Bullet) fixtureB.getUserData());
        }



    }

    private void removeBulletFixture(Bullet bullet) {
            if (BindingOfNewton.level.getCurrentRoom().getBullets().contains(bullet)){
                bullet.setRemove(true);
            }
            /*
            if (fixtureA.getUserData().equals("bullet")) {
                for (int i = 0; i < BindingOfNewton.level.getCurrentRoom().getBullets().size(); i++) {
                    if (BindingOfNewton.level.getCurrentRoom().getBullets().get(i).getBody().equals(fixtureA.getBody())) {
                        // Setting remove flag
                        BindingOfNewton.level.getCurrentRoom().getBullets().get(i).setRemove(true);
                        break;
                    }
                }

            }*/

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        //TODO: player kriegt zur zeit jede sekunde schaden, aber nur wenn er wieder am feuer angeht. Ändern!
        if (fixtureA.getUserData() instanceof Player && fixtureB.getUserData() != null && fixtureB.getUserData().equals("fire")){
            if (System.currentTimeMillis() - fireLastContact > 1000){
                ((Player) fixtureA.getUserData()).setHealth(-0.5f);
            }
            fireLastContact = System.currentTimeMillis();

        }else if (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() != null && fixtureA.getUserData().equals("fire")){
            if (System.currentTimeMillis() - fireLastContact > 1000){
                ((Player) fixtureB.getUserData()).setHealth(-0.5f);
            }
            fireLastContact = System.currentTimeMillis();
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
