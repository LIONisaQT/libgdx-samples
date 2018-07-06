package com.missionbit.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class RectButton extends Button {
    private Rectangle bounds;

    public RectButton(int x, int y, String path) {
        image = new Sprite(new Texture(path));
        bounds = new Rectangle(x, y, image.getWidth(), image.getHeight());
        image.setPosition(bounds.x, bounds.y);
    }

    public void flipSprite(boolean x, boolean y) { image.flip(x, y); }

    public void setPosition(int x, int y) { bounds.setPosition(x, y); }

    @Override
    public boolean checkPressed(Vector3 tap) {
        return bounds.contains(tap.x, tap.y);
    }

    @Override
    public void drawDebug(ShapeRenderer sr) {
        sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
