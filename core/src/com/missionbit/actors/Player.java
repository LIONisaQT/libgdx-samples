package com.missionbit.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.missionbit.LibGDXSamples;
import com.missionbit.states.State;

public class Player {
    private static final int GRAVITY = -15; // Gravity constant
    private final State state;              // Reference to the in-game state

    private Texture texture;
    private Vector2 position, velocity;
    private Rectangle bounds, hitbox;
    private float moveSpeed, jumpHeight;
    private Animation anim;
    private boolean faceRight, needFlip, isAttacking;
    private int numJumps;

    public Player(int x, int y, State state) {
        this.state = state;
        texture = new Texture("textures/textureregions/run_cycle.png");

        moveSpeed = 150;
        jumpHeight = 300;
        numJumps = 1;

        anim = new Animation(new TextureRegion(texture), 8, 0.7f, 3, 3);

        velocity = new Vector2(0, 0);
        position = new Vector2(x, y);

        bounds = new Rectangle(position.x, position.y, getTexture().getRegionWidth(), getTexture().getRegionHeight());
        hitbox = new Rectangle(position.x, position.y, 100, 60);

        faceRight = true;
        needFlip = false;
        isAttacking = false;
    }

    public void update(float dt) {
        if (numJumps == 0 && position.y == 0) {
            numJumps++;
        }

        // Add gravity as long as we're not touching the bottom of the screen
        if (position.y > 0) {
            velocity.add(0, GRAVITY);
        }

        // Animate walk animation if our x velocity is anything but 0
        if (velocity.x != 0) {
            anim.update(dt);
        }

        velocity.scl(dt); // Scale velocity to frame rate
        position.add(velocity); // Add scaled velocity to position
        velocity.scl(1 / dt); // Un-scale velocity

        // Ensure player never leaves bottom and side bounds
        if (position.y < 0) { position.y = 0; }
        if (position.x < 0) { position.x = 0; }
        if (position.x + bounds.getWidth() > LibGDXSamples.WIDTH) { position.x = LibGDXSamples.WIDTH - bounds.getWidth(); }

        bounds.setPosition(position.x, position.y); // Update bounds according to position
    }

    public void jump() {
        if (numJumps > 0) {
            velocity.y = jumpHeight;
            numJumps--;
        }

    }

    public void moveLeft() {
        if (faceRight) { anim.flipFrames(); } // Check if we need to flip the animation
        faceRight = false;
        velocity.x = -moveSpeed;
    }

    public void moveRight() {
        if (!faceRight) { anim.flipFrames(); } // Check if we need to flip the animation
        faceRight = true;
        velocity.x = moveSpeed;
    }

    public void resetAnim() {
        velocity.set(0, velocity.y);
        anim.setFrame(0);
    }

    public TextureRegion getTexture() { return anim.getFrame(); }

    public Rectangle getBounds() { return bounds; }

    public void dispose() { texture.dispose(); }
}
