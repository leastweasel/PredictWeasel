create table blog_post (
  id bigint(20) not null auto_increment,
  league_id bigint(20) not null,
  post_time datetime not null,
  title varchar(255) not null,
  lead_text varchar(255) null,
  posted_text text not null,
  draft_text text null,
  primary key (id),
  constraint BLOG_POST_LEAGUE_ID foreign key (league_id) references league (id)
) engine=InnoDB;