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
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><csns:truncate value="${assignment.name}" length="60" /></li>
<li class="align_right"><a href="email?assignmentId=${assignment.id}"><img title="Email Grades" alt="[Email Grades]"
  src="<c:url value='/img/icons/email_go.png' />" /></a></li>
<c:if test="${not assignment.online}">
  <li class="align_right"><a href="<c:url value='/download?assignmentId=${assignment.id}' />"><img
    title="Download All Files" alt="[Download All Files]" src="<c:url value='/img/icons/download.png' />" /></a></li>
</c:if>
<c:if test="${assignment.online and assignment.published}">
  <li class="align_right"><a href="online/summary?assignmentId=${assignment.id}"><img
    title="Submission Summary" alt="[Submission Summary]" src="<c:url value='/img/icons/table_multiple.png' />" /></a></li>
</c:if>
<c:if test="${assignment.online and assignment.pastDue}">
  <li class="align_right"><a href="online/autograde?assignmentId=${assignment.id}"><img
    title="Auto Grade" alt="[Auto Grade]" src="<c:url value='/img/icons/table_multiple_check.png' />" /></a></li>
</c:if>
</ul>

<p>Due Date: <csns:dueDate assignment="${assignment}" /></p>

<c:if test="${not empty assignment.totalPoints}">
<p>Total points: ${assignment.totalPoints}</p>
</c:if>

<table class="viewtable autowidth">
<thead><tr><th>Name</th><c:if test="${not assignment.online}"><th># of Files</th></c:if><th>Grade</th></tr></thead>
<tbody>
  <c:forEach items="${assignment.submissions}" var="submission">
  <tr>
    <td>
      <c:if test="${not empty submission.grade and not submission.gradeMailed}"><img
        src="<c:url value='/img/icons/email.png' />" alt="[Email]" /></c:if>
      <c:if test="${not assignment.online}">
        <a href="grade?id=${submission.id}">${submission.student.lastName}, ${submission.student.firstName}</a>
      </c:if>
      <c:if test="${assignment.online}">
        <a href="online/grade?id=${submission.id}">${submission.student.lastName}, ${submission.student.firstName}</a>
      </c:if>
    </td>
    <c:if test="${not assignment.online}">
    <td class="center">${submission.fileCount}</td>
    </c:if>
    <td class="center">${submission.grade}</td>
  </tr>
  </c:forEach>
</tbody>
</table>
