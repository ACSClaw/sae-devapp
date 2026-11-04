# [US] - En tant que client, je veux me déconnecter #2

**Labels :** `US` `must`

## Description

En tant que client connecté, je veux me déconnecter afin de sécuriser mon compte, notamment sur un appareil partagé.

## Critères d'acceptation

- Un bouton « Déconnexion » visible dans la page de paramètres quand le client est connecté.
- Au clic, la session/le token est invalidé et le client est redirigé vers la page d'accueil.
- Après déconnexion, l'accès aux pages réservées est refusé, y compris avec le bouton « Retour » du navigateur.

## Tâches

- Ajouter le bouton de déconnexion dans les paramètres
- Invalider la session/le token côté serveur

---

# [US] - En tant que client, je veux modifier mes informations personnelles #3

**Labels :** `US` `must`

## Description

En tant que client, je veux modifier mes informations personnelles afin de garder mon profil à jour (coordonnées, adresse, mot de passe).

## Critères d'acceptation

- Le client connecté accède à une page « Mon profil » préremplie avec ses informations actuelles.
- Il peut modifier nom, prénom, e-mail, adresse, téléphone et mot de passe.
- Les mêmes règles de validation que pour la création de compte s'appliquent (e-mail valide et unique, champs obligatoires).
- Le changement de mot de passe exige la saisie de l'ancien mot de passe.
- Un message de confirmation s'affiche après enregistrement, et les nouvelles données sont visibles immédiatement.

## Tâches

- Créer la page de profil et le formulaire d'édition
- Gérer le changement des informations dans la base de données
- Gérer le changement de mot de passe

---

# [US] - En tant que client, je veux consulter l'historique de mes commandes avec leur statut #4

**Labels :** `US` `should`

## Description

En tant que client, je veux consulter l'historique de mes commandes et leur statut afin de suivre mes achats.

## Critères d'acceptation

- Le client connecté voit la liste de ses commandes, de la plus récente à la plus ancienne.
- Chaque commande affiche : numéro, date, montant total et statut (ex. En préparation, Expédiée, Livrée, Annulée).
- Le client peut ouvrir le détail d'une commande (articles, quantités, prix, adresse de livraison).
- Si le client n'a aucune commande, un message explicite s'affiche (« Vous n'avez pas encore passé de commande »).

## Tâches

- Créer la page de liste et le détail des commandes
- Afficher le statut par état

***

## [US] - En tant que client, je veux pouvoir Créer un compte #5
**Labels :** US, must

**Description :**
En tant que client, je veux créer un compte afin de pouvoir passer commande.

**Critères d'acceptation :**
* Possibilité pour un client de créer son compte sur le site web.
* Champs obligatoires (Nom, prénom, Num, email…).

**Tâches :**
* Créer une Interface de création de compte.
* Enregistrement des données du client dans la base de données.

***

## [US] - En tant que client, je veux pouvoir Profiter des réductions #6
**Labels :** US, must

**Description :**
En tant que client je veux ajouter un pack au panier pour pouvoir profiter de la réduction sur les achats groupés.

**Critères d'acceptation :**
* Possibilité pour un client d’ajouter des packs dans un panier.
* Appliquer les réductions si un pack est ajouté au panier.

**Tâches :**
* Créer une Interface de panier.
* Affichage des réductions disponibles et appliquées.

***
