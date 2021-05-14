package com.bindingofnewton.game;

import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.character.Entity;
import com.bindingofnewton.game.character.Player;

public class ContactHandler implements ContactListener {

    private long fireLastContact = 0;

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (bodyA.getUserData() instanceof Bullet){
            // If bodyB extends from Entity
            diminishHealth(bodyB, bodyA);
        }else if (bodyB.getUserData() instanceof Bullet) {
            // If bodyA extends from Entity
            diminishHealth(bodyA, bodyB);
        }
    }

    private void diminishHealth(Body bodyA, Body bodyB) {
        if(bodyA.getUserData().getClass().getSuperclass().equals(Entity.class)) {
            ((Entity) bodyA.getUserData()).setHealth(-1.0f);
            if (((Entity) bodyA.getUserData()).getHealth() <= 0.0) {
                ((Entity) bodyA.getUserData()).setDead(true);
            }
        }
        removeBulletFixture((Bullet) bodyB.getUserData());
    }

    private void removeBulletFixture(Bullet bullet) {
        if (BindingOfNewton.level.getCurrentRoom().getBullets().contains(bullet)){
            bullet.setRemove(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() instanceof Player && fixtureB.getUserData() != null && fixtureB.getUserData().equals("fire")){
            fixtureB.getBody().setActive(true);
        }else if (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() != null && fixtureA.getUserData().equals("fire")){
            fixtureA.getBody().setActive(true);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        //TODO: player kriegt zur zeit jede sekunde schaden, aber nur wenn er wieder am feuer angeht. Ändern!
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() != null && bodyB.getUserData().equals("fire")){

            if (System.currentTimeMillis() - fireLastContact > 1000){
                ((Player) bodyA.getUserData()).setHealth(-0.5f);
            }
            fireLastContact = System.currentTimeMillis();

        }else if (bodyB.getUserData() instanceof Player && bodyA.getUserData() != null && bodyA.getUserData().equals("fire")){
            if (System.currentTimeMillis() - fireLastContact > 1000){
                ((Player) bodyB.getUserData()).setHealth(-0.5f);
            }
            fireLastContact = System.currentTimeMillis();
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
