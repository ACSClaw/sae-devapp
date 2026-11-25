package fr.iutblagnac.s3.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lecture, vérification et écriture du config.txt lu par le Python (lignes "cle=valeur").
 * Les valeurs sont gardées dans l'ordre du fichier, y compris les clés inconnues d'ici.
 */
public class Configuration {

    // Ces clés doivent rester identiques à celles attendues par main.py
    public static final List<String> ENTREES = List.of(
            "fichier_ressources", "fichier_processus", "fichier_energie");
    public static final List<String> SORTIES = List.of(
            "fichier_sortie_global", "fichier_sortie_detaille", "fichier_sortie_metriques");
    public static final String POLITIQUES = "politiques_a_exec";
    public static final String QUANTUM = "quantum";

    /**
     * Lit un config.txt de la même façon que lire_config côté Python.
     *
     * @param fichier fichier à lire
     * @return        les valeurs du fichier, rangées par clé
     * @throws IOException si le fichier ne peut pas être lu
     */
    public static Map<String, String> lire(Path fichier) throws IOException {
        // Une LinkedHashMap pour réécrire plus tard les lignes dans le même ordre
        Map<String, String> valeurs = new LinkedHashMap<>();
        for (String ligne : Files.readAllLines(fichier)) {
            int egal = ligne.indexOf('=');
            if (egal >= 0) { // les lignes sans "=" sont ignorées
                String cle = ligne.substring(0, egal).trim();
                String valeur = ligne.substring(egal + 1).trim();
                valeurs.put(cle, valeur);
            }
        }
        return valeurs;
    }

    /**
     * Écrit un config.txt, en remplaçant son ancien contenu.
     *
     * @param valeurs valeurs à écrire, rangées par clé
     * @param fichier fichier à écrire
     * @throws IOException si le fichier ne peut pas être écrit
     */
    public static void ecrire(Map<String, String> valeurs, Path fichier) throws IOException {
        List<String> lignes = new ArrayList<>();
        for (String cle : valeurs.keySet()) {
            lignes.add(cle + "=" + valeurs.get(cle));
        }
        Files.write(fichier, lignes);
    }

    /**
     * Prépare un chemin pour le config.txt : relatif à Partie-Python quand c'est possible.
     *
     * @param racine  dossier Partie-Python
     * @param fichier chemin absolu du fichier
     * @return        le chemin à écrire, toujours avec des "/"
     */
    public static String versTexte(Path racine, Path fichier) {
        Path chemin = fichier;
        if (fichier.startsWith(racine)) {
            chemin = racine.relativize(fichier);
        }
        // Sous Windows les chemins ont des "\", on uniformise pour tout le groupe
        return chemin.toString().replace('\\', '/');
    }

    /**
     * Cherche les problèmes qui feraient échouer le Python.
     *
     * @param valeurs valeurs à vérifier, rangées par clé
     * @param racine  dossier Partie-Python, auquel les chemins sont relatifs
     * @return        la liste des erreurs, vide si tout est bon
     */
    public static List<String> verifier(Map<String, String> valeurs, Path racine) {
        List<String> erreurs = new ArrayList<>();

        for (String cle : ENTREES) {
            Path chemin = trouverChemin(valeurs, cle, racine);
            if (chemin == null) {
                erreurs.add(cle + " : chemin vide ou invalide");
            } else if (!Files.isRegularFile(chemin)) {
                erreurs.add(cle + " : fichier introuvable");
            }
        }

        // Un fichier de sortie n'existe pas forcément encore, mais son dossier oui : le Python ne le crée pas
        List<Path> sorties = new ArrayList<>();
        for (String cle : SORTIES) {
            Path chemin = trouverChemin(valeurs, cle, racine);
            if (chemin == null) {
                erreurs.add(cle + " : chemin vide ou invalide");
            } else if (Files.isDirectory(chemin)) {
                erreurs.add(cle + " : c'est un dossier, pas un fichier");
            } else if (!Files.isDirectory(chemin.getParent())) {
                erreurs.add(cle + " : le dossier de sortie n'existe pas");
            } else if (sorties.contains(chemin)) {
                erreurs.add(cle + " : fichier déjà utilisé par une autre sortie");
            } else {
                sorties.add(chemin);
            }
        }

        if (valeurs.getOrDefault(POLITIQUES, "").isBlank()) {
            erreurs.add("Choisir au moins un algorithme");
        }
        // Vérifié même sans RR, car main.py convertit toujours le quantum en entier
        if (!estQuantumValide(valeurs.getOrDefault(QUANTUM, ""))) {
            erreurs.add("Le quantum doit être un entier supérieur ou égal à 1");
        }
        return erreurs;
    }

    /**
     * Donne le chemin complet d'un fichier de la configuration.
     *
     * @param valeurs valeurs de la configuration, rangées par clé
     * @param cle     clé du fichier, par exemple "fichier_energie"
     * @param racine  dossier Partie-Python
     * @return        le chemin complet, ou null s'il est vide ou invalide
     */
    private static Path trouverChemin(Map<String, String> valeurs, String cle, Path racine) {
        String valeur = valeurs.getOrDefault(cle, "");
        if (valeur.isBlank()) {
            return null;
        }
        try {
            // normalize enlève les ".." pour pouvoir comparer deux chemins entre eux
            return racine.resolve(valeur).normalize();
        } catch (InvalidPathException e) {
            return null;
        }
    }

    /**
     * Vérifie que le quantum est un entier d'au moins 1.
     *
     * @param texte quantum saisi
     * @return      true si le quantum est utilisable par le Python
     */
    private static boolean estQuantumValide(String texte) {
        try {
            return Integer.parseInt(texte) >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
