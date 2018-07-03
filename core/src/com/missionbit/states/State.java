package com.missionbit.states;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.missionbit.LibGDXSamples;

public abstract class State implements Screen {
    final LibGDXSamples game;
    public OrthographicCamera camera;

    State(LibGDXSamples game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, LibGDXSamples.WIDTH, LibGDXSamples.HEIGHT);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        update(delta);
        drawGame();
    }

    abstract void update(float dt);

    abstract void drawGame();

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
