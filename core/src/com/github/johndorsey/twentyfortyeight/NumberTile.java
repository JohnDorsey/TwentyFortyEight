package com.github.johndorsey.twentyfortyeight;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

/**
 * Created by John on 9/28/15.
 */
public class NumberTile {
    //public Rectangle thisTile;
    public static int tileXSpacing = 64;
    public static int tileYSpacing = 64;
    public static float tileWidth = 60;
    public static float tileHeight = 60;

    public static Texture[] numberTextures;
    public static int xinc, yinc;
    public static float slideTimer;
    public static float slideDuration;
    public static TwentyFortyEight parentGame;
    public static boolean active;
    public boolean combine;
    public int value, freedom;
    public int x, y;
    public int nextValue;
    public boolean added;

    public static void setUpTextures() {
        numberTextures = new Texture[13];
        for (int i = 0; i < 13; i++) {
            numberTextures[i] = new Texture("number" + Integer.toString(i) + ".png");
        }
    }

    public NumberTile(int nX, int nY) {
        //thisTile = new Rectangle();
        //thisTile.x = (x = nX) * 64;
        //thisTile.y = (y = nY) * 64;
        //thisTile.width = 60;
        //thisTile.height = 60;
        x = nX;
        y = nY;
        value = 2;
        freedom = 0;
        combine = false;
        slideTimer = 0;
        slideDuration = 0.07f;
        active = false;
    }

    public void shoutStatus() {
        if (value == 0) {
            System.out.println(x + ", " + y +" is empty");
        } else {
            System.out.println(x + ", " + y +" is a " + value + " tile... it has " + freedom + " square" + (freedom == 1? "" : "s") + " of freedom and will" + (combine? "" : " NOT ") + "combine.");
        }
    }



    public int getScreenX() {
        return (int)(weightedMean((float) x, (float) (x + (-xinc * freedom)), usedProgress())  * tileXSpacing)/* + getSizeAdjust()*/;
    }

    public int getScreenY() {
        //return y * 64;
        return (int)(weightedMean((float) y, (float) (y + (-yinc * freedom)), usedProgress())  * tileYSpacing)/* + getSizeAdjust()*/;
    }

    public int getScreenWidth() {
        //return (int) (added? weightedMean(0, tileWidth, usedProgress()) : tileWidth);
        return (int) tileWidth;
    }

    public int getScreenHeight() {
        //return (int) (added? weightedMean(0, tileHeight, usedProgress()) : tileHeight);
        return (int) tileHeight;
    }

    public int getSizeAdjust() {
        return (int) (added? weightedMean((tileWidth / 2), 0, usedProgress()) : 0);
    }

    public Texture getTexture() {
        //if (usedProgress() == 1) { value = nextValue; }
        return numberTextures[value];
    }

    public static void animate() {
        active = true;
        slideTimer = slideDuration;
        //System.out.println("NumberTile.animate: set slideTimer to " + slideDuration);
    }

    public static void timePasses(float time) {
        slideTimer = Math.max(0.0f, slideTimer - time);
        //if (slideTimer !=0) { System.out.println("NumberTile.timePasses: animated frame, taking " + parentGame.ft); }
        if (slideTimer == 0 && active) {
            animationEnd();
        }
    }

    public static void animationEnd() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                parentGame.board[x][y].value = parentGame.board[x][y].nextValue;
            }
        }
        clearSlideData();
        active = false;
    }

    public static void clearSlideData() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                parentGame.board[x][y].nextValue = 0;
                parentGame.board[x][y].freedom = 0;
                parentGame.board[x][y].added = false;
            }
        }
        parentGame.newOne.nextValue = 0;
        parentGame.newOne.x = 4;
        parentGame.newOne.y = 4;
    }

    public static boolean animating() {
        return (slideTimer != 0.0f);
    }

    public static float weightedMean(float start, float end, float progress) {
        return (end * progress) + (start * (1 - progress));
    }

    public static float usedProgress() {
        return (((slideTimer > 0)? (slideDuration - slideTimer) : 0) / slideDuration);
    }

    //public void doFrame() {
    //    slideTimer
    //}

}
