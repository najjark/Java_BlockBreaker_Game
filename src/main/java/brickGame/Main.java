package brickGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 *  Main function is responsible for starting the game and updating physics upon collision and applying effects if a special block is destroyed
 */
public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    GameState gameState = new GameState();
    private static int LEFT = 1;
    private static int RIGHT = 2;
    private CreateBall ball;
    private Paddle rect;
    public CreateBoard board;
    private GameEngine engine;
    private PhysicsEngine physicsEngine;
    private SaveGame saveGame = new SaveGame();
    private RestartGame restartGame = new RestartGame();
    private LoadGame loadGame = new LoadGame();

    /**
     * returns the game engine instance
     * @return game engine value
     */
    public GameEngine getEngine() {
        return engine;
    }

    //public static String savePath = "D:/save/save.mdds";

    public static final String savePath = "C:/Users/Khalid/Desktop/BlockBreakerTest";
    //public static String savePathDir = "D:/save/";
    public Pane root;
    public Label scoreLabel;
    public Label heartLabel;
    private Label levelLabel;
    //private Label pauseLabel;
    private boolean loadFromSave = false;
    Stage primaryStage;
    Button load = null;
    Button newGame = null;

    /**
     * start method responsible for setting the stage for the game such as creating the ball, the paddle, loading in a save if the load option is chosen
     * @param primaryStage start method takes an instance of Stage called primaryStage
     */
    @Override
    public void start(Stage primaryStage) { // start method takes in a Stage parameter
        try {
            this.primaryStage = primaryStage;

            rect = new Paddle(); // initialising paddle
            ball = new CreateBall(); // initialising the ball

            physicsEngine = new PhysicsEngine(this,ball, rect, engine);

            if (!loadFromSave) {
                gameState.level++;
                if (gameState.level > 1) {
                    new Score().showMessage("Level Up :)", this);
                }
                if (gameState.level == 4) {
                    new Score().showWin(this);
                    return;
                }

                root = new Pane();
                board = new CreateBoard();

                load = new Button("Load Game");
                newGame = new Button("Start New Game");
                load.setTranslateX(220);
                load.setTranslateY(300);
                newGame.setTranslateX(220);
                newGame.setTranslateY(340);
            }

            root = new Pane();
            scoreLabel = new Label("Score: " + gameState.score);
            levelLabel = new Label("Level: " + gameState.level);
            levelLabel.setTranslateY(20);
            heartLabel = new Label("Heart : " + gameState.heart);
            heartLabel.setTranslateX(gameState.sceneWidth - 70);

            if (!loadFromSave) {
                root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, newGame, load);
            } else {
                root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
            }

            for (Block block : board.gameState.blocks) {
                root.getChildren().add(block.rect);
            }

            Scene scene = new Scene(root, gameState.sceneWidth, gameState.sceneHeight);
            scene.getStylesheets().add("style.css");
            scene.setOnKeyPressed(this);

            primaryStage.setTitle("Game");
            primaryStage.setScene(scene);
            primaryStage.show();

            if (!loadFromSave) {
                if (gameState.level > 1 && gameState.level < 4) {
                    load.setVisible(false);
                    newGame.setVisible(false);
                    engine = new GameEngine();
                    engine.setOnAction(this);
                    engine.setFps(120);
                    engine.start();
                }

                load.setOnAction(event -> {
                    loadGame.loadGame(this);

                    load.setVisible(false);
                    newGame.setVisible(false);
                });

                newGame.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        engine = new GameEngine();
                        engine.setOnAction(Main.this);
                        engine.setFps(120);
                        engine.start();

                        load.setVisible(false);
                        newGame.setVisible(false);
                    }
                });
            } else {
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
                loadFromSave = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * handle method is used to perform certain operations if certain buttons are pressed
     * @param event parameter is used to identify if any significant buttons were pressed and to handle appropriately
     */
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:
                move(RIGHT);
                break;
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame.saveGame(this);
                break;
            case P:
                pauseGame();
                break;
            case R:
                restartGame.restartGame(this);
                break;
        }
    }

    /**
     * pauseGame method is responsible for pausing the game engine
     */
    private void pauseGame() { // method responsible for pausing game when user presses P
        gameState.isPaused = !gameState.isPaused;

        if (gameState.isPaused) {
            engine.stop();
        } else {
            engine.start();
        }
    }

    /**
     * move method is responsible for moving the paddle if the user presses the left or right arrow
     * @param direction parameter is used to determine which way the paddle should move
     */
    private void move(final int direction) { // move method to control the movement of the paddle
        if (!gameState.isPaused) {
            int step = (direction == RIGHT) ? 30 : -30;

            // Check if the new position is within bounds
            double newX = gameState.xBreak + step;
            if (newX >= 0 && newX <= gameState.sceneWidth - gameState.breakWidth) {
                // Create a Timeline for smooth movement
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0.1), event -> {
                            gameState.xBreak = newX;
                            rect.setX(gameState.xBreak);
                        })
                );

                // Play the animation
                timeline.play();
            }
        }
    }

    /**
     * checkDestroyed count method checks if all blocks in the current level were destroyed to determine whether to go to the next level or not
     */
    private void checkDestroyedCount() {
        if (gameState.destroyedBlockCount == board.gameState.blocks.size()) {
            //TODO win level todo...
            //System.out.println("You Win");

            nextLevel();
        }
    }

    /**
     * nextLevel method is responsible for going to the next level when all block in the current one are destroyed
     */
    private void nextLevel() { // nextLevel method responsible for taking user to the next level when all blocks are destroyed
        System.out.println("Entering next level");
        Platform.runLater(() -> {
            try {

                engine.stop(); // resetting the game to its original state to go to the next level
                gameState.resetCollideFlags();
                gameState.goDownBall = true;
                gameState.isGoldStatus = false;
                gameState.isExistHeartBlock = false;
                gameState.isSizeBoost = false;
                gameState.isPaddleSmall = false;
                gameState.hitTime = 0;
                gameState.time = 0;
                gameState.goldTime = 0;
                gameState.sizeBoostTime = 0;
                gameState.paddleSmalltime = 0;
                gameState.yBall = 500.0f;
                gameState.xBall = 250;
                board.gameState.blocks.clear();
                board.gameState.chocos.clear();
                gameState.destroyedBlockCount = 0;
                start(primaryStage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * onUpdate method is responsible for incrementing score and decrementing lives, it is also responsible for making blocks invisible when they are destroyed,
     * onUpdate is also responsible for activating power-ups or dropping bonuses when special blocks are destroyed
     */

    @Override
    public void onUpdate() {
        Platform.runLater(() -> {

            scoreLabel.setText("Score: " + gameState.score);
            heartLabel.setText("Heart : " + gameState.heart);

            rect.setX(gameState.xBreak);
            rect.setY(gameState.yBreak);
            ball.setCenterX(gameState.xBall);
            ball.setCenterY(gameState.yBall);

            for (Bonus choco : board.gameState.chocos) {
                choco.choco.setY(choco.y);
            }
        });


        if (gameState.yBall >= Block.getPaddingTop() && gameState.yBall <= (Block.getHeight() * (gameState.level + 1)) + Block.getPaddingTop()) {
            for (Block block : board.gameState.blocks) {
                synchronized (board.gameState.blocks) {
                    int hitCode = block.checkHitToBlock(gameState.xBall, gameState.yBall, ball);
                    if (hitCode != Block.NO_HIT) {

                        // Adjusted scoring logic for gold ball
                        if (gameState.isGoldStatus) {
                            gameState.score += 2; // Reward two points for gold ball
                        } else {
                            gameState.score += 1;
                        }

                        new Score().show(block.x, block.y, 1, this);

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        gameState.destroyedBlockCount++;
                        gameState.resetCollideFlags();

                        if (block.type == Block.BLOCK_PADDLESMALL) { // If a block of this type is destroyed the paddle becomes smaller for a set time
                            System.out.println("Small Paddle");
                            gameState.paddleSmalltime = gameState.time;
                            gameState.isPaddleSmall = true;
                        }

                        if (block.type == Block.BLOCK_SIZEBOOST) { // If a block of this type is destroyed the ball becomes larger for a set time
                            System.out.println("Size Boost");
                            gameState.sizeBoostTime = gameState.time;
                            gameState.ballRadius = 20;
                            gameState.isSizeBoost = true;
                        }

                        if (block.type == Block.BLOCK_CHOCO) { // If a block of this type is destroyed a bonus is dropped that gives the user bonus points if collected
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = gameState.time;
                            Platform.runLater(() -> root.getChildren().add(choco.choco));
                            board.gameState.chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) { // If a block of this type is destroyed the ball becomes gold and can pass through blocks, and the user will not lose lives if the ball touches
                            gameState.goldTime = gameState.time;
                            ball.setFill(new ImagePattern(new Image("goldball.png")));
                            System.out.println("gold ball");
                            root.getStyleClass().add("goldRoot");
                            gameState.isGoldStatus = true;
                        }

                        if (block.type == Block.BLOCK_HEART) {
                            gameState.heart++;
                        }

                        if (gameState.isGoldStatus) { // allows gold ball to skip collision detection
                            continue;
                        }
                        else {
                            if (hitCode == Block.HIT_RIGHT) {
                                gameState.collideToRightBlock = true;
                            } else if (hitCode == Block.HIT_BOTTOM) {
                                gameState.collideToBottomBlock = true;
                            } else if (hitCode == Block.HIT_LEFT) {
                                gameState.collideToLeftBlock = true;
                            } else if (hitCode == Block.HIT_TOP) {
                                gameState.collideToTopBlock = true;
                            }
                        }
                    }
                }
                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }
    }

    @Override
    public void onInit() {

    }

    /**
     * onPhysics update controls how long each special effect lasts and for awarding points for a collected bonus
     */
    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        if (!gameState.isPaused) { // only updates physics if game is not paused
            physicsEngine.setPhysicsToBall();
        }

        if (gameState.time - gameState.goldTime > 5000) {
             gameState.isGoldStatus = false;
             ball.setFill(new ImagePattern(new Image("ball.png")));
             root.getStyleClass().remove("goldRoot");
        }


        if (gameState.isSizeBoost) {
            if (gameState.time - gameState.sizeBoostTime > 5000) {
                gameState.isSizeBoost = false;
                System.out.println("Size Boost ended");

                // Reset the ball's radius to its original size
                gameState.ballRadius = 10;

                //ball.setRadius(10);

                Block.buffer = 3;

                // Revert the ball's appearance to normal
                Platform.runLater(() -> {
                    ball.setFill(new ImagePattern(new Image("ball.png")));
                });
            }
        }

        if (gameState.isPaddleSmall) {
            if (gameState.time - gameState.paddleSmalltime > 5000) {
                gameState.isPaddleSmall = false;
                System.out.println("Paddle Small Ended");

                gameState.breakHeight = 30;
                gameState.breakWidth = 130;

                Platform.runLater(() -> {
                    rect.setFill(new ImagePattern(new Image("block.jpg")));
                });
            }
        }

        for (Bonus choco : board.gameState.chocos) {
            if (!choco.taken && rect.intersects(choco.choco.getBoundsInParent())) { // condition for giving bonus points if it touches platform
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                choco.choco.setVisible(false);
                gameState.score += 3;
                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((gameState.time - choco.timeCreated) / 1000.000) + 1.000;
        }
        //System.out.println("time is:" + time + " goldTime is " + goldTime);
    }

    /**
     * onTime method responsible for setting the time
     * @param time is used to keep track of time
     */
    @Override
    public void onTime(long time) {
        this.gameState.time = time;
    }
}