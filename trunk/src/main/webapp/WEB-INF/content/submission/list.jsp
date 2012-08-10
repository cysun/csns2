<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>${assignment.name}</li>
<li class="align_right"><a href="<c:url value='/download?assignmentId=${assignment.id}' />"><img
  title="Download All Files" alt="[Download All Files]" src="<c:url value='/img/icons/download.png' />" /></a></li>
<li class="align_right"><a href="email?assignmentId=${assignment.id}"><img title="Email Grades" alt="[Email Grades]"
  src="<c:url value='/img/icons/email_go.png' />" /></a></li>
</ul>

<p>Due Date: <csns:dueDate assignment="${assignment}" /></p>

<c:if test="${not empty assignment.totalPoints}">
<p>Total points: ${assignment.totalPoints}</p>
</c:if>

<table class="viewtable halfwidth">
<thead><tr><th>Name</th><th># of Files</th><th>Grade</th></tr></thead>
<tbody>
  <c:forEach items="${assignment.submissions}" var="submission">
  <tr>
    <td><a href="grade?id=${submission.id}">${submission.student.lastName}, ${submission.student.firstName}</a></td>
    <td class="center">${fn:length(submission.files)}</td>
    <td class="center">
      <c:if test="${not submission.gradeMailed}"><b>${submission.grade}</b></c:if>
      <c:if test="${submission.gradeMailed}">${submission.grade}</c:if>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
