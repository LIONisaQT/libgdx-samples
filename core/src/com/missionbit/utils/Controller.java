package com.missionbit.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.missionbit.LibGDXSamples;

import java.util.HashMap;
import java.util.Map;

public class Controller implements InputProcessor {
    private OrthographicCamera camera;  // We need this to unproject our tap coordinates
    private Array<Image> buttons;       // Used for convenience for drawing

    // Different hitboxes depending on what buttons you have
    private Rectangle leftHitbox, rightHitbox;
    private Circle jumpHitbox, attackHitbox;

    private boolean leftPressed, rightPressed, jumpPressed, attackPressed; // Button presses

    // Simple node that holds information about touch
    class TouchInfo {
        float touchX = 0;
        float touchY = 0;
    }

    // Map where our fingers are the keys, updated by InputProcessor methods
    private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();

    public Controller(OrthographicCamera camera) {
        this.camera = camera;
        buttons = new Array<Image>();

        // Set all our buttons, positions, and bounds
        Image left = new Image(new Texture("textures/buttons/left-arrow.png"));
        left.setPosition(0, 0);
        leftHitbox = new Rectangle(left.getX(), left.getY(), left.getWidth(), left.getHeight());
        buttons.add(left);

        Image right = new Image(new Texture("textures/buttons/right-arrow.png"));
        right.setPosition(left.getWidth() + 4, 0);
        rightHitbox = new Rectangle(right.getX(), right.getY(), right.getWidth(), right.getHeight());
        buttons.add(right);

        Image jump = new Image(new Texture("textures/buttons/jump-button.png"));
        jump.setPosition(LibGDXSamples.WIDTH - jump.getWidth(), 0);
        jumpHitbox = new Circle(jump.getX() + jump.getWidth() / 2, jump.getY() + jump.getHeight() / 2, jump.getWidth() / 2);
        buttons.add(jump);

        Image attack = new Image(new Texture("textures/buttons/attack-button.png"));
        attack.setPosition(LibGDXSamples.WIDTH - attack.getWidth(), jump.getHeight() + 4);
        attackHitbox = new Circle(attack.getX() + attack.getWidth() / 2, attack.getY() + attack.getHeight() / 2, attack.getWidth() / 2);
        buttons.add(attack);

        Gdx.input.setInputProcessor(this); // Let game know that controller can receive input

        // Fills in map with empty key-value pairs
        for (int i = 0; i < 5; i++) {
            touches.put(i, new TouchInfo());
        }
    }

    public void draw(SpriteBatch batch) {
        for (Image i : buttons) { i.draw(batch, 0.8f); }
    }

    public void drawDebug(ShapeRenderer sr) {
        sr.rect(leftHitbox.x, leftHitbox.y, leftHitbox.width, leftHitbox.height);
        sr.rect(rightHitbox.x, rightHitbox.y, rightHitbox.width, rightHitbox.height);
        sr.circle(jumpHitbox.x, jumpHitbox.y, jumpHitbox.radius);
        sr.circle(attackHitbox.x, attackHitbox.y, attackHitbox.radius);
    }

    // Getter methods for the game to use
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isJumpPressed() { return jumpPressed; }
    public boolean isAttackPressed() { return attackPressed; }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    // Helper method to check collision between touches and buttons
    private void checkCollisions(TouchInfo t) {
        if (leftHitbox.contains(t.touchX, t.touchY)) {
            leftPressed = true;
            rightPressed = false;
        } else if (rightHitbox.contains(t.touchX, t.touchY)) {
            rightPressed = true;
            leftPressed = false;
        } else if (jumpHitbox.contains(t.touchX, t.touchY)) {
            jumpPressed = true;
        } else if (attackHitbox.contains(t.touchX, t.touchY)) {
            attackPressed = true;
        }
    }


    // Called once as soon as the controller detects a finger input
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);

        if (pointer < 5) {
            touches.get(pointer).touchX = touchPos.x;
            touches.get(pointer).touchY = touchPos.y;

            checkCollisions(touches.get(pointer));
        }
        return true;
    }

    // Called once as soon as the controller detects a finger release
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);

        if (pointer < 5) {
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;

            // Don't have to check for collisions because the button will be unpressed on touchUp()
            leftPressed = false;
            rightPressed = false;
            jumpPressed = false;
            attackPressed = false;
        }
        return true;
    }


    // Called if touchDown() has been called before and controller sees a finger moving around
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);

        if (pointer < 5) {
            touches.get(pointer).touchX = touchPos.x;
            touches.get(pointer).touchY = touchPos.y;

            checkCollisions(touches.get(pointer));
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
