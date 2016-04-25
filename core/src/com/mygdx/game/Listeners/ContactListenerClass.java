package com.mygdx.game.Listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Screens.MainGameScreen;
import static com.mygdx.game.Constants.*;
/**
 * Created by icanul on 4/22/16.
 */
public class ContactListenerClass implements ContactListener {


    private final MainGameScreen screen;

    public ContactListenerClass(MainGameScreen screen){
        this.screen=screen;
    }


    private boolean areCollided(Contact contact, Object userA, Object userB) {
        Object userDataA = contact.getFixtureA().getUserData();
        Object userDataB = contact.getFixtureB().getUserData();
        //System.out.print(userDataA);
        // This is not in the video! It is a good idea to check that user data is not null.
        // Sometimes you forget to put user data or you get collisions by entities you didn't
        // expect. Not preventing this will probably result in a NullPointerException.
        if (userDataA == null || userDataB == null) {
            return false;
        }

        // Because you never know what is A and what is B, you have to do both checks.
        return (userDataA.equals(userA) && userDataB.equals(userB)) ||
                (userDataA.equals(userB) && userDataB.equals(userA));
    }

    /**
     * This method is executed when a contact has started: when two fixtures just collided.
     */
    @Override
    public void beginContact(Contact contact) {
        // The player has collided with the floor.
        if (areCollided(contact, "Player", "Objeto")) {
            screen.actor.setStatus(WALK);
            screen.actor.setJumping(false);

        }
        if (areCollided(contact, "Player", "Bloque")) {
            screen.actor.setStatus(DOWN);
            screen.actor.setJumping(false);
            screen.actor.coliciono=true;
        }
        // The player has collided with something that hurts.
        if (areCollided(contact, "player", "spike")) {


        }
    }

    /**
     * This method is executed when a contact has finished: two fixtures are no more colliding.
     */
    @Override
    public void endContact(Contact contact) {
        // The player is jumping and it is not touching the floor.
        if (areCollided(contact, "Player", "Objeto")) {
            screen.actor.setStatus(JUMP);
            screen.actor.setJumping(true);
        }
        if (areCollided(contact, "Player", "Bloque")) {
            screen.actor.setStatus(DOWN);
            screen.actor.setJumping(false);
            screen.actor.coliciono=false;
        }
    }

    // Here two lonely methods that I don't use but have to override anyway.
    @Override public void preSolve(Contact contact, Manifold oldManifold) { }
    @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
}
