<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}" />

<script>
$(function(){
   $("table").tablesorter({
      sortList: [[0,0]]
   });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="<c:url value='/submission/online/summary?assignmentId=${assignment.id}' />"><csns:truncate
  value="${assignment.name}" length="35" /></a></li>
<li>List of Submissions</li>
</ul>

<h3>Number of submissions: ${fn:length(answerSheets)}</h3>

<c:if test="${not empty question}">
<div>${question.description}</div>
<p>
<c:choose>
  <c:when test="${question.type == 'CHOICE' and question.singleSelection }">
    <c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
      <input type="radio" 
        <c:if test="${choiceStatus.index == param.selection}">checked="checked"</c:if>
      /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${question.type == 'CHOICE' and not question.singleSelection }">
    <c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
      <input type="checkbox"
        <c:if test="${choiceStatus.index == param.selection}">checked="checked"</c:if>
      /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${question.type == 'RATING'}">
    ${question.minRating}
    <c:forEach begin="${question.minRating}" end="${question.maxRating}" step="1" var="rating">
      <input type="radio" <c:if test="${rating == param.rating}">checked="checked"</c:if> />
    </c:forEach>
    ${question.maxRating}
  </c:when>
</c:choose>
</p>
</c:if>

<c:if test="${fn:length(answerSheets) > 0}">
<table class="viewtable autowidth">
<thead>
  <tr><th>Name</th><th>Timestamp</th></tr>
</thead>
<tbody>
<c:forEach items="${answerSheets}" var="answerSheet">
  <tr>
    <td><a href="grade?answerSheetId=${answerSheet.id}">${answerSheet.author.lastName},
      ${answerSheet.author.firstName}</a></td>
    <td>
      <fmt:formatDate value="${answerSheet.date}" pattern="MM/dd/yyyy hh:mm:ss a" />
    </td>
  </tr>
</c:forEach>
</tbody>
</table>
</c:if>
