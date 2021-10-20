<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
     <title>Log in to your account</title>
</head>

<body>


    <form:form method="POST" action="/performLogin">
    
        <h2>Log in</h2>

            <label for="username"><b>Username</b></label>
            <input name="username" type="text" placeholder="Username" required/>

            <br/>
            <br/>
            
            <label for="password"><b>Password</b></label>
            <input name="password" type="password" placeholder="Password" required/>

            <br/>
            <br/>
            
            <button type="submit">Log In</button>

    </form:form>

</body>
</html>

