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
	//Texture img;
    NumberTile[][] board;
    InputManage inputs;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1080, 720);
		//img = new Texture("badlogic.jpg");
        board = new NumberTile[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                board[x][y] = new NumberTile(x, y);
                board[x][y].value = 0;
            }
        }
        NumberTile.setUpTextures();
        inputs = new InputManage(this);
        Gdx.input.setInputProcessor(inputs);

        board[0][3].value = 3;
        board[2][3].value = 3;
        board[3][3].value = 3;

        //signSlide(1, 0);
	}



    public void signSlide (int xinc, int yinc) {
        yinc *= -1;
        int base = -1;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                base = board[x][y].value;
                if (base == 0) { //tell other tiles they are free to move 1 square because I am empty
                    iOffLoop:
                    for (int iOff = 1; iOff < 4; iOff++) { //for all tiles in this direction
                        try { //until the edge of the board is reached
                            board[x + (iOff * xinc)][y + (iOff * yinc)].freedom++;
                            System.out.println("TwentyFortEight.signSlide: " + x + ", " + y + " is value 0 so the freedom of " + (x + (iOff * xinc)) + ", " + (y + (iOff * yinc)) + " has been set to " + board[x + (iOff * xinc)][y + (iOff * yinc)].freedom);
                        } catch (ArrayIndexOutOfBoundsException ack) {
                            break iOffLoop;
                        }
                    }
                } else { //tell other tiles what to do since I am not empty
                    iOffLoop:
                    for (int iOff = 1; iOff < 4; iOff++) { //for all tiles in this direction
                        try { //until the edge of the array is reached
                            if (board[x + (iOff * xinc)][y + (iOff * yinc)].value != board[x][y].value && board[x + (iOff * xinc)][y + (iOff * yinc)].value != 0) { break iOffLoop; }
                            // ^^^ cancel loop before telling any tiles they can move because my nearest neighbor cannot combine with me and isn't empty
                            if ((!board[x][y].combine) && (!board[x + (iOff * xinc)][y + (iOff * yinc)].combine)) { //if neither I nor my neighbor in question is marked for combination
                                if (board[x + (iOff * xinc)][y + (iOff * yinc)].value == base) { //if I can combine with that neighbor
                                    board[x][y].combine = true; //I will combine
                                    board[x + (iOff * xinc)][y + (iOff * yinc)].combine = true; //my neighbor will combine
                                    board[x + (iOff * xinc)][y + (iOff * yinc)].freedom++; //my neighbor's freedom increases
                                    System.out.println("TwentyFortyEight.signSlide: " + x + ", " + y + "is value " + board[x][y].value + " so the freedom of combo " + (x + (iOff * xinc)) + ", " + (y + (iOff * yinc)) + " has been set to " + board[x + (iOff * xinc)][y + (iOff * yinc)].freedom);
                                    System.out.println("    TwentyFortyEight.signSlide: trying to update the freedom of the tiles 1 and 2 sqaures farther from it, which may go out of bounds and end the loop");
                                    board[x + ((iOff+1) * xinc)][y + ((iOff+1) * yinc)].freedom++; //and the tile 1 farther
                                    board[x + ((iOff+2) * xinc)][y + ((iOff+2) * yinc)].freedom++; //and the tile 1 farther
                                    System.out.println("    TwentyFortyEight.signSlide: but it didn't");
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

        NumberTile.xinc = xinc;
        NumberTile.yinc = yinc;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                board[x][y].value += (board[x][y].combine? 1 : 0);
            }
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                board[x][y].nextValue = 0;
            }
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                    board[x + (board[x][y].freedom * -xinc)][y + (board[x][y].freedom * -yinc)].nextValue = board[x][y].value;

            }
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                board[x][y].value = board[x][y].nextValue;
            }
        }

        board[3][3].value = 2;


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
                        board[x][y].getScreenX(),
                        board[x][y].getScreenY());
            }
        }
		batch.end();
	}
}
