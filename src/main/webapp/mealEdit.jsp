<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Meal edit</title>
</head>
<body>
<h4 align="center">Meal edit</h4>
<form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.id}"/><br/>
    Date\Time: <input type="datetime-local" name="date" value="${meal.dateTime}"/><br/>
    Description: <input type="text" name="description" value="${meal.description}"><br/>
    Calories: <input type="number" name="calories" value="${meal.calories}"><br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
