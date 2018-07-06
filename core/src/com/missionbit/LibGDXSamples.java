package com.missionbit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.missionbit.states.MainMenu;

public class LibGDXSamples extends Game {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	public static final String TITLE = "LibGDX Sample";
	
	@Override
	public void create () {
        Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
        this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
	}
	
	@Override
	public void dispose () {

	}
}
