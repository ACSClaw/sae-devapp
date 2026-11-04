## [US] - En tant qu'administrateur, je veux pouvoir éditer le site web #1
**Labels :** US, must

**Description :**
En tant qu’administrateur, je souhaite pouvoir éditer la page du catalogue afin de pouvoir y ajouter nos nouveaux produits.

**Critères d'acceptation :**
* Possibilité d’éditer (Ajouter/Supprimer/Modifier) la page du catalogue.

**Tâches :**
* Synchro avec la base de données.
* Ajout de page automatique et modification facile et intuitif.

***

## [US] - En tant qu'administrateur, je veux pouvoir éditer l’information sur les clients #2
**Labels :** US, must

**Description :**
En tant qu’administrateur, je souhaite pouvoir éditer les informations des clients afin que leur compte soit à jour.

**Critères d'acceptation :**
* Possibilité d’éditer (Ajouter/Supprimer/Modifier) les informations des clients.

**Tâches :**
* Synchro avec la base de données.
* Créer une Interface de modification des informations des clients.

***

## [US] - En tant qu'administrateur, je veux pouvoir ajouter, modifier ou supprimer un produit #3
**Labels :** US, must

**Description :**
En tant qu'administrateur, je veux pouvoir ajouter, modifier ou supprimer un produit afin de maintenir le catalogue à jour.

**Critères d'acceptation :**
- Présence d'un formulaire pour renseigner les détails (nom, description, prix, image, catégorie).
- Possibilité de modifier un produit existant depuis une liste.
- Confirmation requise avant la suppression définitive d'un produit.

**Tâches :**
- Créer l'interface du catalogue côté administration.
- Mettre en place les requêtes SQL (Insert, Update, Delete) correspondantes.

***

## [US] - En tant qu'administrateur, je veux gérer la disponibilité d’un produit #4
**Labels :** US, must

**Description :**
En tant qu'administrateur, je veux gérer la disponibilité d’un produit (manuellement ou via le stock) pour éviter les commandes impossibles.

**Critères d'acceptation :**
- Synchronisation automatique avec le niveau de stock (passage en "Indisponible" si stock = 0).
- Toggle (bouton on/off) permettant de forcer l'indisponibilité d'un produit en stock.
- Possibilité de définir une date d'expiration pour les produits éphémères.
- Pouvoir décider quel objet doit pouvoir être restocké si stock épuisé.
  
**Tâches :**
- Développer la logique de vérification du stock en base de données avant l'ajout au panier.
- Modifier l'affichage front-end (griser le bouton d'achat, badge "Rupture de stock").
- Développer un trigger pour gérer le restock automatique si nécessaire.
  
***

## [US] - En tant qu'administrateur, je veux créer, modifier ou supprimer des catégories et sous-catégories #5
**Labels :** US, must

**Description :**
En tant qu'administrateur, je veux créer, modifier ou supprimer des catégories et sous-catégories pour structurer la navigation du catalogue.

**Critères d'acceptation :**
- Possibilité d'assigner une catégorie "parent" à une autre pour créer une sous-catégorie.
- Mise à jour automatique du menu de navigation sur le site client lors d'une modification.

**Tâches :**
- Créer l'interface de gestion de l'arborescence.
- Implémenter une règle de gestion en base de données pour les catégories supprimées, il ne doit y avoir aucun produit présent dans une catégorie ou sous-    catégorie avant suppression.

***

## [US] - En tant qu'administrateur, je veux pouvoir consulter et gérer les comptes clients #6
**Labels :** US, must

**Description :**
En tant qu'administrateur, je veux pouvoir consulter et gérer les comptes clients pour assurer le support utilisateur.

**Critères d'acceptation :**
- Accès à une liste des inscrits avec recherche par nom ou email.
- Possibilité de voir l'historique d'un client, de réinitialiser son mot de passe ou de suspendre son compte.

**Tâches :**
- Créer le tableau de bord des utilisateurs.
- Implémenter les actions de modération (suspendre/bannir) en base de données.

***

## [US] - En tant qu'administrateur, je veux pouvoir suivre les commandes des clients #7
**Labels :** US, must

**Description :**
En tant qu'administrateur, je veux pouvoir suivre les commandes des clients pour en assurer le traitement et l'expédition.

**Critères d'acceptation :**
- Tableau de bord listant les commandes triées par date (avec statut : en attente, expédiée, annulée).
- Vue détaillée affichant les produits commandés, le montant total, et l'adresse de livraison.

**Tâches :**
- Lier les tables "Commandes", "Lignes de commande" et "Clients" dans l'affichage.
- Ajouter la fonctionnalité de changement d'état de la commande.

***

## [US] - En tant qu'administrateur, je veux pouvoir modifier les contenus visuels et textuels des pages principales #8
**Labels :** US, should

**Description :**
En tant qu'administrateur, je veux pouvoir modifier les contenus visuels et textuels des pages principales pour animer le site sans toucher au code.

**Critères d'acceptation :**
- Éditeur de texte simple pour modifier les descriptions ou les mentions légales.
- Possibilité de changer les images principales (ex: bannière d'accueil).
- Paramètres de base pour ajuster les couleurs primaires du site.

**Tâches :**
- Intégrer un éditeur de texte sécurisé pour éviter les injections de code.
- Gérer l'upload et le stockage des nouvelles images.

***
