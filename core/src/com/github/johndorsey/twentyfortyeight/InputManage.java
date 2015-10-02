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
        System.out.println("InputManage: key down!");
        switch (keycode) {
            case Input.Keys.LEFT: { parentGame.signSlide(-1, 0); } break;
            case Input.Keys.RIGHT: { parentGame.signSlide(1, 0); } break;
            case Input.Keys.UP: { parentGame.signSlide(0, 1); } break;
            case Input.Keys.DOWN: { parentGame.signSlide(0, -1); } break;
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
