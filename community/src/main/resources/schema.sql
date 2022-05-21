DROP TABLE IF EXISTS heechul.comments;
DROP TABLE IF EXISTS heechul.likes;
DROP TABLE IF EXISTS heechul.postings;
DROP TABLE IF EXISTS heechul.users;

-- users Table Create SQL
CREATE TABLE heechul.users
(
    `id`            BIGINT          NOT NULL    AUTO_INCREMENT,
    `account_id`    VARCHAR(100)    NOT NULL,
    `nickname`      VARCHAR(50)     NOT NULL,
    `account_type`  TINYINT         NOT NULL    COMMENT 'enum [LESSOR(0), REALTOR(1), LESSEE(3), NONE(4)]',
    `quit`          TINYINT         NOT NULL    DEFAULT 0 COMMENT '== is_deleted',
    `created_at`    DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    DATETIME        NOT NULL,
     PRIMARY KEY (id)
);


-- postings Table Create SQL
CREATE TABLE heechul.postings
(
    `id`          BIGINT         NOT NULL    AUTO_INCREMENT,
    `title`       VARCHAR(50)    NOT NULL,
    `content`     TEXT           NOT NULL,
    `is_deleted`  TINYINT        NOT NULL    DEFAULT 0,
    `created_at`  DATETIME       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME       NOT NULL,
    `users_id`    BIGINT         NOT NULL,
     PRIMARY KEY (id)
);


-- likes Table Create SQL
CREATE TABLE heechul.likes
(
    `id`           BIGINT      NOT NULL    AUTO_INCREMENT,
    `postings_id`  BIGINT      NOT NULL,
    `users_id`     BIGINT      NOT NULL,
    `is_deleted`   TINYINT     NOT NULL    DEFAULT 0,
    `created_at`   DATETIME    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   DATETIME    NOT NULL,
     PRIMARY KEY (id)
);

ALTER TABLE likes
    ADD CONSTRAINT FK_likes_postings_id_postings_id FOREIGN KEY (postings_id)
        REFERENCES postings (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE likes
    ADD CONSTRAINT FK_likes_users_id_users_id FOREIGN KEY (users_id)
        REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- comments Table Create SQL
CREATE TABLE heechul.comments
(
    `id`           BIGINT      NOT NULL    AUTO_INCREMENT, 
    `users_id`     BIGINT      NOT NULL, 
    `postings_id`  BIGINT      NOT NULL, 
    `content`      TEXT        NOT NULL, 
    `is_deleted`   TINYINT     NOT NULL    DEFAULT 0, 
    `created_at`   DATETIME    NOT NULL    DEFAULT CURRENT_TIMESTAMP, 
    `updated_at`   DATETIME    NOT NULL, 
     PRIMARY KEY (id)
);

ALTER TABLE comments
    ADD CONSTRAINT FK_comments_postings_id_postings_id FOREIGN KEY (postings_id)
        REFERENCES postings (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE comments
    ADD CONSTRAINT FK_comments_users_id_users_id FOREIGN KEY (users_id)
        REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT;



