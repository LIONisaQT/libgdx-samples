package com.missionbit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.missionbit.LibGDXSamples;
import com.missionbit.actors.Player;
import com.missionbit.utils.Controller;

import java.util.Random;

public class InGame extends State {
    private Player player;
    private Array<Player> enemies;
    private Controller controller;

    private ParticleEffectPool effectPool;                  // Pool of inactive particle effects
    private Array<ParticleEffectPool.PooledEffect> effects; // Array of active particle effects

    InGame(LibGDXSamples game) {
        super(game);
        player = new Player(50, 100, this);
        enemies = new Array<Player>();
        Random random = new Random();
        for (int i = 0; i < 1; i++) {
            enemies.add(new Player(random.nextInt(LibGDXSamples.WIDTH), random.nextInt(LibGDXSamples.HEIGHT), this));
        }
        controller = new Controller(camera);

        // Initialize effects array and an instance of the particle effect
        effects = new Array<ParticleEffectPool.PooledEffect>();
        ParticleEffect explosion = new ParticleEffect();

        // You have to load the particle effect, and the image used for the effect
        explosion.load(Gdx.files.internal("effects/explosion.p"),Gdx.files.internal("effects/"));
        explosion.scaleEffect(0.1f); // It's kinda big at 800x480, so I had to scale it down

        // Initializes the pool with one particle
        effectPool = new ParticleEffectPool(explosion, 1, 100);
    }

    @Override
    void update(float dt) {
        handleInput();

        // Updates particle effects, and removes it from the active array when finished
        for (ParticleEffectPool.PooledEffect p : effects) {
            p.update(dt);
            if (p.isComplete()) {
                p.free();
                effects.removeValue(p, true); // Bother Ryan if you want to know why we use true in here
            }
        }

        player.update(dt);

        for (Player p : enemies) {
            p.update(dt);
            if (player.getHitbox().overlaps(p.getBounds()) && player.isAttacking()) {
                p.reduceHp(player.getAtkPower());
                System.out.println(p.getHp());
                if (p.getHp() == 0) {
                    enemies.removeValue(p, true);
                }
            }
        }
    }

    // Anything involving input and the controller goes here
    private void handleInput() {
        // Movement
        if (controller.isLeftPressed()) {player.moveLeft();}
        else if (controller.isRightPressed()) {player.moveRight();}
        else {player.resetAnim();}

        // Jumping and attacking
        if (controller.isJumpPressed()) {player.jump();}
        if (controller.isAttackPressed()) {player.attack();}

        // Spawn particle effects for the lulz
        for (int i = 0; i < 5; i++) {
            if (Gdx.input.isTouched(i)) {
                // Grabs particle effect from pool (or creates a new one if one isn't free)
                ParticleEffectPool.PooledEffect expl = effectPool.obtain();

                // Sets position of the explosion and adds it to the active array for processing
                Vector3 touchPos = new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                camera.unproject(touchPos);
                expl.setPosition(touchPos.x, touchPos.y);
                effects.add(expl);
            }
        }
    }

    @Override
    void drawGame(float dt) {
        batch.begin();
        font.draw(batch, this.getClass().toString(), 0, LibGDXSamples.HEIGHT);
        batch.draw(player.getTexture(dt), player.getPosition().x, player.getPosition().y);
        for (Player p : enemies) {
            batch.draw(p.getTexture(dt), p.getPosition().x, p.getPosition().y);
        }
        controller.draw(batch);
        for(ParticleEffectPool.PooledEffect p : effects) {p.draw(batch);}
        batch.end();

        if (DEBUG) {
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            controller.drawDebug(sr);
            player.drawDebug(sr);
            for (Player p : enemies) {
                p.drawDebug(sr);
            }
            sr.end();
        }
    }

    public void dispose() {
        super.dispose();
        player.dispose();

        // Reset all effects
        for (ParticleEffectPool.PooledEffect e : effects) {
            e.free(); // Frees effect back to pool
        }
        effects.clear(); // Empty the active effects array
    }
}
