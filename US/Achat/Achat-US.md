# [US] - En tant que client, je veux ajouter un produit au panier #1

**Labels :** `US` `must`

## Description

En tant que client, je veux ajouter un produit à mon panier afin de pouvoir le commander plus tard.

## Critères d'acceptation

- Un bouton « Ajouter au panier » est visible sur la page de chaque produit disponible.
- Au clic, le produit est ajouté au panier avec une quantité de 1.
- Dans le panier on peut changer la quantité du produit
- Si le produit est déjà dans le panier, sa quantité est incrémentée au lieu de créer une nouvelle ligne.
- Un message de confirmation s'affiche.
- Un produit en rupture de stock ne peut pas être ajouté : le bouton est désactivé ou un message explicite s'affiche.
- La quantité ajoutée ne peut pas dépasser le stock disponible.

## Tâches

- Ajouter le bouton « Ajouter au panier » sur la page produit
- Ajouter la logique de quantitée dans le panier

---

# [US] - En tant que client, je veux modifier mon panier #2

**Labels :** `US` `must`

## Description

En tant que client, je veux modifier le contenu de mon panier afin d'ajuster ma commande avant de la valider.

## Critères d'acceptation

- Le client accède à une page « Mon panier » listant les produits ajoutés (nom, prix unitaire, quantité, sous-total, total).
- Il peut augmenter ou diminuer la quantité d'un produit, dans la limite du stock disponible.
- Il peut supprimer un produit du panier.
- Si la quantité passe à 0, le produit est retiré du panier.
- Le sous-total et le total du panier est recalculé immédiatement après chaque modification.
- Si le panier est vide, un message explicite s'affiche (« Votre panier est vide »).
- Le contenu du panier est conservé entre les pages et après rechargement.

## Tâches

- Créer la page « Mon panier » et l'affichage des lignes
- Gérer la modification de quantité et la suppression d'un produit
- Recalculer et afficher le total du panier