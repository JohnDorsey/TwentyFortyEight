package com.github.johndorsey.twentyfortyeight;

/**
 * Created by John on 10/20/15.
 */
public class GameManager {
    public int currentScore;
    public int highScore;
    public TwentyFortyEight parentGame;

    public boolean acceptingMoves;
    public boolean acceptingSelects;

    public DialogueBox winDialogue;
    public DialogueBox loseDialogue;


    public GameManager(TwentyFortyEight parentGameInput) {
        parentGame = parentGameInput;
        acceptingMoves = true;
        acceptingSelects = false;
        winDialogue = new DialogueBox(parentGame, "You Win!!", new String[] {"You've reached 2048!", "but It doesn't need to stop here.", "want to see how high you can go?"}, new String[] {"Continue", "Quit"});
        loseDialogue = new DialogueBox(parentGame, "You Lost", new String[] {" want to play again?"}, new String[] {"Play again", "Quit"});

    }

    public void updateCurrentScore() {
        currentScore = boardSum();
    }

    public int boardSum() {
        int displayedValueSum = 0;
        for (NumberTile xt[] : parentGame.board) {
            for (NumberTile yt : xt) {
                displayedValueSum += NumberTile.displayForValue(yt.value);
            }
        }
        return displayedValueSum;
    }

    public void checkDone() {
        checkWin(); checkLoss();
    }

    public void checkLoss() {
        if (!parentGame.emptyTilesExist()) {
            boolean anyDir = false;
            anyDir = anyDir || parentGame.slide(1, 0); NumberTile.clearSlideData();
            anyDir = anyDir || parentGame.slide(-1, 0); NumberTile.clearSlideData();
            anyDir = anyDir || parentGame.slide(0, 1); NumberTile.clearSlideData();
            anyDir = anyDir || parentGame.slide(0, -1); NumberTile.clearSlideData();
            if (!anyDir) { lose(); }
        }
    }

    public void lose() {
        System.out.println("YOU LOSE");
        updateCurrentScore();
    }

    public void checkWin() {
        if (valueExists(12)) {
            win();
        }
    }

    public boolean valueExists(int value) {
        for (NumberTile xt[] : parentGame.board) {
            for (NumberTile yt : xt) {
                if (yt.value == value) { return true; }
            }
        }
        return false;
    }

    public void win() {
        System.out.println("YOU WIN");
        updateCurrentScore();
        acceptingMoves = false;
        acceptingSelects = true;
        winDialogue.enabled = true;
    }

    public void select(int direction) {
        if (winDialogue.enabled) { winDialogue.select(direction); }
    }

    public void render() {
        parentGame.font.draw(parentGame.batch, "Score: " + currentScore, 200, 280);
        parentGame.font.draw(parentGame.batch, "High Score: " + highScore, 200, 268);
        if (winDialogue.enabled) { winDialogue.render(); }
    }





}
