--DROP INDEX IF EXISTS compilations_title_unique_idx;
--DROP INDEX IF EXISTS categories_name_unique_idx;
--DROP INDEX IF EXISTS users_email_unique_idx;

DROP TABLE IF EXISTS events_compilations;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS participation;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS categories;

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(50) NOT NULL,
  name_lower VARCHAR(50) NOT NULL,
  CONSTRAINT pk_categories PRIMARY KEY (id),
  CONSTRAINT uq_categories_name_lower UNIQUE (name_lower)
);

--CREATE UNIQUE INDEX IF NOT EXISTS categories_name_unique_idx on categories(LOWER(name));

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(250) NOT NULL,
  email VARCHAR(254) NOT NULL,
  email_lower VARCHAR(254) NOT NULL,
  CONSTRAINT pk_users PRIMARY KEY (id),
  CONSTRAINT uq_users_email_lower UNIQUE (email_lower)
);

--CREATE UNIQUE INDEX IF NOT EXISTS users_email_unique_idx on users (LOWER(email));

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  category_id BIGINT NOT NULL,
  created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  description VARCHAR(7000) NOT NULL,
  event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  initiator_id BIGINT NOT NULL,
  lat DOUBLE PRECISION NOT NULL,
  lon DOUBLE PRECISION NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INTEGER NOT NULL,
  confirmed_requests BIGINT NOT NULL,
  published_on TIMESTAMP WITHOUT TIME ZONE,
  request_moderation BOOLEAN NOT NULL,
  state VARCHAR(9) NOT NULL,
  title VARCHAR(120) NOT NULL,
  CONSTRAINT pk_events PRIMARY KEY (id),
  CONSTRAINT fk_events_category_id FOREIGN KEY (category_id)
      REFERENCES categories (id)
      ON UPDATE NO ACTION
      ON DELETE NO ACTION,
  CONSTRAINT fk_events_initiator_id FOREIGN KEY (initiator_id)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participation (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  requester_id BIGINT NOT NULL,
  event_id BIGINT NOT NULL,
  status VARCHAR(9) NOT NULL,
  CONSTRAINT pk_participation PRIMARY KEY (id),
  CONSTRAINT uq_participation_event_id_requester_id UNIQUE (event_id, requester_id),
  CONSTRAINT fk_participation_requester_id FOREIGN KEY (requester_id)
          REFERENCES users (id)
          ON UPDATE NO ACTION
          ON DELETE CASCADE,
  CONSTRAINT fk_participation_event_id FOREIGN KEY (event_id)
            REFERENCES events (id)
            ON UPDATE NO ACTION
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  title VARCHAR(50) NOT NULL,
  pinned BOOLEAN NOT NULL,
  title_lower VARCHAR(50) NOT NULL,
  CONSTRAINT pk_compilations PRIMARY KEY (id),
  CONSTRAINT uq_compilations_title_lower UNIQUE (title_lower)
);

--CREATE UNIQUE INDEX IF NOT EXISTS compilations_title_unique_idx on compilations (LOWER(title));

CREATE TABLE IF NOT EXISTS events_compilations (
     compilation_id BIGINT NOT NULL,
     event_id BIGINT NOT NULL,
     CONSTRAINT uq_events_compilations UNIQUE (compilation_id, event_id),
     CONSTRAINT fk_events_compilations_compilation_id FOREIGN KEY (compilation_id)
            REFERENCES compilations(id)
            ON DELETE CASCADE,
     CONSTRAINT fk_events_compilations_event_id FOREIGN KEY (event_id)
            REFERENCES events(id)
            ON DELETE CASCADE
);