package com.missionbit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.missionbit.LibGDXSamples;
import com.missionbit.actors.Player;

public class InGame extends State {
    private Player player;

    private ParticleEffectPool effectPool;                  // Pool of particle effects
    private Array<ParticleEffectPool.PooledEffect> effects; // Array of active particle effects
    private ParticleEffect explosion;                       // The actual particle effect

    InGame(LibGDXSamples game) {
        super(game);
        player = new Player(50, 100, this);

        effects = new Array<ParticleEffectPool.PooledEffect>();
        explosion = new ParticleEffect();

        // You have to load the particle effect, and the image used for the effect
        explosion.load(Gdx.files.internal("particle-effects/explosion.p"),Gdx.files.internal("particle-effects/"));
        explosion.scaleEffect(0.5f); // It's kinda big at 480x800, so I had to scale it down

        // Initializes the pool with one particle
        effectPool = new ParticleEffectPool(explosion, 1, 100);
    }

    @Override
    void update(float dt) {
        if (Gdx.input.isTouched()) {
            // Transforms screen coordinates to game world coordinates
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Grabs particle effect from pool (or creates a new one if one isn't free)
            ParticleEffectPool.PooledEffect expl = effectPool.obtain();
            expl.setPosition(touchPos.x, touchPos.y);
            effects.add(expl);
        }

        // Updates particle effects, and removes it from the active array when finished
        for (ParticleEffectPool.PooledEffect p : effects) {
            p.update(dt);
            if (p.isComplete()) {
                p.free();
                effects.removeValue(p, true);
            }
        }

        player.update(dt);

        // The game actually registers taps off screen :O
        if (player.getX() > LibGDXSamples.WIDTH || player.getX() + player.getWidth() < 0) {
            game.setScreen(new MainMenu(game));
            dispose();
        }
    }

    @Override
    void drawGame() {
        game.batch.begin();
        game.font.draw(game.batch, this.getClass().toString(), 0, LibGDXSamples.HEIGHT);
        game.batch.draw(player.getTexture(), player.getX(), player.getY());
        for(ParticleEffectPool.PooledEffect p : effects) { p.draw(game.batch); }
        game.batch.end();
    }

    public void dispose() {
        player.dispose();
    }
}
