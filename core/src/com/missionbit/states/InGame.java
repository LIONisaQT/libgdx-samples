package com.missionbit.states;

import com.missionbit.LibGDXSamples;
import com.missionbit.actors.Player;

public class InGame extends State {
    private Player player;

    InGame(LibGDXSamples game) {
        super(game);
        player = new Player(50, 100, this);
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
        game.batch.begin();
        game.font.draw(game.batch, this.getClass().toString(), 0, LibGDXSamples.HEIGHT);
        game.batch.draw(player.getTexture(), player.getX(), player.getY());
        game.batch.end();
    }

    public void dispose() {
        player.dispose();
    }
}
