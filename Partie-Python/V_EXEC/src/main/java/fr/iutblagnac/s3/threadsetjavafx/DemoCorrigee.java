package fr.iutblagnac.s3.threadsetjavafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Correction de l'exemple : Thread de fond et Platform.runLater. */
public class DemoCorrigee extends Application {
    private final Label message = new Label("Prêt");
    private int compteur = 0;

    public static void runApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Button lancer = new Button("Lancer un calcul de 5 secondes");
        lancer.setOnAction(event -> lancerCalcul(lancer));
        Button options = new Button("Options");
        options.setOnAction(event -> afficherOptions());

        VBox racine = new VBox(12, lancer, options, this.message);
        racine.setStyle("-fx-padding: 24;");
        stage.setScene(new Scene(racine, 360, 140));
        stage.setTitle("Démonstration JavaFX corrigée");
        stage.show();
    }

    private void lancerCalcul(Button lancer) {
        lancer.setDisable(true);
        this.compteur++;
        this.message.setText("Calcul en cours... : "+this.compteur);

        Thread calcul = new Thread(() -> {
            try {
                Thread.sleep(5_000);
                Platform.runLater(() -> {
                    this.message.setText("Calcul terminé : "+this.compteur);
                    lancer.setDisable(false);
                });
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    this.message.setText("Calcul interrompu");
                    lancer.setDisable(false);
                });
            }
        }, "calcul-de-fond");
        calcul.start();
    }

    private void afficherOptions() {
        Alert dialogue = new Alert(Alert.AlertType.INFORMATION,
                "Les options sont accessibles.", ButtonType.OK);
        dialogue.setTitle("Options");
        dialogue.setHeaderText(null);
        dialogue.showAndWait();
    }
}
