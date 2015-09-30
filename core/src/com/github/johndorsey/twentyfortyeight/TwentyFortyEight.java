package com.github.johndorsey.twentyfortyeight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class TwentyFortyEight extends ApplicationAdapter {
	SpriteBatch batch;
	private OrthographicCamera camera;
	Texture img;
    NumberTile[][] board;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1080, 720);
		img = new Texture("badlogic.jpg");
        board = new NumberTile[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                board[x][y] = new NumberTile(x, y);
                board[x][y].value = 0;
            }
        }
        NumberTile.setUpTextures();

        board[0][3].value = 2;
        //board[1][3].value = 3;
        board[2][3].value = 2;

        signSlide(0, -1);
	}



    public void signSlide (int xinc, int yinc) {
        int base = -1;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                base = board[x][y].value;
                if (base == 0) {
                    iOffLoop:
                    for (int iOff = 1; iOff < 4; iOff++) {
                        try {
                            board[x + (iOff * xinc)][y + (iOff * yinc)].freedom++;
                            System.out.println("TwentyFortEight.signSlide: " + x + ", " + y + "is value 0 so the freedom of " + (x + (iOff * xinc)) + ", " + (y + (iOff * yinc)) + " has been set to " + board[x + (iOff * xinc)][y + (iOff * yinc)].freedom);
                        } catch (ArrayIndexOutOfBoundsException ack) {
                            break iOffLoop;
                        }
                    }
                } else {
                    iOffLoop:
                    for (int iOff = 1; iOff < 4; iOff++) {
                        try {
                            if (board[x + (iOff * xinc)][y + (iOff * yinc)].value != board[x][y].value && board[x + (iOff * xinc)][y + (iOff * yinc)].value != 0) { break iOffLoop; }
                            if ((!board[x][y].combine) && (!board[x + (iOff * xinc)][y + (iOff * yinc)].combine)) {
                                if (board[x + (iOff * xinc)][y + (iOff * yinc)].value == base) {
                                    board[x][y].combine = true;
                                    board[x + (iOff * xinc)][y + (iOff * yinc)].combine = true;
                                    board[x + (iOff * xinc)][y + (iOff * yinc)].freedom++;
                                    System.out.println("TwentyFortEight.signSlide: " + x + ", " + y + "is value " + board[x][y].value + " so the freedom of combo " + (x + (iOff * xinc)) + ", " + (y + (iOff * yinc)) + " has been set to " + board[x + (iOff * xinc)][y + (iOff * yinc)].freedom);
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException ack) {
                            break iOffLoop;
                        }
                    }
                }
            }
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                board[x][y].shoutStatus();
            }
        }

    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                batch.draw(
                        board[x][y].getTexture(),
                        board[x][y].thisTile.x,
                        board[x][y].thisTile.y);
            }
        }
		batch.end();
	}
}
