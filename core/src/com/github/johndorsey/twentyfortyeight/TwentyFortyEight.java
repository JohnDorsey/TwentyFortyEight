package com.github.johndorsey.twentyfortyeight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class TwentyFortyEight extends ApplicationAdapter {
	SpriteBatch batch;
	private OrthographicCamera camera;
    NumberTile[][] board;
    InputManage inputs;
    Random rnd;
    GameManager gameManager;

    public BitmapFont font;

    NumberTile newOne;

    public static boolean xvEnabled = false;
    public static boolean yvEnabled = false;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1080, 720);
        board = new NumberTile[4][4];
        makeEmptyBoard();
        newOne = new NumberTile(4, 4);
        newOne.value = 0;

        gameManager = new GameManager(this);


        NumberTile.parentGame = this;
        NumberTile.setUpTextures();
        inputs = new InputManage(this);
        Gdx.input.setInputProcessor(inputs);

        font = new BitmapFont();
        font.setColor(Color.BLACK);

        rnd = new Random();


        addRandomTile();
        addRandomTile();
        NumberTile.xinc = 0;
        NumberTile.yinc = 0;
        NumberTile.animate();
	}

    public void makeEmptyBoard() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                board[x][y] = new NumberTile(x, y);
                board[x][y].value = 0;
            }
        }
    }


    public void playerSlide(int xinc, int yinc) {
        boolean reverse =  (xinc < 0 || yinc < 0);
        if (reverse) { rotateBoard(); xinc *= -1; yinc *= -1; }
        slide(xinc, yinc);
        if (reverse) { rotateBoard(); xinc *= -1; yinc *= -1; }
        NumberTile.xinc = xinc;
        NumberTile.yinc = yinc;
        NumberTile.animate();
        addRandomTile();
        gameManager.updateCurrentScore();
    }









    public boolean emptyTilesExist() {
        for (NumberTile xt[] : board) {
            for (NumberTile yt : xt) {
                if (yt.value == 0) { return true; }
            }
        }
        return false;
    }

    public boolean slide(int xinc, int yinc) {
        boolean result = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                board[x][y].freedom = 0;
                board[x][y].combine = false;
            }
        }
        for (int x = 0; x != 4; x++) {
            for (int y = 0; y != 4; y ++) {
                if (board[x][y].value == 0) { //tell other tiles they are free to move 1 square because I am empty
                    becauseIAmEmpty(x, y, xinc, yinc);
                } else { //tell other tiles what to do since I am not empty
                    becauseIAmNotEmptyNW(x, y, xinc, yinc);
                }
            }
        }
        lockEmptyTiles();
        //for (int x = 0; x < 4; x++) { for (int y = 0; y < 4; y++) { board[x][y].shoutStatus(); } }
        //textRender("value"); textRender("willCombine"); textRender("nextValue"); textRender("freedom"); System.out.println("running code to change next value");
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                //System.out.println(x + ", " + y);
                if (board[x + (board[x][y].freedom * -xinc)][y + (board[x][y].freedom * -yinc)].nextValue == 0) {
                    board[x + (board[x][y].freedom * -xinc)][y + (board[x][y].freedom * -yinc)].nextValue = (board[x][y].value+ (board[x][y].combine ? 1 : 0));
                }
                //System.out.println("because tile " + x + ", " + y + " has freedom:" + board[x][y].freedom + " value:" + board[x][y].value + ", it'll move to " + (x + (board[x][y].freedom * -xinc)) + ", " + (y + (board[x][y].freedom * -yinc)) + " whose value's " + board[x + (board[x][y].freedom * -xinc)][y + (board[x][y].freedom * -yinc)].value + ". it " + (board[x][y].combine? "will" : "will NOT") + " combine with that tile.");
            }
        }
        for (NumberTile xt[] : board) {
            for (NumberTile yt : xt) {
                result = result || yt.freedom != 0;
            }
        }
        return result;
    }

    public void becauseIAmEmpty( int x, int y, int xinc, int yinc) {
        iOffLoop:
        for (int iOff = 1; iOff < 4; iOff++) { //for all tiles in this direction
            try { //until the edge of the board is reached
                board[x + (iOff * xinc)][y + (iOff * yinc)].freedom++;
                //System.out.println("TwentyFortEight.signSlide: " + x + ", " + y + " is value 0 so the freedom of " + (x + (iOff * xinc)) + ", " + (y + (iOff * yinc)) + " has been set to " + board[x + (iOff * xinc)][y + (iOff * yinc)].freedom);
            } catch (ArrayIndexOutOfBoundsException ack) {
                break iOffLoop;
            }
        }
    }

    public void becauseIAmNotEmptyNW(int x, int y, int xinc, int yinc) {
        iOffLoop:
        for (int iOff = 1; iOff < 4; iOff++) { //for all tiles in this direction
            try { //until the edge of the array is reached
                if (board[x + (iOff * xinc)][y + (iOff * yinc)].value != board[x][y].value && board[x + (iOff * xinc)][y + (iOff * yinc)].value != 0) { break iOffLoop; }
                // ^^^ cancel loop before telling any tiles they can move because my nearest neighbor cannot combine with me and isn't empty
                if ((!board[x][y].combine) && (!board[x + (iOff * xinc)][y + (iOff * yinc)].combine)) { //if neither I nor my neighbor in question is marked for combination
                    if (board[x + (iOff * xinc)][y + (iOff * yinc)].value == board[x][y].value) { //if I can combine with that neighbor
                        board[x][y].combine = true; //I will combine
                        board[x + (iOff * xinc)][y + (iOff * yinc)].combine = true; //my neighbor will combine
                        board[x + (iOff * xinc)][y + (iOff * yinc)].freedom++; //my neighbor's freedom increases
                        //System.out.println("TwentyFortyEight.signSlide: " + x + ", " + y + "is value " + board[x][y].value + " so the freedom of combo " + (x + (iOff * xinc)) + ", " + (y + (iOff * yinc)) + " has been set to " + board[x + (iOff * xinc)][y + (iOff * yinc)].freedom);
                        //System.out.println("    TwentyFortyEight.signSlide: trying to update the freedom of the tiles 1 and 2 sqaures farther from it, which may go out of bounds and end the loop");
                        board[x + ((iOff+1) * xinc)][y + ((iOff+1) * yinc)].freedom++; //and the tile 1 farther
                        board[x + ((iOff+2) * xinc)][y + ((iOff+2) * yinc)].freedom++; //and the tile 1 farther than that
                        //System.out.println("    TwentyFortyEight.signSlide: but it didn't");
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ack) { //there are no more tiles to tell that I am not empty
                break iOffLoop; //stop looking for new tiles to tell where there are none
            }
        }
    }

    public void lockEmptyTiles() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (board[x][y].value == 0) { board[x][y].freedom = 0; } //empty tiles can't move!
            }
        }
    }

    public void rotateBoard() {
        rotateBoard90();
        rotateBoard90();
    }

    public void rotateBoard90() {
        int nextFreedoms[][] = new int[4][4];
        int nextValues[][] = new int[4][4];
        int nextNextValues[][] = new int[4][4];
        boolean nextCombines[][] = new boolean[4][4];
        boolean nextAddeds[][] = new boolean[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                nextValues[y][x] = board[3-x][y].value;
                nextNextValues[y][x] = board[3-x][y].nextValue;
                nextFreedoms[y][x] = board[3-x][y].freedom;
                nextCombines[y][x] = board[3-x][y].combine;
                nextAddeds[y][x] = board[3-x][y].added;
            }
        }
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                board[x][y].value = nextValues[x][y];
                board[x][y].nextValue = nextNextValues[x][y];
                board[x][y].freedom = nextFreedoms[x][y];
                board[x][y].combine = nextCombines[x][y];
                board[x][y].added = nextAddeds[x][y];
            }
        }
    }

    public boolean addRandomTile() {
        int emptyCount = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                emptyCount += (board[x][y].nextValue ==0 )? 1 : 0;
            }
        }
        if (emptyCount == 0) { return false; }
        int toFill = rnd.nextInt(emptyCount) + 1;
        find:
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (board[x][y].nextValue == 0) { toFill--; }
                if (toFill == 0) {
                    board[x][y].nextValue = rnd.nextDouble() > 0.5? 2 : 3; board[x][y].added = true;
                    newOne.added = true;
                    newOne.value = board[x][y].nextValue;
                    newOne.x = x; newOne.y = y;
                    newOne.nextValue = 0;
                    break find;
                }

            }
        }
        return true;
    }

    public void textRender(String dat) {
        if (dat=="all") {
            textRender("value"); textRender("nextValue"); textRender("freedom"); textRender("combine");
        } else {
            System.out.println("/------------------------\\" + dat);
            for (int y = 3; y >= 0; y--) {
                System.out.print("[");
                for (int x = 0; x < 4; x++) {
                    System.out.print(" ." + ((dat == "value") ? board[x][y].value : ((dat == "nextValue") ? board[x][y].nextValue : ((dat == "freedom") ? board[x][y].freedom : ((board[x][y].combine ? "#" : " "))))) + ". ");
                }
                System.out.println("]");
            }
            System.out.println("\\------------------------/");
        }
    }

	@Override
	public void render () {
        NumberTile.timePasses(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //font.draw(batch, (int) (1.0f / ft) + " fps", 200, 200);
        //font.draw(batch, "score: " + gameManager.currentScore, 200, 200);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                batch.draw(board[x][y].getTexture(), board[x][y].getScreenX(), board[x][y].getScreenY(), board[x][y].getScreenWidth(), board[x][y].getScreenHeight());
            }
        }
        batch.draw(newOne.getTexture(), newOne.getScreenX() + newOne.getSizeAdjust(), newOne.getScreenY() + newOne.getSizeAdjust(), NumberTile.weightedMean(0, NumberTile.tileWidth, NumberTile.usedProgress()), NumberTile.weightedMean(0, NumberTile.tileHeight, NumberTile.usedProgress()));
        gameManager.render();
		batch.end();
	}




}
