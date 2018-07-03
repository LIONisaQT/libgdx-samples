package com.missionbit.states;

import com.badlogic.gdx.Gdx;
import com.missionbit.LibGDXSamples;

public class MainMenu extends State {

    public MainMenu(final LibGDXSamples game) {
        super(game);
    }

    @Override
    void update(float dt) {
        if (Gdx.input.justTouched()) {
            game.setScreen(new InGame(game));
            dispose();
        }
    }

    @Override
    void drawGame() {
        game.batch.begin();
        game.font.draw(game.batch, this.getClass().toString(), 0, LibGDXSamples.HEIGHT);
        game.batch.end();
    }
}
