# Homework 3.2 : Change audit filters to audit specific user

**Problem:**

Like the last exercise, for this homework you're going to spin up a replica set with auditing enabled on each of the members. However, this time you're also going to define an audit filter.

Your replica set should be running on the following ports and should should output their logs in JSON to the following locations:

<table border="1" class="docutils">
<colgroup>
<col width="10%">
<col width="30%">
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
<td>31320</td>
<td>31321</td>
<td>31322</td>
</tr>
<tr><td>Audit Log</td>
<td>~/M310-HW-3.2/r0/auditLog.json</td>
<td>~/M310-HW-3.2/r1/auditLog.json</td>
<td>~/M310-HW-3.2/r2/auditLog.json</td>
</tr>
</tbody>
</table>

This time around, instead of auditing, all of the default events you're going to define an audit filter that will only audit events initiated by a user.

You will need to create an account for steve on the admin database with a password of secret and a role of root.

Your audit filter should only audit operations that are performed by steve.

Once you have your replica set up and running, with auditing enabled and your audit filter correctly specified, you can run the validation script and copy the output into the submission area below. The output should be a JSON document with three keys.

```
~
`-- shared
    |-- certs
    |   |-- ca.pem
    |   |-- client.pem
    |   `-- server.pem
    `-- validate-hw-1.3.sh
```

$ cd ~/shared
$ ./validate-hw-3.2.sh

**{ numMembers: 3, auditLog1: 1, auditLog2: 0 }**