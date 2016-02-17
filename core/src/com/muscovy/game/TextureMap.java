package com.muscovy.game;


import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;


/**
 * Project URL : http://teal-duck.github.io/teal-duck
 * <br>
 * New class: Stores all textures in the game as map from their names to their instances.
 */
public class TextureMap {
	private HashMap<String, Texture> textures;


	public TextureMap() {
		textures = new HashMap<String, Texture>();
	}


	/**
	 * @param filename
	 * @return
	 */
	private static final Texture getTextureFromFile(String filename) {
		try {
			return new Texture(Gdx.files.internal(filename));
		} catch (GdxRuntimeException e) {
			Gdx.app.log("Texture", "Unable to load texture \"" + filename + "\"");
			return null;
		}
	}


	/**
	 * Loads a texture from a file, puts it into the map and returns the texture. If there already is a texture for
	 * this key, calls dispose on it.
	 *
	 * @param textureName
	 *                name of file and the key to use
	 * @return
	 */
	public Texture loadTextureOverwriteOld(String textureName) {
		Texture texture = TextureMap.getTextureFromFile(textureName);
		if (texture == null) {
			return null;
		}

		Texture oldTexture = textures.put(textureName, texture);

		if (oldTexture != null) {
			oldTexture.dispose();
		}

		return texture;
	}


	/**
	 * Gets a texture from the map. If it doesn't exist, loads it from a file and adds it to the map.
	 *
	 * @param textureName
	 * @return texture associated with this name
	 */
	public Texture getTextureOrLoadFile(String textureName) {
		Texture texture = textures.get(textureName);

		if (texture != null) {
			return texture;
		} else {
			return loadTextureOverwriteOld(textureName);
		}
	}


	/**
	 * Calls dispose() on the texture if it exists.
	 *
	 * @param textureName
	 */
	public void disposeTexture(String textureName) {
		Texture texture = textures.remove(textureName);
		if (texture != null) {
			texture.dispose();
		}
	}


	/**
	 * Disposes of all textures in the map and empties the map.
	 */
	public void disposeAllTextures() {
		for (Texture texture : textures.values()) {
			texture.dispose();
		}
		textures.clear();
	}
}
