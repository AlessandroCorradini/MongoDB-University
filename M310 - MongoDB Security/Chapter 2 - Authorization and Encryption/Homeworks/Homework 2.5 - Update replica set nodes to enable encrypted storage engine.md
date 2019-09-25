# Homework 2.5 : Update replica set nodes to enable encrypted storage engine

**Problem:**

For this homework exercise you are going to take a running replica set (that doesn't have its storage encrypted), and are to enable the encrypted storage engine via a keyfile in a rolling fashion with no downtime.

One of the provided scripts creates a basic replica set.

After you've copied the handout scripts into the shared folder, run the following commands inside the VM in order to start the replica set.

```
$ cd ~/shared
$ ./setup-hw-2.5.sh
```

You can inspect setup-hw-2.5.sh to see the parameters used to set up the replica set, but for convenience here's that information.

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
<td>31250</td>
<td>31251</td>
<td>31252</td>
</tr>
<tr><td>DBPath</td>
<td>~/M310-HW-2.5/r0</td>
<td>~/M310-HW-2.5/r1</td>
<td>~/M310-HW-2.5/r2</td>
</tr>
<tr><td>LogPath</td>
<td>~/M310-HW-2.5/r0/mongodb.log</td>
<td>~/M310-HW-2.5/r1/mongodb.log</td>
<td>~/M310-HW-2.5/r2/mongodb.log</td>
</tr>
</tbody>
</table>

The name of the replica set is UNENCRYPTED.

In order to perform this rolling upgrade to enable the encrypted storage engine you're going to need to figure out how to perform the following tasks.

1. Create a keyfile to use as the external master key.
2. Safely shutdown a secondary of the replica set and delete the old database files.
3. Restart the server with storage encryption enabled.
4. Repeat steps 2 and 3 for the other secondary.
5. Step down the primary and repeat steps 2 and 3 on the former primary.

Note: for more information on performing a rolling upgrade take a look at this blog post.

After you've successfully enabled storage engine encryption on the replica set you can run the following script and copy the output into the submission area below.

```
$ cd ~/shared
$ ./validate-hw-2.5.sh
```

**{"doc":{"str":"The quick brown fox jumps over the lazy dog"},"isEnabled":true}**