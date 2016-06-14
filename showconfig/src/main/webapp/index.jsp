<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Hello World from JSP!</h2>
<p>Use  this <a href="<c:url value="/ShowConfigServlet" />">link</a> to see servlet config</p>
<c:forEach var="iparam" items="${pageContext.servletContext.initParameterNames}">
<p><c:out value="${iparam}"/> <c:out value="${pageContext.servletContext.getInitParameter(iparam)}"/></p>
</c:forEach>
</body>
</html>
