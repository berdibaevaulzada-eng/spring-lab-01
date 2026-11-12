
CREATE TABLE app_user (
                          id BIGSERIAL PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE user_role (
                           user_id BIGINT NOT NULL,
                           role VARCHAR(50) NOT NULL,
                           PRIMARY KEY (user_id, role),
                           CONSTRAINT fk_user_role_user
                               FOREIGN KEY (user_id)
                                   REFERENCES app_user(id)
);
