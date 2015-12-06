package com.muscovy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

/**
 * Created by ewh502 on 04/12/2015.
 */
public class RoomRenderer {
    private ArrayList<OnscreenDrawable> renderList ;
    private OnscreenDrawable room;
    private BitmapFont list;

    public RoomRenderer(OnscreenDrawable newRoom) {
        this.renderList = new ArrayList<OnscreenDrawable>();
        this.room = newRoom;
        list = new BitmapFont();
        list.setColor(Color.WHITE);
    }

    public void render(SpriteBatch batch){
        /**
         *
         */
        sortDrawables();
        batch.draw(room.getSprite().getTexture(),0,0);
        for (OnscreenDrawable drawable:renderList){
            batch.draw(drawable.getSprite().getTexture(),drawable.getX(),drawable.getY());
        }
        list.draw(batch,"size = " + renderList.size(),(float)256,(float)256);
    }

    private void sortDrawables(){
        /**
        * Quicksorts the list of drawable objects in the room by Y coordinate so
        * it renders the things in the background first.
        */
        ArrayList<OnscreenDrawable> newList = new ArrayList<OnscreenDrawable>();
        newList.addAll(quicksort(renderList));
        renderList.clear();
        renderList.addAll(newList);
    }

    private ArrayList<OnscreenDrawable> quicksort(ArrayList<OnscreenDrawable> input){
        if(input.size() <= 1){
            return input;
        }
        int middle = (int) Math.ceil((double)input.size() / 2);
        OnscreenDrawable pivot = input.get(middle);
        ArrayList<OnscreenDrawable> less = new ArrayList<OnscreenDrawable>();
        ArrayList<OnscreenDrawable> greater = new ArrayList<OnscreenDrawable>();
        for (int i = 0; i < input.size(); i++) {
            if(input.get(i).getY() >= pivot.getY()){
                if(i == middle){
                    continue;
                }
                less.add(input.get(i));
            }
            else{
                greater.add(input.get(i));
            }
        }
        return concatenate(quicksort(less), pivot, quicksort(greater));
    }

    private ArrayList<OnscreenDrawable> concatenate(ArrayList<OnscreenDrawable> less, OnscreenDrawable pivot, ArrayList<OnscreenDrawable> greater){
        ArrayList<OnscreenDrawable> list = new ArrayList<OnscreenDrawable>();
        for (int i = 0; i < less.size(); i++) {
            list.add(less.get(i));
        }
        list.add(pivot);
        for (int i = 0; i < greater.size(); i++) {
            list.add(greater.get(i));
        }
        return list;
    }

    public void addNewDrawable(OnscreenDrawable drawable){
        renderList.add(drawable);
        sortDrawables();
    }
}
