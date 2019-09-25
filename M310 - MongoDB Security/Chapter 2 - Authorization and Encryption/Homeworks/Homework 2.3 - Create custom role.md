# Homework 2.3 : Create custom role

**Problem:**

MongoDB comes with a set of built-in roles that cover the vast majority of user needs. MongoDB also allows us to define our own roles. This can come in handy when the built-in roles don't meet a specific requirement of our users or applications.

In this homework you're going to define a series of custom roles. For the sake of simplicity in this homework there's no need to create a replica set. Just start a standalone mongod on port 31230. Once connected create the following custom roles:

<table border="1" class="docutils">
<colgroup>
<col width="21%">
<col width="79%">
</colgroup>
<thead valign="bottom">
<tr><th class="head">Role name</th>
<th class="head">Privileges</th>
</tr>
</thead>
<tbody valign="top">
<tr><td>HRDEPARTMENT</td>
<td><ul class="first last simple">
<li>Can find documents on any collection on the HR database</li>
<li>Can insert documents only on HR.employees collection</li>
<li>Can remove users from the HR database</li>
</ul>
</td>
</tr>
<tr><td>MANAGEMENT</td>
<td><ul class="first last simple">
<li>Inherits the dbOwner role of the HR database</li>
</ul>
</td>
</tr>
<tr><td>EMPLOYEEPORTAL</td>
<td><ul class="first last simple">
<li>Can find documents on HR.employees collection</li>
<li>Can update HR.employees documents</li>
</ul>
</td>
</tr>
</tbody>
</table>

When building these custom roles you should create privilege documents for each of these requirements (instead of inheriting a role) unless specified. You should also "merge" as many privileges together when defining your privilege documents. In other words, if the same resource document can have multiple actions you should group them into one privilege document instead of multiple.

After you've created your user-defined roles you can run the validation script and copy the output into the submission area below.

```
$ cd ~/shared
$ ./validate-hw-2.3.sh
```

**[{"role":"EMPLOYEEPORTAL","inheritedRoles":[],"privileges":[{"resource":{"db":"HR","collection":"employees"},"actions":["find","update"]}]},{"role":"HRDEPARTMENT","inheritedRoles":[],"privileges":[{"resource":{"db":"HR","collection":""},"actions":["dropUser","find"]},{"resource":{"db":"HR","collection":"employees"},"actions":["insert"]}]},{"role":"MANAGEMENT","inheritedRoles":[{"role":"dbOwner","db":"HR"}],"privileges":[]}]**