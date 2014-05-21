drop table if exists password_reset;
drop table if exists user_role;
drop table if exists user;
drop table if exists role;

create table role (
  id bigint(20) not null auto_increment,
  description varchar(255) default null,
  granted_authority varchar(255) default null,
  name varchar(255) default null,
  primary key (id)
) type=InnoDB;

insert into role(id, description, granted_authority, name) values
(1, '', 'ROLE_USER', ''),
(2, '', 'ROLE_COMP_ADMIN', ''),
(3, '', 'ROLE_GUEST', '');

create table user (
  id bigint(20) not null auto_increment,
  username varchar(50) not null,
  enabled tinyint(1) not null,
  name varchar(30) not null,
  password varchar(100) not null,
  password_reminder varchar(100) default null,
  primary key (id),
  unique key name (name),
  unique key username (username)
) type=InnoDB;

create table user_role (
  user_id bigint(20) not null,
  role_id bigint(20) not null,
  primary key (user_id, role_id),
  key USER_ROLE_ROLE_ID (role_id),
  key USER_ROLE_USER_ID (user_id),
  constraint USER_ROLE_USER_ID foreign key (user_id) references user (id),
  constraint USER_ROLE_ROLE_ID foreign key (role_id) references role (id)
) type=InnoDB;

create table password_reset (
  id bigint(20) not null auto_increment,
  expiry_date timestamp not null,
  token varchar(255) not null,
  used_date timestamp null,
  user_id bigint(20) not null,
  primary key (id),
  constraint PASSWORD_RESET_USER_ID foreign key (user_id) references user (id)
) type=InnoDB;
