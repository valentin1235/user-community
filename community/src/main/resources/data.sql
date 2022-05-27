INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (1, "account1", "name1", 0, "2022-05-20T03:03:12", "2022-05-20T03:03:12");
INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (2, "account2", "name2", 0, "2022-05-21T03:03:12", "2022-05-21T03:03:12");
INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (3, "account3", "name3", 0, "2022-05-22T03:03:12", "2022-05-22T03:03:12");
INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (4, "account4", "name4", 0, "2022-05-23T03:03:12", "2022-05-23T03:03:12");
INSERT INTO users (id, account_id, nickname, account_type, created_at, updated_at) VALUES (5, "account5", "name5", 0, "2022-05-24T03:03:12", "2022-05-24T03:03:12");

INSERT INTO postings (id, title, content, created_at, updated_at, users_id) VALUES (1, "title1", "content1", "2022-05-20T03:03:12", "2022-05-20T03:03:12", 1);
INSERT INTO postings (id, title, content, created_at, updated_at, users_id) VALUES (2, "title2", "content2", "2022-05-21T03:03:12", "2022-05-21T03:03:12", 1);
INSERT INTO postings (id, title, content, created_at, updated_at, users_id) VALUES (3, "title3", "content3", "2022-05-22T03:03:12", "2022-05-22T03:03:12", 1);
INSERT INTO postings (id, title, content, created_at, updated_at, users_id) VALUES (4, "title4", "content4", "2022-05-23T03:03:12", "2022-05-23T03:03:12", 1);
INSERT INTO postings (id, title, content, created_at, updated_at, users_id) VALUES (5, "title5", "content5", "2022-05-24T03:03:12", "2022-05-24T03:03:12", 1);
INSERT INTO postings (id, title, content, created_at, updated_at, users_id) VALUES (6, "title6", "content6", "2022-05-25T03:03:12", "2022-05-25T03:03:12", 1);
INSERT INTO postings (id, title, content, created_at, updated_at, users_id) VALUES (7, "title7", "content7", "2022-05-26T03:03:12", "2022-05-26T03:03:12", 1);

INSERT INTO likes (id, postings_id, users_id, updated_at, created_at) VALUES (1, 1, 1, "2022-05-20T03:03:12", "2022-05-20T03:03:12");
INSERT INTO likes (id, postings_id, users_id, updated_at, created_at) VALUES (2, 1, 2, "2022-05-21T03:03:12", "2022-05-21T03:03:12");
INSERT INTO likes (id, postings_id, users_id, updated_at, created_at) VALUES (3, 1, 3, "2022-05-22T03:03:12", "2022-05-22T03:03:12");
INSERT INTO likes (id, postings_id, users_id, updated_at, created_at) VALUES (4, 1, 4, "2022-05-23T03:03:12", "2022-05-23T03:03:12");

INSERT INTO comments (id, postings_id, users_id, content, updated_at, created_at) VALUES (1, 1, 1, "content1", "2022-05-21T03:03:12", "2022-05-21T03:03:12");
INSERT INTO comments (id, postings_id, users_id, content, updated_at, created_at) VALUES (2, 1, 2, "content2", "2022-05-22T03:03:12", "2022-05-22T03:03:12");
INSERT INTO comments (id, postings_id, users_id, content, updated_at, created_at) VALUES (3, 1, 3, "content3", "2022-05-23T03:03:12", "2022-05-23T03:03:12");
INSERT INTO comments (id, postings_id, users_id, content, updated_at, created_at) VALUES (4, 1, 4, "content4", "2022-05-24T03:03:12", "2022-05-24T03:03:12");



