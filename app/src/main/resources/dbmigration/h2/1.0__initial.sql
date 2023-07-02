-- apply changes
create table urls (
  id                            bigint generated by default as identity not null,
  name                          varchar(255),
  created_at                    timestamp not null,
  constraint pk_urls primary key (id)
);

create table checks (
  id                            bigint generated by default as identity not null,
  status_code                   integer not null,
  title                         varchar(255),
  h1                            varchar(255),
  description                   clob,
  url_id                        bigint not null,
  created_at                    timestamp not null,
  constraint pk_checks primary key (id)
);

-- foreign keys and indices
create index ix_checks_url_id on checks (url_id);
alter table checks add constraint fk_checks_url_id foreign key (url_id) references urls (id) on delete restrict on update restrict;

