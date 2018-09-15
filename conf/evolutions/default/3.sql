# --- !Ups

CREATE TYPE status_enum AS ENUM ('NotStarted', 'InProgress', 'Finished');
ALTER TABLE todo ADD COLUMN status status_enum NOT NULL;

# --- !Downs

ALTER TABLE todo DROP COLUMN status;
DROP TYPE status_enum;
