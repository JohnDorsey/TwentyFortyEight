package com.github.johndorsey.twentyfortyeight;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by John on 10/2/15.
 */
public class InputManage implements InputProcessor {

    public TwentyFortyEight parentGame;

    public InputManage(TwentyFortyEight nParent) {
        parentGame = nParent;
    }


    public boolean keyDown (int keycode) {
        //System.out.println("InputManage: key down!");
        if (keycode == Input.Keys.BACKSPACE) { /*parentGame.addTile(10); parentGame.addTile(10); parentGame.addTile(10); parentGame.addTile(10);*/ }
        if (parentGame.gameManager.acceptingMoves) {
            switch (keycode) {
                case Input.Keys.LEFT: { parentGame.playerSlide(1, 0); } break;
                case Input.Keys.RIGHT: { parentGame.playerSlide(-1, 0); } break;
                case Input.Keys.UP: { parentGame.playerSlide(0, -1); } break;
                case Input.Keys.DOWN: { parentGame.playerSlide(0, 1); } break;
                //case Input.Keys.BACKSPACE: { System.out.println("Backspace"); parentGame.rotateBoard(); }
            }
        } else if (parentGame.gameManager.acceptingSelects) {
            switch (keycode) {
                case Input.Keys.LEFT: { parentGame.gameManager.select(-1); } break;
                case Input.Keys.RIGHT: { parentGame.gameManager.select(1); } break;
                case Input.Keys.ENTER: { parentGame.gameManager.enter(); } break;
            }
        }
        return false;
    }

    public boolean keyUp (int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (int amount) {
        return false;
    }

}
