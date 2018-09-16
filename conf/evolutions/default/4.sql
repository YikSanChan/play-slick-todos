# --- !Ups

CREATE TYPE label_enum AS ENUM ('Easy', 'Medium', 'Hard', 'Coding', 'Design', 'Research');
ALTER TABLE todo ADD COLUMN labels label_enum [] DEFAULT '{}' NOT NULL;

# --- !Downs

ALTER TABLE todo DROP COLUMN labels;
DROP TYPE label_enum;
