<#import "/spring.ftl" as spring/>

<html>
<head>
    <title>Search result</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<table border="1" width="300px">
    <caption><h3>SEARCH PARAMS:</h3></caption>
    <#if searchRequest.cityId??>
        <tr>
            <td>cityId</td>
            <td><p>${searchRequest.cityId}</p></td>
        </tr>
    </#if>
    <#if searchRequest.cityName??>
        <tr>
            <td>cityName</td>
            <td><p>${searchRequest.cityName}</p></td>
        </tr>
    </#if>
    <#if searchRequest.latitude??>
        <tr>
            <td>latitude</td>
            <td><p>${searchRequest.latitude}</p></td>
        </tr>
    </#if>
    <#if searchRequest.longitude??>
        <tr>
            <td>longitude</td>
            <td><p>${searchRequest.longitude}</p></td>
        </tr>
    </#if>
</table>
<b>
    <p>Weather for city ${weather.cityName} is ${weather.weatherList[0].mainInfo!unknown}.</p>
    <p>Temp ${weather.mainData.temp}C </p>
</b>
</body>
<form action="<@spring.url '/searchForm'/>">
    <input type="submit" value="Go back">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</html>