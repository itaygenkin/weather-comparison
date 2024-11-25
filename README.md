# Weather Comparison

This repository's main goal is to allow us to compare the differences in weather measurements between various providers and evaluate their accuracy.


## Features
- Spring Boot Server: Utilizes Spring Boot to handle the communication between services.
- API requests: Data is fetched from 3 different 3rd parties (currently only from Tomorrow Weather).
- Data Streaming: The fetched data is streamed to the database using Apache Kafka.
- Database: The data is stored in MySQL database.



## Getting Started
- clone the repository to your end (frontend should be cloned from different branch - see frontend README).
- run 'docker-compose up --build'.
- run all the services.