# --- !Ups

CREATE TABLE todo (
  id SERIAL PRIMARY KEY,
  content VARCHAR NOT NULL
)

# --- !Downs

DROP TABLE todo
