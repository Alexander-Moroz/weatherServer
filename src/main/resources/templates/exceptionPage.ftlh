<#import "/spring.ftl" as spring />

<html>
<head>
    <title>Exception</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<h2>Exception</h2>
<p style="color: #FF0000">${exception}</p>
<form action="<@spring.url '/searchForm' />">
    <input type="submit" value="Return">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</body>
</html>