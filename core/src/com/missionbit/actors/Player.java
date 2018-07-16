package com.missionbit.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.missionbit.LibGDXSamples;
import com.missionbit.states.State;

public class Player {
    private static final int GRAVITY = -15; // Gravity constant
    private final State state;              // Reference to the in-game state

    private Texture runSheet, attackSheet, jumpSheet;
    private Vector2 position, offset, velocity;
    private Rectangle bounds, hitbox;
    private float moveSpeed, jumpHeight;
    private Animation anim, run, attack, jump;
    private boolean faceRight, isAttacking;
    private int numJumps;

    public Player(int x, int y, State state) {
        this.state = state;

        // Initialize all spritesheets
        runSheet = new Texture("textures/textureregions/red_run.png");
        attackSheet = new Texture("textures/textureregions/red_attack.png");
        jumpSheet = new Texture("textures/textureregions/red_jump.png");

        // Initial all animations
        run = new Animation(new TextureRegion(runSheet), 10, 0.7f, 4, 3, true);
        attack = new Animation(new TextureRegion(attackSheet), 8, 5f, 3, 3, false);
        jump = new Animation(new TextureRegion(jumpSheet), 1, 1f, 1, 1, false);
        anim = run;

        // Player variables
        moveSpeed = 150;
        jumpHeight = 300;
        numJumps = 1;
        faceRight = true;
        isAttacking = false;

        position = new Vector2(x, y);
        offset = new Vector2(100, 70);
        velocity = new Vector2(0, 0);

        bounds = new Rectangle(
                position.x + offset.x,
                position.y + offset.y,
                56,
                120
        );

        hitbox = new Rectangle(position.x, position.y, 100, 60);
    }

    public void update(float dt) {
        // Add gravity as long as we're not touching the bottom of the screen
        if (position.y > -offset.y) {
            velocity.add(0, GRAVITY);
            anim = jump;
        }

        velocity.scl(dt); // Scale velocity to frame rate
        position.add(velocity); // Add scaled velocity to position
        velocity.scl(1 / dt); // Un-scale velocity

        // Ensure player never leaves bottom and side bounds
        if (position.y + offset.y < 0) {
            position.y = -offset.y;
            if (numJumps == 0) {numJumps++;}
        }
        if (position.x + offset.x < 0) {position.x = -offset.x;}
        if (position.x + offset.x + bounds.width > LibGDXSamples.WIDTH) {position.x = LibGDXSamples.WIDTH - bounds.getWidth() - offset.x;}

        // Update bounds according to position
        bounds.setPosition(position.x + offset.x, position.y + offset.y);
        hitbox.setPosition(position.x, position.y);

        // Shitty animation state machine
        if (anim == run) {
            // Only update our run animation if we're moving, otherwise stay at frame 0 for pseudo idle state
            if (velocity.x != 0) {
                anim.update(dt);
            } else {
                anim.setFrame(0);
            }
        } else {
            if (anim == jump) {
                // Set our animation back to run when we touch the ground and our current state is jump (i.e. in the air)
                if (position.y == -offset.y) {
                    anim = run;
                }
                anim.update(dt);
            } else if (anim == attack) {
                anim.update(dt);
            }
        }
    }

    public void jump() {
        if (numJumps > 0) {
            velocity.y = jumpHeight;
            numJumps--;
            anim = jump;
        }
    }

    public void attack() {
        if (!isAttacking) {
            isAttacking = true;
            anim = attack;
        }
    }

    public void moveLeft() {
        if (faceRight) {anim.flipFrames();} // Check if we need to flip the animation
        faceRight = false;
        velocity.x = -moveSpeed;
    }

    public void moveRight() {
        if (!faceRight) {anim.flipFrames();} // Check if we need to flip the animation
        faceRight = true;
        velocity.x = moveSpeed;
    }

    public void resetAnim() {
        velocity.set(0, velocity.y);
        anim.setFrame(0);
    }

    public void drawDebug(ShapeRenderer sr) {
        sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        sr.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    public TextureRegion getTexture() {return anim.getFrame();}

    public Vector2 getPosition() {return position;}

    public Rectangle getBounds() {return bounds;}

    public void dispose() {
        runSheet.dispose();
        attackSheet.dispose();
        jumpSheet.dispose();
    }
}
