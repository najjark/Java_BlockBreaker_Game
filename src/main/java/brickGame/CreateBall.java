package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * CreateBall class is responsible for creating a ball and setting its spawn point
 */
public class CreateBall extends Circle {
    GameState gameState = new GameState();
    public CreateBall() {
        //System.out.println("Initial x-coordinate: " + (gameState.sceneWidth / 2.0));
        //System.out.println("Initial y-coordinate: " + (gameState.yBreak - 100));
        //Random random = new Random();
        setCenterX(250);  // Set x-coordinate to the center of the scene
        setCenterY(500);
        setRadius(10);
        setFill(new ImagePattern(new Image("ball.png")));

    }
}