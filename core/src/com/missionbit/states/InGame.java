package com.missionbit.states;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.missionbit.LibGDXSamples;
import com.missionbit.actors.Player;

public class InGame extends State {
    private Player player;

    //declare tilemap and renderer
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private MapObjects mapObjects;

    private ShapeRenderer shapeRenderer;

    InGame(LibGDXSamples game) {
        super(game);
        player = new Player(50, 100, this);

        tiledMap = new TmxMapLoader().load("maps/TiledDemo.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        mapObjects = tiledMap.getLayers().get("Collision").getObjects();

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    void update(float dt) {
        player.update(dt);
        if (player.getX() > LibGDXSamples.WIDTH || player.getX() + player.getWidth() < 0) {
            game.setScreen(new MainMenu(game));
            dispose();
        }

        if (checkCollision()) {
            System.out.println("Collided");
        }
    }

    public boolean checkCollision () {
        for (PolygonMapObject obj : mapObjects.getByType(PolygonMapObject.class)) {
            if (Intersector.overlapConvexPolygons(obj.getPolygon(), player.getPolyBounds())) {
                return true;
            }
        }
        return false;
    }

    @Override
    void drawGame() {
        //draw map
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
