<#import "/spring.ftl" as spring />

<html>
<head>
    <title>Search form</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        function myFunc() {
            var s = document.getElementById('notifyUser').innerHTML = "PLEASE WAIT";
            document.weather.submit();
        }
    </script>
</head>
<body>
<h3>Hey, ${httpServletRequest.remoteUser!anonymous}! Enter your search params:</h3>
<form name="weather" action="<@spring.url '/searchResult' />" method="post">
    <table>
        <tr>
            <td>City id (number)</td>
            <td><input name="cityId" type="text"></td>
        </tr>
        <tr>
            <td colspan="2"><b>or</b></td>
        </tr>
        <tr>
            <td>City name</td>
            <td><input name="cityName" type="text"></td>
        </tr>
        <tr>
            <td colspan="2"><b>or coordinates:</b></td>
        </tr>
        <tr>
            <td>Latitude (-90 to 90)</td>
            <td><input name="latitude" type="text"></td>
        </tr>
        <tr>
            <td>Longitude (-180 to 180)</td>
            <td><input name="longitude" type="text"></td>
        </tr>
    </table>
    <input name="userName" type="hidden" value="${httpServletRequest.remoteUser!anonymous}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button name="but" onclick="myFunc();">Search</button>
    <b id="notifyUser"></b>
</form>
<br>
<form action="<@spring.url '/logout' />" method="post">
    <input type="submit" value="Sign out" />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</body>
</html>