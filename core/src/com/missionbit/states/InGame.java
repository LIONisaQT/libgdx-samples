package com.missionbit.states;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.missionbit.LibGDXSamples;
import com.missionbit.actors.Player;

public class InGame extends State {
    private Player player;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;
    public World world;
    private Box2DDebugRenderer debugRenderer;

//    private BodyDef playerDef;
//    private Body playerBody;
//    private PolygonShape rect;
//    private FixtureDef fixtureDef;
//    private Fixture fixture;

    private BodyDef floorDef;
    PolygonShape floorShape;
    private Array<Body> floors;
    private int counter = 0;

    InGame(LibGDXSamples game) {
        super(game);

        //initialize box2d and world
        Box2D.init();
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        player = new Player(1, 4, this);
        tiledMap = new TmxMapLoader().load("maps/TiledDemo.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, State.PIXEL_TO_METER);

        floors = new Array<Body>();

//        //create player def and fixture
//        playerDef = new BodyDef();
//        playerDef.type = BodyDef.BodyType.DynamicBody;
//        playerDef.position.set(player.getX(), player.getY() + 50);
//
//
//        playerBody = world.createBody(playerDef);
//
//        rect = new PolygonShape();
//        rect.set(new float[]{player.getWidth() / 3, 0, player.getWidth() / 3 + player.getWidth() / 2 - 10, 0,
//                player.getWidth() / 3 + player.getWidth() / 2 - 10, player.getHeight(), player.getWidth() / 3, player.getHeight()});
//
//        fixtureDef = new FixtureDef();
//        fixtureDef.shape = rect;
//        fixtureDef.density = 0.5f;
//        fixtureDef.friction = 0.4f;
//
//        fixture = playerBody.createFixture(fixtureDef);
//
//        rect.dispose();


        //create floor def and fixture
        floorDef = new BodyDef();
        floorShape = new PolygonShape();

        for (PolygonMapObject obj : tiledMap.getLayers().get("Collision").getObjects().getByType(PolygonMapObject.class)) {
            floorDef.position.set(obj.getPolygon().getX() * State.PIXEL_TO_METER, obj.getPolygon().getY() * State.PIXEL_TO_METER);
            floors.add(world.createBody(floorDef));
            float[] vertices = obj.getPolygon().getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] * State.PIXEL_TO_METER;
            }
            floorShape.set(vertices);
            floors.get(counter).createFixture(floorShape, 0.0f);
            counter++;
        }

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
        game.batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        game.batch.begin();
//        game.font.draw(game.batch, this.getClass().toString(), 0, LibGDXSamples.HEIGHT * State.PIXEL_TO_METER);
        game.batch.draw(player.getTexture(), player.getX(), player.getY(),
                player.getTexture().getRegionWidth() * State.PIXEL_TO_METER, player.getTexture().getRegionHeight() * State.PIXEL_TO_METER);
        game.batch.end();

        debugRenderer.render(world, camera.combined);
        world.step(1/60f, 6, 2);
    }

    public void dispose() {
        player.dispose();
    }
}
