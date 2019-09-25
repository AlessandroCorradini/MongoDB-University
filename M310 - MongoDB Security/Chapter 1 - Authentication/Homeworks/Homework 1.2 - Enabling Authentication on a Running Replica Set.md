# Homework 1.2 : Enabling Authentication on a Running Replica Set

**Problem:**

For this lab you are going to take a running replica set (that doesn't have authentication enabled) and are going to enable authentication on it.

One of the provided scripts creates a replica set with authentication disabled.

After you've copied the handout scripts into the shared folder, run the following commands inside the VM in order to start the replica set.

```
$ cd ~/shared
$ ./setup-hw-1.2.sh
```

You can inspect setup-hw-1.2.sh to see the parameters used to set up the replica set, but for convenience here's that information.

<table border="1" class="docutils">
<colgroup>
<col width="12%">
<col width="29%">
<col width="29%">
<col width="29%">
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
<td>31120</td>
<td>31121</td>
<td>31122</td>
</tr>
<tr><td>DBPath</td>
<td>~/M310-HW-1.2/r0</td>
<td>~/M310-HW-1.2/r1</td>
<td>~/M310-HW-1.2/r2</td>
</tr>
<tr><td>LogPath</td>
<td>~/M310-HW-1.2/r0/mongodb.log</td>
<td>~/M310-HW-1.2/r1/mongodb.log</td>
<td>~/M310-HW-1.2/r2/mongodb.log</td>
</tr>
</tbody>
</table>

In order to enable authentication on this running replica set you're going to need to figure out how to perform the following tasks.

- Create a keyfile to use for internal authentication between the members of the replica set.
- Safely shutdown each member of the replica set, starting with the secondaries to prevent any rollbacks.
- Starting with the primary, restart each member using the shared keyfile you generated.
- Finally, create a user with the root role with the username admin and the password webscale on the admin database.

Note: If you don't create a user with these credentials, you will not get the correct output for submitting your answer.

After you've enabled authentication on the replica set you can run the following script and copy the output into the submission area below.

```
$ cd ~/shared
$ ./validate-hw-1.2.sh
```

Note: If you've successfully enabled authentication on the replica set, then the validation script should output a JSON object with two keys: unauthorizedStatus and memberStatuses. The first key confirms that authentication is enabled, and the second verifies that all members are still up and running.

**{ unauthorizedStatus: { "ok" : 0, "errmsg" : "not authorized on admin to execute command { replSetGetStatus: 1.0 }", "code" : 13 }, memberStatuses: ["PRIMARY","SECONDARY","SECONDARY"] }**