package brickGame;

import java.util.Random;

public class CreateBoard {

    GameState gameState = new GameState();

    public CreateBoard() {
        Random random = new Random();  // Move Random instance outside the loop

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < gameState.level + 2; j++) {
                int r = random.nextInt(500);

                // Adjusted conditions to ensure better block distribution
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2 && !gameState.isExistHeartBlock) {
                    type = Block.BLOCK_HEART;
                    gameState.isExistHeartBlock = true;
                } else if (r % 10 == 5) {
                    type = Block.BLOCK_STAR;
                } else if (r % 10 == 4) {
                    type = Block.BLOCK_SIZEBOOST;
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_PADDLESMALL;
                }else {
                    type = Block.BLOCK_NORMAL;
                }

                gameState.blocks.add(new Block(j, i, gameState.colors[r % gameState.colors.length], type));
            }
        }
    }

}
