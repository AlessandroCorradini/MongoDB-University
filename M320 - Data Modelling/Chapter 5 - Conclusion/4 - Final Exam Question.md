# Final Exam: Question 4

We are building a social media site where users can share a lot of details about their whereabouts in their lives.

The site will likely have many European customers. For all these users, the GDPR law in Europe cites that a person has the right to be forgotten, meaning all data related to that person should be erased per request of the user.

Which one of the following implementations would be the preferred way to represent the One-to-Many relationship between restaurants and each user's last-visited restaurant to enable easy removal of a user's activity in accordance with GDPR?



- A restaurants collection with references to users documents.

![](https://university-courses.s3.amazonaws.com/M320/m320-final-one-to-many-3.png)

- **A users collection with an extended reference to a restaurants document.**

![](https://university-courses.s3.amazonaws.com/M320/m320-final-one-to-many-1.png)

- Only a restaurants collection in which all user information is embedded.

![](https://university-courses.s3.amazonaws.com/M320/m320-final-one-to-many-2.png)

- Only a users collection in which all restaurant information is embedded.

![](https://university-courses.s3.amazonaws.com/M320/m320-final-one-to-many-4.png)
