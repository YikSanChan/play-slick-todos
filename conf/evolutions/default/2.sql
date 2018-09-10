# --- !Ups

ALTER TABLE todo ADD COLUMN priority SMALLINT NOT NULL

# --- !Downs

ALTER TABLE todo DROP COLUMN priority
