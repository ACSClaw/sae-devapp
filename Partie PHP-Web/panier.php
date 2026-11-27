<?php
$pageTitle = 'Mon panier';
$activePage = '';
require __DIR__ . '/includes/header.php';
?>
<section class="cart-page section-wrap">
    <div class="breadcrumbs"><a href="index.php">Accueil</a><span>/</span><span>Mon panier</span></div>
    <div class="checkout-steps"><span class="step-current">1</span><i></i><span>2</span><i></i><span>3</span></div>
    <h1>Mon panier <span>4 articles</span></h1>
    <div class="cart-layout">
        <div class="cart-items">
            <article class="cart-item"><div class="cart-product-art art-helmet"><span class="helmet-illustration"></span></div><div class="cart-product-info"><span>Exploration · Équipement</span><h2>Casque Horizon</h2><p>Taille : M</p><strong>249,00 €</strong></div><div class="fake-quantity">1 <span>⌃<br>⌄</span></div><span class="line-total">249,00 €</span></article>
            <article class="cart-item"><div class="cart-product-art art-suit"><span class="suit-illustration"></span></div><div class="cart-product-info"><span>Astronautes · Combinaison</span><h2>Combinaison Nova</h2><p>Taille : S</p><strong>890,00 €</strong></div><div class="fake-quantity">1 <span>⌃<br>⌄</span></div><span class="line-total">890,00 €</span></article>
            <article class="cart-item"><div class="cart-product-art art-gloves"><span class="glove-illustration">✳</span></div><div class="cart-product-info"><span>Astronautes · Accessoires</span><h2>Gants de mission</h2><p>Taille : M</p><strong>89,00 €</strong></div><div class="fake-quantity">1 <span>⌃<br>⌄</span></div><span class="line-total">89,00 €</span></article>
            <article class="cart-item"><div class="cart-product-art art-pack"><span class="pack-illustration"></span></div><div class="cart-product-info"><span>Exploration · Accessoires</span><h2>Pack Explorateur</h2><p>Coloris : Nuit</p><strong>179,00 €</strong></div><div class="fake-quantity">1 <span>⌃<br>⌄</span></div><span class="line-total">179,00 €</span></article>
        </div>
        <aside class="order-summary"><h2>Récapitulatif</h2><div><span>Produits</span><span>1 407,00 €</span></div><div><span>Livraison</span><span>Offerte</span></div><div class="summary-total"><strong>TOTAL</strong><strong>1 407,00 €</strong></div><div class="static-cta">Valider la commande <span>↗</span></div><p>Paiement sécurisé · Livraison offerte</p></aside>
    </div>
    <a class="text-link continue-shopping" href="categories.php">← Continuer mes achats</a>
</section>
<?php require __DIR__ . '/includes/footer.php'; ?>
