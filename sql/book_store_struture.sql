-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: book_store
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `author_book`
--

DROP TABLE IF EXISTS `author_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `author_book` (
  `book_id` bigint NOT NULL,
  `author_id` bigint NOT NULL,
  KEY `FK7cqs8nb7l859jcwwqoawcokqf` (`author_id`),
  KEY `FKmeehr164a2cpxegeiawuv40a3` (`book_id`),
  CONSTRAINT `FK7cqs8nb7l859jcwwqoawcokqf` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`),
  CONSTRAINT `FKmeehr164a2cpxegeiawuv40a3` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci,
  `fullname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount_in_stock` bigint DEFAULT NULL,
  `book_title` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci,
  `entry_price` double DEFAULT NULL,
  `img_link` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `publishing_year` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKleqa3hhc0uhfvurq6mil47xk0` (`category_id`),
  CONSTRAINT `FKleqa3hhc0uhfvurq6mil47xk0` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `price` double DEFAULT NULL,
  `book_id` bigint DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjqe04yonp6a52rhbf2y0m03qw` (`book_id`),
  KEY `FKjyu2qbqt8gnvno9oe9j2s2ldk` (`order_id`),
  CONSTRAINT `FKjqe04yonp6a52rhbf2y0m03qw` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
  CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_date` datetime DEFAULT NULL,
  `order_status` int DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `profile_user`
--

DROP TABLE IF EXISTS `profile_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profile_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `phone_number` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs3sunokbxcofhx27qv03rha3g` (`user_id`),
  CONSTRAINT `FKs3sunokbxcofhx27qv03rha3g` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `registration_token`
--

DROP TABLE IF EXISTS `registration_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registration_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiration` bigint DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpmf52fk27bywm8is5s2jv3xam` (`user_id`),
  CONSTRAINT `FKpmf52fk27bywm8is5s2jv3xam` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reset_password_token`
--

DROP TABLE IF EXISTS `reset_password_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reset_password_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiration` bigint DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlu8wgvb3mkbxir0sls596u7np` (`user_id`),
  CONSTRAINT `FKlu8wgvb3mkbxir0sls596u7np` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `FKt7e7djp752sqn6w22i6ocqy6q` (`role_id`),
  KEY `FKj345gk1bovqvfame88rcx7yyx` (`user_id`),
  CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKt7e7djp752sqn6w22i6ocqy6q` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `fullname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `username` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-23 20:20:58
