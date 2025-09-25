<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .container { max-width: 800px; margin: 0 auto; }
    </style>
</head>

<body>
        <h1>${message}</h1>
        <p>Server time: ${serverTime}</p>


        <!-- Forms example -->
        <form action="home" method="post">
            <input type="text" name="name" placeholder="Input name">
            <button type="submit">Send</button>
        </form>
    </div>
</body>
</html>