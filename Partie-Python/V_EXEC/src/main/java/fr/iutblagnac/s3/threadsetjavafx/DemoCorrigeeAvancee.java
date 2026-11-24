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

/** Démonstration d'un thread de fond qui informe l'interface à chaque étape. */
public class DemoCorrigeeAvancee extends Application {
    private static final int NOMBRE_ETAPES = 5;

    private final Label message = new Label("Prêt");
    private int compteur = 0;

    public static void runApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Button lancer = new Button("Lancer les étapes");
        lancer.setOnAction(event -> lancerTraitement(lancer));
        Button options = new Button("Options");
        options.setOnAction(event -> afficherOptions());

        VBox racine = new VBox(12, lancer, options, this.message);
        racine.setStyle("-fx-padding: 24;");
        stage.setScene(new Scene(racine, 360, 140));
        stage.setTitle("Démonstration JavaFX avancée");
        stage.show();
    }

    private void lancerTraitement(Button lancer) {
        lancer.setDisable(true);
        this.compteur++;
        int numeroLancement = this.compteur;
        this.message.setText("Démarrage du traitement " + numeroLancement);

        Thread traitement = new Thread(() -> {
            try {
                for (int etape = 1; etape <= NOMBRE_ETAPES; etape++) {
                    int finalEtape = etape;

                    effectuerEtape(etape);
                    Platform.runLater(() -> this.message.setText(
                            "Étape " + finalEtape + " / " + NOMBRE_ETAPES
                                    + " du traitement " + numeroLancement));

                    Thread.sleep(1000);
                }

                Platform.runLater(() -> {
                    this.message.setText("Traitement " + numeroLancement + " terminé");
                    lancer.setDisable(false);
                });
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    this.message.setText("Traitement interrompu");
                    lancer.setDisable(false);
                });
            }
        }, "traitement-par-etapes");
        traitement.start();
    }

    private void effectuerEtape(int etape) {
        // Simule un traitement long
        // Ici, on pourrait effectuer un calcul ou une opération de lecture/écriture
        // Un traitement qu'on ne veut pas exécuter sur le thread de l'interface graphique pour ne pas bloquer l'interface.
        // On affiche le résultat de l'étape dans la console pour le suivi
        System.out.println("Résultat de l'etape " + etape);
    }

    private void afficherOptions() {
        Alert dialogue = new Alert(Alert.AlertType.INFORMATION,
                "Les options sont accessibles.", ButtonType.OK);
        dialogue.setTitle("Options");
        dialogue.setHeaderText(null);
        dialogue.showAndWait();
    }
}
