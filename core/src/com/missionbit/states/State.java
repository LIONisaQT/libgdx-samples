package com.missionbit.states;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.missionbit.LibGDXSamples;

public abstract class State implements Screen {
    final LibGDXSamples game;
    public OrthographicCamera camera;

    State(LibGDXSamples game) {
        this.game = game;
        camera = new OrthographicCamera(LibGDXSamples.WIDTH, LibGDXSamples.HEIGHT);
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        drawGame();
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

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
