package com.missionbit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionbit.states.MainMenu;

public class LibGDXSamples extends Game {
    public BitmapFont font;
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "LibGDX Sample";
	public SpriteBatch batch;
	private Music music;
	
	@Override
	public void create () {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
		batch = new SpriteBatch();
        Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
        music = Gdx.audio.newMusic(Gdx.files.internal("music/bgm1.mp3"));
        music.setLooping(true);
        music.setVolume(1f);
        music.play();
        this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        font.dispose();
        music.dispose();
	}
}
