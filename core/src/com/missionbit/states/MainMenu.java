package com.missionbit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.missionbit.LibGDXSamples;

public class MainMenu extends State {
    public MainMenu(final LibGDXSamples game) {
        super(game);
    }

    @Override
    void update(float dt) {
        if (Gdx.input.justTouched()) {

            /*
            If you call dispose after setScreen() the game WILL crash. This is because a new
            instance of the next State will be created, where it initializes a couple things,
            such as its SpriteBatch and BitmapFont. However, we're initialing a SpriteBatch
            while we have still have one running in this State! Calling dispose() first will
            change the order of what instructions happen, preventing this fatal error.
             */
            dispose();
            game.setScreen(new InGame(game));
        }
    }

    @Override
    void drawGame(float dt) {
        batch.begin();
        font.draw(batch, this.getClass().toString(), 0, LibGDXSamples.HEIGHT);
        batch.end();

        if (DEBUG) {
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            sr.end();
        }
    }
}
