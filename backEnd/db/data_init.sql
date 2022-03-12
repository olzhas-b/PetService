INSERT INTO "user" ("login","username","phone","password","first_name","last_name","full_name","city","country","location","description","created","updated")
VALUES ('admin','Dezgan','+77472718017','admin','admin','Bazarbekov','Olzhas Bazarbekov','Almaty','Kazahstan','google','I want to finish diplom','2021-12-25 22:13:20.368','2021-12-25 22:13:20.368');

INSERT INTO "service" (user_id, service_type, status, price, currency_code, price_per_time)
VALUES (1, 0, 0, 3000, 'KZT', 'час');

insert into "image" (name)
values ('olz');

insert into "additional_properties" ("service_id", "label", "text")
values (1, 'information', 'this is important info');