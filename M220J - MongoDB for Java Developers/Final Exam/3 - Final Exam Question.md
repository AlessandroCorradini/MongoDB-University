# Final: Question 3

Let's take the following code, where URI is a connection to your Atlas cluster using an SRV record.

```
MongoClientSettings settings = MongoClientSettings.builder() \
    .applyConnectionString(new ConnectionString(URI)).build();
MongoClient mongoClient = MongoClients.create(settings);

// TODO do a read on the cluster to ensure you are connected

SslSettings sslSettings = settings.getSslSettings();
ReadPreference readPreference = settings.getReadPreference();
ReadConcern readConcern = settings.getReadConcern();
WriteConcern writeConcern = settings.getWriteConcern();
```

Which of the following variables contained the associated value?



- readPreference.toString() = "primary"
- **writeConcern.asDocument().toString() = "{ w : 1 }"**
- readConcern.asDocument().toString() = "{ }"
- **sslSettings.isInvalidHostNameAllowed() = true**
- sslSettings.isEnabled() = false
