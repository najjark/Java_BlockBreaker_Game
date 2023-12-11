package brickGame;

/**
 * RestartGame class is responsible for resetting the game
 */
public class RestartGame {

    public RestartGame () {

    }

    /**
     * restartGame method is responsible for restarting the game back to level 1
     * @param main method takes in an instance of main to reset values to initial values
     */
    public void restartGame(Main main) {

        try {
            GameEngine engine = main.getEngine();

            // stop the existing game engine if it's running
            if (engine != null) {
                engine.stop();
            }

            main.gameState.level = 0;
            main.gameState.heart = 3;
            main.gameState.score = 0;
            main.gameState.vX = 0.800;
            main.gameState.destroyedBlockCount = 0;
            main.gameState.resetCollideFlags();
            main.gameState.goDownBall = true;

            main.gameState.isExistHeartBlock = false;
            main.gameState.isPaddleSmall =false;
            main.gameState.hitTime = 0;
            main.gameState.time = 0;
            main.gameState.paddleSmalltime = 0;

            // reset power-up-related variables
            main.gameState.isSizeBoost = false;
            main.gameState.isGoldStatus = false;
            main.gameState.sizeBoostTime = 0;
            main.gameState.goldTime = 0;
            main.gameState.ballRadius = 10; // reset the ball's radius to its original size
            // reset the pause flag
            main.gameState.isPaused = false;

            main.board.gameState.blocks.clear();
            main.board.gameState.chocos.clear();

            // remove existing nodes from the scene
            main.root.getChildren().clear();

            // reset the ball position to the spawn point
            main.gameState.xBall = 250;
            main.gameState.yBall = 500.0f;

            main.start(main.primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}