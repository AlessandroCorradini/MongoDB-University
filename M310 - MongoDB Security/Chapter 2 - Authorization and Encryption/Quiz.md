# Quiz

## Authorization Model

**Problem:**

Which of the following defines MongoDB's authorization model?

- **Role-Based Access Control**
- Rule-Based Authorization Control
- Role-Based Authorization Control
- Rule-Based Access Control

## Role based Access Control

**Problem:**

Why does MongoDB use role-based access control for its authorization model?

- **Because it allows users to grant specific actions over specific resources**
- **So applications can act within tightly defined, tailored roles in MongoDB that match the needs of their end-users**
- **Because it is a widely used authorization model**
- **To provide administrators a high level of responsibility isolation for users' operational tasks**

## Built in roles

**Problem:**

Which of the following are built-in roles in MongoDB?

- userOwner
- **userAdminAnyDatabase**
- **clusterMonitor**
- **dbAdmin**

## User defined roles

**Problem:**

Which of the following is/are properties of MongoDB's authorization model?

- **Roles are granted to users with a per-database granularity**
- **Actions on resources define privileges**
- **Role Inheritance**
- Roles are groups of privileged individuals

## Actions

**Problem:**

Which of the following is/are valid privilege actions?

- recover
- **unlock**
- **find**
- killProc
- **viewUser**

## Resources

**Problem:**

Which of the following is/are valid resource documents?

- { collection: 'orders' }
- { db: 'twitter' }
- **{ cluster: true }**
- **{ db: 'amazon', collection: 'users' }**

## Privileges

**Problem:**

The read role gives a user access to which of the following actions?

- **killCursors**
- **collStats**
- **find**
- showCursors

## Create user with built in role

**Problem:**

Which of the following is/are valid built-in roles?

- adminUserAnyDatabase
- **restore**
- **root**
- readWriteAllDatabases

## List user roles and privilege

**Problem:**

Which of the following is not a collection that the userAdminAnyDatabase role has privileges on?

- system.version
- **system.namespaces**
- system.backup_users
- system.users

## Create user defined role

**Problem:**

Which of the following is/are configuration options for user-defined roles?

- Replica set name
- Username
- **Privileges**
- **Roles**
- **Role name**

## Grant new privileges to role

**Problem:**

Which of the following methods can update the privileges of a role?

- db.createRole()
- **db.grantPrivilegesToRole()**
- **db.grantRolesToRole()**
- **db.updateRole()**

## Revoke privilege from role

**Problem:**

Which of the following is the correct function to revoke a privilege from a role?

- db.removePrivilegeFromRole
- db.removePrivilegesFromRole
- db.revokePrivilegeFromRole
- **db.revokePrivilegesFromRole**

## Encryption Intro

**Problem:**

Which of the following is/are supported encryption methods for MongoDB?

- **TLS connection encryption**
- **Encryption at rest**

## Transport encryption (TLS)

**Problem:**

TLS encryption works through the use of...

- **Public/Private key encryption**
- **SSL certificates**
- GPG encryption
- PGP encryption

## TLS connection modes

**Problem:**

Which of the following are valid TLS connection modes?

- **disabled**
- **preferSSL**
- **allowSSL**
- **requireSSL**

## Enable TLS between client and mongod

**Problem:**

What is the purpose of the --sslCAFile option when passed to mongod?

- To provide the certificate authority's private key to the client
- To provide the certificate authority's SSL certificate to the client
- **To verify the identity of the client**
- To provide the certificate authority's public key to the client

## Enable mixed TLS with encrypted nodes in replica set

**Problem:**

Which of the following arguments can be passed to the --sslMode option to require TLS connections between the members of a replica set, but not require them for connections via clients?

- **preferSSL**
- allowSSL
- disabled
- requireSSL

## Encrypted Storage Engine

**Problem:**

Which of the following facts about MongoDB's encrypted storage engine are true?

- The master key is stored in MongoDB
- It's supported by both MMAPv1 and WiredTiger
- **An encryption key is generated for each database**
- It's supported by the Community Edition of MongoDB

## KMIP Integration

**Problem:**

What does KMIP stand for?

- Key Management Interchange Procedure
- **Key Management Interoperability Protocol**
- Key Management Interoperability Procedure
- Key Management Interchange Protocol
