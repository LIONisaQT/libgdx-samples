package com.missionbit.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation {
    private Array<TextureRegion> frames;
    private float maxFrameTime, currentFrameTime;
    private int frameCount, frame;
    private boolean isLooping, isFinished;

    Animation(TextureRegion region, int frameCount, float cycleTime, int row, int col, boolean loop) {
        frames = new Array<TextureRegion>();
        int frameWidth = region.getRegionWidth() / col;
        int frameHeight = region.getRegionHeight() / row;

        /*
        Add frames from the spritesheet.
        The reason why I have a count is because my spritesheet looks like:

            [1][2][3]
            [4][5][6]
            [7][8][X]

         where X is wasted space. Without the count check, my nested for loop will add that wasted
         space as a frame, bulking up the size of my animation array.
         */
        int count = 0;
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                if (count < frameCount) {
                    frames.add(
                            new TextureRegion(
                                    region,
                                    c * frameWidth,
                                    r * frameHeight,
                                    frameWidth,
                                    frameHeight
                            )
                    );
                    count++;
                }
            }
        }

        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
        isFinished = false;
        isLooping = loop;
    }

    public void update(float dt) {
        currentFrameTime += dt;

        // Go to next frame
        if (currentFrameTime > maxFrameTime) {
//            System.out.println("Current frame: " + frame + "/" + (frameCount - 1));
            frame++;
//            System.out.println("Moving to frame " + frame);
            currentFrameTime = 0;
        }

        // Loop animation if isLooping is true, otherwise stay on last frame
        if (frame >= frameCount) {
            if (isLooping && !isFinished) {
//                System.out.println("loop!");
                frame = 0;
            } else {
//                System.out.println("done!");
                isFinished = true;
            }
        }

//        System.out.println("frame" + frame + "/" + (frameCount-1));
    }

    public void setFrame(int frame) {this.frame = frame;}

    // Modified FlappyDemo Animation getFrame(), freezes on final frame if animation doesn't loop
    public TextureRegion getFrame() {
        if (frame >= frameCount) {return frames.get(frameCount - 1);}
        return frames.get(frame);
    }

    public void flipFrames() {
        for (TextureRegion textureRegion : frames) {textureRegion.flip(true, false);}
    }

    public boolean isFinished() {return isFinished;}
}
