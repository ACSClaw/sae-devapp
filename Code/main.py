import sys
import copy

from gestion_fichiers import lire_config, lire_ressources, lire_csv

from algorithmes.fifo import executer_fifo
from algorithmes.round_robin import executer_rr

def formater_donnees(liste_processus, liste_energie):
    for p in liste_processus:
        p['date_soumission'] = int(p['date_soumission'])
        p['temps_execution'] = int(p['temps_execution'])
        p['ram'] = int(p['ram'])
        p['priorite'] = int(p['priorite'])
        p['deadline'] = int(p['deadline'])

    for e in liste_energie:
        e['pas_de_temps'] = int(e['pas_de_temps'])
        e['energie_disponible'] = int(e['energie_disponible'])

    return liste_processus, liste_energie


def main():
    if len(sys.argv) < 2:
        print("Use : python main.py <chemin_vers_config.txt>")
        sys.exit(1)

    chemin_config = sys.argv[1]

    config = lire_config(chemin_config)
    ressources = lire_json(config['fichier_ressources'])

    processus_bruts = lire_csv(config['fichier_processus'])
    energie_brute = lire_csv(config['fichier_energie'])
    liste_processus, liste_energie = formater_donnees(processus_bruts, energie_brute)

    politiques = config['politiques_a_executer'].split(',')
    quantum = int(config.get('quantum'))

    for politique in politiques:
        print(f"\nExécution de l'algo : {politique}")

        processus_pour_algo = copy.deepcopy(liste_processus)

        if politique == "FIFO":
            print("Simulation FIFO terminée.")

        elif politique == "RR":
            resultats = round_robin(liste_processus,ressources,liste_energie,quantum)

        else:
            print(f"Politique {politique} non reconnue.")
            continue

        sauvegarder_resultats(resultats, config, politique)
