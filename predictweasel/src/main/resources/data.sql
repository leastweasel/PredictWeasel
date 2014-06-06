insert into user(id,username,enabled,name,password,password_reminder) values
(1,'andrew@braycastle.co.uk',1,'Andrew','1b07c720496b2c5eb2ca6078b621fce7d4d71a91091ce9c8d24b99ed91b7eb48a6d914a588067477','My reminder'), -- password is andrewxx
(2,'andrew@leastweasel.org',1,'leastweasel','79ffb6131bb61b55184986d157b2f4704e414832761ef2c341377f9261d03e6a3771c87b7a76a5df','Leastweasel''s reminder'), -- password is andrewyy
(3,'mjwalsh1971@hotmail.com',1,'Mick','a744fcb3307bd4c607afc741688d9515ad491a17761f7947038038eb92ec7514ff0caab833098c0f', null), -- password is andrewzz
(4,'harry@notmail.com',1,'Harry','a744fcb3307bd4c607afc741688d9515ad491a17761f7947038038eb92ec7514ff0caab833098c0f', null), -- password is andrewzz
(5,'jimmy@notmail.com',1,'Jimmy','a744fcb3307bd4c607afc741688d9515ad491a17761f7947038038eb92ec7514ff0caab833098c0f', null), -- password is andrewzz
(6,'willy@notmail.com',1,'Willy','a744fcb3307bd4c607afc741688d9515ad491a17761f7947038038eb92ec7514ff0caab833098c0f', null), -- password is andrewzz
(7,'freddy@notmail.com',1,'Freddy','a744fcb3307bd4c607afc741688d9515ad491a17761f7947038038eb92ec7514ff0caab833098c0f', null), -- password is andrewzz
(8,'perry@notmail.com',1,'Perry','a744fcb3307bd4c607afc741688d9515ad491a17761f7947038038eb92ec7514ff0caab833098c0f', null), -- password is andrewzz
(9,'barry@notmail.com',1,'Barry','a744fcb3307bd4c607afc741688d9515ad491a17761f7947038038eb92ec7514ff0caab833098c0f', null), -- password is andrewzz
(10,'kelly@notmail.com',1,'Kelly','a744fcb3307bd4c607afc741688d9515ad491a17761f7947038038eb92ec7514ff0caab833098c0f', null); -- password is andrewzz

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
(10,1,5,1,null,null,'2014-06-02:15:00:00'),
(11,1,2,6,null,null,'2014-06-02:15:05:00'),
(12,1,4,3,null,null,'2014-06-02:15:10:00'),
(13,1,1,3,null,null,'2014-06-03:16:00:00'),
(14,1,5,2,null,null,'2014-06-03:17:00:00'),
(15,1,6,4,null,null,'2014-06-03:17:30:00');

insert into league(id, name, code, state, competition_id, owner_id, prize_one_code, prize_two_code, prize_three_code) values
(1, 'Andrew''s Conference League', 'AN7h2KhA', 'OPEN', 1, 1, "O", null, null),
(2, 'Mick''s Conference League', 'G7AB3jSg', 'ACTIVE', 1, 3, "O", "KO", "SO");

insert into user_subscription(id, user_id, league_id) values
(1, 2, 1),
(2, 2, 2),
(3, 3, 1),
(4, 4, 2),
(5, 5, 2),
(6, 6, 2),
(7, 7, 2),
(8, 8, 2),
(9, 9, 2),
(10, 10, 2);

insert into prediction(id, user_id, fixture_id, home_score, away_score) values
(1, 3, 11, 7, 2),
(2, 3, 13, 1, 3),
(3, 3, 5, 0, 1),
(4, 4, 8, 5, 0),
(5, 5, 8, 1, 2),
(6, 6, 8, 3, 5),
(7, 7, 8, 2, 2),
(8, 8, 8, 4, 1),
(9, 9, 8, 0, 3),
(10, 10, 8, 1, 2),
(11, 4, 9, 2, 4),
(12, 5, 9, 3, 1),
(13, 6, 9, 3, 3),
(14, 7, 9, 1, 0),
(15, 8, 9, 3, 2),
(16, 9, 9, 1, 0),
(17, 10, 9, 0, 2);