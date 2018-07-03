package com.missionbit.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation {
    private Array<TextureRegion> frames;
    private float maxFrameTime, currentFrameTime;
    private int frameCount, frame;

    Animation(TextureRegion region, int frameCount, float cycleTime, int row, int col) {
        frames = new Array<TextureRegion>();
        int frameWidth = region.getRegionWidth() / row;
        int frameHeight = region.getRegionHeight() / col;

        // Why do we start the for loop with the number of columns?
        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                frames.add(
                        new TextureRegion(
                                region,
                                x * frameWidth,
                                y * frameHeight,
                                frameWidth,
                                frameHeight
                        )
                );
            }
        }

        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }

    public void update(float dt) {
        currentFrameTime += dt;
        if (currentFrameTime > maxFrameTime) {
            frame++;
            currentFrameTime = 0;
        }
        if (frame >= frameCount) {
            frame = 0;
        }
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public TextureRegion getFrame() {
        return frames.get(frame);
    }

    public void flipFrames() {
        for (TextureRegion textureRegion : frames) {
            textureRegion.flip(true, false);
        }
    }
}
