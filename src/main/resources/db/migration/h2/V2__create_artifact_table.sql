CREATE TABLE artifact (
  id VARCHAR(255) NOT NULL,
   name VARCHAR(255),
   description VARCHAR(255),
   image_url VARCHAR(255),
   owner_id INT,
   CONSTRAINT pk_artifact PRIMARY KEY (id)
);

ALTER TABLE artifact ADD CONSTRAINT FK_ARTIFACT_ON_OWNER FOREIGN KEY (owner_id) REFERENCES wizard (id);