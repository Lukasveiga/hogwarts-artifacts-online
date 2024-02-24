CREATE TABLE `artifact` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9wrsj4y4t5uiba7957lcjdfge` (`owner_id`),
  CONSTRAINT `FK9wrsj4y4t5uiba7957lcjdfge` FOREIGN KEY (`owner_id`) REFERENCES `wizard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;