package com.muscovy.game;


import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class TextureMap {
	// TODO: TextureMap should have a way to tell if a texture was already loaded
	private HashMap<String, Texture> textures;


	public TextureMap() {
		textures = new HashMap<String, Texture>();
	}


	/**
	 * @param filename
	 * @return
	 */
	private static final Texture getTextureFromFile(String filename) {
		// System.out.println("Loading texture \"" + filename + "\"");
		return new Texture(Gdx.files.internal(filename));
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
		// System.out.println("Loading (overwrite old) texture \"" + textureName + "\"");

		Texture texture = TextureMap.getTextureFromFile(textureName);
		Texture oldTexture = textures.put(textureName, texture);

		if (oldTexture != null) {
			// System.out.println("\tTexture \"" + textureName + "\" already existed, disposing");
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
		// System.out.println("Getting texture \"" + textureName + "\"");
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
		// System.out.println("Disposing texture: \"" + textureName + "\"");
		Texture texture = textures.remove(textureName);
		if (texture != null) {
			texture.dispose();
		}
		// else {
		// System.out.println("\tTexture to dispose is null");
		// }
	}


	/**
	 * Disposes of all textures in the map and empties the map.
	 */
	public void disposeAllTextures() {
		// System.out.println("Disposing all textures");
		for (Texture texture : textures.values()) {
			texture.dispose();
		}
		textures.clear();
	}
}
