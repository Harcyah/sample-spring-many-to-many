create table project
(
    id    UUID        not null,
    name  varchar(32) not null,
    start date        not null,
    primary key (id)
);

create table developer
(
    id         UUID        not null,
    first_name varchar(32) not null,
    last_name  varchar(32) not null,
    primary key (id)
);

create table developer_project
(
    developer_id UUID        not null,
    project_id   UUID        not null,
    role         varchar(32) not null,
    created      date        not null,
    primary key (developer_id, project_id)
);
