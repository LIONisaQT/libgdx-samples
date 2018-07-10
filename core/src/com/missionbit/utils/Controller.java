package com.missionbit.utils;

import com.badlogic.gdx.Gdx;
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

/*
Note to self:
To use InputListener, would need a way to reference the pointer that initiated the button to
correctly implement touchUp.
 */

public class Controller {
    private Array<Image> buttons;
    private Rectangle leftHitbox, rightHitbox;
    private Circle jumpHitbox, attackHitbox;
    private boolean leftPressed, rightPressed, jumpPressed, attackPressed;

    public Controller() {
        buttons = new Array<Image>();

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
    }

    public void update(OrthographicCamera camera) {
        for (int i = 0; i < 5; i++) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0);
            camera.unproject(touchPos);
            if (leftHitbox.contains(touchPos.x, touchPos.y)) {
                leftPressed = Gdx.input.isTouched(i);
                rightPressed = false;
            } else if (rightHitbox.contains(touchPos.x, touchPos.y)) {
                rightPressed = Gdx.input.isTouched(i);
                leftPressed = false;
            } else if (jumpHitbox.contains(touchPos.x, touchPos.y)) {
                jumpPressed = Gdx.input.isTouched(i);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Image i : buttons) {
            i.draw(batch, 0.8f);
        }
    }

    public void drawDebug(ShapeRenderer sr) {
        sr.rect(leftHitbox.x, leftHitbox.y, leftHitbox.width, leftHitbox.height);
        sr.rect(rightHitbox.x, rightHitbox.y, rightHitbox.width, rightHitbox.height);
        sr.circle(jumpHitbox.x, jumpHitbox.y, jumpHitbox.radius);
        sr.circle(attackHitbox.x, attackHitbox.y, attackHitbox.radius);
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isJumpPressed() {
        return jumpPressed;
    }

    public boolean isAttackPressed() {
        return attackPressed;
    }
}
