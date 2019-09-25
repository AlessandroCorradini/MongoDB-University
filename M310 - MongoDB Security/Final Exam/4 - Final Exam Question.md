# Final: Question 4

Which of the following createRole parameters are valid when executed on the production database?

```
{
  role: "intern",
  privileges: [{
    resource: { db: "staging", collection: "products" },
    actions: [ "insert" ]
  }],
  roles:[]
}
```
```
{
  role: "junior-engineer",
  privileges: [{
    resource: { db: "production" },
    actions: [ "insert" ]
  }],
  roles:[]
}
```
[X]
{
  role: "senior-engineer",
  privileges: [],
  roles: [ "dbAdmin" ]
}
```
```
{
  role: "team-lead",
  privileges: [{
    resource: { replicaSet: true },
    actions: [ "createUser" ]
  }],
  roles:[{
    role: "root", db: "production"
  }]
}
```