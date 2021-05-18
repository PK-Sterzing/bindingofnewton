package com.bindingofnewton.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.character.Enemy;
import com.bindingofnewton.game.character.Entity;
import com.bindingofnewton.game.character.Player;
import com.bindingofnewton.game.items.HealthBoostItem;
import com.bindingofnewton.game.items.Item;
import com.bindingofnewton.game.map.Level;

public class ContactHandler implements ContactListener {

    private long fireLastContact = 0;

    private Level level;

    public ContactHandler(Level level){
        this.level = level;
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        if(!(bodyA.getUserData() == null || bodyB.getUserData() == null)) {
            if (bodyA.getUserData() instanceof Bullet) {
                // If bodyB extends from Entity
                diminishHealth(bodyB);
                removeBulletFixture((Bullet) bodyA.getUserData());
            } else if (bodyB.getUserData() instanceof Bullet) {
                // If bodyA extends from Entity
                diminishHealth(bodyA);
                removeBulletFixture((Bullet) bodyB.getUserData());
            } else if (bodyA.getUserData() instanceof Enemy) {
                if (bodyB.getUserData() instanceof Player) {
                    diminishHealth(bodyB);
                }
            } else if (bodyB.getUserData() instanceof Enemy) {
                if (bodyA.getUserData() instanceof Player) {
                    diminishHealth(bodyA);
                }
            }
        }
        // Collision with item
        if(bodyA.getUserData().getClass().getSuperclass().equals(Item.class) || bodyB.getUserData().getClass().getSuperclass().equals(Item.class)){
            giveItemEffect(bodyB, bodyA);
            giveItemEffect(bodyA, bodyB);
        }
        // Remove Bullet if other body is unknown
        if(bodyA.getUserData() instanceof Bullet) {
            removeBulletFixture((Bullet) bodyA.getUserData());
        }
        if(bodyB.getUserData() instanceof Bullet) {
            removeBulletFixture((Bullet) bodyB.getUserData());
        }
    }

    private void giveItemEffect(Body bodyA, Body bodyB) {
        if(bodyB.getUserData() instanceof Player && bodyA.getUserData().getClass().getSuperclass().equals(Item.class)){
            // Give buff
            ((Item)bodyA.getUserData()).use((Player) bodyB.getUserData());
            // Remove item
            ((Item)bodyA.getUserData()).setShouldBeRemoved(true);
        }
    }

    /**
     * Checks if a collision with a door happened
     * @return  true - collision with door
     *          false - no collision with door
     */
    public boolean isDoorCollision(){
        String layerName = "doors-";

        Player player = level.getCurrentRoom().getPlayer();
        Polygon polygon = player.getPolygon();
        Rectangle playerRectangle = polygon.getBoundingRectangle();

        //Rectangle playerRectangle = level.getCurrentRoom().getPlayer().getPolygon().getBoundingRectangle();

        for (Orientation orientation : Orientation.values()){
            MapLayer layer = level.getCurrentRoom().getMap().getLayers().get(layerName + orientation.name());
            if (layer == null) return false;

            MapObjects objects = layer.getObjects();

            for(int i = 0; i < objects.getCount(); i++){
                if (objects.get(i) instanceof RectangleMapObject){
                    Rectangle rectangle = ((RectangleMapObject) objects.get(i)).getRectangle();

                    Vector2 position, size;
                    if (orientation == Orientation.DOWN || orientation == Orientation.UP){
                        position = orientation.moveCoord(new Vector2(0,0), 16);

                        size = new Vector2(-16, 0);
                    }else{
                        position = new Vector2(0,0);

                        size = new Vector2(0, -16);
                    }

                    Rectangle rectangle1 = new Rectangle(
                            rectangle.x + position.x,
                            rectangle.y + position.y,
                            rectangle.width + size.x,
                            rectangle.height + size.y);


                    if (playerRectangle.overlaps(rectangle1)){
                        //The Player enters a door
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private void diminishHealth(Body body) {
        // Check if body extends from Entity
        if(body.getUserData().getClass().getSuperclass().equals(Entity.class)) {

            Entity entity = (Entity) body.getUserData();
            // Check if body has cooldown pending
            if (System.currentTimeMillis() - entity.getLastSustainedDamage() >= entity.getInvincibilityCooldown()) {
                entity.setHealth(-1.0f);
                if (entity.getHealth() <= 0.0) {
                    entity.setDead(true);
                }
                entity.setLastSustainedDamage(System.currentTimeMillis());
            }

        }
    }

    private void removeBulletFixture(Bullet bullet) {
        if (BindingOfNewton.getInstance().level.getCurrentRoom().getBullets().contains(bullet)){
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
                if (((Entity) bodyA.getUserData()).getHealth() <= 0.0) {
                    ((Entity) bodyA.getUserData()).setDead(true);
                }
            }
            fireLastContact = System.currentTimeMillis();

        }else if (bodyB.getUserData() instanceof Player && bodyA.getUserData() != null && bodyA.getUserData().equals("fire")){
            if (System.currentTimeMillis() - fireLastContact > 1000){
                ((Player) bodyB.getUserData()).setHealth(-0.5f);
                if (((Entity) bodyA.getUserData()).getHealth() <= 0.0) {
                    ((Entity) bodyA.getUserData()).setDead(true);
                }
            }
            fireLastContact = System.currentTimeMillis();
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
