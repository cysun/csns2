<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="date" required="true" rtexprvalue="true" type="java.util.Date" %>
<%@ attribute name="datePattern" required="false" %>
<%@ attribute name="datePassed" required="true" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<c:if test="${empty datePattern}">
  <c:set var="datePattern" value="yyyy-MM-dd hh:mm a" />
</c:if>

<c:if test="${datePassed}">
  <fmt:formatDate value="${date}" pattern="${datePattern}" />
</c:if>

<c:if test="${not datePassed}">
  <span style="color: green;"><fmt:formatDate value="${date}" pattern="${datePattern}" /></span>
</c:if>
