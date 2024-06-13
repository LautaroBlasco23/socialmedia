CREATE DATABASE socialmedia;

GRANT ALL PRIVILEGES ON DATABASE socialmedia TO lauti;

\c socialmedia

-- Auth Tables
CREATE TABLE users_auth (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  roles VARCHAR(255) NOT NULL,
  is_account_non_expired BOOLEAN,
  is_account_non_locked BOOLEAN,
  is_credentials_non_expired BOOLEAN,
  is_enabled BOOLEAN
);

CREATE SEQUENCE users_auth_seq START 1;

CREATE TABLE refresh_tokens (
  id BIGSERIAL PRIMARY KEY,
  REFRESH_TOKEN VARCHAR(10000) NOT NULL,
  REVOKED BOOLEAN,
  user_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES users_auth(id) ON DELETE CASCADE
);

CREATE SEQUENCE refresh_tokens_seq START 1;

-- Business logic tables
CREATE TABLE users_profiles (
  id BIGSERIAL PRIMARY KEY,
  auth_id BIGINT UNIQUE NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  FOREIGN KEY (auth_id) REFERENCES users_auth(id) ON DELETE CASCADE
);

CREATE SEQUENCE users_profiles_seq START 1;

CREATE TABLE user_following (
  user_id BIGINT NOT NULL,
  following_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, following_id),
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE,
  FOREIGN KEY (following_id) REFERENCES users_profiles(id) ON DELETE CASCADE
);

CREATE TABLE posts (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  text VARCHAR(1000) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE
);

CREATE SEQUENCE posts_seq START 1;

CREATE TABLE posts_likes (
  post_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (post_id, user_id),
  FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE
);

CREATE TABLE comments (
  id BIGSERIAL PRIMARY KEY,
  text VARCHAR(255),
  user_id BIGINT NOT NULL,
  post_id BIGINT NOT NULL,
  is_reply BOOLEAN NOT NULL,
  parent_comment_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE,
  FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
  FOREIGN KEY (parent_comment_id) REFERENCES comments(id) ON DELETE CASCADE
);

CREATE SEQUENCE comments_seq START 1;

CREATE TABLE comments_likes (
  comment_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (comment_id, user_id),
  FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE
);
