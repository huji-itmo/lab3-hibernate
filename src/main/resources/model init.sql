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

    CONSTRAINT strength CHECK ( strength>=0 AND strength <= 10 ),
    CONSTRAINT verbosity CHECK ( verbosity>=0 AND verbosity <= 10 )
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

-- INSERT INTO CONVERSATIONS( about, length, mood) VALUES
--    ('допрос', 'краткий', 'глубокий');
--
-- INSERT INTO ROBOTS(model, conversation_id) VALUES
--     ('r2d2', 1);
--
-- INSERT INTO ROBOT_FEATURES(description, robot_id) VALUES
--     ('краткость', 1),
--     ('точность', 1),
--     ('может привести в отчаяние', 1);
--
-- INSERT INTO PEOPLE(gender, name, conversation_id) VALUES
--     ('мужчина', 'Олвин', 1),
--     ('мужчина', 'ХИВЛА', 1);
--
-- INSERT INTO EMOTIONS(person_id, description, cause, intensity) VALUES
--     (1, 'отчаяние', 'диалог с роботом', 8),
--     (1, 'обессилен', 'диалог с роботом', 6);
--
-- INSERT INTO ACTIONS_IN_CONVERSATIONS(acting_person_id, action_description, conversation_id) VALUES
--     (2, 'вмешался в диалог', 1);
