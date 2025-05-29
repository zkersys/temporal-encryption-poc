Steps to set up:

To run the temporal containers, run the following commands in the project directory:
git clone https://github.com/temporalio/docker-compose
cd docker-compose
docker-compose up

To run the encryption PoC project, run the following:
./gradlew build quarkusDev

Temporal UI should be accessible via browser, localhost:8080
The PoC project runs under localhost:8081

E.g. command to start workflow:

curl --location 'localhost:8081/workflow' \
--header 'Content-Type: application/json' \
--data '{
    "uuid": "5f20cfeb-d232-4717-b4aa-b79e19c0a5bc",
    "string": "unencrypted string",
    "int": 30,
    "listOfStrings": ["abc", "def", "ghk"],
    "bigDecimal": 20.5,
    "date": "2025-03-01"
}'

