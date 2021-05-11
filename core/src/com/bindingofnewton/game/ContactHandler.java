package com.bindingofnewton.game;

import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.character.Enemy;
import com.bindingofnewton.game.character.Entity;
import com.bindingofnewton.game.character.Player;

import javax.naming.Binding;

public class ContactHandler implements ContactListener {

    private long fireLastContact = 0;

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();


        if (bodyA.getUserData() instanceof Bullet){
            if (bodyB.getUserData() instanceof Player) {
                ((Player) bodyB.getUserData()).setHealth(-1.0f);
            }
            if (bodyB.getUserData() instanceof Enemy) {
                ((Enemy) bodyB.getUserData()).setHealth(-1.0f);
            }

            removeBulletFixture((Bullet) bodyA.getUserData());
        }else if (bodyB.getUserData() instanceof Bullet) {
            if (bodyA.getUserData() instanceof Player) {
                ((Player) bodyA.getUserData()).setHealth(-1.0f);
            }
            if (bodyA.getUserData() instanceof Enemy) {
                ((Enemy) bodyA.getUserData()).setHealth(-1.0f);
            }
            if(((Entity) bodyA.getUserData()).getHealth() <= 0.0){
                ((Entity) bodyA.getUserData()).setDead(true);
            }

            removeBulletFixture((Bullet) bodyB.getUserData());
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
