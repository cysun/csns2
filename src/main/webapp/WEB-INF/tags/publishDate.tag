<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="assignment" required="false" rtexprvalue="true" type="csns.model.academics.Assignment" %>
<%@ attribute name="survey" required="false" rtexprvalue="true" type="csns.model.survey.Survey" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<c:if test="${not empty pageScope.assignment}">
  <c:set var="publishDate" value="${assignment.publishDate}" />
  <c:set var="published" value="${assignment.published}" />
  <c:set var="id" value="${assignment.id}" />
  <c:set var="pattern" value="yyyy-MM-dd hh:mm a" />
</c:if>

<c:if test="${not empty pageScope.survey}">
  <c:set var="publishDate" value="${survey.publishDate}" />
  <c:set var="published" value="${survey.published}" />
  <c:set var="id" value="${survey.id}" />
  <c:set var="pattern" value="yyyy-MM-dd" />
</c:if>

<c:if test="${published}">
  <span id="pdate-${id}"><fmt:formatDate value="${publishDate.time}" pattern="${pattern}" /></span>
</c:if>

<c:if test="${not published}">
  <span id="pdate-${id}"><a href="javascript:publish(${id})"><c:if
    test="${empty publishDate}">Publish</c:if><c:if test="${not empty publishDate}"><fmt:formatDate
    value="${publishDate.time}" pattern="${pattern}" /></c:if></a></span>
</c:if>
