package fr.iutblagnac.s3.configuration;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

// Formulaire de modification du config.txt, dont la mise en page est dans ConfigurationPane.fxml
public class ConfigurationPane extends VBox {

    // Éléments du FXML, reliés par leur fx:id
    @FXML private Label cheminConfig;
    @FXML private TextField ressources;
    @FXML private TextField processus;
    @FXML private TextField energie;
    @FXML private TextField sortieGlobal;
    @FXML private TextField sortieDetaille;
    @FXML private TextField sortieMetriques;
    @FXML private HBox politiques; // contient une case à cocher par algorithme
    @FXML private TextField quantum;
    @FXML private Label succes;
    @FXML private Label erreur;

    private final Path racine; // dossier Partie-Python
    private final Map<String, TextField> champs = new LinkedHashMap<>(); // clé de config.txt -> champ de saisie

    private Path fichier; // config.txt actuellement ouvert
    // Contenu du fichier, pour conserver les clés que le formulaire ne montre pas
    private Map<String, String> valeurs = new LinkedHashMap<>();
    // Formulaire tel qu'au dernier chargement, pour savoir s'il a été modifié depuis
    private Map<String, String> reference = new LinkedHashMap<>();

    /**
     * Crée le formulaire à partir du FXML.
     *
     * @param racine dossier Partie-Python (chemin absolu)
     */
    public ConfigurationPane(Path racine) {
        this.racine = racine;

        FXMLLoader chargeur = new FXMLLoader(ConfigurationPane.class.getResource("ConfigurationPane.fxml"));
        chargeur.setRoot(this);
        chargeur.setController(this);
        try {
            chargeur.load();
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible de charger ConfigurationPane.fxml", e);
        }

        champs.put("fichier_ressources", ressources);
        champs.put("fichier_processus", processus);
        champs.put("fichier_energie", energie);
        champs.put("fichier_sortie_global", sortieGlobal);
        champs.put("fichier_sortie_detaille", sortieDetaille);
        champs.put("fichier_sortie_metriques", sortieMetriques);
    }

    /**
     * Action commune aux six boutons "Parcourir…" : remplit le champ associé au bouton.
     *
     * @param evenement clic sur l'un des boutons
     */
    @FXML
    private void parcourir(ActionEvent evenement) {
        // Dans le FXML, l'id de chaque bouton est la clé du fichier qu'il permet de choisir
        Button bouton = (Button) evenement.getSource();
        String cle = bouton.getId();
        TextField champ = champs.get(cle);

        String extension = "*.csv";
        if (cle.equals("fichier_ressources")) {
            extension = "*.json";
        }
        boolean sortie = Configuration.SORTIES.contains(cle);

        Path choisi = demander("Choisir un fichier", extension, sortie, resoudre(champ.getText()));
        if (choisi != null) {
            champ.setText(Configuration.versTexte(racine, choisi));
        }
    }

    /**
     * Action du bouton "Ouvrir…" : charge un autre config.txt.
     * Demande confirmation si des modifications n'ont pas été enregistrées.
     */
    @FXML
    private void choisirConfig() {
        if (!lireFormulaire().equals(reference)) {
            Alert alerte = new Alert(Alert.AlertType.CONFIRMATION,
                    "Les modifications non enregistrées seront perdues. Continuer ?",
                    ButtonType.YES, ButtonType.NO);
            alerte.showAndWait();
            if (alerte.getResult() != ButtonType.YES) {
                return;
            }
        }
        Path choisi = demander("Ouvrir une configuration", "*.txt", false, trouverDepart());
        if (choisi != null) {
            ouvrir(choisi);
        }
    }

    /**
     * Action du bouton "Enregistrer" : écrit dans le fichier ouvert.
     */
    @FXML
    private void clicEnregistrer() {
        if (fichier != null) {
            enregistrer(fichier);
        }
    }

    /**
     * Action du bouton "Enregistrer sous…" : demande d'abord où écrire.
     */
    @FXML
    private void clicEnregistrerSous() {
        Path choisi = demander("Enregistrer la configuration", "*.txt", true, trouverDepart());
        if (choisi != null) {
            enregistrer(choisi);
        }
    }

    /**
     * Donne l'endroit où ouvrir le sélecteur pour choisir un config.txt.
     *
     * @return le fichier ouvert, ou le dossier Partie-Python s'il n'y en a pas
     */
    private Path trouverDepart() {
        if (fichier == null) {
            return racine;
        }
        return fichier;
    }

    /**
     * Transforme un chemin saisi dans le formulaire en chemin complet.
     *
     * @param texte chemin à convertir en objet Path
     * @return      le chemin complet, ou le dossier Partie-Python si le texte est invalide
     */
    private Path resoudre(String texte) {
        try {
            return racine.resolve(texte.trim());
        } catch (InvalidPathException e) {
            return racine;
        }
    }

    /**
     * Ouvre le sélecteur de fichier, dans le dossier du fichier actuel.
     *
     * @param nom        titre de la fenêtre
     * @param extension  extension à afficher, par exemple "*.csv"
     * @param sauvegarde true pour enregistrer un fichier, false pour en ouvrir un
     * @param actuel     fichier (ou dossier) de départ
     * @return           le chemin absolu du fichier choisi, ou null si l'utilisateur annule
     */
    private Path demander(String nom, String extension, boolean sauvegarde, Path actuel) {
        FileChooser selecteur = new FileChooser();
        selecteur.setTitle(nom);
        selecteur.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension, extension));

        Path dossier = actuel;
        if (!Files.isDirectory(actuel)) {
            dossier = actuel.getParent();
            if (sauvegarde) {
                selecteur.setInitialFileName(actuel.getFileName().toString());
            }
        }
        // Le sélecteur refuse de s'ouvrir dans un dossier qui n'existe pas
        if (!Files.isDirectory(dossier)) {
            dossier = racine;
        }
        selecteur.setInitialDirectory(dossier.toFile());

        Window fenetre = getScene().getWindow();
        File choisi;
        if (sauvegarde) {
            choisi = selecteur.showSaveDialog(fenetre);
        } else {
            choisi = selecteur.showOpenDialog(fenetre);
        }

        if (choisi == null) {
            return null;
        }
        return choisi.toPath().toAbsolutePath();
    }

    /**
     * Charge un config.txt dans le formulaire.
     *
     * @param nouveau chemin absolu du fichier à ouvrir
     */
    public void ouvrir(Path nouveau) {
        try {
            valeurs = Configuration.lire(nouveau);
        } catch (IOException e) {
            afficherErreur("Lecture impossible : " + nouveau);
            return;
        }
        fichier = nouveau;
        cheminConfig.setText(Configuration.versTexte(racine, nouveau));

        for (String cle : champs.keySet()) {
            champs.get(cle).setText(valeurs.getOrDefault(cle, ""));
        }
        quantum.setText(valeurs.getOrDefault(Configuration.QUANTUM, ""));
        cocherPolitiques(valeurs.getOrDefault(Configuration.POLITIQUES, ""));

        reference = lireFormulaire();
        afficherErreur(""); // efface le message précédent
    }

    /**
     * Coche les algorithmes présents dans la configuration et décoche les autres.
     *
     * @param texte noms des algorithmes séparés par des virgules, par exemple "FIFO,RR"
     */
    private void cocherPolitiques(String texte) {
        List<String> choisies = new ArrayList<>();
        for (String nom : texte.split(",")) {
            choisies.add(nom.trim());
        }
        for (Node noeud : politiques.getChildren()) {
            CheckBox caseACocher = (CheckBox) noeud;
            caseACocher.setSelected(choisies.contains(caseACocher.getText()));
        }
    }

    /**
     * Récupère les algorithmes cochés.
     *
     * @return leurs noms séparés par des virgules, par exemple "FIFO,RR"
     */
    private String lirePolitiques() {
        List<String> choisies = new ArrayList<>();
        for (Node noeud : politiques.getChildren()) {
            CheckBox caseACocher = (CheckBox) noeud;
            if (caseACocher.isSelected()) {
                choisies.add(caseACocher.getText());
            }
        }
        // Surtout pas d'espace après la virgule, main.py ne reconnaîtrait plus l'algorithme
        return String.join(",", choisies);
    }

    /**
     * Récupère ce que l'utilisateur a saisi.
     *
     * @return les valeurs du fichier ouvert, mises à jour avec celles du formulaire
     */
    private Map<String, String> lireFormulaire() {
        Map<String, String> resultat = new LinkedHashMap<>(valeurs);
        for (String cle : champs.keySet()) {
            resultat.put(cle, champs.get(cle).getText().trim());
        }
        resultat.put(Configuration.POLITIQUES, lirePolitiques());
        resultat.put(Configuration.QUANTUM, quantum.getText().trim());
        return resultat;
    }

    /**
     * Enregistre le formulaire, sauf s'il contient des erreurs.
     *
     * @param cible fichier à écrire
     */
    private void enregistrer(Path cible) {
        Map<String, String> nouvelles = lireFormulaire();
        List<String> erreurs = Configuration.verifier(nouvelles, racine);
        if (!erreurs.isEmpty()) {
            afficherErreur("Non enregistré :\n" + String.join("\n", erreurs));
            return;
        }
        try {
            Configuration.ecrire(nouvelles, cible);
        } catch (IOException e) {
            afficherErreur("Écriture impossible : " + cible);
            return;
        }
        // Après un "Enregistrer sous", le nouveau fichier devient celui qui est ouvert
        ouvrir(cible);
        succes.setVisible(true);
    }

    /**
     * Affiche une erreur en bas du formulaire, à la place du message de réussite.
     *
     * @param texte message à afficher, vide pour ne rien afficher
     */
    private void afficherErreur(String texte) {
        succes.setVisible(false);
        erreur.setText(texte);
    }
}
