package com.missionbit.states;

import com.badlogic.gdx.Gdx;
import com.missionbit.LibGDXSamples;

class InGame extends State {

    InGame(LibGDXSamples game) {
        super(game);
    }

    @Override
    void update() {
        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenu(game));
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
