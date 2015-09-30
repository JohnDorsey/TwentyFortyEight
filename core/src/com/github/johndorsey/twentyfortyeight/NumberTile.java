package com.github.johndorsey.twentyfortyeight;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

/**
 * Created by John on 9/28/15.
 */
public class NumberTile {
    public Rectangle thisTile;
    public static Texture[] numberTextures;
    public int value;
    public int freedom;
    public boolean combine;
    public int x;
    public int y;

    public static void setUpTextures() {
        numberTextures = new Texture[13];
        for (int i = 0; i < 13; i++) {
            numberTextures[i] = new Texture("number" + Integer.toString(i) + ".png");
        }
    }

    public NumberTile(int nX, int nY) {
        thisTile = new Rectangle();
        thisTile.x = (x = nX) * 64;
        thisTile.y = (y = nY) * 64;
        thisTile.width = 60;
        thisTile.height = 60;
        value = 2;
        freedom = 0;
        combine = false;
    }

    public void shoutStatus() {
        if (value == 0) {
            System.out.println(x + ", " + y +" is empty");
        } else {
            System.out.println(x + ", " + y +" is a " + value + " tile... it has " + freedom + " squares of freedom and " + (combine? "will" : "will NOT") + " combine.");
        }
    }

    public Texture getTexture() {
        return numberTextures[value];
    }

}
