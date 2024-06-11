CREATE DATABASE socialmedia;

GRANT ALL PRIVILEGES ON DATABASE socialmedia TO lauti;

\c socialmedia

-- Auth Tables
CREATE TABLE users_auth (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  roles VARCHAR(255) NOT NULL,
  isAccountNonExpired BOOLEAN,
  isAccountNonLocked BOOLEAN,
  isCredentialsNonExpired BOOLEAN,
  isEnabled BOOLEAN
);

CREATE TABLE refresh_tokens (
  id SERIAL PRIMARY KEY,
  REFRESH_TOKEN VARCHAR(10000) NOT NULL,
  REVOKED BOOLEAN,
  user_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES users_auth(id) ON DELETE CASCADE
);

-- Business logic tables
CREATE TABLE users_profiles (
  id SERIAL PRIMARY KEY,
  auth_id BIGINT UNIQUE NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  FOREIGN KEY (auth_id) REFERENCES users_auth(id) ON DELETE CASCADE
);

CREATE TABLE user_following (
  user_id BIGINT NOT NULL,
  following_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, following_id),
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE,
  FOREIGN KEY (following_id) REFERENCES users_profiles(id) ON DELETE CASCADE
);

CREATE TABLE posts (
  id SERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  text VARCHAR(1000) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE
);

CREATE TABLE post_likes (
  post_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (post_id, user_id),
  FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE
);

CREATE TABLE comments (
  id SERIAL PRIMARY KEY,
  text VARCHAR(255),
  user_id BIGINT NOT NULL,
  post_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE,
  FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

CREATE TABLE comment_likes (
  comment_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (comment_id, user_id),
  FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users_profiles(id) ON DELETE CASCADE
);
