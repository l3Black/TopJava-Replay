curl http://localhost:8080/topjava/rest/meals
curl http://localhost:8080/topjava/rest/meals/100004
curl -X DELETE http://localhost:8080/topjava/rest/meals/100004
curl -d '{"id":100011,"dateTime":"2020-06-02T19:29:00","description":"lunchUpd","calories":350,"user":null}' -H 'Content-Type: application/json;charset=UTF-8' -X PUT http://localhost:8080/topjava/rest/meals/100011
curl http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=00:15:30&endDate=2020-01-30&endTime=18:15:30