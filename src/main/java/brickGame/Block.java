package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

/**
 * Block class is responsible for collision detection between the ball and the blocks
 */
public class Block implements Serializable {
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);

    public int row;
    public int column;


    public boolean isDestroyed = false;

    private Color color;
    public int type;

    public int x;
    public int y;

    private int width = 100;
    private int height = 30;
    private int paddingTop = height * 2;
    private int paddingH = 50;
    public Rectangle rect;


    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;

    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static int BLOCK_SIZEBOOST = 103;
    public static  int BLOCK_PADDLESMALL = 104;

    public static double buffer = 3; // Adjust this buffer as needed

    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    /**
     * draw method is responsible for filling colors and pictures in blocks
     */
    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if(type == BLOCK_SIZEBOOST) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if(type == BLOCK_PADDLESMALL) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }

    /**
     * checkHitToBlock method checks which side the ball collides with the block
     * @param xBall x position of the ball
     * @param yBall y position of the ball
     * @param ball ball object
     * @return the method returns the corresponding side that the ball made contact with the block, and if it is destroyed it returns no hit
     */
    public int checkHitToBlock(double xBall, double yBall, CreateBall ball) {

        if (isDestroyed) {
            return NO_HIT;
        }

        if (yBall >= y && yBall <= y + height + ball.getRadius() && xBall >= x + width - buffer && xBall <= x + width + buffer) {
            //System.out.println("Collision with right side of the block!");
            return HIT_RIGHT;
        }

        if (yBall >= y && yBall <= y + height +  ball.getRadius() && xBall >= x - buffer && xBall <= x + buffer) {
            //System.out.println("Collision with left side of the block!");
            return HIT_LEFT;
        }

        if (yBall >= y - ball.getRadius() - buffer && yBall <= y + buffer && xBall >= x && xBall <= x + width) {
            // System.out.println("Collision with top side of the block!");
            return HIT_TOP;
        }

        if (yBall >= y + height - ball.getRadius() && yBall <= y + height + buffer && xBall >= x && xBall <= x + width) {
        //System.out.println("Collision with bottom side of the block!");
        return HIT_BOTTOM;
        }

        return NO_HIT;
    }

    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }
}



        //double buffer = 2.5; // Adjust this buffer as needed




        /* if (yBall + Main.ballRadius >= y - buffer && yBall - Main.ballRadius <= y + height + buffer && xBall + Main.ballRadius >= x + width - buffer && xBall - Main.ballRadius <= x + width + buffer) {
            return HIT_RIGHT;
        }

        if (yBall + Main.ballRadius >= y - buffer && yBall - Main.ballRadius <= y + height + buffer && xBall + Main.ballRadius >= x - buffer && xBall - Main.ballRadius <= x + buffer) {
            return HIT_LEFT;
        } */



        /* if (xBall >= x && xBall <= x + width && yBall == y + height) {
            //System.out.println("Collision with bottom side of the block!");
            return HIT_BOTTOM;
        }

        if (xBall >= x && xBall <= x + width && yBall == y) {
            //System.out.println("Collision with top side of the block!");
            return HIT_TOP;
        } */






        /* if (xBall >= x && xBall <= x + width && yBall == y + height) {
            //System.out.println("Collision with bottom side of the block!");
            return HIT_BOTTOM;
        }

        if (xBall >= x && xBall <= x + width && yBall == y) {
            //System.out.println("Collision with top side of the block!");
            return HIT_TOP;
        } */