create table department_options (
    department_id   bigint not null references departments(id),
    option          varchar(255) not null,
  primary key (department_id, option)
);
