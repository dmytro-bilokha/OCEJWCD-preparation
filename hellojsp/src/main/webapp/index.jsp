<html>
    <body>
        <h2>Hello JSP!</h2>
        <p>Your IP address is: <% out.print(request.getRemoteAddr()); %> </p>
        <p>Your referrer header is: <%= request.getHeader("referer") %> </p>
    </body>
</html>
