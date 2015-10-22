package com.github.johndorsey.twentyfortyeight;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by John on 10/20/15.
 */

//import java.util.Arrays;

public class DialogueBox {

    TwentyFortyEight parentGame;
    public String title;
    public String body[];
    public String buttons[];

    //public int buttonCount;

    public int ox = 48;
    public int oy = 84;
    //public int currentButton = 0;

    public int selectedButton = 0;

    public boolean enabled;

    public static Texture bg;
    public static Texture button;

    public DialogueBox(TwentyFortyEight parentGameInput, String newTitle, String newBody[], String newButtons[]) {
        //System.out.println("DialogueBox: initialized");
        parentGame = parentGameInput;
        title = newTitle; body = newBody; buttons = newButtons;
        enabled = false;
        bg = new Texture("dialogue.png");
        button = new Texture("button.png");
    }

    public void select(int direction) {
        selectedButton += direction;
        limitSelection();

    }

    public void limitSelection() {
        if (selectedButton > 1) { selectedButton = 0; }
        if (selectedButton < 0) { selectedButton = 1; }
    }

    public void render() {
        parentGame.batch.draw(bg, ox, oy, 256, 108);

        parentGame.batch.draw(button, ox + 12, oy + 8, 96, 24);
        parentGame.batch.draw(button, ox + 148, oy + 8, 96, 24);
        parentGame.font.draw(parentGame.batch, (selectedButton==0)?">"+buttons[0]+"<":" "+buttons[0]+" ", ox + 24, oy + 24);
        parentGame.font.draw(parentGame.batch, (selectedButton==1)?">"+buttons[1]+"<":" "+buttons[1]+" ", ox + 156, oy + 24);
        //System.out.println("winrender of " + buttons[0] + " and " + buttons[1] + " with button " + selectedButton + " selected");

        parentGame.font.draw(parentGame.batch, title, ox + 84, oy + 124);
        int ln = 0;
        for (String line : body) {
            //if (ln == selectedButton) { line = line.toUpperCase(); }
            parentGame.font.draw(parentGame.batch, line, ox + 12, oy + 104 - ((ln + 1) * 12));
            ln++;
        }
    }

}
