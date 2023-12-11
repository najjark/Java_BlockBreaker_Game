package brickGame;

import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * LoadGame class is used to load in the state of the game
 */
public class LoadGame {

    private GameState gameState = new GameState();

    private CreateBoard board = new CreateBoard();

    public LoadGame() {

    }

    /**
     * loadGame method loads in saved game state when load button is pressed
     * @param main method takes in saved variables from main
     */
    public void loadGame(Main main) {

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

                    new Score().showMessage("Game Loaded", main);

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
}
