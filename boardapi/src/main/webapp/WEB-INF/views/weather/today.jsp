<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>weather</title>
</head>
<body>
<div>
    <h2>${city}</h2>
    오늘의 날씨: ${weahter.weater[0].description} <img src="${iconUrl}"/>
</div>
<div>
    온도 : ${weather.main.temp}도 / 습도 : ${weather.main.humidity}%
</div>
</body>
</html>