package com.github.johndorsey.twentyfortyeight;

/**
 * Created by John on 10/22/15.
 */
public class NoReset {
    public static int highScore;
    public static boolean openMenu;

    public static void setup() {
        try {
            highScore = Math.max(highScore, 0);
        } catch (NullPointerException n) {
            highScore = 0;
        }
        openMenu = true;
    }

}
