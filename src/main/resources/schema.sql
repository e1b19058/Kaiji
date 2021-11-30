CREATE TABLE users(
  id IDENTITY,
  name CHAR NOT NULL,
  gu int not null,
  cho int not null,
  pa int not null,
  star int not null
);

CREATE TABLE match(
  matchid IDENTITY,
  user1id int not null,
  user2id int not null,
  user1hand char not null,
  user2hand char
);
