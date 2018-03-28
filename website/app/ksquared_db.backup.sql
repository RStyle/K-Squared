-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Mar 20, 2018 at 10:10 PM
-- Server version: 10.1.26-MariaDB-0+deb9u1
-- PHP Version: 7.0.27-0+deb9u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ksquared_db`
--
CREATE DATABASE IF NOT EXISTS `ksquared_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `ksquared_db`;

-- --------------------------------------------------------

--
-- Table structure for table `ACTIVITY`
--

CREATE TABLE `ACTIVITY` (
  `user` varchar(100) NOT NULL,
  `setname` varchar(100) NOT NULL,
  `chapter` int(11) NOT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `CARDS`
--

CREATE TABLE `CARDS` (
  `user` varchar(100) NOT NULL,
  `setname` varchar(100) NOT NULL,
  `chapter` int(11) NOT NULL,
  `fronttext` varchar(100) NOT NULL,
  `backtext` varchar(100) NOT NULL,
  `frontimageurl` varchar(100) NOT NULL,
  `backimageurl` varchar(100) NOT NULL,
  `timetolearn` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `CARDS`
--

INSERT INTO `CARDS` (`user`, `setname`, `chapter`, `fronttext`, `backtext`, `frontimageurl`, `backimageurl`, `timetolearn`) VALUES
('RStyle', 'words to use instead of very', 1, 'very accurate', 'exact', '', '', '2018-03-20 20:20:04'),
('RStyle', 'words to use instead of very', 1, 'very afraid', 'fearful', '', '', '2018-03-20 20:00:26'),
('RStyle', 'words to use instead of very', 1, 'very angry', 'furious', '', '', '2018-03-20 20:00:36'),
('RStyle', 'words to use instead of very', 1, 'very annoying', 'exasperating', '', '', '2018-03-20 20:00:44'),
('RStyle', 'words to use instead of very', 1, 'very bad', 'awful', '', '', '2018-03-20 20:00:52'),
('RStyle', 'words to use instead of very', 1, 'very beautiful', 'gorgeous', '', '', '2018-03-20 20:01:04'),
('RStyle', 'words to use instead of very', 1, 'very big', 'massive', '', '', '2018-03-20 20:01:10'),
('RStyle', 'words to use instead of very', 1, 'very boring', 'dull', '', '', '2018-03-20 20:01:19'),
('RStyle', 'words to use instead of very', 1, 'very bright', 'luminous', '', '', '2018-03-20 20:01:30'),
('RStyle', 'words to use instead of very', 1, 'very busy', 'swamped', '', '', '2018-03-20 20:01:37'),
('RStyle', 'words to use instead of very', 1, 'very calm', 'serene', '', '', '2018-03-20 20:01:44'),
('RStyle', 'words to use instead of very', 1, 'very careful', 'cautious', '', '', '2018-03-20 20:01:51'),
('RStyle', 'words to use instead of very', 1, 'very cheap', 'stingy', '', '', '2018-03-20 20:01:58'),
('RStyle', 'words to use instead of very', 1, 'very clean', 'spotless', '', '', '2018-03-20 20:02:04'),
('RStyle', 'words to use instead of very', 1, 'very clear', 'obvious', '', '', '2018-03-20 20:02:12'),
('RStyle', 'words to use instead of very', 1, 'very cold', 'freezing', '', '', '2018-03-20 20:02:18'),
('RStyle', 'words to use instead of very', 1, 'very colorful', 'vibrant', '', '', '2018-03-20 20:02:24'),
('RStyle', 'words to use instead of very', 1, 'very competitive', 'cutthroat', '', '', '2018-03-20 20:02:40'),
('RStyle', 'words to use instead of very', 1, 'very complete', 'comprehensive', '', '', '2018-03-20 20:02:48'),
('RStyle', 'words to use instead of very', 1, 'very confused', 'perplexed', '', '', '2018-03-20 20:02:56'),
('RStyle', 'words to use instead of very', 1, 'very creative', 'innovative', '', '', '2018-03-20 20:03:04'),
('RStyle', 'words to use instead of very', 1, 'very crowded', 'bustling', '', '', '2018-03-20 20:03:20'),
('RStyle', 'words to use instead of very', 1, 'very cute', 'adorable', '', '', '2018-03-20 20:20:22'),
('RStyle', 'words to use instead of very', 1, 'very dangerous', 'perilous', '', '', '2018-03-20 20:03:30'),
('RStyle', 'words to use instead of very', 1, 'very dear', 'cherished', '', '', '2018-03-20 20:03:35'),
('RStyle', 'words to use instead of very', 1, 'very deep', 'profound', '', '', '2018-03-20 20:03:42'),
('RStyle', 'words to use instead of very', 1, 'very depressed', 'despondent', '', '', '2018-03-20 20:03:50'),
('RStyle', 'words to use instead of very', 1, 'very detailed', 'meticulous', '', '', '2018-03-20 20:04:00'),
('RStyle', 'words to use instead of very', 1, 'very different', 'disparate', '', '', '2018-03-20 20:04:24'),
('RStyle', 'words to use instead of very', 1, 'very difficult', 'arduous', '', '', '2018-03-20 20:04:33'),
('RStyle', 'words to use instead of very', 1, 'very dirty', 'filthy', '', '', '2018-03-20 20:04:41'),
('RStyle', 'words to use instead of very', 1, 'very dry', 'arid', '', '', '2018-03-20 20:04:47'),
('RStyle', 'words to use instead of very', 1, 'very dull', 'tedious', '', '', '2018-03-20 20:04:51'),
('RStyle', 'words to use instead of very', 1, 'very easy', 'effortless', '', '', '2018-03-20 20:04:57'),
('RStyle', 'words to use instead of very', 1, 'very empty', 'desolate', '', '', '2018-03-20 20:05:04'),
('RStyle', 'words to use instead of very', 1, 'very excited', 'thrilled', '', '', '2018-03-20 20:05:09'),
('RStyle', 'words to use instead of very', 1, 'very exciting', 'exhilarating', '', '', '2018-03-20 20:05:21'),
('RStyle', 'words to use instead of very', 1, 'very expensive', 'costly', '', '', '2018-03-20 20:05:27'),
('RStyle', 'words to use instead of very', 1, 'very fancy', 'lavish', '', '', '2018-03-20 20:05:38'),
('RStyle', 'words to use instead of very', 1, 'very fast', 'swift', '', '', '2018-03-20 20:20:34'),
('RStyle', 'words to use instead of very', 1, 'very fat', 'obese', '', '', '2018-03-20 20:05:42'),
('RStyle', 'words to use instead of very', 1, 'very friendly', 'amiable', '', '', '2018-03-20 20:05:50'),
('RStyle', 'words to use instead of very', 1, 'very frightened', 'alarmed', '', '', '2018-03-20 20:05:59'),
('RStyle', 'words to use instead of very', 1, 'very frightening', 'terrifying', '', '', '2018-03-20 20:06:16'),
('RStyle', 'words to use instead of very', 1, 'very funny', 'hilarious', '', '', '2018-03-20 20:06:24'),
('RStyle', 'words to use instead of very', 1, 'very glad', 'overjoyed', '', '', '2018-03-20 20:06:45'),
('RStyle', 'words to use instead of very', 1, 'very good', 'excellent', '', '', '2018-03-20 20:06:52'),
('RStyle', 'words to use instead of very', 1, 'very great', 'terrific', '', '', '2018-03-20 20:06:56'),
('RStyle', 'words to use instead of very', 1, 'very happy', 'ecstatic', '', '', '2018-03-20 20:07:04'),
('RStyle', 'words to use instead of very', 1, 'very hard', 'difficult', '', '', '2018-03-20 20:07:09'),
('RStyle', 'words to use instead of very', 1, 'very hard-to-find', 'rare', '', '', '2018-03-20 20:07:18'),
('RStyle', 'words to use instead of very', 1, 'very heavy', 'leaden', '', '', '2018-03-20 20:07:24'),
('RStyle', 'words to use instead of very', 1, 'very high', 'soaring', '', '', '2018-03-20 20:07:29'),
('RStyle', 'words to use instead of very', 1, 'very hot', 'sweltering', '', '', '2018-03-20 20:07:46'),
('RStyle', 'words to use instead of very', 1, 'very huge', 'colossal', '', '', '2018-03-20 20:07:53'),
('RStyle', 'words to use instead of very', 1, 'very hungry', 'starving', '', '', '2018-03-20 20:08:02'),
('RStyle', 'words to use instead of very', 1, 'very hurt', 'battered', '', '', '2018-03-20 20:08:07'),
('RStyle', 'words to use instead of very', 1, 'very important', 'crucial', '', '', '2018-03-20 20:08:16'),
('RStyle', 'words to use instead of very', 1, 'very intelligent', 'brilliant', '', '', '2018-03-20 20:08:36'),
('RStyle', 'words to use instead of very', 1, 'very interesting', 'captivating', '', '', '2018-03-20 20:08:46'),
('RStyle', 'words to use instead of very', 1, 'very large', 'huge', '', '', '2018-03-20 20:08:52'),
('RStyle', 'words to use instead of very', 1, 'very lazy', 'indolent', '', '', '2018-03-20 20:08:57'),
('RStyle', 'words to use instead of very', 1, 'very little', 'tiny', '', '', '2018-03-20 20:09:02'),
('RStyle', 'words to use instead of very', 1, 'very long', 'extensive', '', '', '2018-03-20 20:09:11'),
('RStyle', 'words to use instead of very', 1, 'very long-term', 'enduring', '', '', '2018-03-20 20:09:20'),
('RStyle', 'words to use instead of very', 1, 'very loose', 'slack', '', '', '2018-03-20 20:09:24'),
('RStyle', 'words to use instead of very', 1, 'very loud', 'thunderous', '', '', '2018-03-20 20:20:54'),
('RStyle', 'words to use instead of very', 1, 'very mean', 'cruel', '', '', '2018-03-20 20:09:27'),
('RStyle', 'words to use instead of very', 1, 'very messy', 'slovenly', '', '', '2018-03-20 20:09:36'),
('RStyle', 'words to use instead of very', 1, 'very necessary', 'essential', '', '', '2018-03-20 20:09:43'),
('RStyle', 'words to use instead of very', 1, 'very nervous', 'apprehensive', '', '', '2018-03-20 20:09:54'),
('RStyle', 'words to use instead of very', 1, 'very nice', 'kind', '', '', '2018-03-20 20:09:57'),
('RStyle', 'words to use instead of very', 1, 'very noisy', 'deeafening', '', '', '2018-03-20 20:10:04'),
('RStyle', 'words to use instead of very', 1, 'very often', 'frequently', '', '', '2018-03-20 20:10:10'),
('RStyle', 'words to use instead of very', 1, 'very old', 'ancient', '', '', '2018-03-20 20:10:16'),
('RStyle', 'words to use instead of very', 1, 'very old-fashioned', 'archaic', '', '', '2018-03-20 20:10:25'),
('RStyle', 'words to use instead of very', 1, 'very open', 'transparent', '', '', '2018-03-20 20:10:36'),
('RStyle', 'words to use instead of very', 1, 'very painful', 'excruciating', '', '', '2018-03-20 20:10:47'),
('RStyle', 'words to use instead of very', 1, 'very pale', 'ashen', '', '', '2018-03-20 20:10:51'),
('RStyle', 'words to use instead of very', 1, 'very perfect', 'flawless', '', '', '2018-03-20 20:11:00'),
('RStyle', 'words to use instead of very', 1, 'very poor', 'destitute', '', '', '2018-03-20 20:11:13'),
('RStyle', 'words to use instead of very', 1, 'very powerful', 'compelling', '', '', '2018-03-20 20:11:25'),
('RStyle', 'words to use instead of very', 1, 'very pretty', 'beautiful', '', '', '2018-03-20 20:11:32'),
('RStyle', 'words to use instead of very', 1, 'very quick', 'rapid', '', '', '2018-03-20 20:11:35'),
('RStyle', 'words to use instead of very', 1, 'very quiet', 'hushed', '', '', '2018-03-20 20:12:06'),
('RStyle', 'words to use instead of very', 1, 'very rainy', 'pouring', '', '', '2018-03-20 20:12:30'),
('RStyle', 'words to use instead of very', 1, 'very rich', 'wealthy', '', '', '2018-03-20 20:12:36'),
('RStyle', 'words to use instead of very', 1, 'very sad', 'sorrowful', '', '', '2018-03-20 20:13:01'),
('RStyle', 'words to use instead of very', 1, 'very scared', 'pertified', '', '', '2018-03-20 20:13:08'),
('RStyle', 'words to use instead of very', 1, 'very scary', 'chilling', '', '', '2018-03-20 20:13:15'),
('RStyle', 'words to use instead of very', 1, 'very serious', 'grave', '', '', '2018-03-20 20:13:19'),
('RStyle', 'words to use instead of very', 1, 'very sharp', 'keen', '', '', '2018-03-20 20:13:25'),
('RStyle', 'words to use instead of very', 1, 'very shiny', 'gleaming', '', '', '2018-03-20 20:13:30'),
('RStyle', 'words to use instead of very', 1, 'very short', 'brief', '', '', '2018-03-20 20:13:36'),
('RStyle', 'words to use instead of very', 1, 'very shy', 'timid', '', '', '2018-03-20 20:13:44'),
('RStyle', 'words to use instead of very', 1, 'very simple', 'basic', '', '', '2018-03-20 20:14:13'),
('RStyle', 'words to use instead of very', 1, 'very skinny', 'skeletal', '', '', '2018-03-20 20:14:20'),
('RStyle', 'words to use instead of very', 1, 'very slow', 'sluggish', '', '', '2018-03-20 20:21:05'),
('RStyle', 'words to use instead of very', 1, 'very small', 'petite', '', '', '2018-03-20 20:14:24'),
('RStyle', 'words to use instead of very', 1, 'very smart', 'intelligent', '', '', '2018-03-20 20:14:28'),
('RStyle', 'words to use instead of very', 1, 'very smelly', 'pungent', '', '', '2018-03-20 20:14:33'),
('RStyle', 'words to use instead of very', 1, 'very smooth', 'sleek', '', '', '2018-03-20 20:14:40'),
('RStyle', 'words to use instead of very', 1, 'very soft', 'downy', '', '', '2018-03-20 20:14:46'),
('RStyle', 'words to use instead of very', 1, 'very sorry', 'apologetic', '', '', '2018-03-20 20:14:51'),
('RStyle', 'words to use instead of very', 1, 'very special', 'exceptional', '', '', '2018-03-20 20:14:59'),
('RStyle', 'words to use instead of very', 1, 'very strong', 'forceful', '', '', '2018-03-20 20:15:08'),
('RStyle', 'words to use instead of very', 1, 'very stupid', 'idiotic', '', '', '2018-03-20 20:15:16'),
('RStyle', 'words to use instead of very', 1, 'very sure', 'certain', '', '', '2018-03-20 20:15:20'),
('RStyle', 'words to use instead of very', 1, 'very sweet', 'thoughtful', '', '', '2018-03-20 20:15:36'),
('RStyle', 'words to use instead of very', 1, 'very talented', 'gifted', '', '', '2018-03-20 20:15:45'),
('RStyle', 'words to use instead of very', 1, 'very tall', 'towering', '', '', '2018-03-20 20:15:51'),
('RStyle', 'words to use instead of very', 1, 'very tasty', 'delicious', '', '', '2018-03-20 20:15:58'),
('RStyle', 'words to use instead of very', 1, 'very thirsty', 'parched', '', '', '2018-03-20 20:16:04'),
('RStyle', 'words to use instead of very', 1, 'very tight', 'constricting', '', '', '2018-03-20 20:16:13'),
('RStyle', 'words to use instead of very', 1, 'very tiny', 'minuscule', '', '', '2018-03-20 20:16:56'),
('RStyle', 'words to use instead of very', 1, 'very tired', 'exhausted', '', '', '2018-03-20 20:17:05'),
('RStyle', 'words to use instead of very', 1, 'very ugly', 'hideous', '', '', '2018-03-20 20:17:11'),
('RStyle', 'words to use instead of very', 1, 'very unhappy', 'miserable', '', '', '2018-03-20 20:17:21'),
('RStyle', 'words to use instead of very', 1, 'very upset', 'distraught', '', '', '2018-03-20 20:17:31'),
('RStyle', 'words to use instead of very', 1, 'very warm', 'hot', '', '', '2018-03-20 20:17:34'),
('RStyle', 'words to use instead of very', 1, 'very weak', 'frail', '', '', '2018-03-20 20:17:38'),
('RStyle', 'words to use instead of very', 1, 'very well-to-do', 'wealthy', '', '', '2018-03-20 20:17:52'),
('RStyle', 'words to use instead of very', 1, 'very wet', 'soaked', '', '', '2018-03-20 20:17:57'),
('RStyle', 'words to use instead of very', 1, 'very wide', 'expansive', '', '', '2018-03-20 20:18:09'),
('RStyle', 'words to use instead of very', 1, 'very willing', 'eager', '', '', '2018-03-20 20:18:18'),
('RStyle', 'words to use instead of very', 1, 'very windy', 'blustery', '', '', '2018-03-20 20:18:27'),
('RStyle', 'words to use instead of very', 1, 'very wise', 'sage', '', '', '2018-03-20 20:18:30'),
('RStyle', 'words to use instead of very', 1, 'very worried', 'distressed', '', '', '2018-03-20 20:18:36');

-- --------------------------------------------------------

--
-- Table structure for table `LOGIN_TOKENS`
--

CREATE TABLE `LOGIN_TOKENS` (
  `user` varchar(100) NOT NULL,
  `token` varchar(100) NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `LOGIN_TOKENS`
--

-- --------------------------------------------------------

--
-- Table structure for table `SETS`
--

CREATE TABLE `SETS` (
  `id` varchar(30) NOT NULL,
  `name` varchar(100) NOT NULL,
  `chapter` int(11) NOT NULL DEFAULT '1',
  `user` varchar(100) NOT NULL,
  `descreption` varchar(2000) DEFAULT '',
  `is_public` tinyint(1) NOT NULL DEFAULT '0',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `downloads` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `SETS`
--

INSERT INTO `SETS` (`id`, `name`, `chapter`, `user`, `descreption`, `is_public`, `created_on`, `downloads`) VALUES
('CtyFDzRv', 'words to use instead of very', 1, 'RStyle', NULL, 0, '2018-03-20 19:59:55', 0);

-- --------------------------------------------------------

--
-- Table structure for table `STATS`
--

CREATE TABLE `STATS` (
  `user` varchar(100) NOT NULL,
  `setname` varchar(100) NOT NULL,
  `chapter` int(11) NOT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `USERS`
--

CREATE TABLE `USERS` (
  `user` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(1000) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'ok'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `USERS`
--

INSERT INTO `USERS` (`user`, `password`, `email`, `status`) VALUES
('RStyle', '$2y$10$mslBewZEAuShbMMCatFmOOYgjhvtAAHkaHOP3jNbkpCckkvSC1lRC', '', 'ok');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ACTIVITY`
--
ALTER TABLE `ACTIVITY`
  ADD KEY `ACTIVITY_ibfk_1` (`user`,`setname`,`chapter`);

--
-- Indexes for table `CARDS`
--
ALTER TABLE `CARDS`
  ADD PRIMARY KEY (`user`,`setname`,`chapter`,`fronttext`,`backtext`,`frontimageurl`,`backimageurl`);

--
-- Indexes for table `LOGIN_TOKENS`
--
ALTER TABLE `LOGIN_TOKENS`
  ADD PRIMARY KEY (`token`),
  ADD KEY `LOGIN_TOKENS_ibfk_1` (`user`);

--
-- Indexes for table `SETS`
--
ALTER TABLE `SETS`
  ADD PRIMARY KEY (`name`,`chapter`,`user`),
  ADD KEY `author` (`user`);

--
-- Indexes for table `STATS`
--
ALTER TABLE `STATS`
  ADD KEY `user` (`user`,`setname`,`chapter`);

--
-- Indexes for table `USERS`
--
ALTER TABLE `USERS`
  ADD PRIMARY KEY (`user`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ACTIVITY`
--
ALTER TABLE `ACTIVITY`
  ADD CONSTRAINT `ACTIVITY_ibfk_1` FOREIGN KEY (`user`,`setname`,`chapter`) REFERENCES `SETS` (`user`, `name`, `chapter`);

--
-- Constraints for table `CARDS`
--
ALTER TABLE `CARDS`
  ADD CONSTRAINT `CARDS_ibfk_1` FOREIGN KEY (`user`,`setname`,`chapter`) REFERENCES `SETS` (`user`, `name`, `chapter`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `LOGIN_TOKENS`
--
ALTER TABLE `LOGIN_TOKENS`
  ADD CONSTRAINT `LOGIN_TOKENS_ibfk_1` FOREIGN KEY (`user`) REFERENCES `USERS` (`user`) ON DELETE CASCADE;

--
-- Constraints for table `SETS`
--
ALTER TABLE `SETS`
  ADD CONSTRAINT `SETS_ibfk_1` FOREIGN KEY (`user`) REFERENCES `USERS` (`user`) ON DELETE CASCADE;

--
-- Constraints for table `STATS`
--
ALTER TABLE `STATS`
  ADD CONSTRAINT `STATS_ibfk_1` FOREIGN KEY (`user`,`setname`,`chapter`) REFERENCES `SETS` (`user`, `name`, `chapter`) ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
