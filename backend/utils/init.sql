CREATE DATABASE socialmedia;

GRANT ALL PRIVILEGES ON DATABASE socialmedia TO lauti;

\c socialmedia


CREATE TABLE users_profiles (
    profile_id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL
);

CREATE TABLE refresh_tokens (
    refresh_id BIGSERIAL PRIMARY KEY,
    REFRESH_TOKEN VARCHAR(10000) NOT NULL,
    REVOKED BOOLEAN,
    user_id BIGINT
);

CREATE TABLE user_auth (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(255) NOT NULL,
    fk_profile_id BIGINT,
    fk_token_id BIGINT,
    isAccountNonExpired BOOLEAN,
    isAccountNonLocked BOOLEAN,
    isCredentialsNonExpired BOOLEAN,
    isEnabled BOOLEAN,
    FOREIGN KEY (fk_profile_id) REFERENCES users_profiles(profile_id),
    FOREIGN KEY (fk_token_id) REFERENCES refresh_tokens(refresh_id)
);