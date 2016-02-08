package com.muscovy.game.save;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue.PrettyPrintSettings;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.muscovy.game.MuscovyGame;


public abstract class Saver<T> {
	// https://github.com/libgdx/libgdx/wiki/Reading-&-writing-JSON
	protected final MuscovyGame game;
	protected final Json json;
	protected Class<T> dataClass;
	private final PrettyPrintSettings prettyPrintSettings;


	public Saver(MuscovyGame game) {
		this.game = game;

		json = new Json();
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(OutputType.json);

		prettyPrintSettings = new PrettyPrintSettings();
		prettyPrintSettings.outputType = OutputType.json;
		prettyPrintSettings.singleLineColumns = 80;
		prettyPrintSettings.wrapNumericArrays = true;

		initialiseSerializers();
	}


	protected abstract void initialiseSerializers();


	public String getPrettySaveString(T data) {
		if (data == null) {
			Gdx.app.log("Save", "Attempt to save null data");
			return "";
		}
		return json.prettyPrint(data, prettyPrintSettings);
	}


	public String saveToFile(T data, FileHandle fileHandle) {
		if (data == null) {
			Gdx.app.log("Save", "Attempt to save null data");
			return "";
		}

		if (fileHandle == null) {
			Gdx.app.log("Save", "Attempt to save to null file handle");
			return "";
		}

		String text = getPrettySaveString(data);
		fileHandle.writeString(text, false);
		return text;
	}


	public String saveToFile(T data, String fileName) {
		if (data == null) {
			Gdx.app.log("Save", "Attempt to save null data");
			return "";
		}

		if (fileName == null) {
			Gdx.app.log("Save", "Attempt to save to null file name");
			return "";
		}

		return saveToFile(data, Gdx.files.local(fileName));
	}


	public T loadFromSaveString(String string) {
		if (string == null) {
			Gdx.app.log("Load", "Attempt to load from null string");
			return null;
		}

		return json.fromJson(dataClass, string);
	}


	/**
	 * Throws SerializationException if the file doesn't exist.
	 *
	 * @param fileHandle
	 * @return
	 */
	public T loadFromFile(FileHandle fileHandle) {
		if (fileHandle == null) {
			Gdx.app.log("Load", "Attempt to load from null file handle");
			return null;
		}

		return json.fromJson(dataClass, fileHandle);
	}


	/**
	 * Throws SerializationException if the file doesn't exist.
	 *
	 * @param fileName
	 * @return
	 */
	public T loadFromFile(String fileName) {
		if (fileName == null) {
			Gdx.app.log("Load", "Attempt to load from null file name");
			return null;
		}

		return loadFromFile(Gdx.files.local(fileName));
	}


	public T loadFromFileOrNull(FileHandle fileHandle) {
		if (fileHandle == null) {
			Gdx.app.log("Load", "Attempt to load from null file handle");
			return null;
		}

		if (fileHandle.exists()) {
			return loadFromFile(fileHandle);
		} else {
			return null;
		}
	}


	public T loadFromFileOrNull(String fileName) {
		if (fileName == null) {
			Gdx.app.log("Load", "Attempt to load from null file name");
			return null;
		}

		return loadFromFileOrNull(Gdx.files.local(fileName));
	}
}
