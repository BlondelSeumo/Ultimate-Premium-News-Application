-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 07, 2019 at 05:25 PM
-- Server version: 10.1.36-MariaDB
-- PHP Version: 7.0.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ristana_newsapp`
--

-- --------------------------------------------------------

--
-- Table structure for table `answers_table`
--

CREATE TABLE `answers_table` (
  `id` int(11) NOT NULL,
  `question_id` int(11) DEFAULT NULL,
  `answer` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `value` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `category_table`
--

CREATE TABLE `category_table` (
  `id` int(11) NOT NULL,
  `media_id` int(11) DEFAULT NULL,
  `language_id` int(11) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `comment_table`
--

CREATE TABLE `comment_table` (
  `id` int(11) NOT NULL,
  `post_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `content` longtext COLLATE utf8_unicode_ci NOT NULL,
  `created` datetime NOT NULL,
  `enabled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `device_table`
--

CREATE TABLE `device_table` (
  `id` int(11) NOT NULL,
  `token` longtext COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `fos_user_table`
--

CREATE TABLE `fos_user_table` (
  `id` int(11) NOT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `username_canonical` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `email_canonical` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `salt` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `last_login` datetime DEFAULT NULL,
  `locked` tinyint(1) NOT NULL,
  `expired` tinyint(1) NOT NULL,
  `expires_at` datetime DEFAULT NULL,
  `confirmation_token` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password_requested_at` datetime DEFAULT NULL,
  `roles` longtext COLLATE utf8_unicode_ci NOT NULL COMMENT '(DC2Type:array)',
  `credentials_expired` tinyint(1) NOT NULL,
  `credentials_expire_at` datetime DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `facebook` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `instagram` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `twitter` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `emailo` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `token` longtext COLLATE utf8_unicode_ci,
  `image` longtext COLLATE utf8_unicode_ci NOT NULL,
  `trusted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `fos_user_table`
--

INSERT INTO `fos_user_table` (`id`, `username`, `username_canonical`, `email`, `email_canonical`, `enabled`, `salt`, `password`, `last_login`, `locked`, `expired`, `expires_at`, `confirmation_token`, `password_requested_at`, `roles`, `credentials_expired`, `credentials_expire_at`, `name`, `code`, `facebook`, `instagram`, `twitter`, `emailo`, `type`, `token`, `image`, `trusted`) VALUES
(1, 'ADMIN', 'admin', 'ADMIN', 'admin', 1, 'djtfgbufxr4gwk4k0gss4sgs4k48wc4', '$2y$13$djtfgbufxr4gwk4k0gss4ekodAwfJ3IP01OyKvMD.stoxgr6MMa2S', '2019-10-07 18:14:46', 0, 0, NULL, NULL, NULL, 'a:1:{i:0;s:10:\"ROLE_ADMIN\";}', 0, NULL, 'Julian Zelizer', NULL, NULL, NULL, NULL, NULL, 'email', NULL, 'https://cdn.cnn.com/cnnnext/dam/assets/140926165927-julian-e-zelizer-profile-image-small-11.jpg', 0);

-- --------------------------------------------------------

--
-- Table structure for table `gallery_table`
--

CREATE TABLE `gallery_table` (
  `id` int(11) NOT NULL,
  `titre` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `language_table`
--

CREATE TABLE `language_table` (
  `id` int(11) NOT NULL,
  `media_id` int(11) DEFAULT NULL,
  `language` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `position` int(11) NOT NULL,
  `enabled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `medias_gallerys_table`
--

CREATE TABLE `medias_gallerys_table` (
  `gallery_id` int(11) NOT NULL,
  `media_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `media_table`
--

CREATE TABLE `media_table` (
  `id` int(11) NOT NULL,
  `titre` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `extension` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  `enabled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------
INSERT INTO `media_table` (`id`, `titre`, `url`, `type`, `extension`, `date`, `enabled`) VALUES (1, 'g1204.png', 'f88750d5bd3bfa7175b856c303822751.png', 'image/png', 'png', '2019-09-09 17:23:01', '1');
--
-- Table structure for table `posts_tags_table`
--

CREATE TABLE `posts_tags_table` (
  `post_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `post_categories`
--

CREATE TABLE `post_categories` (
  `post_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `post_languages`
--

CREATE TABLE `post_languages` (
  `post_id` int(11) NOT NULL,
  `language_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `post_table`
--

CREATE TABLE `post_table` (
  `id` int(11) NOT NULL,
  `media_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `localvideo_id` int(11) DEFAULT NULL,
  `title` longtext COLLATE utf8_unicode_ci NOT NULL,
  `type` longtext COLLATE utf8_unicode_ci NOT NULL,
  `content` longtext COLLATE utf8_unicode_ci,
  `views` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `review` tinyint(1) NOT NULL,
  `comment` tinyint(1) NOT NULL,
  `tags` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `shares` int(11) NOT NULL,
  `video` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `question_table`
--

CREATE TABLE `question_table` (
  `id` int(11) NOT NULL,
  `language_id` int(11) DEFAULT NULL,
  `question` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `featured` tinyint(1) NOT NULL,
  `open` tinyint(1) NOT NULL,
  `multi` tinyint(1) NOT NULL,
  `created` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rate_table`
--

CREATE TABLE `rate_table` (
  `id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `value` int(11) NOT NULL,
  `review` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `settings_table`
--

CREATE TABLE `settings_table` (
  `id` int(11) NOT NULL,
  `media_id` int(11) DEFAULT NULL,
  `appname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `appdescription` longtext COLLATE utf8_unicode_ci,
  `googleplay` longtext COLLATE utf8_unicode_ci,
  `privacypolicy` longtext COLLATE utf8_unicode_ci,
  `firebasekey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `settings_table`
--

INSERT INTO `settings_table` (`id`, `media_id`, `appname`, `appdescription`, `googleplay`, `privacypolicy`, `firebasekey`) VALUES
(1, 1, 'Ultimate News App', 'This is a demo application for News App on Codecanyon,', 'https://play.google.com/store/apps/details?id=com.virmana.news_app', '<h3 dir=\"ltr\"><strong>Politique de confidentialit&eacute;</strong></h3>\r\n\r\n<p dir=\"ltr\">Votre vie priv&eacute;e est importante pour nous.&nbsp;Nous avons parfois besoin d&#39;informations pour fournir les services que vous demandez. Cette d&eacute;claration de confidentialit&eacute; s&#39;applique &agrave; Justapps Apps et &agrave; son produit King of status.</p>\r\n\r\n<p dir=\"ltr\">Renseignements personnels</p>\r\n\r\n<p dir=\"ltr\">Nous NE collectons, stockons ou utilisons aucune information personnelle pendant que vous visitez, t&eacute;l&eacute;chargez ou mettez &agrave; niveau nos produits.</p>\r\n\r\n<p dir=\"ltr\">Nous pouvons utiliser les informations personnelles que vous avez communiqu&eacute;es aux seules fins suivantes: nous aider &agrave; d&eacute;velopper, fournir et am&eacute;liorer nos produits et services et &agrave; fournir un service de meilleure qualit&eacute;;&nbsp;g&eacute;rer des sondages en ligne et d&#39;autres activit&eacute;s auxquelles vous avez particip&eacute;.</p>\r\n\r\n<p dir=\"ltr\">Dans les circonstances suivantes, nous pouvons divulguer vos informations personnelles conform&eacute;ment &agrave; votre souhait ou &agrave; la r&eacute;glementation en vigueur:</p>\r\n\r\n<p dir=\"ltr\">&middot; (1) votre autorisation pr&eacute;alable;</p>\r\n\r\n<p dir=\"ltr\">&middot; (2) Par la loi applicable dans ou hors de votre pays de r&eacute;sidence, proc&eacute;dure judiciaire, demandes de litige;</p>\r\n\r\n<p dir=\"ltr\">&middot; (3) &Agrave; la demande d&#39;autorit&eacute;s publiques et gouvernementales;</p>\r\n\r\n<p dir=\"ltr\">&middot; (4) Prot&eacute;ger nos droits et int&eacute;r&ecirc;ts l&eacute;gaux.</p>\r\n\r\n<p dir=\"ltr\">Informations non personnelles</p>\r\n\r\n<p dir=\"ltr\">Nous pouvons collecter et utiliser des informations non personnelles dans les circonstances suivantes.&nbsp;Pour mieux comprendre le comportement des utilisateurs, r&eacute;soudre les probl&egrave;mes de produits et services, am&eacute;liorer nos produits, nos services et notre publicit&eacute;, nous pouvons collecter des informations non personnelles telles que le nom de l&#39;application install&eacute;e et le nom du package, les donn&eacute;es d&#39;installation, la fr&eacute;quence d&#39;utilisation, le pays. , &eacute;quipement et canal.</p>\r\n\r\n<p dir=\"ltr\">Si des informations non personnelles sont combin&eacute;es &agrave; des informations personnelles, nous les traitons comme des informations personnelles aux fins de la pr&eacute;sente politique de confidentialit&eacute;.</p>\r\n\r\n<p dir=\"ltr\">Informations que nous obtenons de votre utilisation de nos services</p>\r\n\r\n<p dir=\"ltr\">Nous pouvons collecter des informations sur les services que vous utilisez et sur la mani&egrave;re dont vous les utilisez, par exemple lorsque vous visualisez et interagissez avec notre contenu.&nbsp;Nous pouvons collecter des informations sp&eacute;cifiques &agrave; votre appareil (telles que votre mod&egrave;le de mat&eacute;riel, la version de votre syst&egrave;me d&#39;exploitation, vos identifiants uniques, vos param&egrave;tres, la langue de votre appareil et les informations relatives au r&eacute;seau mobile).&nbsp;Nous ne partagerons pas ces informations avec des tiers.</p>\r\n\r\n<p dir=\"ltr\">Les autorisations dans l&#39;application King of status sont les suivantes:</p>\r\n\r\n<p dir=\"ltr\">Obtenir des comptes</p>\r\n\r\n<p dir=\"ltr\">Cette autorisation est n&eacute;cessaire pour cr&eacute;er une liste de s&eacute;lection avec vos comptes Google dans l&#39;application, lorsque vous exportez / importez des donn&eacute;es de statut King sur / depuis Google Drive.</p>\r\n\r\n<p dir=\"ltr\">&Eacute;tat du r&eacute;seau d&#39;acc&egrave;s</p>\r\n\r\n<p dir=\"ltr\">Cette autorisation est n&eacute;cessaire pour v&eacute;rifier si vous &ecirc;tes connect&eacute; &agrave; Internet lorsque vous exportez / importez des donn&eacute;es de statut King sur / depuis Google Drive.</p>\r\n\r\n<p dir=\"ltr\">l&#39;Internet</p>\r\n\r\n<p dir=\"ltr\">Cette autorisation est n&eacute;cessaire pour exporter / importer des donn&eacute;es de statut King sur / depuis Google Drive.</p>\r\n\r\n<p dir=\"ltr\">Lire l&#39;&eacute;tat du t&eacute;l&eacute;phone</p>\r\n\r\n<p dir=\"ltr\">Cette autorisation est n&eacute;cessaire pour v&eacute;rifier si un appel a &eacute;t&eacute; lanc&eacute; &agrave; partir de l&#39;application King of Status et lorsque l&#39;appel est termin&eacute; et que vous avez activ&eacute; l&#39;option &quot;Garder l&#39;application sur appel&quot; dans les &quot;Param&egrave;tres syst&egrave;me&quot; de l&#39;application, l&#39;application King of status sera reprise. .</p>\r\n\r\n<p dir=\"ltr\">Aussi n&eacute;cessaire pour v&eacute;rifier si votre appareil a une ou plusieurs cartes SIM.</p>\r\n\r\n<p dir=\"ltr\">Ecrire un stockage externe</p>\r\n\r\n<p dir=\"ltr\">Cette autorisation est n&eacute;cessaire pour &eacute;crire sur le stockage externe de votre appareil lors de l&#39;exportation de donn&eacute;es d&#39;&eacute;tat King of dans le stockage interne de l&#39;appareil.</p>\r\n\r\n<p>Vibrer</p>\r\n\r\n<p dir=\"ltr\">Cette autorisation est n&eacute;cessaire pour vibrer sur certains &eacute;v&eacute;nements tactiles.</p>\r\n\r\n<p dir=\"ltr\">NOUS NE PARTAGERONS AUCUNE INFORMATION DE VOTRE APPAREIL AVEC DES TIERS.</p>\r\n\r\n<p dir=\"ltr\">SI VOUS NE COMPRENEZ PAS ET / OU ACCEPTEZ LA VIE PRIV&Eacute;E, VOUS DEVEZ IMM&Eacute;DIATEMENT SORTIR ET &Eacute;VITER DE FAIRE L&#39;UTILISATION DES SERVICES DE King of status</p>\r\n\r\n<p dir=\"ltr\">Mise &agrave; jour de la politique de confidentialit&eacute;</p>\r\n\r\n<p dir=\"ltr\">Justapps Apps mettra occasionnellement &agrave; jour cette d&eacute;claration de confidentialit&eacute;.&nbsp;Dans ce cas, nous r&eacute;viserons &eacute;galement la date de la &quot;derni&egrave;re modification&quot; de la d&eacute;claration de confidentialit&eacute;.</p>\r\n\r\n<p dir=\"ltr\">Contenu utilisateur.</p>\r\n\r\n<p dir=\"ltr\">Tout contenu ajout&eacute;, t&eacute;l&eacute;charg&eacute;, soumis, distribu&eacute;, publi&eacute; ou cr&eacute;&eacute; &agrave; l&#39;aide des services par les utilisateurs (collectivement, le &laquo;contenu de l&#39;utilisateur&raquo;), publi&eacute; ou transmis de mani&egrave;re priv&eacute;e, rel&egrave;ve de la seule responsabilit&eacute; de la personne &agrave; l&#39;origine de ce contenu.&nbsp;Vous d&eacute;clarez que tout le contenu utilisateur que vous avez fourni est exact, complet, &agrave; jour et conforme &agrave; toutes les lois, r&egrave;gles et r&eacute;glementations applicables.&nbsp;Sans limiter la port&eacute;e g&eacute;n&eacute;rale de ce qui pr&eacute;c&egrave;de, vous d&eacute;clarez que tout Contenu utilisateur que vous cr&eacute;ez avec des outils accessibles sur les Services n&#39;enfreint pas les droits de propri&eacute;t&eacute; intellectuelle de tiers et est par ailleurs conforme &agrave; toutes les lois, r&egrave;gles et r&eacute;glementations applicables.&nbsp;Vous reconnaissez que tout le contenu, y compris le contenu de l&#39;utilisateur,&nbsp;auquel vous acc&eacute;dez en utilisant les Services est &agrave; vos risques et p&eacute;rils et vous serez seul responsable de tout dommage ou de toute perte en r&eacute;sultant pour vous ou toute autre partie.&nbsp;Nous ne garantissons pas que le contenu auquel vous acc&eacute;dez sur ou via les services est ou continuera d&#39;&ecirc;tre exact.</p>\r\n\r\n<p dir=\"ltr\">Disponibilit&eacute; du contenu.</p>\r\n\r\n<p dir=\"ltr\">Nous ne garantissons pas que le contenu sera mis &agrave; disposition sur le site ou par le biais des services.&nbsp;Nous nous r&eacute;servons le droit de (i) supprimer, &eacute;diter, modifier ou bloquer des Services tout contenu &agrave; notre seule discr&eacute;tion, &agrave; tout moment, sans pr&eacute;avis et pour quelque raison que ce soit (y compris, sans toutefois s&#39;y limiter, &agrave; la r&eacute;ception de r&eacute;clamations ou d&#39;all&eacute;gations de tiers ou d&#39;autorit&eacute;s relatives &agrave; ce Contenu ou si nous craignons que vous n&#39;ayez enfreint la derni&egrave;re phrase du paragraphe pr&eacute;c&eacute;dent), ou pour aucune raison.</p>\r\n\r\n<p><br />\r\n&nbsp;</p>', 'AIzaSyCg77N96veclCZBruelCXqKY5MVJc1nUds');

-- --------------------------------------------------------

--
-- Table structure for table `slide_table`
--

CREATE TABLE `slide_table` (
  `id` int(11) NOT NULL,
  `post_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `media_id` int(11) DEFAULT NULL,
  `language_id` int(11) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `support_table`
--

CREATE TABLE `support_table` (
  `id` int(11) NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `subject` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `message` longtext COLLATE utf8_unicode_ci NOT NULL,
  `created` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tags_table`
--

CREATE TABLE `tags_table` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `search` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_followers`
--

CREATE TABLE `user_followers` (
  `user_id` int(11) NOT NULL,
  `follower_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `version_table`
--

CREATE TABLE `version_table` (
  `id` int(11) NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `features` longtext COLLATE utf8_unicode_ci NOT NULL,
  `code` int(11) NOT NULL,
  `enabled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `answers_table`
--
ALTER TABLE `answers_table`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_86F50A951E27F6BF` (`question_id`);

--
-- Indexes for table `category_table`
--
ALTER TABLE `category_table`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_1E1AC00FEA9FDD75` (`media_id`),
  ADD KEY `IDX_1E1AC00F82F1BAF4` (`language_id`);

--
-- Indexes for table `comment_table`
--
ALTER TABLE `comment_table`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_5FB317B74B89032C` (`post_id`),
  ADD KEY `IDX_5FB317B7A76ED395` (`user_id`);

--
-- Indexes for table `device_table`
--
ALTER TABLE `device_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fos_user_table`
--
ALTER TABLE `fos_user_table`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UNIQ_C3D4D4BD92FC23A8` (`username_canonical`),
  ADD UNIQUE KEY `UNIQ_C3D4D4BDA0D96FBF` (`email_canonical`);

--
-- Indexes for table `gallery_table`
--
ALTER TABLE `gallery_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `language_table`
--
ALTER TABLE `language_table`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_89718B17EA9FDD75` (`media_id`);

--
-- Indexes for table `medias_gallerys_table`
--
ALTER TABLE `medias_gallerys_table`
  ADD PRIMARY KEY (`gallery_id`,`media_id`),
  ADD KEY `IDX_CC965DCE4E7AF8F` (`gallery_id`),
  ADD KEY `IDX_CC965DCEEA9FDD75` (`media_id`);

--
-- Indexes for table `media_table`
--
ALTER TABLE `media_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `posts_tags_table`
--
ALTER TABLE `posts_tags_table`
  ADD PRIMARY KEY (`post_id`,`tag_id`),
  ADD KEY `IDX_F7D95304B89032C` (`post_id`),
  ADD KEY `IDX_F7D9530BAD26311` (`tag_id`);

--
-- Indexes for table `post_categories`
--
ALTER TABLE `post_categories`
  ADD PRIMARY KEY (`post_id`,`category_id`),
  ADD KEY `IDX_198B4FA94B89032C` (`post_id`),
  ADD KEY `IDX_198B4FA912469DE2` (`category_id`);

--
-- Indexes for table `post_languages`
--
ALTER TABLE `post_languages`
  ADD PRIMARY KEY (`post_id`,`language_id`),
  ADD KEY `IDX_1BF2D8E4B89032C` (`post_id`),
  ADD KEY `IDX_1BF2D8E82F1BAF4` (`language_id`);

--
-- Indexes for table `post_table`
--
ALTER TABLE `post_table`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_613203A9EA9FDD75` (`media_id`),
  ADD KEY `IDX_613203A9A76ED395` (`user_id`),
  ADD KEY `IDX_613203A9C05CE4BE` (`localvideo_id`);

--
-- Indexes for table `question_table`
--
ALTER TABLE `question_table`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_C6C29E4282F1BAF4` (`language_id`);

--
-- Indexes for table `rate_table`
--
ALTER TABLE `rate_table`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_666996654B89032C` (`post_id`),
  ADD KEY `IDX_66699665A76ED395` (`user_id`);

--
-- Indexes for table `settings_table`
--
ALTER TABLE `settings_table`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_4EF0C90FEA9FDD75` (`media_id`);

--
-- Indexes for table `slide_table`
--
ALTER TABLE `slide_table`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UNIQ_77A059652B36786B` (`title`),
  ADD KEY `IDX_77A059654B89032C` (`post_id`),
  ADD KEY `IDX_77A0596512469DE2` (`category_id`),
  ADD KEY `IDX_77A05965EA9FDD75` (`media_id`),
  ADD KEY `IDX_77A0596582F1BAF4` (`language_id`);

--
-- Indexes for table `support_table`
--
ALTER TABLE `support_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tags_table`
--
ALTER TABLE `tags_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_followers`
--
ALTER TABLE `user_followers`
  ADD PRIMARY KEY (`user_id`,`follower_id`),
  ADD KEY `IDX_84E87043A76ED395` (`user_id`),
  ADD KEY `IDX_84E87043AC24F853` (`follower_id`);

--
-- Indexes for table `version_table`
--
ALTER TABLE `version_table`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `answers_table`
--
ALTER TABLE `answers_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `category_table`
--
ALTER TABLE `category_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `comment_table`
--
ALTER TABLE `comment_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `device_table`
--
ALTER TABLE `device_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `fos_user_table`
--
ALTER TABLE `fos_user_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `gallery_table`
--
ALTER TABLE `gallery_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `language_table`
--
ALTER TABLE `language_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `media_table`
--
ALTER TABLE `media_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=119;

--
-- AUTO_INCREMENT for table `post_table`
--
ALTER TABLE `post_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `question_table`
--
ALTER TABLE `question_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rate_table`
--
ALTER TABLE `rate_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `settings_table`
--
ALTER TABLE `settings_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `slide_table`
--
ALTER TABLE `slide_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `support_table`
--
ALTER TABLE `support_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tags_table`
--
ALTER TABLE `tags_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=129;

--
-- AUTO_INCREMENT for table `version_table`
--
ALTER TABLE `version_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `answers_table`
--
ALTER TABLE `answers_table`
  ADD CONSTRAINT `FK_86F50A951E27F6BF` FOREIGN KEY (`question_id`) REFERENCES `question_table` (`id`);

--
-- Constraints for table `category_table`
--
ALTER TABLE `category_table`
  ADD CONSTRAINT `FK_1E1AC00F82F1BAF4` FOREIGN KEY (`language_id`) REFERENCES `language_table` (`id`),
  ADD CONSTRAINT `FK_1E1AC00FEA9FDD75` FOREIGN KEY (`media_id`) REFERENCES `media_table` (`id`);

--
-- Constraints for table `comment_table`
--
ALTER TABLE `comment_table`
  ADD CONSTRAINT `FK_5FB317B74B89032C` FOREIGN KEY (`post_id`) REFERENCES `post_table` (`id`),
  ADD CONSTRAINT `FK_5FB317B7A76ED395` FOREIGN KEY (`user_id`) REFERENCES `fos_user_table` (`id`);

--
-- Constraints for table `language_table`
--
ALTER TABLE `language_table`
  ADD CONSTRAINT `FK_89718B17EA9FDD75` FOREIGN KEY (`media_id`) REFERENCES `media_table` (`id`);

--
-- Constraints for table `medias_gallerys_table`
--
ALTER TABLE `medias_gallerys_table`
  ADD CONSTRAINT `FK_CC965DCE4E7AF8F` FOREIGN KEY (`gallery_id`) REFERENCES `gallery_table` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_CC965DCEEA9FDD75` FOREIGN KEY (`media_id`) REFERENCES `media_table` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `posts_tags_table`
--
ALTER TABLE `posts_tags_table`
  ADD CONSTRAINT `FK_F7D95304B89032C` FOREIGN KEY (`post_id`) REFERENCES `post_table` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_F7D9530BAD26311` FOREIGN KEY (`tag_id`) REFERENCES `tags_table` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `post_categories`
--
ALTER TABLE `post_categories`
  ADD CONSTRAINT `FK_198B4FA912469DE2` FOREIGN KEY (`category_id`) REFERENCES `category_table` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_198B4FA94B89032C` FOREIGN KEY (`post_id`) REFERENCES `post_table` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `post_languages`
--
ALTER TABLE `post_languages`
  ADD CONSTRAINT `FK_1BF2D8E4B89032C` FOREIGN KEY (`post_id`) REFERENCES `post_table` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_1BF2D8E82F1BAF4` FOREIGN KEY (`language_id`) REFERENCES `language_table` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `post_table`
--
ALTER TABLE `post_table`
  ADD CONSTRAINT `FK_613203A9A76ED395` FOREIGN KEY (`user_id`) REFERENCES `fos_user_table` (`id`),
  ADD CONSTRAINT `FK_613203A9C05CE4BE` FOREIGN KEY (`localvideo_id`) REFERENCES `media_table` (`id`),
  ADD CONSTRAINT `FK_613203A9EA9FDD75` FOREIGN KEY (`media_id`) REFERENCES `media_table` (`id`);

--
-- Constraints for table `question_table`
--
ALTER TABLE `question_table`
  ADD CONSTRAINT `FK_C6C29E4282F1BAF4` FOREIGN KEY (`language_id`) REFERENCES `language_table` (`id`);

--
-- Constraints for table `rate_table`
--
ALTER TABLE `rate_table`
  ADD CONSTRAINT `FK_666996654B89032C` FOREIGN KEY (`post_id`) REFERENCES `post_table` (`id`),
  ADD CONSTRAINT `FK_66699665A76ED395` FOREIGN KEY (`user_id`) REFERENCES `fos_user_table` (`id`);

--
-- Constraints for table `settings_table`
--
ALTER TABLE `settings_table`
  ADD CONSTRAINT `FK_4EF0C90FEA9FDD75` FOREIGN KEY (`media_id`) REFERENCES `media_table` (`id`);

--
-- Constraints for table `slide_table`
--
ALTER TABLE `slide_table`
  ADD CONSTRAINT `FK_77A0596512469DE2` FOREIGN KEY (`category_id`) REFERENCES `category_table` (`id`),
  ADD CONSTRAINT `FK_77A059654B89032C` FOREIGN KEY (`post_id`) REFERENCES `post_table` (`id`),
  ADD CONSTRAINT `FK_77A0596582F1BAF4` FOREIGN KEY (`language_id`) REFERENCES `language_table` (`id`),
  ADD CONSTRAINT `FK_77A05965EA9FDD75` FOREIGN KEY (`media_id`) REFERENCES `media_table` (`id`);

--
-- Constraints for table `user_followers`
--
ALTER TABLE `user_followers`
  ADD CONSTRAINT `FK_84E87043A76ED395` FOREIGN KEY (`user_id`) REFERENCES `fos_user_table` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_84E87043AC24F853` FOREIGN KEY (`follower_id`) REFERENCES `fos_user_table` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
