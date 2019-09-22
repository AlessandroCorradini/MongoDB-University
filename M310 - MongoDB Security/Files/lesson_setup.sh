#!/bin/sh

# review mongodb configuration file
cat /shared/mongod.cnf

# review users ldif file
cat /vagrant/Users.ldif


# validate ldap options in the configuration file
mongoldap --user alice --password secret -f /shared/mongod.cnf

# boot up mongod
mongod -f /shared/mongod.cnf

# connect to mongod
mongo --port 30000

# authenticate user alice
mongo --port 30000 --eval "db.getSiblingDB('\$external').auth(
  {user:'alice', pwd:'secret', mechanism: 'PLAIN'})"

# create the LDAP group role

mongo --port 30000 --eval "db.getSiblingDB('\$external').auth(
  {user:'alice', pwd:'secret', mechanism: 'PLAIN'});
  db=db.getSiblingDB('admin');
  db.createRole(
    { role: 'cn=admins,ou=Users,dc=mongodb,dc=com',
      privileges: [],
      roles:['userAdminAnyDatabase']
    });"

# extending the LDAP group role permissions
mongo --port 30000 --eval "db.getSiblingDB('\$external').auth(
  {user:'alice', pwd:'secret', mechanism: 'PLAIN'});
  db=db.getSiblingDB('admin');
  db.runCommand({
    grantRolesToRole: 'cn=admins,ou=Users,dc=mongodb,dc=com',
    roles: [{role: 'readWrite', db: 'test'}]
  });"

# insert document on test database
mongo --port 30000 --eval "db.getSiblingDB('\$external').auth(
  {user:'alice', pwd:'secret', mechanism: 'PLAIN'});
  db=db.getSiblingDB('test');
  db.notes.insert({'message': 'ldap authz is great'});"
