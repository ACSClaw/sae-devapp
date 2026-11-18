import csv
import json
from pathlib import Path


def lire_csv(chemin_fichier):
    info = []
    with open(chemin_fichier, mode='r', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        
        for row in reader:
            info.append(dict(row))
            
    return info


def lire_config(chemin_fichier):
    config = {}
    with open(chemin_fichier, mode='r', encoding='utf-8') as fichier:
        for ligne in fichier:
            if '=' in ligne:
                cle, valeur = ligne.split('=', 1)
                config[cle.strip()] = valeur.strip()
                
    return config


def lire_json(chemin_fichier):
    with open(chemin_fichier, mode='r', encoding='utf-8') as fichier:
        ressources = json.load(fichier)
        
    return ressources


def generer_nom_fichier(chemin_base, politique):
    chemin = Path(chemin_base)
    nouveau_nom = f"{politique}_{chemin.name}"
    chemin_final = chemin.parent / nouveau_nom
    
    return str(chemin_final)


def ecrire_liste_dicos_csv(liste_donnees, chemin_fichier):
    noms_colonnes = liste_donnees[0].keys()
    with open(chemin_fichier, mode='w', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=noms_colonnes)
        writer.writeheader()        
        writer.writerows(liste_donnees)


def ecrire_metriques_csv(dico_metriques, chemin_fichier):
    noms_colonnes = dico_metriques.keys()
    with open(chemin_fichier, mode='w', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=noms_colonnes)
        writer.writeheader()
        writer.writerow(dico_metriques)


def sauvegarder_resultats(resultats, config, politique):
    chemin_global = generer_nom_fichier(config['fichier_sortie_global'], politique)
    chemin_detaille = generer_nom_fichier(config['fichier_sortie_detaille'], politique)
    chemin_metriques = generer_nom_fichier(config['fichier_sortie_metriques'], politique)

    ecrire_liste_dicos_csv(resultats["global"], chemin_global)
    ecrire_liste_dicos_csv(resultats["detaille"], chemin_detaille)
    ecrire_metriques_csv(resultats["metriques"], chemin_metriques)