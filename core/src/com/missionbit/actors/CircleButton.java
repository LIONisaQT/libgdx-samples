package com.missionbit.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class CircleButton extends Button {
    private Circle bounds;

    public CircleButton(int x, int y, String path) {
        image = new Sprite(new Texture(path));
        bounds = new Circle(x, y,image.getWidth() / 2);
        image.setPosition(bounds.x - bounds.radius, bounds.y - bounds.radius);
    }

    @Override
    public boolean checkPressed(Vector3 tap) {
        return bounds.contains(tap.x, tap.y);
    }

    @Override
    public void drawDebug(ShapeRenderer sr) {
        sr.circle(bounds.x, bounds.y, bounds.radius);
    }
}
