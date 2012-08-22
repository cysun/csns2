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
</ul>

<p>Due Date: <csns:dueDate assignment="${assignment}" /></p>

<c:if test="${not empty assignment.totalPoints}">
<p>Total points: ${assignment.totalPoints}</p>
</c:if>

<table class="outer_viewtable halfwidth">
<tr><td>
<table class="viewtable">
<thead><tr><th>Name</th><c:if test="${not assignment.online}"><th># of Files</th></c:if><th>Grade</th></tr></thead>
<tbody>
  <c:forEach items="${assignment.submissions}" var="submission">
  <tr>
<c:if test="${not assignment.online}">
    <td><a href="grade?id=${submission.id}">${submission.student.lastName}, ${submission.student.firstName}</a></td>
    <td class="center">${fn:length(submission.files)}</td>
</c:if>
<c:if test="${assignment.online}">
    <td><a href="online/grade?id=${submission.id}">${submission.student.lastName}, ${submission.student.firstName}</a></td>
</c:if>
    <td class="center">
      <c:if test="${not submission.gradeMailed}"><b>${submission.grade}</b></c:if>
      <c:if test="${submission.gradeMailed}">${submission.grade}</c:if>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</td></tr>
<tr class="rowtypeb">
  <td>
    <a href="email?assignmentId=${assignment.id}">Email Grades</a>
    <c:if test="${not assignment.online}">
      | <a href="<c:url value='/download?assignmentId=${assignment.id}' />">Download All Files</a>
    </c:if>
    <c:if test="${assignment.online and assignment.pastDue}">
      | <a href="autoGradeAssignment.html?assignmentId=${assignment.id}">Auto Grade</a>
    </c:if>
  </td>
</tr>
</table>
