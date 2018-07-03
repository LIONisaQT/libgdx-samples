package com.missionbit.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.missionbit.states.InGame;

public class Player extends Actor {
    private Texture texture;
    private Vector2 velocity;
    private float moveSpeed;
    private Animation anim;
    private boolean faceRight, needFlip;
    private Vector3 tap;                    // Holds tap position
    private static final int GRAVITY = -15; // Gravity constant
    private final InGame game;              // Reference to the in-game state

    public Player(int x, int y, InGame game) {
        setDebug(true);
        this.game = game;
        texture = new Texture("texture-regions/run_cycle.png");
        moveSpeed = 150;
        anim = new Animation(new TextureRegion(texture), 8, 1f, 3, 3);
        velocity = new Vector2(0, 0);
        setPosition(x, y);
        setBounds(x, y, texture.getWidth() / 3, texture.getHeight() / 3);
        faceRight = true;
        tap = new Vector3();
    }

    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            if (getTapPosition().x > getX() + anim.getFrame().getRegionWidth() / 2) {
                if (!faceRight) { needFlip = true; } // Check if we need to flip the animation
                faceRight = true;
                velocity.set(moveSpeed, velocity.y);
            } else {
                if (faceRight) { needFlip = true; } // Check if we need to flip the animation
                faceRight = false;
                velocity.set(-moveSpeed, velocity.y);
            }

            // Flips animation frames once if we need to
            if (needFlip) {
                anim.flipFrames();
                needFlip = false;
            }

            anim.update(dt);
        } else {
            velocity.set(0, velocity.y);
            anim.setFrame(0);
        }
        velocity.add(0, GRAVITY);
        velocity.scl(dt);
        moveBy(velocity.x, velocity.y);
        velocity.scl(1 / dt);
    }

    /*
    By default, LibGDX's camera origin is top-left. When textures are drawn, the origin is the
    bottom-left, because textures are drawn in world space. To fix this, we simply get the positions
    of the tap according to LibGDX's camera, and then use unproject() to translate the point in
    screen coordinates to world space.
     */
    private Vector3 getTapPosition() {
        tap.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        return game.camera.unproject(tap);
    }

    public TextureRegion getTexture() {
        return anim.getFrame();
    }

    public void dispose() {
        texture.dispose();
    }
}
