INSERT INTO oauth_client_details (client_id, client_secret, web_server_redirect_uri, scope, access_token_validity, refresh_token_validity, resource_ids, authorized_grant_types, additional_information) VALUES ('flymine', '{bcrypt}$2y$10$QMLfvMcGdyEyCiIyy0.HJuenJ67NopKP6CxbPJpxOdvG.a2WU1Wqi', 'http://localhost:8080/code', 'READ,WRITE', '3600', '10000', 'inventory,payment', 'authorization_code,password,refresh_token,implicit', '{}');

INSERT INTO PERMISSION (NAME) VALUES
  ('create_profile'),
  ('read_profile'),
  ('update_profile'),
  ('delete_profile');

INSERT INTO role (NAME) VALUES
  ('ROLE_admin'),('ROLE_operator');

INSERT INTO PERMISSION_ROLE (PERMISSION_ID, ROLE_ID) VALUES
  (1,1), /*create-> admin */
  (2,1), /* read admin */
  (3,1), /* update admin */
  (4,1), /* delete admin */
  (2,2),  /* read operator */
  (3,2);  /* update operator */
insert into users (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('1', 'admin','{bcrypt}$2y$10$GUz670ztcDJvUKCIL7nvD.nRkObwFnFXnywTs8pdLtdTaGSH47NCq', 'admin@intermine.com', '1', '1', '1', '1');
insert into  users (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('2', 'testUser', '{bcrypt}$2y$10$gtb89utyMaaBdZxEXXxhZ.hSg0Z/CfiI3ck4SCmt/S.vHZBeBvCSq','tuser@intermine.com', '1', '1', '1', '1');

INSERT INTO ROLE_USER (ROLE_ID, USER_ID)
VALUES
  (1, 1) /* admin */,
  (2, 2) /* testUser-operator */ ;