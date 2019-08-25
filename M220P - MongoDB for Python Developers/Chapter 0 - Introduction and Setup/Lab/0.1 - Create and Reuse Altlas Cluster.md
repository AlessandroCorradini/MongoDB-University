# Lab (Ungraded): Create or Reuse Atlas Cluster

**Reusing an Atlas Account**

If you previously created an Atlas account, you can reuse it for this course.

Go ahead and create a new Project and call it M220. We will be creating an Atlas free tier cluster (M0) in that project.

**Creating an Atlas Account**

If you did not previously create an Atlas account, we have provided instructions for you to create one, and use it to create a cluster for this course.

To sign up for Atlas, visit the Atlas Registration page and enter your information. Please note that Atlas has a hierarchical structure:

- Organizations: The name of the organization will be set using your account company name. Feel free to use whatever name you'd like.
- Projects: Under Organizations you can have several Projects. A project is logical group shared by different Atlas users. You can invite friends and colleagues to access a given project. For this course our project will be named M220.
- Clusters: A cluster is a MongoDB deployment. You can define several different types of clusters (Replica Sets, Shard Clusters) deployed in different Cloud Providers (AWS, Azure, GCP) and with a given Cluster Tier. For this course we will be using an M0 Free Tier cluster named mflix.

**Create Atlas Cluster**

1. Creating a New Cluster

Follow the Atlas free tier setup instructions, and make sure of the following:

- When selecting a new region, make sure to choose a region with the "Free Tier Available" flag.
- When selecting a Cluster Tier, choose M0 (free tier) so you don't get charged!
- Please choose the name "mflix" for this cluster.

After these selections have been made, click "Create Cluster" so we can get started!

2. Allowing Access to Your Cluster

In order to use Atlas with the application, you need to update your IP Whitelist so that your app can talk to the cluster:

- Navigate to the "Security" tab from the Clusters page.
- From the "IP Whitelist" tab, there should be an option to "Add IP Address" in the top right corner.
- Choose "Allow Access from Anywhere" and then click "Confirm".

Note that we do not generally recommend allowing access to your Atlas cluster from anywhere. However, in this class it minimizes network issues and allows us to provide better support.

3. Load Sample Dataset (optional)

Once you've created a cluster, Atlas will notice that there's no data there. It will give you the option to Load Sample Dataset. This will load the Atlas sample dataset, containing the MFlix database, into your cluster:

![](https://s3.amazonaws.com/university-courses/m220/load_sample_dataset.png)

Note: The MFlix database in the Sample Dataset is called "sample_mflix".

If you decide not to use the Load Sample Dataset, you will need to use mongorestore in the next step.
