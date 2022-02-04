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
    id           UUID        not null,
    developer_id UUID        not null,
    project_id   UUID        not null,
    role         varchar(32) not null,
    created      date        not null,
    primary key (id),
    constraint developer_project_to_developer foreign key (developer_id) references developer (id)
        on update cascade
        on delete cascade,
    constraint developer_project_to_project foreign key (project_id) references project (id)
        on update cascade
        on delete cascade
);
