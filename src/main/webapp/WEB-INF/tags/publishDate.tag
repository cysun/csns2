<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="date" required="true" rtexprvalue="true" type="java.util.Date" %>
<%@ attribute name="datePattern" required="false" %>
<%@ attribute name="datePast" required="true" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="itemId" required="true" rtexprvalue="true" type="java.lang.Long" %>
<%@ attribute name="itemType" required="false" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<c:if test="${empty datePattern}">
  <c:set var="datePattern" value="yyyy-MM-dd" />
</c:if>

<c:if test="${datePast}">
  <span id="pdate-${itemId}"><fmt:formatDate value="${date}" pattern="${datePattern}" /></span>
</c:if>

<c:set var="args" value="${itemId}" />
<c:if test="${not empty itemType}">
  <c:set var="args" value="${itemId}, '${itemType}'" />
</c:if>

<c:if test="${not datePast}">
  <span id="pdate-${itemId}"><a href="javascript:publish(${args})"><c:if
    test="${empty date}">Publish</c:if><c:if test="${not empty date}"><fmt:formatDate
    value="${date}" pattern="${datePattern}" /></c:if></a></span>
</c:if>
