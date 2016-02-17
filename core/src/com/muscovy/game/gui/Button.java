package com.muscovy.game.gui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Makes pretty buttons for menus. Set up buttons text and texture No logic - see ButtonList
 */
public class Button {
	private String text;
	private GlyphLayout glyphLayout;
	private BitmapFont font;
	private float x;
	private float y;
	private float width;
	private float height;
	private float textYOffset;

	private boolean selected = false;
	private Texture selectedTexture;
	private Texture deselectedTexture;

	private Color selectedTextColour = Color.WHITE;
	private Color deselectedTextColour = Color.BLACK;


	/**
	 *
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param textYOffset
	 * @param font
	 */
	public Button(String text, float x, float y, float width, float height, float textYOffset, BitmapFont font) {
		this(text, x, y, width, height, textYOffset, font, null, null);
	}


	/**
	 *
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param textYOffset
	 * @param font
	 * @param selectedTexture
	 * @param deselectedTexture
	 */
	public Button(String text, float x, float y, float width, float height, float textYOffset, BitmapFont font,
			Texture selectedTexture, Texture deselectedTexture) {
		this.text = text;
		this.font = font;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.textYOffset = textYOffset;

		selected = false;
		this.selectedTexture = selectedTexture;
		this.deselectedTexture = deselectedTexture;

		createGlyphLayout();
	}


	/**
	 * Button layout
	 */
	private void createGlyphLayout() {
		glyphLayout = new GlyphLayout(font, text);
	}


	/**
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		float textX = (x + (width / 2)) - (glyphLayout.width / 2);
		float textY = y + textYOffset;

		Texture texture = null;
		if (selected) {
			texture = selectedTexture;
			font.setColor(selectedTextColour);
		} else {
			texture = deselectedTexture;
			font.setColor(deselectedTextColour);
		}

		batch.draw(texture, x, y, width, height);
		font.draw(batch, text, textX, textY);
	}


	/**
	 * @param px
	 * @param py
	 * @return
	 */
	public boolean pointOnButton(float px, float py) {
		return ((px >= getLeft()) && (px <= getRight()) && (py >= getBottom()) && (py <= getTop()));
	}


	/**
	 * @return
	 */
	public String getText() {
		return text;
	}


	/**
	 * Recreates the glyph layout.
	 *
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
		createGlyphLayout();
	}


	/**
	 * @return
	 */
	public BitmapFont getFont() {
		return font;
	}


	/**
	 * Recreates the glyph layout.
	 *
	 * @param font
	 */
	public void setFont(BitmapFont font) {
		this.font = font;
		createGlyphLayout();
	}


	/**
	 * @return
	 */
	public float getX() {
		return x;
	}


	/**
	 * @param x
	 */
	public void setX(float x) {
		this.x = x;
	}


	/**
	 * @return
	 */
	public float getY() {
		return y;
	}


	/**
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
	}


	/**
	 * @return
	 */
	public float getWidth() {
		return width;
	}


	/**
	 * @param width
	 */
	public void setWidth(float width) {
		this.width = width;
	}


	/**
	 * @return
	 */
	public float getHeight() {
		return height;
	}


	/**
	 * @param height
	 */
	public void setHeight(float height) {
		this.height = height;
	}


	/**
	 * @return
	 */
	public float getLeft() {
		return x;
	}


	/**
	 * @return
	 */
	public float getRight() {
		return x + width;
	}


	/**
	 * @return
	 */
	public float getTop() {
		return y + height;
	}


	/**
	 * @return
	 */
	public float getBottom() {
		return y;
	}


	/**
	 * @return
	 */
	public float getTextYOffset() {
		return textYOffset;
	}


	/**
	 * @param textYOffset
	 */
	public void setTextYOffset(float textYOffset) {
		this.textYOffset = textYOffset;
	}


	/**
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}


	/**
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	/**
	 * @return
	 */
	public Texture getSelectedTexture() {
		return selectedTexture;
	}


	/**
	 * @param selectedTexture
	 */
	public void setSelectedTexture(Texture selectedTexture) {
		this.selectedTexture = selectedTexture;
	}


	/**
	 * @return
	 */
	public Texture getDeselectedTexture() {
		return deselectedTexture;
	}


	/**
	 * @param deselectedTexture
	 */
	public void setDeselectedTexture(Texture deselectedTexture) {
		this.deselectedTexture = deselectedTexture;
	}


	/**
	 * @return
	 */
	public Color getSelectedTextColour() {
		return selectedTextColour;
	}


	/**
	 * @param selectedTextColour
	 */
	public void setSelectedTextColour(Color selectedTextColour) {
		this.selectedTextColour = selectedTextColour;
	}


	/**
	 * @return
	 */
	public Color getDeselectedTextColour() {
		return deselectedTextColour;
	}


	/**
	 * @param deselectedTextColour
	 */
	public void setDeselectedTextColour(Color deselectedTextColour) {
		this.deselectedTextColour = deselectedTextColour;
	}


	/**
	 * @return
	 */
	public GlyphLayout getGlyphLayout() {
		return glyphLayout;
	}


	@Override
	public String toString() {
		return "Button(\"" + text + "\", " + x + ", " + y + ", " + width + ", " + height + ")";
	}
}
