-- country region city
CREATE TABLE country (
                         country_id serial primary key ,
                         city_id integer NOT NULL default 0,
                         name varchar(128) NOT NULL default '',
                         region_id integer NOT NULL default  0
);

CREATE TABLE region (
                        region_id  serial PRIMARY KEY ,
                        country_id integer NOT NULL default 0,
                        city_id integer NOT NULL default 0,
                        name varchar(64) NOT NULL default ''
) ;

CREATE TABLE city (
                      city_id serial primary key ,
                      country_id integer NOT NULL default 0,
                      region_id integer NOT NULL default 0,
                      name varchar(128) NOT NULL default ''
);

-- region constraints
alter table region add CONSTRAINT fk_country
    FOREIGN KEY(country_id)
        REFERENCES country(country_id);


-- city constraints
alter table city add CONSTRAINT fk_region
    FOREIGN KEY(region_id)
        REFERENCES region(region_id);

alter table city add CONSTRAINT fk_country
    FOREIGN KEY(country_id)
        REFERENCES country(country_id);




-- others table
CREATE TABLE "user" (
                        "id" BIGSERIAL PRIMARY KEY,
                        "image_id" bigint,
                        "username" varchar(40) not null unique,
                        "phone" varchar(40) not null unique,
                        "password" varchar(50) not null,
                        "first_name" varchar(40) not null,
                        "last_name" varchar(40),
                        "full_name" varchar(70),
                        "city" varchar(40),
                        "country" varchar(40),
                        "location" varchar(80),
                        "description" varchar(1000),
                        "count_rating" numeric(18) default 0,
                        "average_rating" numeric(7, 6) default 0.000000,
                        "created" timestamp(6),
                        "updated" timestamp(6),
                        "is_deleted" boolean default false not null ,
                        "status" numeric(3) default 0 not null
);


CREATE TABLE "service" (
                           "id" BIGSERIAL PRIMARY KEY,
                           "user_id" BIGINT,
                           "service_type" numeric(3) default 1,
                           "price" numeric(18) default 0,
                           "currency_code" varchar(10) default 'KZT',
                           "price_per_time" varchar(10) default '',
                           "rating" numeric(7, 6) default 5.000000 not null ,
                           "description"     varchar(1000),
                           "acceptable_pets" varchar(200),
                           "longitude" numeric(10, 6) ,
                           "latitude" numeric(10, 6) ,
                           "acceptable_size" numeric(4) default 20,
                           "last_activity"   timestamp(6),
                           "status" numeric(3) default 0 not null,
                           "is_deleted" boolean default false not null
);

--
-- CREATE TABLE "service_detail" (
--                                   "id" BIGSERIAL PRIMARY KEY,
--                                   "description" varchar(1000),
--                                   "acceptable_pets" varchar(200),
--                                   "acceptable_size" varchar(200),
--                                   "last_activity" timestamp(6)
-- );

CREATE TABLE "pet" (
                       "id" BIGSERIAL PRIMARY KEY,
                       "user_id" BIGINT,
                       "name" varchar(40),
                       "type" varchar(40),
                       "weight" int,
                       "breed" varchar(30),
                       "image_id" BIGINT,
                       "status" numeric(3) default 0 not null
);

CREATE TABLE "service_image" (
                                 "image_id" BIGINT not null ,
                                 "service_id" BIGINT not null
);

CREATE TABLE "rating" (
                          "id" BIGSERIAL PRIMARY KEY,
                          "user_id" BIGINT,
                          "estimator_id" BIGINT,
                          "created" timestamp(6),
                          score numeric(1) not null default 0
);

CREATE TABLE "image" (
                         "id" BIGSERIAL PRIMARY KEY,
                         "name" varchar(40),
                         "content" bytea,
                         "content_type" varchar(40),
                         "status" numeric(3)  not null default 0
);

CREATE TABLE "review" (
                          "id" BIGSERIAL PRIMARY KEY,
                          "user_id" BIGINT not null,
                          "reviewer_id" BIGINT not null,
                          "service_id" BIGINT not null,
                          "text" varchar(1000),
                          "count_like" int,
                          "created" timestamp(6),
                          UNIQUE(user_id, reviewer_id, service_id)
);


CREATE TABLE "additional_properties" (
                     "id" BIGSERIAL PRIMARY KEY,
                     "service_id" BIGINT,
                     "label" varchar(40),
                     "text" varchar(400)
);

create table favorite_user_service (
                                       user_id bigint not null,
                                       service_id bigint not null,
                                       constraint unique_ck_fav
                                           unique (user_id, service_id)
);



ALTER TABLE "service_image" ADD FOREIGN KEY ("service_id") REFERENCES "service" ("id");
ALTER TABLE "service_image" ADD FOREIGN KEY ("image_id") REFERENCES "image" ("id") on delete cascade;

ALTER TABLE "service" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

-- ALTER TABLE "service_detail" ADD FOREIGN KEY ("id") REFERENCES "service" ("id");

ALTER TABLE "pet" ADD FOREIGN KEY ("image_id") REFERENCES "image" ("id");
ALTER TABLE "pet" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "rating" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");
ALTER TABLE "rating" ADD FOREIGN KEY ("estimator_id") REFERENCES "user" ("id");

ALTER TABLE "review" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");
ALTER TABLE "review" ADD FOREIGN KEY ("reviewer_id") REFERENCES "user" ("id");
ALTER TABLE "review" ADD FOREIGN KEY ("service_id") REFERENCES "service" ("id");

ALTER TABLE "additional_properties" ADD FOREIGN KEY ("service_id") REFERENCES "service" ("id");

ALTER TABLE "user" ADD FOREIGN KEY ("image_id") REFERENCES "image" ("id");

alter table favorite_user_service add CONSTRAINT fk_user  FOREIGN KEY(user_id) REFERENCES "user"(id);
alter table favorite_user_service add CONSTRAINT fk_service  FOREIGN KEY(service_id) REFERENCES service(id);





-- trigger section
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