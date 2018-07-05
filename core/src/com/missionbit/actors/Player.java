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
    }

    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            // Transforms screen coordinates to game world coordinates
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touchPos);

            // Make player move towards tap position
            if (touchPos.x > getX() + anim.getFrame().getRegionWidth() / 2) {
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
//        velocity.add(0, GRAVITY);
        velocity.scl(dt);
        moveBy(velocity.x, velocity.y);
        velocity.scl(1 / dt);
    }

    public TextureRegion getTexture() {
        return anim.getFrame();
    }

    public void dispose() {
        texture.dispose();
    }
}
