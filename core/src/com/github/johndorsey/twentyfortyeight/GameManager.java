package com.github.johndorsey.twentyfortyeight;

/**
 * Created by John on 10/20/15.
 */
public class GameManager {
    public int currentScore;
    public TwentyFortyEight parentGame;

    public boolean acceptingMoves;
    public boolean acceptingSelects;

    public DialogueBox winDialogue;
    public DialogueBox loseDialogue;
    public DialogueBox menuDialogue;

    public boolean continuation;


    public GameManager(TwentyFortyEight parentGameInput) {
        parentGame = parentGameInput;
        acceptingMoves = false;
        acceptingSelects = true;
        winDialogue = new DialogueBox(parentGame, "Error!!", new String[] {"This should be a win message", "-", "-"}, new String[] {"-", "-"});
        loseDialogue = new DialogueBox(parentGame, "Error!!", new String[] {"This should be a loss message"}, new String[] {"-", "-"});
        menuDialogue = new DialogueBox(parentGame, "2048", new String[] {"high score: " + NoReset.highScore }, new String[] {"new game", "exit"});
        continuation = false;
        if (NoReset.openMenu) { menuDialogue.enabled = true; }
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

    public boolean valueExists(int value) {
        for (NumberTile xt[] : parentGame.board) {
            for (NumberTile yt : xt) {
                if (yt.value == value) { return true; }
            }
        }
        return false;
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
        acceptingMoves = false;
        acceptingSelects = true;
        if (continuation) {
            loseDialogue = new DialogueBox(parentGame, "Game Over", new String[] {getScoreMessage(true), " want to play again?"}, new String[] {"Play again", "Quit"});
        } else {
            loseDialogue = new DialogueBox(parentGame, "You Lose", new String[] {getScoreMessage(true), " want to play again?"}, new String[] {"Play again", "Quit"});
        }
        loseDialogue.enabled = true;
    }

    public String getScoreMessage(boolean over) {
        String result;
        result = "Your score " + ((over)?"was":"is") + " " + currentScore + ((currentScore > NoReset.highScore)?"... a new record!":".") ;
        return result;
    }

    public void checkWin() {
        if (!continuation) {
            if (valueExists(12)) {
                win();
            }
        }
    }

    public void win() {
        System.out.println("YOU WIN");
        updateCurrentScore();
        acceptingMoves = false;
        acceptingSelects = true;
        winDialogue = new DialogueBox(parentGame, "You Win!!", new String[] {"You've reached 2048!", getScoreMessage(false), "but It doesn't need to stop here.", "want to see how high you can go?"}, new String[] {"Continue", "I'm done"});
        winDialogue.enabled = true;
    }

    public void select(int direction) {
        if (winDialogue.enabled) { winDialogue.select(direction); }
        if (loseDialogue.enabled) { loseDialogue.select(direction); }
        if (menuDialogue.enabled) { menuDialogue.select(direction); }
    }

    public void enter() {
        if (winDialogue.enabled) {
            if (winDialogue.selectedButton==0) { continuation = true; winDialogue.enabled = false; acceptingMoves = true; acceptingSelects = false; }
            if (winDialogue.selectedButton==1) { continuation = false; winDialogue.enabled = false; NoReset.openMenu = true; stopGame(); }

        }
        if (loseDialogue.enabled) {
            if (loseDialogue.selectedButton==0) { loseDialogue.enabled = false; NoReset.openMenu = false; stopGame(); }
            if (loseDialogue.selectedButton==1) { loseDialogue.enabled = false; NoReset.openMenu = true; stopGame(); }
        }
        if (menuDialogue.enabled) {
            if (menuDialogue.selectedButton==0) { menuDialogue.enabled = false; NoReset.openMenu = false; acceptingSelects = false; acceptingMoves = true; }
            if (menuDialogue.selectedButton==1) { }
        }
    }

    public void render() {
        parentGame.font.draw(parentGame.batch, "Score: " + currentScore, 24, 280);
        parentGame.font.draw(parentGame.batch, "High Score: " + NoReset.highScore, 24, 268);
        if (winDialogue.enabled) { winDialogue.render(); }
        if (loseDialogue.enabled) { loseDialogue.render(); }
        if (menuDialogue.enabled) { menuDialogue.render(); }
    }

    public void stopGame() {
        if (NoReset.highScore < currentScore) { NoReset.highScore = currentScore; }
        parentGame.newGame();
    }





}
