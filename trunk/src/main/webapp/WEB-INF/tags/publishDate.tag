<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="item" required="true" rtexprvalue="true" type="csns.model.core.Publishable" %>
<%@ attribute name="datePattern" required="false" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<c:if test="${empty pageScope.datePattern}">
  <c:set var="datePattern" value="yyyy-MM-dd" />
</c:if>

<c:if test="${item.published}">
  <span id="pdate-${item.id}"><fmt:formatDate value="${item.publishDate.time}" pattern="${datePattern}" /></span>
</c:if>

<c:if test="${not item.published}">
  <span id="pdate-${item.id}"><a href="javascript:publish(${item.id})"><c:if
    test="${empty item.publishDate}">Publish</c:if><c:if test="${not empty item.publishDate}"><fmt:formatDate
    value="${item.publishDate.time}" pattern="${datePattern}" /></c:if></a></span>
</c:if>
