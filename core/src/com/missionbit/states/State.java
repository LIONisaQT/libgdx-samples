package com.missionbit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.missionbit.LibGDXSamples;

public abstract class State implements Screen {
    final LibGDXSamples game;
    final boolean DEBUG = true;
    OrthographicCamera camera;

    BitmapFont font;
    SpriteBatch batch;
    ShapeRenderer sr;
    Music music;


    State(LibGDXSamples game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, LibGDXSamples.WIDTH, LibGDXSamples.HEIGHT);

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        batch = new SpriteBatch();
        sr = new ShapeRenderer();

        music = Gdx.audio.newMusic(Gdx.files.internal("music/bgm1.mp3"));
        music.setLooping(true);
        music.setVolume(1f);
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        update(delta);
        drawGame(delta);
    }

    abstract void update(float dt);

    abstract void drawGame(float dt);

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        music.dispose();
    }

    @Override
    public void show() {

    }

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
}
