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
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    GameState gameState = new GameState();
    private static int LEFT = 1;
    private static int RIGHT = 2;
    private CreateBall ball;
    private Paddle rect;
    private CreateBoard board;
    private GameEngine engine;
    private PhysicsEngine physicsEngine;
    //public static String savePath = "D:/save/save.mdds";
    // Define a constant for the save directory
    public static final String savePath = "C:/Users/Khalid/Desktop/BlockBreakerTest";
    //public static String savePathDir = "D:/save/";
    public Pane root;
    private Label scoreLabel;
    private Label heartLabel;
    private Label levelLabel;
    //private Label pauseLabel;
    private boolean loadFromSave = false;
    Stage primaryStage;
    Button load = null;
    Button newGame = null;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;

            rect = new Paddle();
            ball = new CreateBall();

            physicsEngine = new PhysicsEngine(this,ball, rect);

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
                    loadGame();

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
                saveGame();
                break;
            case P:
                pauseGame();
                break;
            case R:
                restartGame();
                break;
        }
    }

    private void pauseGame() {
        gameState.isPaused = !gameState.isPaused;

        if (gameState.isPaused) {
            engine.stop();
        } else {
            engine.start();
        }
    }

    private void move(final int direction) {
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

    private void checkDestroyedCount() {
        if (gameState.destroyedBlockCount == board.gameState.blocks.size()) {
            //TODO win level todo...
            //System.out.println("You Win");

            nextLevel();
        }
    }

    private void saveGame() {
        new Thread(() -> {
            try {
                // Specify the filename
                String saveFileName = "GameSave";

                // Construct the full save path
                String savePath = Main.savePath + File.separator + saveFileName;

                // Create the File object
                File file = new File(savePath);

                // Print debug information
                System.out.println("Directory exists: " + file.getParentFile().exists());
                System.out.println("Directory path: " + file.getParentFile().getAbsolutePath());
                System.out.println("Full save path: " + savePath);
                System.out.println("File exists: " + file.exists());
                System.out.println("File path: " + file.getAbsolutePath());

                // Check if the parent directory exists, create if not
                if (!file.getParentFile().exists()) {
                    if (file.getParentFile().mkdirs()) {
                        System.out.println("Directory created: " + file.getParentFile().getAbsolutePath());
                    } else {
                        System.err.println("Failed to create directory: " + file.getParentFile().getAbsolutePath());
                        return;
                    }
                }

                // Add the try-catch block for file creation
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
                    outputStream.writeInt(gameState.level);
                    outputStream.writeInt(gameState.score);
                    outputStream.writeInt(gameState.heart);
                    outputStream.writeInt(gameState.destroyedBlockCount);
                    outputStream.writeDouble(gameState.xBall);
                    outputStream.writeDouble(gameState.yBall);
                    outputStream.writeDouble(gameState.xBreak);
                    outputStream.writeDouble(gameState.yBreak);
                    outputStream.writeDouble(gameState.centerBreakX);
                    outputStream.writeLong(gameState.time);
                    outputStream.writeLong(gameState.goldTime);
                    outputStream.writeDouble(gameState.vX);
                    outputStream.writeBoolean(gameState.isExistHeartBlock);
                    outputStream.writeBoolean(gameState.isGoldStatus);
                    outputStream.writeBoolean(gameState.isSizeBoost);
                    outputStream.writeLong(gameState.sizeBoostTime);
                    outputStream.writeBoolean(gameState.goDownBall);
                    outputStream.writeBoolean(gameState.goRightBall);
                    outputStream.writeBoolean(gameState.collideToBreak);
                    outputStream.writeBoolean(gameState.collideToBreakAndMoveToRight);
                    outputStream.writeBoolean(gameState.collideToRightWall);
                    outputStream.writeBoolean(gameState.collideToLeftWall);
                    outputStream.writeBoolean(gameState.collideToRightBlock);
                    outputStream.writeBoolean(gameState.collideToBottomBlock);
                    outputStream.writeBoolean(gameState.collideToLeftBlock);
                    outputStream.writeBoolean(gameState.collideToTopBlock);

                    ArrayList<BlockSerializable> blockSerializables = new ArrayList<>();
                    for (Block block : board.gameState.blocks) {
                        if (!block.isDestroyed) {
                            blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                        }
                    }

                    outputStream.writeObject(blockSerializables);

                    new Score().showMessage("Game Saved", Main.this);

                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error during file creation: " + e.getMessage());
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Unexpected Exception: " + e.getMessage());
            }
        }).start();
    }

    private void loadGame() {

        System.out.println("Load button clicked");

        new Thread(() -> {
            try {

                // Specify the filename
                String saveFileName = "GameSave";

                // Construct the full save path
                String savePath = Main.savePath + File.separator + saveFileName;

                // Create the File object
                File file = new File(savePath);

                // Check if the file exists
                if (!file.exists()) {
                    System.err.println("Save file does not exist.");
                    return;
                }

                // Print the file path
                System.out.println("Loading from file: " + file.getAbsolutePath());

                // Add the try-catch block for file reading
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                    // Read the game state
                    gameState.level = inputStream.readInt();
                    gameState.score = inputStream.readInt();
                    gameState.heart = inputStream.readInt();
                    gameState.destroyedBlockCount = inputStream.readInt();
                    gameState.xBall = inputStream.readDouble();
                    gameState.yBall = inputStream.readDouble();
                    gameState.xBreak = inputStream.readDouble();
                    gameState.yBreak = inputStream.readDouble();
                    gameState.centerBreakX = inputStream.readDouble();
                    gameState.time = inputStream.readLong();
                    gameState.goldTime = inputStream.readLong();
                    gameState.sizeBoostTime = inputStream.readLong();
                    gameState.isSizeBoost = inputStream.readBoolean();
                    gameState.vX = inputStream.readDouble();
                    gameState.isExistHeartBlock = inputStream.readBoolean();
                    gameState.isGoldStatus = inputStream.readBoolean();
                    gameState.goDownBall = inputStream.readBoolean();
                    gameState.goRightBall = inputStream.readBoolean();
                    gameState.collideToBreak = inputStream.readBoolean();
                    gameState.collideToBreakAndMoveToRight = inputStream.readBoolean();
                    gameState.collideToRightWall = inputStream.readBoolean();
                    gameState.collideToLeftWall = inputStream.readBoolean();
                    gameState.collideToRightBlock = inputStream.readBoolean();
                    gameState.collideToBottomBlock = inputStream.readBoolean();
                    gameState.collideToLeftBlock = inputStream.readBoolean();
                    gameState.collideToTopBlock = inputStream.readBoolean();

                    // Print loaded values
                    System.out.println("Loaded Level: " + gameState.level);
                    System.out.println("Loaded Score: " + gameState.score);
                    System.out.println("Loaded Heart: " + gameState.heart);
                    System.out.println("Loaded Destroyed Block Count: " + gameState.destroyedBlockCount);

                    ArrayList<BlockSerializable> blockSerializables = (ArrayList<BlockSerializable>) inputStream.readObject();

                    board.gameState.blocks.clear();

                    // Update the blocks ArrayList with the deserialized data
                    for (BlockSerializable blockSerializable : blockSerializables) {
                        Color color = board.gameState.colors[blockSerializable.type % board.gameState.colors.length]; // Use the type as an index to get a color from the colors array
                        Block block = new Block(blockSerializable.row, blockSerializable.j, color, blockSerializable.type);
                        board.gameState.blocks.add(block);
                    }

                    // Print the contents of the blocks list
                    System.out.println("Number of Blocks after deserialization: " + board.gameState.blocks.size());
                    for (Block block : board.gameState.blocks) {
                        System.out.println("Block: " + block);  // Assuming Block class has a meaningful toString method
                    }

                    new Score().showMessage("Game Loaded", Main.this);

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.err.println("Error during file reading: " + e.getMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Unexpected Exception: " + e.getMessage());
            }
        }).start();
    }

    private void nextLevel() {
        System.out.println("Entering next level");
        Platform.runLater(() -> {
            try {
                gameState.vX = 1.000;

                engine.stop();
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

                //engine.stop();
                board.gameState.blocks.clear();
                board.gameState.chocos.clear();
                gameState.destroyedBlockCount = 0;
                start(primaryStage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void restartGame() {

        try {

            // stop the existing game engine if it's running
            if (engine != null) {
                engine.stop();
            }

            gameState.level = 0;
            gameState.heart = 3;
            gameState.score = 0;
            gameState.vX = 1.000;
            gameState.destroyedBlockCount = 0;
            gameState.resetCollideFlags();
            gameState.goDownBall = true;

            gameState.isExistHeartBlock = false;
            gameState.isPaddleSmall =false;
            gameState.hitTime = 0;
            gameState.time = 0;
            gameState.paddleSmalltime = 0;

            // reset power-up-related variables
            gameState.isSizeBoost = false;
            gameState.isGoldStatus = false;
            gameState.sizeBoostTime = 0;
            gameState.goldTime = 0;
            gameState.ballRadius = 10; // reset the ball's radius to its original size
            // reset the pause flag
            gameState.isPaused = false;

            board.gameState.blocks.clear();
            board.gameState.chocos.clear();

            // remove existing nodes from the scene
            root.getChildren().clear();

            // reset the ball position to the spawn point
            gameState.xBall = 250;
            gameState.yBall = 500.0f;

            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                        //System.out.println("size is " + blocks.size());
                        gameState.resetCollideFlags();

                        if (block.type == Block.BLOCK_PADDLESMALL) {
                            System.out.println("Small Paddle");
                            gameState.paddleSmalltime = gameState.time;
                            gameState.isPaddleSmall = true;
                        }

                        if (block.type == Block.BLOCK_SIZEBOOST) {
                            System.out.println("Size Boost");
                            gameState.sizeBoostTime = gameState.time;
                            gameState.ballRadius = 20;
                            gameState.isSizeBoost = true;
                        }

                        if (block.type == Block.BLOCK_CHOCO) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = gameState.time;
                            Platform.runLater(() -> root.getChildren().add(choco.choco));
                            board.gameState.chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) {
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


    @Override
    public void onTime(long time) {
        this.gameState.time = time;
    }
}