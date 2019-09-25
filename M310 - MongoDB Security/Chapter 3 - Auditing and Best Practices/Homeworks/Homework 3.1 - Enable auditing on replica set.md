# Homework 3.1 : Enable auditing on replica set

**Problem:**

For this homework exercise, you're going to spin up a replica set with auditing enabled on each of the members.

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
<td>31310</td>
<td>31311</td>
<td>31312</td>
</tr>
<tr><td>Audit Log</td>
<td>~/M310-HW-3.1/r0/auditLog.json</td>
<td>~/M310-HW-3.1/r1/auditLog.json</td>
<td>~/M310-HW-3.1/r2/auditLog.json</td>
</tr>
</tbody>
</table>

Once you have your replica set up and running with auditing enabled you can run the validation script and copy the output into the submission area below.

```
$ cd ~/shared
$ ./validate-hw-3.1.sh
```

**{ numMembers: 3, auditLog: 1 }**
