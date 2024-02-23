CREATE TABLE hogwarts_user (
  id INT AUTO_INCREMENT NOT NULL,
   username VARCHAR(255) NULL,
   password VARCHAR(255) NULL,
   enable BIT(1) NOT NULL,
   roles VARCHAR(255) NULL,
   CONSTRAINT pk_hogwartsuser PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;