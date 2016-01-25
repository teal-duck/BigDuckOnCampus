package com.muscovy.game;


import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by SeldomBucket on 06-Dec-15.
 */
public class GUI {
	private ArrayList<Sprite> guiElements;
	// TODO: Should guiData be a HashMap?
	// TODO: Should gui ID comparison use .equals() instead of .matches()?
	private ArrayList<GuiData> guiData;


	private class GuiData {
		/**
		 * Helper Class: guiData Structure for the text elements of the GUI. No need to access directly, getters
		 * and setters are provided for the bits you might need to change.
		 */
		private String data;
		private String ID;
		private BitmapFont font;
		private int x, y;


		public GuiData(String ID, BitmapFont font, String data, int x, int y) {
			this.ID = ID;
			this.data = data;
			this.font = font;
			this.x = x;
			this.y = y;
		}


		public String getID() {
			return ID;
		}


		@SuppressWarnings("unused")
		public void setID(String ID) {
			this.ID = ID;
		}


		public int getX() {
			return x;
		}


		public void setX(int x) {
			this.x = x;
		}


		public int getY() {
			return y;
		}


		public void setY(int y) {
			this.y = y;
		}


		public String getData() {
			return data;
		}


		public void setData(String data) {
			this.data = data;
		}


		public BitmapFont getFont() {
			return font;
		}


		public void setFont(BitmapFont font) {
			this.font = font;
		}
	}


	public GUI() {
		guiElements = new ArrayList<Sprite>();
		guiData = new ArrayList<GuiData>();
	}
	/**
	 * GUI Text Methods These are confusing, I think, so give me a shout if you need it explaining. You could
	 * probably find an easier way to do it
	 */


	/**
	 * @param ID
	 *                the ID you will use to refer to this particular piece of text when editing using editData,
	 *                moveData, changeDataFont
	 * @param data
	 *                the String you want to be displayed
	 * @param font
	 *                the font you want the string displayed in
	 * @param x
	 *                X location
	 * @param y
	 *                Y location
	 */
	public void addData(String ID, String data, BitmapFont font, int x, int y) {
		/**
		 * ID is used to identify the text you want to change using editData, so use something memorable (works
		 * like a variable name)
		 */
		guiData.add(new GuiData(ID, font, data, x, y));
	}


	public void editData(String ID, String newData) {
		for (GuiData data : guiData) {
			if (data.getID().matches(ID)) {
				data.setData(newData);
			}
		}
	}


	public void moveData(String ID, int x, int y) {
		for (GuiData data : guiData) {
			if (data.getID().matches(ID)) {
				data.setX(x);
				data.setY(y);
			}
		}
	}


	public void changeDataFont(String ID, BitmapFont font) {
		for (GuiData data : guiData) {
			if (data.getID().matches(ID)) {
				data.setFont(font);
			}
		}
	}


	public void addElement(Sprite sprite) {
		guiElements.add(sprite);
	}


	public void render(SpriteBatch batch) {
		/**
		 * Renders the elements so the first element you added using addElement is on the bottom, and each other
		 * is layered on top. The data elements go on top of the sprites, so you cannot have text hidden by a
		 * GUI element.
		 */
		for (Sprite sprite : guiElements) {
			batch.draw(sprite.getTexture(), sprite.getX(), sprite.getY());
		}

		for (GuiData element : guiData) {
			element.getFont().draw(batch, element.getData(), element.getX(), element.getY());
		}
	}
}
