# Homework 2.2 : Create application specific users

**Problem:**

Now that we have seen how to create users with different roles we will expand our understanding and see how to create application users whose privileges are for designated resources.

For this homework you are going to create users whose roles are scoped to a specific database.

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
<td>31220</td>
<td>31221</td>
<td>31222</td>
</tr>
</tbody>
</table>

Your replica set should have authentication enabled using a shared keyfile.

After you have your members up and running you can connect to the mongod running on port 31220. Once connected create the following users:

<table border="1" class="docutils">
<colgroup>
<col width="20%">
<col width="20%">
<col width="18%">
<col width="11%">
<col width="31%">
</colgroup>
<thead valign="bottom">
<tr><th class="head">User</th>
<th class="head">Password</th>
<th class="head">Role</th>
<th class="head">Db</th>
<th class="head">Authentication db</th>
</tr>
</thead>
<tbody valign="top">
<tr><td>admin</td>
<td>webscale</td>
<td>root</td>
<td>admin</td>
<td>admin</td>
</tr>
<tr><td>reader</td>
<td>books</td>
<td>read</td>
<td>acme</td>
<td>admin</td>
</tr>
<tr><td>writer</td>
<td>typewriter</td>
<td>readWrite</td>
<td>acme</td>
<td>admin</td>
</tr>
</tbody>
</table>

Make sure that at some point you initiate the replica set and add all the members.

After you've created the users and set up the replica set you can run the validation script and copy the output into the submission area below.

```
$ cd ~/shared
$ ./validate-hw-2.2.sh
```

**{ "users": [ {"user":"admin","roles":[{"role":"root","db":"admin"}]}, {"user":"reader","roles":[{"role":"read","db":"acme"}]}, {"user":"writer","roles":[{"role":"readWrite","db":"acme"}]} ], "numMembers":3 }**