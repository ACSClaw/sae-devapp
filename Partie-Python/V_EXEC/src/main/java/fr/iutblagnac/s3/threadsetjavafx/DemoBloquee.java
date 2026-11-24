package fr.iutblagnac.s3.threadsetjavafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Exemple de départ : le bouton bloque volontairement l'interface. */
public class DemoBloquee extends Application {
    private final Label message = new Label("Prêt");
    private int compteur = 0;

    public static void runApp(String[] args) {
        Application.launch(args);
        System.out.println("Fin");
    }

    @Override
    public void start(Stage stage) {
        Button lancer = new Button("Lancer un calcul de 5 secondes");
        lancer.setOnAction(event -> calculer());
        Button options = new Button("Options");
        options.setOnAction(event -> afficherOptions());

        VBox racine = new VBox(12, lancer, options, this.message);
        racine.setStyle("-fx-padding: 24;");
        stage.setScene(new Scene(racine, 360, 140));
        stage.setTitle("Démonstration JavaFX");
        stage.show();
        System.out.println("Start");
    }

    private void calculer() {
        this.compteur++;
        this.message.setText("Calcul en cours... : "+this.compteur);
        try {
            Thread.sleep(5_000);
            this.message.setText("Calcul terminé : "+this.compteur);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            this.message.setText("Calcul interrompu");
        }
    }

    private void afficherOptions() {
        Alert dialogue = new Alert(Alert.AlertType.INFORMATION,
                "Les options sont accessibles.", ButtonType.OK);
        dialogue.setTitle("Options");
        dialogue.setHeaderText(null);
        dialogue.showAndWait();
    }
}
