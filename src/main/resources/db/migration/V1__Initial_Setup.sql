
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'app_user') THEN
            CREATE TABLE app_user (
                                      id BIGINT PRIMARY KEY,
                                      username VARCHAR(50) NOT NULL,
                                      password VARCHAR(100) NOT NULL,
                                      email VARCHAR(100) NOT NULL,
                                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      UNIQUE (username),
                                      UNIQUE (email)
            );
        END IF;
    END
$$;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'item') THEN
            CREATE TABLE item (
                                  id BIGINT PRIMARY KEY,
                                  name VARCHAR(255) NOT NULL,
                                  starting_price DECIMAL(10, 2) NOT NULL,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            );
        END IF;
    END
$$;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'offer') THEN
            CREATE TABLE offer (
                                   id BIGINT PRIMARY KEY,
                                   amount DECIMAL(10, 2) NOT NULL,
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   user_id BIGINT,
                                   item_id BIGINT,
                                   CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_user (id),
                                   CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES item (id)
            );
        END IF;
    END
$$;

-- Insert initial user data if it doesn't exist
INSERT INTO app_user (id, username, password, email)
SELECT 1, 'testuser1', 'password1', 'testuser1@example.com'
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE id = 1);

INSERT INTO app_user (id, username, password, email)
SELECT 2, 'testuser2', 'password2', 'testuser2@example.com'
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE id = 2);

-- Insert initial item data if it doesn't exist
INSERT INTO item (id, name, starting_price)
SELECT 1, 'Item1', 50.00
WHERE NOT EXISTS (SELECT 1 FROM item WHERE id = 1);

INSERT INTO item (id, name, starting_price)
SELECT 2, 'Item2', 100.00
WHERE NOT EXISTS (SELECT 1 FROM item WHERE id = 2);

-- Optionally, insert initial offer data if it doesn't exist
INSERT INTO offer (id, amount, user_id, item_id)
SELECT 1, 100.00, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM offer WHERE id = 1);

INSERT INTO offer (id, amount, user_id, item_id)
SELECT 2, 200.00, 2, 2
WHERE NOT EXISTS (SELECT 1 FROM offer WHERE id = 2);
