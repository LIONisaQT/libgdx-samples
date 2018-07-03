package com.missionbit.states;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.missionbit.LibGDXSamples;
import com.missionbit.actors.Player;

public class InGame extends State {
    private Player player;

    //required to render tiledmap and get map objects
    public static TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    public static MapObjects mapObjects;

    InGame(LibGDXSamples game) {
        super(game);
        player = new Player(50, 100, this);
        // load tilemap and set renderer
        tiledMap = new TmxMapLoader().load("TiledDemo.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        mapObjects = tiledMap.getLayers().get("Collision").getObjects();
    }

    @Override
    void update(float dt) {
        player.update(dt);
        if (player.getX() > LibGDXSamples.WIDTH || player.getX() + player.getWidth() < 0) {
            game.setScreen(new MainMenu(game));
            dispose();
        }
    }

    @Override
    void drawGame() {
        // sets view to camera for renderer
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        game.batch.begin();
        game.font.draw(game.batch, this.getClass().toString(), 0, LibGDXSamples.HEIGHT);
        game.batch.draw(player.getTexture(), player.getX(), player.getY());
        game.batch.end();
    }

    public void dispose() {
        player.dispose();
    }
}
