INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (1, "account1", "name1", 0, now(), now());
INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (2, "account2", "name2", 0, now(), now());
INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (3, "account3", "name3", 0, now(), now());
INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (4, "account4", "name4", 0, now(), now());
INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (5, "account5", "name5", 0, now(), now());

INSERT INTO postings (id, title, content, created_at, updated_at, users_id) VALUES (1, "title1", "content1", now(), now(), 1);
INSERT INTO postings (id, title, content, created_at, updated_at, users_id) VALUES (2, "title2", "content2", now(), now(), 1);