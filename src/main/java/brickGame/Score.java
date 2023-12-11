package brickGame;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
//import sun.plugin2.message.Message;

/**
 * Score class is responsible for printing messages when a block is destroyed, or when a heart is deducted, and displaying a win message when the user beats the last level
 */
public class Score {
    GameState gameState = new GameState();
    RestartGame restartGame = new RestartGame();
    GameEngine engine = new GameEngine();
    CreateBoard board = new CreateBoard();

    /**
     *
     * @param x used to determine what x coordinate to print the message at
     * @param y used to determine what y coordinate to print the message at
     * @param score parameter is used to determine what value to print for the score
     * @param main instance of main is used to update the values appropriately
     */
    public void show(final double x, final double y, int score, final Main main) {
        String sign;
        if (score >= 0) {
            sign = "+";
        } else {
            sign = "";
        }

        //Adjust the score for gold ball
        if (gameState.isGoldStatus) {
            score = 2;
        }

        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> {
                    main.root.getChildren().add(label);
                    ScaleTransition ani = new ScaleTransition(Duration.millis(800), label);
                    ani.setFromX(0);
                    ani.setToX(10.0);
                    ani.setFromY(0);
                    ani.setToY(10.0f);
                    ani.setCycleCount(1);
                    ani.play();
                    ani.setOnFinished(event -> main.root.getChildren().remove(label));
                });
    }

    /**
     * showMessage method is used to print messages on key events
     * @param message is used to determine the message to be printed
     * @param main instance of main is used to edit the stage
     */
    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> {
            main.root.getChildren().add(label);
            ScaleTransition ani = new ScaleTransition(Duration.millis(500), label);
            ani.setFromX(10.0);
            ani.setToX(0);
            ani.setFromY(10.0);
            ani.setToY(0);
            ani.setCycleCount(1);
            ani.play();
            ani.setOnFinished(event -> main.root.getChildren().remove(label));
        });
    }

    /**
     * showGameOver method is used to print game over message when the game ends
     * @param main instance of main is used to update the game stage
     */
    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("Game Over :(");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Restart");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> restartGame.restartGame(main));

            main.root.getChildren().addAll(label, restart);

        });
    }

    /**
     * showWin method is used to show the win screen when the user wins the game
     * @param main instance of main is used to print messages to the stage
     */
    public void showWin(final Main main) {
        Platform.runLater(() -> {
            Label pauselabel = new Label("You Win :)");
            pauselabel.setTranslateX(200);
            pauselabel.setTranslateY(250);
            pauselabel.setScaleX(2);
            pauselabel.setScaleY(2);


            Button restart = new Button("Restart");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> restartGame.restartGame(main));

            main.root.getChildren().addAll(pauselabel, restart);

        });
    }


    /* public void showPause(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("Game Paused");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            main.root.getChildren().addAll(label);

        });
    } */
}