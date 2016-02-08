package com.muscovy.game.save;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.muscovy.game.MuscovyGame;


public abstract class Saver<T> {
	// https://github.com/libgdx/libgdx/wiki/Reading-&-writing-JSON
	protected final MuscovyGame game;
	protected final Json json;
	protected Class<T> dataClass;


	public Saver(MuscovyGame game) {
		this.game = game;

		json = new Json();
		// json.setTypeName(null);
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(OutputType.json);

		initialiseSerializers();

	}


	protected abstract void initialiseSerializers();


	public String getPrettySaveString(T data) {
		return json.prettyPrint(data);
	}


	public void saveToFile(T data, FileHandle fileHandle) {
		json.toJson(data, fileHandle);
	}


	public T loadFromSaveString(String string) {
		return json.fromJson(dataClass, string);
	}


	public T loadFromFile(FileHandle fileHandle) {
		return json.fromJson(dataClass, fileHandle);
	}
}
