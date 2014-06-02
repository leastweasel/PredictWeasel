insert into user(id,username,enabled,name,password,password_reminder) values
(1,'andrew@braycastle.co.uk',1,'andrew','$2a$16$gwzforw1NtmosQ2etXMDUem0hanbAbg5NbcEsjoxSjpC2g0OPMqy2','My reminder'), -- password is andrewxx
(2,'andrew@leastweasel.org',1,'leastweasel','$2a$16$wTypytdnx85ToKp8kAAh1evnGoGZPp0THnr3FwQYQg4zFDZHMRn/W','Leastweasel''s reminder'), -- password is andrewyy
(3,'mjwalsh1971@hotmail.com',1,'mick','$2a$16$VsnlSUKcE7w1bC709WDvPegod9yfqob61TIjSHsbYyCo02k2zpXpe', null); -- password is andrewzz

insert into user_role(user_id, role_id) values
(1, 2);

insert into competition(id, name, active) values
(1, 'Football Conference 2014', 1),
(2, 'World Cup 2014', 1),
(3, 'World Cup 2010', 0);

insert into team(id, name) values
(1, 'Alfreton Town'),
(2, 'Cambridge United'),
(3, 'Kidderminster Harriers'),
(4, 'Lincoln City'),
(5, 'Salisbury City'),
(6, 'Woking');

insert into fixture(id, competition_id, home_team_id, away_team_id, home_score, away_score, match_time) values
(1,1,1,2,1,1,'2013-09-17:19:30:00'),
(2,1,3,4,4,1,'2013-09-17:19:30:00'),
(3,1,5,6,2,0,'2013-09-17:19:30:00'),
(4,1,6,1,2,1,'2013-09-18:15:00:00'),
(5,1,4,2,1,0,'2013-09-18:15:00:00'),
(6,1,3,5,3,0,'2013-09-18:15:00:00'),
(7,1,1,4,1,1,'2013-09-19:19:30:00'),
(8,1,2,5,null,null,'2013-09-19:19:30:00'),
(9,1,6,3,null,null,'2013-09-19:19:30:00'),
(10,1,5,1,null,null,'2014-09-20:15:00:00'),
(11,1,2,6,null,null,'2014-09-20:15:00:00'),
(12,1,4,3,null,null,'2014-09-20:15:00:00'),
(13,1,1,3,null,null,'2014-09-21:16:00:00'),
(14,1,5,2,null,null,'2014-09-21:16:00:00'),
(15,1,6,4,null,null,'2014-09-21:16:00:00');


insert into league(id, name, code, state, competition_id, owner_id) values
(1, 'Andrew''s Conference League', 'AN7h2KhA', 'OPEN', 1, 1),
(2, 'Mick''s Conference League', 'G7AB3jSg', 'ACTIVE', 1, 3);

insert into user_subscription(id, user_id, league_id) values
(1, 2, 1),
(2, 2, 2),
(3, 3, 1);

insert into prediction(id, user_id, fixture_id, home_score, away_score) values
(1, 3, 11, 7, 2),
(2, 3, 13, 1, 3),
(3, 3, 5, 0, 1);