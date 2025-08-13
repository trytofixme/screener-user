CREATE TABLE country (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    title varchar(64) UNIQUE NOT NULL,
    locale VARCHAR(16) NOT NULL,
);

CREATE TABLE users (
    telegram_id text PRIMARY KEY,
    username varchar(64) UNIQUE NOT NULL,
    email varchar(64) UNIQUE NOT NULL,
    phone varchar(32) UNIQUE,
    about_me varchar(4096),
    city varchar(64),
    country_id bigint NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_country_id FOREIGN KEY (country_id) REFERENCES country (id)
);

CREATE TABLE subscription (
    id uuid PRIMARY KEY,
    telegram_id text NOT NULL,
    start_date timestamptz NOT NULL DEFAULT current_timestamp,
    end_date timestamptz NOT NULL,

    CONSTRAINT fk_subscription_telegram_id FOREIGN KEY (telegram_id) REFERENCES users (telegram_id)
);