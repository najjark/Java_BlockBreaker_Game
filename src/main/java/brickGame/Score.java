package brickGame;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
//import sun.plugin2.message.Message;

public class Score {
    public void show(final double x, final double y, int score, final Main main) {
        String sign;
        if (score >= 0) {
            sign = "+";
        } else {
            sign = "";
        }

        //Adjust the score for gold ball
        if (main.isGoldStatus) {
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
            restart.setOnAction(event -> main.restartGame());

            main.root.getChildren().addAll(label, restart);

        });
    }

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
            restart.setOnAction(event -> main.restartGame());

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