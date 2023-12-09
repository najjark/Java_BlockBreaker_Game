package brickGame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SaveGame {

    private GameState gameState = new GameState();
    private CreateBoard board = new CreateBoard();

    public SaveGame() {

    }
    public void saveGame(Main main) {
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

                    new Score().showMessage("Game Saved",main);

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
}