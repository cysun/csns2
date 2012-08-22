<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="assignment" required="false" rtexprvalue="true" type="csns.model.academics.Assignment" %>
<%@ attribute name="submission" required="false" rtexprvalue="true" type="csns.model.academics.Submission" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<c:if test="${not empty pageScope.assignment}">
  <c:set var="dueDate" value="${assignment.dueDate}" />
  <c:set var="pastDue" value="${assignment.pastDue}" />
</c:if>

<c:if test="${not empty pageScope.submission}">
  <c:set var="dueDate" value="${submission.effectiveDueDate}" />
  <c:set var="pastDue" value="${submission.pastDue}" />
</c:if>

<c:if test="${pastDue}">
  <fmt:formatDate value="${dueDate.time}" pattern="yyyy-MM-dd hh:mm a" />
</c:if>

<c:if test="${not pastDue}">
  <span style="color: green;"><fmt:formatDate value="${dueDate.time}" pattern="yyyy-MM-dd hh:mm a" /></span>
</c:if>
