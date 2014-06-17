create table role (
  id bigint(20) not null auto_increment,
  description varchar(255) default null,
  granted_authority varchar(255) default null,
  name varchar(255) default null,
  primary key (id)
) engine=InnoDB;

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
) engine=InnoDB;

create table user_role (
  user_id bigint(20) not null,
  role_id bigint(20) not null,
  primary key (user_id, role_id),
  key USER_ROLE_ROLE_ID (role_id),
  key USER_ROLE_USER_ID (user_id),
  constraint USER_ROLE_USER_ID foreign key (user_id) references user (id),
  constraint USER_ROLE_ROLE_ID foreign key (role_id) references role (id)
) engine=InnoDB;

create table password_reset (
  id bigint(20) not null auto_increment,
  expiry_date timestamp not null,
  token varchar(255) not null,
  used_date timestamp null,
  user_id bigint(20) not null,
  primary key (id),
  constraint PASSWORD_RESET_USER_ID foreign key (user_id) references user (id)
) engine=InnoDB;

create table competition (
  id bigint(20) not null auto_increment,
  name varchar(30) not null,
  active tinyint(1) not null,
  primary key (id)
) engine=InnoDB;

create table team_category (
  id bigint(20) not null auto_increment,
  name varchar(30) not null,
  parent_category_id bigint(20) not null,
  primary key (id),
  constraint TEAM_CATEGORY_PARENT_ID foreign key (parent_category_id) references team_category (id)
) engine=InnoDB;

create table team (
  id bigint(20) not null auto_increment,
  name varchar(30) not null,
  primary key (id)
) engine=InnoDB;

create table assigned_team_category (
  category_id bigint(20) not null,
  team_id bigint(20) not null,
  primary key (category_id, team_id),
  constraint ASSIGNED_CATEGORY_TEAM_ID foreign key (team_id) references team (id),
  constraint ASSIGNED_CATEGORY_CATEGORY_ID foreign key (category_id) references team_category (id)
) engine=InnoDB;

create table fixture (
  id bigint(20) not null auto_increment,
  competition_id bigint(20) not null,
  home_team_id bigint(20) not null,
  away_team_id bigint(20) not null,
  home_score int(11) null,
  away_score int(11) null,
  match_time datetime not null,
  knockout_fixture tinyint(1) not null,
  primary key (id),
  constraint FIXTURE_COMPETITION_ID foreign key (competition_id) references competition (id),
  constraint FIXTURE_HOME_TEAM_ID foreign key (home_team_id) references team (id),
  constraint FIXTURE_AWAY_TEAM_ID foreign key (away_team_id) references team (id)
) engine=InnoDB;

create table league (
  id bigint(20) not null auto_increment,
  name varchar(30) not null,
  code varchar(10) not null,
  state varchar(10) not null,
  prize_one_code varchar(10) not null,
  prize_two_code varchar(10) null,
  prize_three_code varchar(10) null,
  competition_id bigint(20) not null,
  owner_id bigint(20) not null,
  primary key (id),
  unique key league_name (name),
  unique key league_code (code),
  constraint LEAGUE_COMP_ID foreign key (competition_id) references competition (id),
  constraint LEAGUE_OWNER_ID foreign key (owner_id) references user (id)
) engine=InnoDB;

create table user_subscription (
  id bigint(20) not null auto_increment,
  user_id bigint(20) not null,
  league_id bigint(20) not null,
  prize_one_points int(11) not null default 0,
  prize_two_points int(11) not null default 0,
  prize_three_points int(11) not null default 0,
  primary key (id),
  unique key user_league (user_id, league_id),
  key USER_SUBS_LEAGUE_ID (league_id),
  key USER_SUBS_USER_ID (user_id),
  constraint USER_SUBS_LEAGUE_ID foreign key (league_id) references league (id),
  constraint USER_SUBS_USER_ID foreign key (user_id) references user (id)
) engine=InnoDB;

create table prediction (
  id bigint(20) not null auto_increment,
  user_id bigint(20) not null,
  fixture_id bigint(20) not null,
  home_score int(11) not null,
  away_score int(11) not null,
  primary key (id),
  constraint PREDICTION_FIXTURE_ID foreign key (fixture_id) references fixture (id),
  constraint PREDICTION_USER_ID foreign key (user_id) references user (id)
) engine=InnoDB;

create table prize_points (
  id bigint(20) not null auto_increment,
  subscription_id bigint(20) not null,
  fixture_id bigint(20) not null,
  prize_code varchar(10) not null,
  points_scored int(11) not null,
  primary key (id),
  constraint PRIZE_POINTS_FIXTURE_ID foreign key (fixture_id) references fixture (id),
  constraint PRIZE_POINTS_SUBSCRIPTON_ID foreign key (subscription_id) references user_subscription (id)
) engine=InnoDB;
