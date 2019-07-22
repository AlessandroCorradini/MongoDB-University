# Lab 2.0: Create an Atlas Sandbox Cluster (Ungraded)

*Please note that, while we've labeled this as a lab, it is ungraded. This writeup is here simply to get you started on creating an Atlas cluster.*

In this lab you will have to complete two different tasks:

- Create an Atlas Account
- Create Atlas Sandbox Cluster

**Creating a new MongoDB Atlas Account:**

If you do not have an existing Atlas account, go ahead and create an Atlas Account by filling in the required fields:

![](https://s3.amazonaws.com/university-courses/m220/atlas_registration.png)

**Creating an Atlas Sandbox Cluster:**

1. After creating a new account, you will be prompted to create the first cluster in that project:

![](https://s3.amazonaws.com/university-courses/m220/cluster_create.png)

2. Choose AWS as the cloud provider, in a Region that has the label Free Tier Available:

![](https://s3.amazonaws.com/university-courses/m220/cluster_provider.png)

3. Select Cluster Tier M0:

![](https://s3.amazonaws.com/university-courses/m220/cluster_tier.png)

4. Set Cluster Name to Sandbox and click "Create Cluster":

![](https://s3.amazonaws.com/university-courses/M001/m001_cluster_name.png)

5. Once you press Create Cluster, you will be redirected to the account dashboard. In this dashboard, make sure you set your project name to M001. Go to Settings menu item and change the project name from the default Project 0 to M001:

![](https://s3.amazonaws.com/university-courses/M001/m001_project_rename.png)

6. Next, configure the security settings of this cluster, by enabling the IP Whitelist and MongoDB Users:

Update your IP Whitelist so that your app can talk to the cluster. Click the "Security" tab from the "Clusters" page. Then click "IP Whitelist" followed by "Add IP Address". Finally, click "Allow Access from Anywhere" and click "Confirm".

![](https://s3.amazonaws.com/university-courses/M001/m001_ip_whitelisting.png)

Note that we do not generally recommend opening an Atlas cluster to allow access from anywhere. We do that for this class to minimize network issues that you might run into, and to be able to provide you better support

7. Then create the application MongoDB database user required for this course:

- username: m001-student
- password: m001-mongodb-basics

You can create new users through Security -> Add New User.

Allow this user the privilege to Read and write to any database:

![](https://s3.amazonaws.com/university-courses/M001/m001_user.png)