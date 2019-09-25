# Homework 1.3: Enabling Internal Authentication using X.509

**Problem:**

In this lab you are going to start your own replica set with internal authentication enabled using X.509 certificates.

We have provided you with the necessary certificates via the handout.

Your task is to create a three member replica set that uses X.509 certificates for internal authentication. Below are the settings you should use for your replica set. Failure to comply with these parameters will result in incorrect results.

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
<td>31130</td>
<td>31131</td>
<td>31132</td>
</tr>
<tr><td>DBPath</td>
<td>~/M310-HW-1.3/r0</td>
<td>~/M310-HW-1.3/r1</td>
<td>~/M310-HW-1.3/r2</td>
</tr>
<tr><td>LogPath</td>
<td>~/M310-HW-1.3/r0/mongodb.log</td>
<td>~/M310-HW-1.3/r1/mongodb.log</td>
<td>~/M310-HW-1.3/r2/mongodb.log</td>
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
    `-- validate-hw-1.3.sh
```

After you have setup your replica set with internal authentication using X.509 certificates, create a user on the admin database with the root role for the client.pem certificate.

After creating your user run the following script and copy the output into the submission area below.

When you set up your replica set make sure that you use the fully qualified domain name (FQDN) when initiating the replica set. Failing to do so will prevent the validation script from working properly. All of the homework should be completed with Vagrant, so the FQDN will be database.m310.mongodb.university.

```
$ cd ~/shared
$ ./validate-hw-1.3.sh
```

Note: If you've successfully enabled authentication on the replica set, then the validation script should output a JSON object with two keys: unauthorizedStatus and memberStatuses. The first key confirms that authentication is enabled, and the second verifies that all members are still up and running.

**{ unauthorizedStatus: { "ok" : 0, "errmsg" : "not authorized on admin to execute command { replSetGetStatus: 1.0 }", "code" : 13 }, memberStatuses: ["PRIMARY","SECONDARY","SECONDARY"] }**