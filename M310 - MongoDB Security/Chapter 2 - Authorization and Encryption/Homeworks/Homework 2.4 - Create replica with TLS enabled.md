# Homework 2.4 : Create replica with TLS enabled

**Problem:**

By now you should be very comfortable spinning up replica sets in various configurations. For this homework you're going to spin up a replica set that requires all connections to use TLS.

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
<td>31240</td>
<td>31241</td>
<td>31242</td>
</tr>
</tbody>
</table>

Make sure to copy the certs folder directly to the m310-vagrant-env/shared folder. If the certificates are not in this location then your results will be incorrect. When SSHed into the VM your home file structure should look similar to this:

```
~
`-- shared
    |-- certs
    |   |-- ca.pem
    |   |-- client.pem
    |   `-- server.pem
    `-- validate-hw-2.4.sh
```

Once you have your servers up and running go ahead connect to the member running on port 31240. From there you should be able to initiate your replica set and add the remaining members.

You can then run the validation script and copy the output into the submission area below.

```
$ cd ~/shared
$ ./setup-hw-2.4.sh
```

**{"numMembers":3}**
