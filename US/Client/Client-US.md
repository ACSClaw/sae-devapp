***

# [US] - En tant que client, je veux pouvoir Créer un compte #1
**Labels :** US, must

### Description :
En tant que client, je veux créer un compte afin de pouvoir passer commande.

### Critères d'acceptation :
* Possibilité pour un client de créer son compte sur le site web.
* Champs obligatoires (Nom, Prénom, Num, Email…).

### Tâches :
* Créer une Interface de création de compte.
* Enregistrement des données du client dans la base de données.

***

# [US] - En tant que client, je veux me déconnecter #2

**Labels :** US, must

### Description

En tant que client connecté, je veux me déconnecter afin de sécuriser mon compte, notamment sur un appareil partagé.

### Critères d'acceptation

- Un bouton « Déconnexion » visible dans la page de paramètres quand le client est connecté.
- Au clic, la session/le token est invalidé et le client est redirigé vers la page d'accueil.
- Après déconnexion, l'accès aux pages réservées est refusé, y compris avec le bouton « Retour » du navigateur.

### Tâches

- Ajouter le bouton de déconnexion dans les paramètres
- Invalider la session/le token côté serveur

# [US] - En tant que client, je veux modifier mes informations personnelles #3

**Labels :** US, must

### Description

En tant que client, je veux modifier mes informations personnelles afin de garder mon profil à jour (coordonnées, adresse, mot de passe).

### Critères d'acceptation

- Le client connecté accède à une page « Mon profil » préremplie avec ses informations actuelles.
- Il peut modifier nom, prénom, e-mail, adresse, téléphone et mot de passe.
- Les mêmes règles de validation que pour la création de compte s'appliquent (e-mail valide et unique, champs obligatoires).
- Le changement de mot de passe exige la saisie de l'ancien mot de passe.
- Un message de confirmation s'affiche après enregistrement, et les nouvelles données sont visibles immédiatement.

### Tâches

- Créer la page de profil et le formulaire d'édition
- Gérer le changement des informations dans la base de données
- Gérer le changement de mot de passe

# [US] - En tant que client, je veux consulter l'historique de mes commandes avec leur statut #4

**Labels :** US, could

### Description

En tant que client, je veux consulter l'historique de mes commandes et leur statut afin de suivre mes achats.

### Critères d'acceptation

- Le client connecté voit la liste de ses commandes, de la plus récente à la plus ancienne.
- Chaque commande affiche : numéro, date, montant total et statut (ex. En préparation, Expédiée, Livrée, Annulée).
- Le client peut ouvrir le détail d'une commande (articles, quantités, prix, adresse de livraison).
- Si le client n'a aucune commande, un message explicite s'affiche (« Vous n'avez pas encore passé de commande »).

### Tâches

- Créer la page de liste et le détail des commandes
- Afficher le statut par état

***

## [US] - En tant que client, je veux pouvoir profiter des réductions #5

**Labels :** US, must

### Description :
En tant que client je veux ajouter un pack au panier pour pouvoir profiter de la réduction sur les achats groupés.

### Critères d'acceptation :
* Possibilité pour un client d’ajouter des packs dans un panier.
* Appliquer les réductions si un pack est ajouté au panier.

### Tâches :
* Créer une Interface de panier.
* Affichage des réductions disponibles et appliquées.

***

# [US] - En tant que client, je veux ajouter un produit au panier #6

**Labels :** US, must

### Description

En tant que client, je veux ajouter un produit à mon panier afin de pouvoir le commander plus tard.

### Critères d'acceptation

- Un bouton « Ajouter au panier » est visible sur la page de chaque produit disponible.
- Au clic, le produit est ajouté au panier avec la quantité définie et la taille chosie.
- Si le produit est déjà dans le panier, sa quantité est incrémentée par le nombre choisi au lieu de créer une nouvelle ligne.
- Un message de confirmation s'affiche.
- Un produit en rupture de stock ne peut pas être ajouté : le bouton est désactivé ou un message explicite s'affiche.
- La quantité ajoutée ne peut pas dépasser le stock disponible.

### Tâches

- Ajouter le bouton « Ajouter au panier » sur la page produit
- Ajouter la logique de quantitée dans le panier


# [US] - En tant que client, je veux modifier mon panier #7

**Labels :** US, must

### Description

En tant que client, je veux modifier le contenu de mon panier afin d'ajuster ma commande avant de la valider.

### Critères d'acceptation

- Le client accède à une page « Mon panier » listant les produits ajoutés (nom, prix unitaire, quantité, sous-total, total).
- Il peut augmenter ou diminuer la quantité d'un produit, dans la limite du stock disponible.
- Il peut supprimer un produit du panier.
- Si la quantité passe à 0, le produit est retiré du panier.
- Le sous-total et le total du panier est recalculé immédiatement après chaque modification.
- Si le panier est vide, un message explicite s'affiche (« Votre panier est vide »).
- Le contenu du panier est conservé entre les pages et après rechargement.

### Tâches

- Créer la page « Mon panier » et l'affichage des lignes
- Gérer la modification de quantité et la suppression d'un produit
- Recalculer et afficher le total du panier
- petite modif ddd