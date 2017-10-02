<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}" />

<script>
$(function(){
   $("table").tablesorter({
      sortList: [[0,0]]
   });
   <c:if test="${assignment.pastDue}">
   $(".no-eval").hide();
   </c:if>
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taken#section-${section.id}' />">${section.course.code}
  - ${section.number}</a></li>
<li><csns:truncate value="${assignment.name}" length="60" /></li>
</ul>

<table class="general autowidth">
<tr>
  <th>Rubric</th>
  <td>
    <a href="<c:url value='/department/${dept}/rubric/view?id=${assignment.rubric.id}' />">View</a>
  </td>
</tr>
<tr>
  <th>Due Date</th><td><csns:dueDate date="${assignment.dueDate.time}"
  datePast="${assignment.pastDue}" /></td>
</tr>
</table>

<p></p>

<table class="viewtable autowidth">
<thead><tr><th>Name</th>
  <th>Instructor Evaluations</th>
  <c:if test="${assignment.evaluatedByExternal}"><th>External Evaluations</th></c:if>
  <c:if test="${assignment.evaluatedByStudents}"><th>Peer Evaluations</th></c:if>
</tr></thead>
<tbody>
  <c:forEach items="${assignment.submissions}" var="submission">
  <c:if test="${submission.student.id != user.id}">
  <tr <c:if test="${submission.totalEvaluationCount == 0}">class="no-eval"</c:if>>
    <td><a href="<c:url value='/rubric/evaluation/student/view?submissionId=${submission.id}' />">${submission.student.lastName},
        ${submission.student.firstName}</a></td>
    <td class="center">${submission.instructorEvaluationCount}</td>
    <c:if test="${assignment.evaluatedByExternal}">
      <td class="center">${submission.externalEvaluationCount}</td>
    </c:if>
    <c:if test="${assignment.evaluatedByStudents}">
      <td class="center">${submission.peerEvaluationCount}</td>
    </c:if>
  </tr>
  </c:if>
  </c:forEach>
</tbody>
</table>
