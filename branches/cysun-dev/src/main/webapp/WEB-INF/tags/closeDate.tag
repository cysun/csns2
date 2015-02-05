<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="survey" required="false" rtexprvalue="true" type="csns.model.survey.Survey" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<c:if test="${not empty pageScope.survey}">
  <c:set var="closeDate" value="${survey.closeDate}" />
  <c:set var="closed" value="${survey.closed}" />
  <c:set var="id" value="${survey.id}" />
  <c:set var="pattern" value="yyyy-MM-dd" />
</c:if>

<c:if test="${closed}">
  <span id="cdate-${id}"><fmt:formatDate value="${closeDate.time}" pattern="${pattern}" /></span>
</c:if>

<c:if test="${not closed}">
  <span id="cdate-${id}"><a href="javascript:close(${id})"><c:if
    test="${empty closeDate}">Close</c:if><c:if test="${not empty closeDate}"><fmt:formatDate
    value="${closeDate.time}" pattern="${pattern}" /></c:if></a></span>
</c:if>
