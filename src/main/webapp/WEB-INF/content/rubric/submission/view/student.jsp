<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${assignment.section}" />
<c:set var="rubric" value="${assignment.rubric}" />

<script>
$(function(){
    $("#tabs").tabs();
    $("#chartContainer").highcharts(${chart});
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taken#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>${assignment.name}</li>
<c:if test="${assignment.evaluatedByStudents}">
<li class="align_right"><a href="<c:url value='/rubric/submission/student/list?assignmentId=${assignment.id}' />"><img
  title="Evaluate Others" alt="[Evaluate Others]" src="<c:url value='/img/icons/table_heatmap2.png' />" /></a></li>
</c:if>
<li class="align_right"><a href="<c:url value='/rubric/submission/student/rubric?assignmentId=${assignment.id}' />"><img
  title="View Rubric" alt="[View Rubric]" src="<c:url value='/img/icons/table.png' />" /></a></li>
</ul>

<c:if test="${not assignment.published}">
<p>This assignment is not published yet.</p>
</c:if>

<c:if test="${assignment.published}">

<table class="general autowidth">
<tr>
  <th>Rubric</th>
  <td>
    <a href="<c:url value='/department/${dept}/rubric/view?id=${rubric.id}' />">View</a>
  </td>
</tr>
<tr>
  <th>Due Date</th><td><csns:dueDate date="${assignment.dueDate.time}"
  datePast="${assignment.pastDue}" /></td>
</tr>
</table>

<p></p>

<div id="tabs">
<ul>
  <li><a href="#tab-summary">Summary</a></li>
  <li><a href="#tab-chart">Chart</a></li>
</ul>
<%@include file="_summary.jsp" %>
<%@include file="_chart.jsp" %>
</div>

</c:if> <%-- end of assignment.published --%>
