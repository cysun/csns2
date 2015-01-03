<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${assignment.section}" />
<c:set var="rubric" value="${assignment.rubric}" />

<script>
$(function(){
	$("#evaluate").click(function(){
		window.location.href = "../../evaluation/evaluator/view?submissionId=${submission.id}";
	});
    $("#ok").click(function(){
        window.location.href = "list?assignmentId=${assignment.id}";
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/evaluated#section-${section.id}' />">${section.course.code}
  - ${section.number}</a></li>
<li><a class="bc" href="list?assignmentId=${assignment.id}"><csns:truncate
  value="${assignment.name}" length="50" /></a></li>
<li><csns:truncate value="${submission.student.name}" length="25" /></li>
</ul>

<c:if test="${not assignment.published}">
<p>This assignment is not published yet.</p>
</c:if>

<c:if test="${assignment.published}">

<c:if test="${not assignment.pastDue}">
<p style="text-align: right;"><button id="evaluate" class="subbutton">Evaluate</button></p>
</c:if>

<table class="viewtable">
<thead>
  <tr>
    <th>Evaluator</th>
    <c:forEach items="${rubric.indicators}" var="indicator">
      <th>${indicator.name}</th>
    </c:forEach>
  </tr>
</thead>
<tbody>
  <%-- Instructor Evaluation --%>
  <c:if test="${submission.instructorEvaluationCount > 0}">
  <tr>
    <td colspan="${fn:length(rubric.indicators)+1}"
        class="evaluation-type">Instructor Evaluation</td>
  </tr>
  <c:forEach items="${submission.instructorEvaluations}" var="ievaluation">
  <c:if test="${ievaluation.completed}">
  <tr>
    <td class="nowrap">${ievaluation.evaluator.name}</td>
    <c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
      <td class="center">
        <c:if test="${ievaluation.ratings[status.index] > 0}">
          ${ievaluation.ratings[status.index]}
        </c:if>
      </td>
    </c:forEach>
  </tr>
  </c:if>
  </c:forEach>
  </c:if>
  <%-- External Evaluations --%>
  <tr>
    <td colspan="${fn:length(rubric.indicators)+1}"
        class="evaluation-type">External Evaluation</td>
  </tr>
  <c:forEach items="${submission.externalEvaluations}" var="eevaluation">
  <c:if test="${eevaluation.evaluator.id == user.id or eevaluation.completed}">
  <tr>
    <td class="nowrap">${eevaluation.evaluator.name}</td>
    <c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
      <td class="center">
        <c:if test="${eevaluation.ratings[status.index] > 0}">
          ${eevaluation.ratings[status.index]}
        </c:if>
      </td>
    </c:forEach>
  </tr>
  </c:if>
  </c:forEach>
  <%-- Peer Evaluations --%>
  <c:if test="${submission.peerEvaluationCount > 0}">
  <tr>
    <td colspan="${fn:length(rubric.indicators)+1}"
        class="evaluation-type">Peer Evaluation</td>
  </tr>
  <c:forEach items="${submission.peerEvaluations}" var="pevaluation">
  <c:if test="${pevaluation.completed}">
  <tr>
    <td class="nowrap">${pevaluation.evaluator.name}</td>
    <c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
      <td class="center">
        <c:if test="${pevaluation.ratings[status.index] > 0}">
          ${pevaluation.ratings[status.index]}
        </c:if>
      </td>
    </c:forEach>
  </tr>
  </c:if>
  </c:forEach>
  </c:if>
</tbody>
</table>

</c:if> <%-- end of assignment.published --%>

<p><button id="ok" class="subbutton">OK</button></p>
