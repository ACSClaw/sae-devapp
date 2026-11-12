def preparer_processus(processus):
    """Convertit les textes du CSV en nombres et ajoute les infos de suivi."""
    liste = []
    for ligne in processus:
        p = {
            "id": ligne["id"],
            "date_soumission": int(ligne["date_soumission"]),
            "temps_execution": int(ligne["temps_execution"]),
            "ram": int(ligne["ram"]),
            "priorite": int(ligne["priorite"]),
            "temps_restant": int(ligne["temps_execution"]),
            "date_debut": None,      # premiere fois qu'il a un processeur
            "date_fin": None,        # quand il a fini
            "processeur": None,      # processeur utilise en ce moment
            "debut_morceau": None,   # debut du morceau d'execution en cours
            "quantum_utilise": 0,    # nombre de pas faits depuis qu'il a le processeur
        }
        liste.append(p)
    return liste


def lire_priorite(p):
    return p["priorite"]


def energie_au_pas(energie, t):
    """Energie disponible au pas t (apres la fin du fichier : on garde la derniere valeur)."""
    if t < len(energie):
        return float(energie[t]["energie_disponible"])
    return float(energie[-1]["energie_disponible"])


def tous_finis(processus):
    for p in processus:
        if p["date_fin"] is None:
            return False
    return True


def round_robin(processus, ressources, energie, quantum):
    quantum = int(quantum)
    processus = preparer_processus(processus)

    file_attente = []   # processus prets, dans l'ordre
    en_cours = []       # processus qui ont un processeur
    a_remettre = []     # processus qui ont fini leur quantum, a remettre en fin de file
    detaille = []       # morceaux d'execution : qui, quand, sur quel processeur
    energie_consommee = 0
    t = 0

    while not tous_finis(processus):

        # 1. Les processus qui arrivent maintenant entrent dans la file (plus prioritaire d'abord)
        arrivees = []
        for p in processus:
            if p["date_soumission"] == t:
                arrivees.append(p)
        arrivees.sort(key=lire_priorite, reverse=True)
        file_attente = file_attente + arrivees

        # 2. Ceux qui ont fini leur quantum vont en fin de file
        file_attente = file_attente + a_remettre
        a_remettre = []

        # 3. Combien de processeurs peut-on alimenter avec l'energie de ce pas ?
        nb_cpu = int(energie_au_pas(energie, t) // ressources["energie_par_processeur"])
        if nb_cpu > ressources["processeurs_total"]:
            nb_cpu = ressources["processeurs_total"]
        if nb_cpu == 0 and t >= len(energie):
            raise ValueError("Pas assez d'energie pour finir les processus")

        # 4. Si l'energie a baisse, on arrete des processus en trop (ils repassent en tete de file)
        while len(en_cours) > nb_cpu:
            p = en_cours.pop()
            detaille.append({"id": p["id"], "date_debut": p["debut_morceau"], "date_fin": t, "processeur": p["processeur"]})
            file_attente.insert(0, p)

        # 5. On donne les processeurs libres aux processus de la file, s'il y a assez de RAM
        ram_utilisee = 0
        cpu_occupes = []
        for p in en_cours:
            ram_utilisee = ram_utilisee + p["ram"]
            cpu_occupes.append(p["processeur"])

        for p in list(file_attente):   # list() = copie, car on retire des elements pendant la boucle
            if len(en_cours) == nb_cpu:
                break
            if ram_utilisee + p["ram"] <= ressources["ram_totale"]:
                for cpu in ressources["ids_processeur"]:
                    if cpu not in cpu_occupes:
                        p["processeur"] = cpu
                        cpu_occupes.append(cpu)
                        break
                p["debut_morceau"] = t
                p["quantum_utilise"] = 0
                if p["date_debut"] is None:
                    p["date_debut"] = t
                ram_utilisee = ram_utilisee + p["ram"]
                file_attente.remove(p)
                en_cours.append(p)

        # 6. Chaque processus en cours avance d'un pas de temps
        energie_consommee = energie_consommee + len(en_cours) * ressources["energie_par_processeur"]
        for p in list(en_cours):
            p["temps_restant"] = p["temps_restant"] - 1
            p["quantum_utilise"] = p["quantum_utilise"] + 1

            if p["temps_restant"] == 0 or p["quantum_utilise"] == quantum:
                detaille.append({"id": p["id"], "date_debut": p["debut_morceau"], "date_fin": t + 1, "processeur": p["processeur"]})
                en_cours.remove(p)
                if p["temps_restant"] == 0:
                    p["date_fin"] = t + 1      # termine
                else:
                    a_remettre.append(p)       # quantum fini, il retourne dans la file

        t = t + 1

    return {
        "global": faire_global(processus),
        "detaille": detaille,
        "metriques": calculer_metriques(processus, energie, energie_consommee, t),
    }


def faire_global(processus):
    resultat = []
    for p in processus:
        resultat.append({
            "id": p["id"],
            "date_soumission": p["date_soumission"],
            "date_debut": p["date_debut"],
            "date_fin": p["date_fin"],
            "ram": p["ram"],
        })
    return resultat


def calculer_metriques(processus, energie, energie_consommee, makespan):
    total_reponse = 0
    total_attente = 0
    for p in processus:
        reponse = p["date_fin"] - p["date_soumission"]
        total_reponse = total_reponse + reponse
        total_attente = total_attente + reponse - p["temps_execution"]

    energie_disponible = 0
    for t in range(makespan):
        energie_disponible = energie_disponible + energie_au_pas(energie, t)

    return {
        "temps_attente_moyen": total_attente / len(processus),
        "temps_reponse_moyen": total_reponse / len(processus),
        "makespan": makespan,
        "energie_consommee": energie_consommee,
        "energie_disponible": energie_disponible,
        "taux_utilisation": energie_consommee / energie_disponible,
    }


#python3 Code/algorithmes/round_robin.py
if __name__ == "__main__":
    import sys
    sys.path.append("Code")
    from gestion_fichiers import lire_csv, lire_json, lire_config

    dossier = "Inputs/Test1/"
    config = lire_config(dossier + "config.txt")
    resultats = round_robin(
        lire_csv(dossier + config["fichier_processus"]),
        lire_json(dossier + config["fichier_ressources"]),
        lire_csv(dossier + config["fichier_energie"]),
        config["quantum"],
    )

    for ligne in resultats["global"]:
        print(ligne)
    for ligne in resultats["detaille"]:
        print(ligne)
    print(resultats["metriques"])

    assert resultats["metriques"]["makespan"] == 14
    assert resultats["metriques"]["energie_consommee"] == 1300
