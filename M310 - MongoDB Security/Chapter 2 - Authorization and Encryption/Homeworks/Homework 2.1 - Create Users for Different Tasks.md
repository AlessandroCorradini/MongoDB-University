# Homework 2.1 : Create Users for Different Tasks

**Problem:**

For this homework exercise you're going to spin up a replica set and create some users with different roles based on the different tasks that each user needs to be able to perform.

Your replica set should be running on the following ports:

<table border="1" class="docutils">
<colgroup>
<col width="16%">
<col width="24%">
<col width="30%">
<col width="30%">
</colgroup>
<thead valign="bottom">
<tr><th class="head">Type</th>
<th class="head">Primary</th>
<th class="head">Secondary</th>
<th class="head">Secondary</th>
</tr>
</thead>
<tbody valign="top">
<tr><td>Port</td>
<td>31210</td>
<td>31211</td>
<td>31212</td>
</tr>
</tbody>
</table>

Your replica set should have authentication enabled using a shared keyfile.

After you have your members up and running you can connect to the mongod running on port 31210. Once connected create the following users:

<table border="1" class="docutils">
<colgroup>
<col width="12%">
<col width="13%">
<col width="40%">
<col width="36%">
</colgroup>
<thead valign="bottom">
<tr><th class="head">User</th>
<th class="head">Password</th>
<th class="head">Can</th>
<th class="head">Cannot</th>
</tr>
</thead>
<tbody valign="top">
<tr><td>userAdmin</td>
<td>badges</td>
<td>create users on any database</td>
<td>run dbhash</td>
</tr>
<tr><td>sysAdmin</td>
<td>cables</td>
<td>configure a replica set and add shards</td>
<td>run hostInfo</td>
</tr>
<tr><td>dbAdmin</td>
<td>collections</td>
<td>create a collection on any database</td>
<td>run insert</td>
</tr>
<tr><td>dataLoader</td>
<td>dumpin</td>
<td>insert data on any database</td>
<td>run validate</td>
</tr>
</tbody>
</table>

The Can column denotes tasks that the user should be able to complete. The Cannot column denotes tasks that the user shouldn't have the privileges to complete. Your goal is to figure out which built-in role best suits the needs of each user.

All of these user's roles should be scoped to the admin database.

Note: You should assume that these privileges only extend to non-system collections. If you find a role that seems to fit these requirements, but find an action in the Cannot column being applied to a system collection you should continue to use that role.

After you've created the sysAdmin user you can authenticate against that user and add the other running members to the replica set.

Once you have your replica set up and running with authentication enabled and have all 4 users created with the appropriate roles you can run the validation script and copy the output into the submission area below.

```
$ cd ~/shared
$ ./validate-hw-2.1.sh
```

**{"users": [ {"user":"dataLoader","roles":[{"role":"readWriteAnyDatabase","db":"admin"}]}, {"user":"dbAdmin","roles":[{"role":"dbAdminAnyDatabase","db":"admin"}]}, {"user":"sysAdmin","roles":[{"role":"clusterManager","db":"admin"}]}, {"user":"userAdmin","roles":[{"role":"userAdminAnyDatabase","db":"admin"}]} ], "numMembers":3 }**