package com.missionbit.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public final AssetManager manager = new AssetManager();
    public final AssetDescriptor<Texture> img = new AssetDescriptor<Texture>("badlogic.jpg", Texture.class);

}
