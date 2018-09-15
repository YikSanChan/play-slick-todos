# --- !Ups

ALTER TABLE todo ADD COLUMN status VARCHAR NOT NULL

# --- !Downs

ALTER TABLE todo DROP COLUMN status