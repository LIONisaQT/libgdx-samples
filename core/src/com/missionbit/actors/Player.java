package com.missionbit.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.missionbit.LibGDXSamples;
import com.missionbit.states.State;

public class Player {
    private static final int GRAVITY = -15; // Gravity constant
    private final State state;              // Reference to the in-game state

    private Texture texture;
    private Vector2 position, velocity;
    private Rectangle bounds;
    private float moveSpeed, jumpHeight;
    private Animation anim;
    private boolean faceRight, needFlip;

    public Player(int x, int y, State state) {
        this.state = state;
        texture = new Texture("textures/textureregions/run_cycle.png");
        moveSpeed = 150;
        jumpHeight = 300;
        anim = new Animation(new TextureRegion(texture), 8, 1f, 3, 3);
        velocity = new Vector2(0, 0);
        position = new Vector2(x, y);
        bounds = new Rectangle(position.x, position.y, getTexture().getRegionWidth(), getTexture().getRegionHeight());
        faceRight = true;
    }

    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            // Transforms screen coordinates to game world coordinates
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            state.camera.unproject(touchPos);

            // Make player move towards tap position
            if (touchPos.x > position.x + anim.getFrame().getRegionWidth() / 2) {
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
            velocity.set(0, velocity.y);    // Make sure to turn off our x velocity
            anim.setFrame(0);               // Set our frame to idle
        }

        if (position.y > 0) {
            velocity.add(0, GRAVITY);
        }
        velocity.scl(dt);
        position.add(velocity);
        velocity.scl(1 / dt);

        // Ensure player next leaves bottom and side bounds
        if (position.y < 0) { position.y = 0; }
        if (position.x < 0) { position.x = 0; }
        if (position.x + bounds.getWidth() > LibGDXSamples.WIDTH) { position.x = LibGDXSamples.WIDTH - bounds.getWidth(); }

        bounds.setPosition(position.x, position.y);
    }

    public void jump() { velocity.y = jumpHeight; }

    public TextureRegion getTexture() { return anim.getFrame(); }

    public Rectangle getBounds() { return bounds; }

    public void dispose() { texture.dispose(); }
}
