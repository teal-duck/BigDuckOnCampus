package com.muscovy.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

/**
 * Created by SeldomBucket on 06-Dec-15.
 */
public class GUI {
    private ArrayList<Sprite> guiElements;
    private ArrayList<guiData> guiData;
    private class guiData{
        private String data;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        private String ID;
        private BitmapFont font;
        private int x,y;
        public guiData(String ID, BitmapFont font, String data, int x, int y) {
            this.ID = ID;
            this.data = data;
            this.font = font;
            this.x = x;
            this.y = y;
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
        guiData = new ArrayList<guiData>();
    }

    public void addData(String ID, String data, BitmapFont font, int x, int y){
        guiData.add(new guiData(ID, font,data,x,y));
    }
    public void editData(String ID, String newData){
        for (guiData data: guiData){
            if (data.getID().matches(ID)){
                data.setData(newData);
            }
        }
    }

    public void addElement(Sprite sprite){
        guiElements.add(sprite);
    }

    public void render(SpriteBatch batch){
        for (Sprite sprite:guiElements){
            batch.draw(sprite.getTexture(), sprite.getX(), sprite.getY());
        }
        for (guiData element: guiData) {
            element.getFont().draw(batch,element.getData(),element.getX(),element.getY());
        }
    }
}
