package com.missionbit.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.missionbit.LibGDXSamples;
import com.missionbit.states.State;

public class Player {
    private static final int GRAVITY = -15; // Gravity constant
    private final State state;              // Reference to the in-game state

    private Vector2 position, offset, velocity;
    private Rectangle bounds, hitbox;
    private float moveSpeed, jumpHeight;
    private int numJumps;

    private Texture runSheet, attackSheet, jumpSheet;
    private enum AnimState { JUMPING, ATTACKING, RUNNING, IDLING};
    private AnimState currentState, previousState;
    private Animation<TextureRegion> runAnim, atkAnim, jumpAnim, idleAnim;
    private float stateTime;
    private boolean faceRight, isAttacking;

    public Player(int x, int y, State state) {
        this.state = state;

        currentState = AnimState.RUNNING;
        previousState = currentState;

        // Initialize all spritesheets
        runSheet = new Texture("textures/textureregions/red_run.png");
        attackSheet = new Texture("textures/textureregions/red_attack.png");
        jumpSheet = new Texture("textures/textureregions/red_jump.png");

        // Run animation
        // Split spritesheet into a 2D array
        TextureRegion[][] tmp1 = TextureRegion.split(runSheet, runSheet.getWidth() / 3, runSheet.getHeight() / 4);

        // A temporary Array for holding frames
        Array<TextureRegion> runFrames = new Array<TextureRegion>();

        // A counter for the number of frames in an animation
        int count = 0;

        // For loop to add frames from the split TextureRegion into the Array
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (count < 10) {
                    runFrames.add(tmp1[i][j]); // Fill in Array with frames
                    count++;
                }
            }
        }
        runAnim = new Animation<TextureRegion>(0.125f, runFrames); // Set Animation to use Array of frames

        // Idle animation (just uses the first frame of the run animation)
        Array<TextureRegion> idleFrames = new Array<TextureRegion>();
        idleFrames.add(tmp1[0][0]);
        idleAnim = new Animation<TextureRegion>(0f, idleFrames);

        // Attack animation
        TextureRegion[][] tmp2 = TextureRegion.split(attackSheet, attackSheet.getWidth() / 3, attackSheet.getHeight() / 3);
        Array<TextureRegion> atkFrames = new Array<TextureRegion>();
        count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (count < 8) {
                    atkFrames.add(tmp2[i][j]);
                    count++;
                }
            }
        }
        atkAnim = new Animation<TextureRegion>(0.125f, atkFrames);

        // Jump animation (one frame animation)
        jumpAnim = new Animation<TextureRegion>(0f, new TextureRegion(jumpSheet));

        stateTime = 0f;

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

        hitbox = new Rectangle(
                position.x + offset.x,
                position.y + offset.y,
                130,
                150);
    }

    public void update(float dt) {
        // Add gravity as long as we're not touching the bottom of the screen
        if (position.y > -offset.y) {
            velocity.add(0, GRAVITY);
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
        hitbox.setPosition(position.x + offset.x, position.y + offset.y);

        stateTime += Gdx.graphics.getDeltaTime();

        // Shitty animation state machine
        if (isAttacking) {
            currentState = AnimState.ATTACKING;
            if (atkAnim.isAnimationFinished(stateTime)) {
                isAttacking = false;
            }
        } else if (position.y != -offset.y) {
            currentState = AnimState.JUMPING;
        } else if (velocity.x != 0) {
            currentState = AnimState.RUNNING;
        } else {
            currentState = AnimState.IDLING;
        }
    }

    public void jump() {
        if (numJumps > 0) {
            velocity.y = jumpHeight;
            numJumps--;
        }
    }

    public void attack() {
        if (!isAttacking) {
            isAttacking = true;
        }
    }

    public void moveLeft() {
        faceRight = false;
        velocity.x = -moveSpeed;
    }

    public void moveRight() {
        faceRight = true;
        velocity.x = moveSpeed;
    }

    public void resetAnim() {
        velocity.set(0, velocity.y);
    }

    public void drawDebug(ShapeRenderer sr) {
        sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        sr.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    public TextureRegion getTexture(float dt) {
        TextureRegion region;
        switch(currentState) {
            case ATTACKING:
                region = atkAnim.getKeyFrame(stateTime, false);
                break;
            case JUMPING:
                region = jumpAnim.getKeyFrame(stateTime, false);
                break;
            case RUNNING:
                region = runAnim.getKeyFrame(stateTime, true);
                break;
            case IDLING:
            default:
                region = idleAnim.getKeyFrame(stateTime, true);
                break;
        }

        if ((velocity.x < 0 || !faceRight) && !region.isFlipX()) {
            region.flip(true, false);
            faceRight = false;
        } else if ((velocity.x > 0 || faceRight) && region.isFlipX()) {
            region.flip(true, false);
            faceRight = true;
        }

        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    public Vector2 getPosition() {return position;}

    public Rectangle getBounds() {return bounds;}

    public void dispose() {
        runSheet.dispose();
        attackSheet.dispose();
        jumpSheet.dispose();
    }
}
