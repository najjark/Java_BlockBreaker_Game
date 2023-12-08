package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle{
    GameState gameState = new GameState();
    public Paddle() {
        setWidth(gameState.breakWidth);
        setHeight(gameState.breakHeight);

        setX(gameState.xBreak);
        setY(gameState.yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));

        setFill(pattern);
    }
}