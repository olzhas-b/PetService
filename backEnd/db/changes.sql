
-- 05.02.2022
alter table rating add column score numeric(1) not null default 5;

alter table rating add constraint unique_ck unique (user_id, estimator_id);
