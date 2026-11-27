<?php
$pageTitle = $pageTitle ?? 'The Star Land Company';
$activePage = $activePage ?? '';
?>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#222222">
    <title><?= htmlspecialchars($pageTitle, ENT_QUOTES, 'UTF-8') ?> — The Star Land Company</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
<header class="site-header">
    <div class="header-main">
        <a class="brand" href="index.php" aria-label="The Star Land Company, accueil">
            <span class="brand-orbit" aria-hidden="true"><span></span></span>
            <span class="brand-name">THE<br>STAR LAND<br>COMPANY</span>
        </a>
        <div class="header-content">
            <nav class="primary-nav" aria-label="Navigation principale">
                <a class="<?= $activePage === 'home' ? 'is-active' : '' ?>" href="index.php">Accueil</a>
                <a class="<?= $activePage === 'parcelles' ? 'is-active' : '' ?>" href="categories.php">Parcelles</a>
                <a class="<?= $activePage === 'produits' ? 'is-active' : '' ?>" href="categories.php">Produits</a>
                <a href="categories.php">Soldes</a>
                <a href="categories.php">Nouveautés</a>
                <a href="categories.php">Édition limitée</a>
            </nav>
            <div class="header-tools">
                <div class="search-visual" aria-label="Recherche décorative">
                    <span class="search-icon" aria-hidden="true"></span>
                    <span>Recherche</span>
                </div>
                <div class="utility-links">
                    <a class="utility-icon help-icon" href="index.php#contact" aria-label="Aide">?</a>
                    <a class="utility-icon user-icon" href="connexion.php" aria-label="Connexion"></a>
                    <a class="utility-icon cart-icon" href="panier.php" aria-label="Panier"><span class="cart-count">0</span></a>
                </div>
            </div>
        </div>
    </div>
    <nav class="category-nav" aria-label="Catégories">
        <a href="categories.php">Astronautes</a>
        <a href="categories.php">Exploration</a>
        <a href="categories.php">Mobilier</a>
        <a href="categories.php?type=lune">Parcelles Lune</a>
        <a href="categories.php?type=mars">Parcelles Mars</a>
    </nav>
</header>
<main>
