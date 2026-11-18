def ordonnancer_fifo(processus, ressources, energie):
    processeurs = ressources["ids_processeur"]
    ram_totale = int(ressources["ram_totale"])
    energie_par_processeur = float(ressources["energie_par_processeur"])
    if not processeurs or energie_par_processeur <= 0:
        raise ValueError("Les ressources doivent contenir des processeurs et une consommation positive.")

    taches = []
    for ordre, source in enumerate(processus):
        tache = {
            "id": source["id"],
            "arrivee": int(source["date_soumission"]),
            "duree": int(source["temps_execution"]),
            "ram": int(source["ram"]),
            "priorite": int(source["priorite"]),
            "ordre": ordre,
            "restant": int(source["temps_execution"]),
            "segments": [],
            "debut_segment": None,
            "debut": None,
            "fin": None,
            "cpu": None,
        }
        if tache["duree"] <= 0:
            raise ValueError(f"Le processus {tache['id']} doit avoir un temps d'exécution positif.")
        if tache["ram"] > ram_totale:
            raise ValueError(f"Le processus {tache['id']} dépasse la RAM totale disponible.")
        taches.append(tache)

    taches.sort(key=lambda tache: (tache["arrivee"], -tache["priorite"], tache["ordre"]))
    energie_par_temps = {int(ligne["pas_de_temps"]): float(ligne["energie_disponible"]) for ligne in energie}
    if not taches:
        return {"global": [], "detaille": [], "metriques": {}}
    if not energie_par_temps:
        raise ValueError("Le fichier energie.csv ne contient aucune donnée.")

    dernier_pas_energie = max(energie_par_temps)

    en_attente = []
    actifs = []
    index_arrivee = 0
    nombre_termine = 0
    energie_consommee = 0.0
    temps = 0

    while nombre_termine < len(taches):
        if temps in energie_par_temps:
            energie_du_pas = energie_par_temps[temps]
        elif temps > dernier_pas_energie:
            energie_du_pas = energie_par_temps[dernier_pas_energie]
        else:
            raise ValueError(
                f"Le profil energie.csv ne contient pas de donnée pour le pas {temps}."
            )

        while index_arrivee < len(taches) and taches[index_arrivee]["arrivee"] <= temps:
            en_attente.append(taches[index_arrivee])
            index_arrivee += 1

        capacite = max(0, min(int(energie_du_pas // energie_par_processeur), len(processeurs)))
        ram_occupee = sum(tache["ram"] for tache in actifs)

        while len(actifs) < capacite:
            index = next(
                (i for i, tache in enumerate(en_attente) if tache["ram"] <= ram_totale - ram_occupee),
                None,
            )
            if index is None:
                break

            tache = en_attente.pop(index)
            cpu_utilises = {active["cpu"] for active in actifs}
            tache.update({
                "cpu": next(cpu for cpu in processeurs if cpu not in cpu_utilises),
                "debut": temps,
                "debut_segment": temps,
            })
            actifs.append(tache)
            ram_occupee += tache["ram"]

        en_execution = actifs[:capacite]
        for tache in actifs[capacite:]:
            if tache["debut_segment"] is not None:
                tache["segments"].append({
                    "id": tache["id"],
                    "date_debut_execution": tache["debut_segment"],
                    "date_fin_execution": temps,
                    "id_processeur": tache["cpu"],
                })
                tache["debut_segment"] = None

        energie_consommee += len(en_execution) * energie_par_processeur
        for tache in en_execution:
            if tache["debut_segment"] is None:
                tache["debut_segment"] = temps
            tache["restant"] -= 1
            if tache["restant"] == 0:
                tache["fin"] = temps + 1
                tache["segments"].append({
                    "id": tache["id"],
                    "date_debut_execution": tache["debut_segment"],
                    "date_fin_execution": temps + 1,
                    "id_processeur": tache["cpu"],
                })
                tache["debut_segment"] = None
                nombre_termine += 1

        actifs = [tache for tache in actifs if tache["restant"] > 0]
        temps += 1

    resultat_global = []
    resultat_detaille = []
    for tache in taches:
        resultat_global.append({
            "id": tache["id"],
            "date_soumission": tache["arrivee"],
            "date_debut_execution": tache["debut"],
            "date_fin_execution": tache["fin"],
            "ram_demandee": tache["ram"],
            "ram_utilisee": tache["ram"],
        })
        resultat_detaille.extend(tache["segments"])

    resultat_detaille.sort(key=lambda segment: (segment["date_debut_execution"], segment["id"]))
    makespan = max(tache["fin"] for tache in taches)
    temps_reponse = sum(tache["debut"] - tache["arrivee"] for tache in taches) / len(taches)
    temps_attente = sum(tache["fin"] - tache["arrivee"] - tache["duree"] for tache in taches) / len(taches)
    energie_disponible = sum(
        energie_par_temps.get(instant, energie_par_temps[dernier_pas_energie])
        for instant in range(makespan)
    )

    return {
        "global": resultat_global,
        "detaille": resultat_detaille,
        "metriques": {
            "temps_attente_moyen": temps_attente,
            "temps_reponse_moyen": temps_reponse,
            "makespan": makespan,
            "energie_consommee_totale": energie_consommee,
            "energie_disponible_totale": energie_disponible,
            "taux_utilisation_energie": (
                energie_consommee / energie_disponible if energie_disponible else 0
            ),
        },
    }