# Homework 3.3 : Enable DML audit

**Problem:**

Like the two last exercises, in this homework you're going to spin up a replica set with auditing enabled on each of the members. However, this time you're going to enable the auditing of CRUD operations.

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
<td>31330</td>
<td>31331</td>
<td>31332</td>
</tr>
<tr><td>Audit Log</td>
<td>~/M310-HW-3.3/r0/auditLog.json</td>
<td>~/M310-HW-3.3/r1/auditLog.json</td>
<td>~/M310-HW-3.3/r2/auditLog.json</td>
</tr>
</tbody>
</table>

You need to figure out how to audit CRUD operations, as well as all of the default operations. You shouldn't need to make an auditFilter to accomplish this task.

Once you have your replica set up and running with CRUD auditing enabled, you can run the validation script and copy the output into the submission area below.

```
$ cd ~/shared
$ ./validate-hw-3.3.sh
```

**{ numMembers: 3, auditLog: 1 }**