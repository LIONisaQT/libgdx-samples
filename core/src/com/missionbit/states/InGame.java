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

public class InGame extends State {
    private Player player;
    private Controller controller;

    private ParticleEffectPool effectPool;                  // Pool of inactive particle effects
    private Array<ParticleEffectPool.PooledEffect> effects; // Array of active particle effects

    InGame(LibGDXSamples game) {
        super(game);
        player = new Player(50, 100, this);
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
                /*
                Setting identity to true uses == comparison instead of .equals(). This is important
                because .equals() compares value(s) whereas == compares memory addresses.

                For example, if you create two identical Player objects, the compiler will optimize
                the two objects to the same address in memory because they're both identical in all
                aspects. However, as soon as you change a single variable, for example if the speed
                was 15, and you even run speed = 15 somewhere else in code, that instance of the
                class will be moved to a new location in memory, because something about it was
                touched. Java's compiler is not smart enough to notice this kind of subtlety.

                You can test this by creating two identical Strings, checking both == and .equals()
                on it, changing the value of the one of the Strings, and then rechecking equality.
                == will return false, and .equals() will return true.

                With pools, an instance of an object will never change its address in memory. You
                can test this by printing the pooled object, which will give you its memory address.
                After freeing the pooled object, obtain it from the pool again, and you can see it
                still retains its address in memory. This is the entire point of the pool, and why
                all games with rapidly-spawning objects will implement some form of pooling.

                Anyway, when we remove the value of the pooled object from the active array, we
                absolutely want to make sure we're removing the correct object from the pool, so we
                use the == operator.

                You can learn more about what exactly is going on here by studying compilers, which
                many colleges have at least one course on.
                 */
                effects.removeValue(p, true);
            }
        }

        player.update(dt);
    }

    // Anything involving input and the controller goes here
    private void handleInput() {
        controller.update();

        if (controller.isLeftPressed()) {
            player.moveLeft();
        } else if (controller.isRightPressed()) {
            player.moveRight();
        } else {
            player.resetAnim();
        }

        if (controller.isJumpPressed()) {
            player.jump();
        }

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
    void drawGame() {
        batch.begin();
        font.draw(batch, this.getClass().toString(), 0, LibGDXSamples.HEIGHT);
        batch.draw(player.getTexture(), player.getBounds().getX(), player.getBounds().getY());
        controller.draw(batch);
        for(ParticleEffectPool.PooledEffect p : effects) { p.draw(batch); }
        batch.end();

        if (DEBUG) {
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            controller.drawDebug(sr);
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
