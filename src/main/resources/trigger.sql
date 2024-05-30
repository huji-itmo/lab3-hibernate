create or replace function trigger_function()
    returns trigger as
$$

BEGIN
    insert into emotions(person_id, despair, joy, fear)
    select distinct people.id as person_id, 10.0 as despair, 0 as joy, 0 as fear
        from people join robots
            on people.conversation_id = robots.conversation_id
        full outer join emotions on people.id = emotions.person_id
        where robots.brevity >= 8 and emotions.person_id is NULL;

--     получаем людей, которые в диалоге с роботом, у которого краткость >= 8, и
--     которые еще не отчаялись (нет записи в эмоциях)
--     добавляем их в эмоции (кайфарик)

    return NEW;
end
$$ language plpgsql;
--
create or replace trigger robots_trigger after insert on robots execute function trigger_function();
create or replace trigger people_trigger after insert on people execute function trigger_function();
