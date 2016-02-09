package com.muscovy.game.gui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


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


	public Button(String text, float x, float y, float width, float height, float textYOffset, BitmapFont font) {
		this(text, x, y, width, height, textYOffset, font, null, null);
	}


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


	private void createGlyphLayout() {
		glyphLayout = new GlyphLayout(font, text);
	}


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


	public boolean pointOnButton(float px, float py) {
		return ((px >= getLeft()) && (px <= getRight()) && (py >= getBottom()) && (py <= getTop()));
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
		createGlyphLayout();
	}


	public BitmapFont getFont() {
		return font;
	}


	public void setFont(BitmapFont font) {
		this.font = font;
		createGlyphLayout();
	}


	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}


	public float getWidth() {
		return width;
	}


	public void setWidth(float width) {
		this.width = width;
	}


	public float getHeight() {
		return height;
	}


	public void setHeight(float height) {
		this.height = height;
	}


	public float getLeft() {
		return x;
	}


	public float getRight() {
		return x + width;
	}


	public float getTop() {
		return y + height;
	}


	public float getBottom() {
		return y;
	}


	public float getTextYOffset() {
		return textYOffset;
	}


	public void setTextYOffset(float textYOffset) {
		this.textYOffset = textYOffset;
	}


	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public Texture getSelectedTexture() {
		return selectedTexture;
	}


	public void setSelectedTexture(Texture selectedTexture) {
		this.selectedTexture = selectedTexture;
	}


	public Texture getDeselectedTexture() {
		return deselectedTexture;
	}


	public void setDeselectedTexture(Texture deselectedTexture) {
		this.deselectedTexture = deselectedTexture;
	}


	public Color getSelectedTextColour() {
		return selectedTextColour;
	}


	public void setSelectedTextColour(Color selectedTextColour) {
		this.selectedTextColour = selectedTextColour;
	}


	public Color getDeselectedTextColour() {
		return deselectedTextColour;
	}


	public void setDeselectedTextColour(Color deselectedTextColour) {
		this.deselectedTextColour = deselectedTextColour;
	}


	public GlyphLayout getGlyphLayout() {
		return glyphLayout;
	}


	@Override
	public String toString() {
		return "Button(\"" + text + "\", " + x + ", " + y + ", " + width + ", " + height + ")";
	}
}
