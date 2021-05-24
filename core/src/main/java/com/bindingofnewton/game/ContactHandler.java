package com.bindingofnewton.game;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.bullets.Bullet;
import com.bindingofnewton.game.assets.SoundHandler;
import com.bindingofnewton.game.character.Enemy;
import com.bindingofnewton.game.character.Entity;
import com.bindingofnewton.game.character.Player;
import com.bindingofnewton.game.items.Item;
import com.bindingofnewton.game.mainmenu.PauseScreen;
import com.bindingofnewton.game.mainmenu.WinScreen;
import com.bindingofnewton.game.map.Door;
import com.bindingofnewton.game.map.Level;

public class ContactHandler implements ContactListener {

    private long fireLastContact = 0;
    private boolean isContactFire = false;

    private Level level;

    public ContactHandler(Level level) {
        this.level = level;
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        if (!(bodyA.getUserData() == null || bodyB.getUserData() == null)) {
            if (bodyA.getUserData() instanceof Bullet) {
                // If bodyB extends from Entity

                if (!((Bullet) bodyA.getUserData()).isEnemyBullet()){
                    diminishHealth(bodyB, 1.0f);
                }


                removeBulletFixture((Bullet) bodyA.getUserData());
            } else if (bodyB.getUserData() instanceof Bullet) {
                if (!((Bullet) bodyB.getUserData()).isEnemyBullet()){
                    diminishHealth(bodyA, 1.0f);
                }

                // If bodyA extends from Entity
                removeBulletFixture((Bullet) bodyB.getUserData());
            } else if (bodyA.getUserData() instanceof Enemy) {
                if (bodyB.getUserData() instanceof Player) {
                    diminishHealth(bodyB, ((Enemy) bodyA.getUserData()).getDamage());
                }
            } else if (bodyB.getUserData() instanceof Enemy) {
                if (bodyA.getUserData() instanceof Player) {
                    diminishHealth(bodyA, ((Enemy) bodyB.getUserData()).getDamage());
                }
            }

            // Collision with item
            if (bodyA.getUserData().getClass().getSuperclass().equals(Item.class) || bodyB.getUserData().getClass().getSuperclass().equals(Item.class)) {
                giveItemEffect(bodyB, bodyA);
                giveItemEffect(bodyA, bodyB);
            }
        }

        // Remove Bullet if other body is unknown
        if (bodyA.getUserData() instanceof Bullet) {
            removeBulletFixture((Bullet) bodyA.getUserData());
        }
        if (bodyB.getUserData() instanceof Bullet) {
            removeBulletFixture((Bullet) bodyB.getUserData());
        }

        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() != null && bodyB.getUserData().equals("fire")) {
            isContactFire = true;
            contactWithFire(bodyA);

        } else if (bodyB.getUserData() instanceof Player && bodyA.getUserData() != null && bodyA.getUserData().equals("fire")) {
            isContactFire = true;
            contactWithFire(bodyB);
        }

        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Door){
            contactWithPortal(bodyB);
        }else if (bodyB.getUserData() instanceof Player && bodyA.getUserData() instanceof Door){
            contactWithPortal(bodyA);
        }
    }

    private void contactWithPortal(Body body){
        Door door = (Door) body.getUserData();
        if (door.getId() == Door.Id.PORTAL || door.getId() == Door.Id.PORTAL2) {
            BindingOfNewton.getInstance().levelNumber++;
            // Moves to new level
            BindingOfNewton.getInstance().setShouldMoveToNewLevel(true);
        }
    }

    private void contactWithFire(Body body) {
        if (System.currentTimeMillis() - fireLastContact > 1000) {
            ((Player) body.getUserData()).setHealth(-0.5f);
            if (((Entity) body.getUserData()).getHealth() <= 0.0) {
                ((Entity) body.getUserData()).setDead(true);
            }
            fireLastContact = System.currentTimeMillis();
        }
    }

    private void giveItemEffect(Body bodyA, Body bodyB) {
        if (bodyB.getUserData() instanceof Player && bodyA.getUserData().getClass().getSuperclass().equals(Item.class)) {
            // Give buff
            ((Item) bodyA.getUserData()).use((Player) bodyB.getUserData());
            // Remove item
            ((Item) bodyA.getUserData()).setShouldBeRemoved(true);
        }
    }

    /**
     * Checks if a collision with a door happened
     *
     * @return true - collision with door
     * false - no collision with door
     */
    public Orientation isDoorCollision() {
        String layerName = "doors-";

        Player player = level.getCurrentRoom().getPlayer();
        Polygon polygon = player.getPolygon();
        Rectangle playerRectangle = polygon.getBoundingRectangle();

        for (Orientation orientation : Orientation.values()) {
            MapLayer layer = level.getCurrentRoom().getMap().getLayers().get(layerName + orientation.name());
            if (layer == null) return null;

            MapObjects objects = layer.getObjects();

            for (int i = 0; i < objects.getCount(); i++) {
                if (objects.get(i) instanceof RectangleMapObject) {
                    Rectangle rectangle = ((RectangleMapObject) objects.get(i)).getRectangle();

                    if (orientation == Orientation.LEFT && rectangle.x != 0) {
                        continue;
                    }

                    Vector2 position, size;
                    if (orientation == Orientation.DOWN || orientation == Orientation.UP) {
                        position = orientation.moveCoord(new Vector2(0, 0), 16);

                        size = new Vector2(-16, 0);
                    } else {
                        position = new Vector2(0, 0);

                        size = new Vector2(0, -16);
                    }

                    Rectangle rectangle1 = new Rectangle(
                            rectangle.x + position.x,
                            rectangle.y + position.y,
                            rectangle.width + size.x,
                            rectangle.height + size.y);

                    if (playerRectangle.overlaps(rectangle1)) {
                        //The Player enters a door
                        return orientation;
                    }
                }
            }

        }
        return null;
    }

    private void diminishHealth(Body body, float damage) {
        // Check if body extends from Entity
        if (body.getUserData() instanceof Entity) {

            Entity entity = (Entity) body.getUserData();
            // Check if body has cooldown pending
            if (System.currentTimeMillis() - entity.getLastSustainedDamage() >= entity.getInvincibilityCooldown()) {
                entity.setHealth(-damage);
                if (body.getUserData() instanceof Player) {
                    SoundHandler.getInstance().playSound(SoundHandler.Sound.HIT);
                }
                if (entity.getHealth() <= 0.0) {
                    entity.setDead(true);
                }
                entity.setLastSustainedDamage(System.currentTimeMillis());
            }

        }
    }

    private void removeBulletFixture(Bullet bullet) {
        if (BindingOfNewton.getInstance().level.getCurrentRoom().getBullets().contains(bullet)) {
            bullet.setRemove(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() != null && bodyB.getUserData().equals("fire")) {
            isContactFire = false;
        } else if (bodyB.getUserData() instanceof Player && bodyA.getUserData() != null && bodyA.getUserData().equals("fire")) {
            isContactFire = false;
        }


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public void contactWithFire() {
        if (isContactFire) {
            contactWithFire(level.getCurrentRoom().getPlayer().getBody());
        }
    }
}
