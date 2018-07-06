package com.missionbit.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.missionbit.states.InGame;
import com.missionbit.states.State;

public class Player extends Actor {
    private Texture texture;
    private Vector2 force;
    private Animation anim;
    private boolean faceRight, needFlip;
    private Vector3 tap;                    // Holds tap position
    private final InGame game;              // Reference to the in-game state

    private BodyDef playerDef;
    public Body playerBody;
    private PolygonShape rect;
    private FixtureDef fixtureDef;

    public Player(int x, int y, InGame game) {
        setDebug(true);
        this.game = game;
        texture = new Texture("texture-regions/run_cycle.png");
        force = new Vector2(0.5f, 0);
        anim = new Animation(new TextureRegion(texture), 8, 1f, 3, 3);
        setBounds(x, y, texture.getWidth() /  3, texture.getHeight() / 3);
        faceRight = true;
        tap = new Vector3();

        //create player def and fixture
        playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;
        playerDef.position.set(x, y);
        setPosition(playerDef.position.x, playerDef.position.y);


        playerBody = game.world.createBody(playerDef);
        playerBody.setFixedRotation(true);

        rect = new PolygonShape();
        rect.set(new float[]{(getWidth() / 3) * State.PIXEL_TO_METER, 0, (getWidth() / 3 + getWidth() / 2 - 10) * State.PIXEL_TO_METER, 0,
                (getWidth() / 3 + getWidth() / 2 - 10) * State.PIXEL_TO_METER, getHeight() * State.PIXEL_TO_METER,
                (getWidth() / 3) * State.PIXEL_TO_METER, getHeight() * State.PIXEL_TO_METER});

        fixtureDef = new FixtureDef();
        fixtureDef.shape = rect;
        fixtureDef.density = 0.9f;
        fixtureDef.friction = 1f;

        playerBody.createFixture(fixtureDef);

        rect.dispose();
    }

    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            if (getTapPosition().x > playerBody.getPosition().x + (anim.getFrame().getRegionWidth() / 2 * State.PIXEL_TO_METER)) {
                if (!faceRight) { needFlip = true; } // Check if we need to flip the animation
                faceRight = true;
                playerBody.applyLinearImpulse(force, new Vector2(getX(), getY()), true);
            } else {
                if (faceRight) { needFlip = true; } // Check if we need to flip the animation
                faceRight = false;
                playerBody.applyLinearImpulse(-force.x, force.y, getX(), getY(), true);
            }

            // Flips animation frames once if we need to
            if (needFlip) {
                anim.flipFrames();
                needFlip = false;
            }

            anim.update(dt);
        } else {
            anim.setFrame(0);
        }
        setPosition(playerBody.getPosition().x, playerBody.getPosition().y);
    }

    /*
    By default, LibGDX's camera origin is top-left. When textures are drawn, the origin is the
    bottom-left, because textures are drawn in world space. To fix this, we simply get the positions
    of the tap according to LibGDX's camera, and then use unproject() to translate the point in
    screen coordinates to world space.
     */
    private Vector3 getTapPosition() {
        tap.set(Gdx.input.getX() , Gdx.input.getY() , 0);
        return game.camera.unproject(tap);
    }

    public TextureRegion getTexture() {
        return anim.getFrame();
    }

    public void dispose() {
        texture.dispose();
    }
}
