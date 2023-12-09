package brickGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class GameState {

    public int level;
    public double xBreak;
    public double centerBreakX;
    public double yBreak;
    public int breakWidth;
    public int breakHeight;
    public int halfBreakWidth;
    public int sceneWidth;
    public int sceneHeight;
    public double xBall;
    public double yBall;
    public boolean isGoldStatus;
    public boolean isSizeBoost;
    public boolean isPaddleSmall;
    public boolean isExistHeartBlock;
    public int ballRadius;
    public int destroyedBlockCount;
    public double v;
    public int heart;
    public int score;
    public long time;
    public long hitTime;
    public long goldTime;
    public long sizeBoostTime;
    public long paddleSmalltime;
    public boolean goDownBall;
    public boolean goRightBall;
    public boolean collideToBreak;
    public boolean collideToBreakAndMoveToRight;
    public boolean collideToRightWall;
    public boolean collideToLeftWall;
    public boolean collideToRightBlock;
    public boolean collideToBottomBlock;
    public boolean collideToLeftBlock;
    public boolean collideToTopBlock;
    public double vX;
    public double vY;
    public boolean isPaused;

    public ArrayList<Block> blocks;
    public ArrayList<Bonus> chocos;
    public Color[] colors = new Color[] {
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN
    };

    public GameState() {
        this.heart = 3;
        this.score = 0;
        this.time = 0;
        this.hitTime =0;
        this.goldTime = 0;
        this.sizeBoostTime = 0;
        this.paddleSmalltime = 0;
        this.v = 1.000;
        this.destroyedBlockCount = 0;
        this.isExistHeartBlock = false;
        this.isGoldStatus = false;
        this.isSizeBoost = false;
        this.isPaddleSmall = false;
        this.yBall = 500.0f;
        this.xBreak = 200.0f;
        this.yBreak = 660.0f;
        this.ballRadius = 10;
        this.xBall = 250;
        this.sceneHeight = 700;
        this.sceneWidth = 500;
        this.halfBreakWidth = 65;
        this.breakHeight = 30;
        this.breakWidth = 130;
        this.level = 0;
        this.centerBreakX =  xBreak + halfBreakWidth;
        this.goDownBall = true;
        this.goRightBall = true;
        this.collideToBreak = false;
        this.collideToBreakAndMoveToRight = true;
        this.collideToRightWall = false;
        this.collideToLeftWall = false;
        this.collideToRightBlock = false;
        this.collideToBottomBlock = false;
        this.collideToLeftBlock = false;
        this.collideToTopBlock = false;
        this.vX = 2.000;
        this.vY = 2.000;
        this.score = 0;
        this.isPaused = false;


        blocks = new ArrayList<Block>();
        chocos = new ArrayList<Bonus>();
        colors = new Color[]{
                Color.MAGENTA,
                Color.RED,
                Color.GOLD,
                Color.CORAL,
                Color.AQUA,
                Color.VIOLET,
                Color.GREENYELLOW,
                Color.ORANGE,
                Color.PINK,
                Color.SLATEGREY,
                Color.YELLOW,
                Color.TOMATO,
                Color.TAN,
        };


    }
}
