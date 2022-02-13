
-- 05.02.2022
alter table rating add column score numeric(1) not null default 5;

alter table rating add constraint unique_ck unique (user_id, estimator_id);


-- 06.02.2022
-- create trigger
create trigger calculate_rating
    after insert
    on rating
    for each row
    EXECUTE function calculate_rating_function();

CREATE or REPLACE FUNCTION calculate_rating_function() returns TRIGGER
LANGUAGE plpgsql
AS $$
declare
avg_rating numeric(10, 5);
    counter numeric(10, 5);
    new_rating numeric(10, 5);
BEGIN

    avg_rating = (select average_rating from "user" where id = new.user_id);
    counter = (select count_rating from "user" where id = new.user_id);
    new_rating = ((avg_rating * counter) + new.score) / (counter + 1);
update "user"
set average_rating = new_rating, count_rating = count_rating + 1
where id = new.user_id;
return null;
END
$$;


-- add column
alter table "user" add column image_id bigint;
ALTER TABLE "user" ADD FOREIGN KEY ("image_id") REFERENCES "image" ("id");


-- create table favorite user service
create table favorite_user_service (
    user_id bigint not null,
    service_id bigint not null,
    constraint unique_ck_fav
       unique (user_id, service_id)
);
alter table favorite_user_service add CONSTRAINT fk_user  FOREIGN KEY(user_id) REFERENCES "user"(id);
alter table favorite_user_service add CONSTRAINT fk_service  FOREIGN KEY(service_id) REFERENCES service(id);
