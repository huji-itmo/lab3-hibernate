DROP TABLE IF EXISTS PEOPLE cascade;
DROP TABLE IF EXISTS CONVERSATIONS cascade;
DROP TABLE IF EXISTS ACTIONS_IN_CONVERSATIONS cascade;
DROP TABLE IF EXISTS ROBOTS cascade;
DROP TABLE IF EXISTS ROBOT_FEATURES cascade;
DROP TABLE IF EXISTS EMOTIONS cascade;
DROP TABLE IF EXISTS FEATURES cascade;

DROP TYPE IF EXISTS CONVERSATION_MOOD;

CREATE TYPE CONVERSATION_MOOD as enum ('SLUGGISH', 'USELESS', 'BORING', 'DEEP');

CREATE TABLE CONVERSATIONS(
    id serial PRIMARY KEY,
    length_in_seconds int NOT NULL ,
    mood CONVERSATION_MOOD NOT NULL
);

CREATE TABLE PEOPLE(
    id serial PRIMARY KEY,
    gender boolean NOT NULL,
    name varchar(64) NOT NULL ,
    strength float NOT NULL,
    verbosity float NOT NULL,
    conversation_id bigint references CONVERSATIONS(id) NOT NULL

    CONSTRAINT strength CHECK ( strength between 0 and 10),
    CONSTRAINT verbosity CHECK ( verbosity between 0 and 10)
);

CREATE TABLE ROBOTS(
    id serial PRIMARY KEY,
    model text NOT NULL ,
--     model_view text NOT NULL, --Описание внешнего вида модели робота убрали по нормальной форме 3NF
    conversation_id bigint references CONVERSATIONS(id) NOT NULL,
    brevity float,
    damage float,

    CONSTRAINT brevity CHECK ( brevity between 0 and 10)
);

CREATE TABLE EMOTIONS(
    id serial PRIMARY KEY,
    person_id bigint references PEOPLE(id) NOT NULL,
    joy float NOT NULL,
    fear float NOT NULL,
    despair float NOT NULL,

    CONSTRAINT joy CHECK ( joy between 0 AND 10 ),
    CONSTRAINT fear CHECK ( fear between 0 AND  10 ),
    CONSTRAINT despair CHECK ( despair between 0 AND 10 )

);
