<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="assignment" required="false" rtexprvalue="true" type="csns.model.academics.Assignment" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<c:if test="${not empty assignment}">
  <c:set var="publishDate" value="${assignment.publishDate}" />
  <c:set var="published" value="${assignment.published}" />
</c:if>

<c:if test="${published}">
  <fmt:formatDate value="${publishDate.time}" pattern="yyyy-MM-dd hh:mm a" />
</c:if>

<c:if test="${not published}">
  <a href="javascript:publish(${assignment.id})">Publish</a>
</c:if>
