package brickGame;

import javafx.application.Platform;

public class PhysicsEngine {
    GameState gameState = new GameState();
    private CreateBall ball;
    private Paddle rect;
    private GameEngine engine;
    private final Main main;

    public PhysicsEngine(Main main, CreateBall ball, Paddle rect) {
        this.main = main;
        this.ball = ball;
        this.rect = rect;
        this.gameState = main.gameState;
    }

    public void setPhysicsToBall() {
        gameState.v = ((gameState.time - gameState.hitTime) / 1000.000) + 1.000;

        if (gameState.goDownBall) {
            gameState.yBall += gameState.vY;
        } else {
            gameState.yBall -= gameState.vY;
        }

        if (gameState.goRightBall) {
            gameState.xBall += gameState.vX;
        } else {
            gameState.xBall -= gameState.vX;
        }


        // Adjust the ball's appearance for the size boost
        Platform.runLater(() -> {
            if (gameState.isSizeBoost) {
                //gameState.ballRadius = 18; // Adjust the radius for the boost
                ball.setRadius(15);
                Block.buffer = 7;
            } else {
                //gameState.ballRadius = 10; // Set it back to the normal radius
                ball.setRadius(10);
            }
            ball.setRadius(gameState.ballRadius); // Update the ball's radius
        });

        // Adjust the paddle's appearance for Small paddle condition
        Platform.runLater(() -> {
            if (gameState.isPaddleSmall) {
                gameState.breakHeight = 20;
                gameState.breakWidth = 65;
            } else {
                gameState.breakHeight = 30;
                gameState.breakWidth = 130;
            }
            rect.setWidth(gameState.breakWidth);
            rect.setHeight(gameState.breakHeight);
        });

        if (gameState.yBall <= 0) {
            //vX = 1.000;
            gameState.resetCollideFlags();
            gameState.goDownBall = true;
            return; // handles collision with the top of the screen
        }
        if (gameState.yBall >= gameState.sceneHeight) { // handles collision with the bottom of the screen
            gameState.goDownBall = false;
            if (!gameState.isGoldStatus) { // this if condition only runs if the ball is not gold
                //TODO gameover
                gameState.heart--;
                new Score().show(gameState.sceneWidth / 2, gameState.sceneHeight / 2, -1, this.main);

                if (gameState.heart == 0) { // if condition for game over
                    new Score().showGameOver(this.main);
                    engine.stop();
                }

            }
            return;
        }

        if (gameState.yBall >= gameState.yBreak - gameState.ballRadius) { // checks if ball has the same y-axis value as the platform
            //System.out.println("Collide1");
            if (gameState.xBall >= gameState.xBreak && gameState.xBall <= gameState.xBreak + gameState.breakWidth) { // checks if the ball hits the platform
                gameState.hitTime = gameState.time;
                gameState.resetCollideFlags();
                gameState.collideToBreak = true;
                gameState.goDownBall = false;

                double relation = (gameState.xBall - gameState.centerBreakX) / (gameState.breakWidth / 2); // distance of ball from paddle center

                if (Math.abs(relation) <= 0.3) { // condition responsible for how the ball bounces off of the platform
                    //vX = 0;
                    gameState.vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    gameState.vX = (Math.abs(relation) * 1.5) + (gameState.level / 3.500);
                    //System.out.println("vX " + vX);
                } else {
                    gameState.vX = (Math.abs(relation) * 2) + (gameState.level / 3.500);
                    //System.out.println("vX " + vX);
                }

                if (gameState.xBall - gameState.centerBreakX > 0) { // makes ball go right if hit right side of platform when coming from right
                    gameState.collideToBreakAndMoveToRight = true;
                } else { // same but for left
                    gameState.collideToBreakAndMoveToRight = false;
                }
                //System.out.println("Collide2");
            }
        }



        if (gameState.xBall >= gameState.sceneWidth) { // handles ball collision with right side of screen
            gameState.resetCollideFlags();
            //vX = 1.000;
            //collideToRightWall = true;
            gameState.goRightBall = false;
            return;
        }

        if (gameState.xBall <= 0) { // handles ball collision with left side of screen
            gameState.resetCollideFlags();
            //vX = 1.000;
            //collideToLeftWall = true;
            gameState.goRightBall = true;
            return;
        }

        if (gameState.collideToBreak) { // same as line 382
            if (gameState.collideToBreakAndMoveToRight) {
                gameState.goRightBall = true;
            } else {
                gameState.goRightBall = false;
            }
            return;
        }

        //Wall Collide

        //Block Collide

        if (gameState.collideToRightBlock) { // handles collision with each side of blocks
            gameState.goRightBall = true;
        }

        if (gameState.collideToLeftBlock) {
            gameState.goRightBall = false;
        }

        if (gameState.collideToTopBlock) {
            gameState.goDownBall = false;
        }

        if (gameState.collideToBottomBlock) {
            gameState.goDownBall = true;
        }
    }


}
