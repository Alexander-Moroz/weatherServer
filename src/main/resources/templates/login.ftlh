<#import "/spring.ftl" as spring/>

<html>
<head>
    <meta charset="UTF-8">
    <title>Login page</title>
</head>
<body>
    <#if RequestParameters.error??>
        <div><b style="color:#FF0000">Invalid username & password.</b></div>
    </#if>
    <#if RequestParameters.logout??>
        <div><b>You have logged out.</b></div>
    </#if>
    <p>Try credentials: (user1/p), (user2/p), (user3/p), (user4/p)</p>
    <br>
    <form action="login" method="post">
        <table>
            <tr>
                <td>User name</td>
                <td><input type="text" name="username" /></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" name="password" /></td>
            </tr>
        </table>
        <div><input type="submit" value="Sign In" /></div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</body>
</html>