<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="img" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ tag body-content="scriptless"%>
<a class="help" href="javascript:help('${name}')"><jsp:doBody /><c:if test="${empty img or img}">
<img title="Help" alt="[Help]" src="<c:url value='/img/icons/question.png' />" /></c:if></a>
