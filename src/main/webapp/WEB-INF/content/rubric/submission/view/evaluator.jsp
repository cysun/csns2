<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${assignment.section}" />
<c:set var="rubric" value="${assignment.rubric}" />

<script>
$(function(){
	$("#evaluate").click(function(){
		window.location.href = "../../evaluation/evaluator/view?submissionId=${submission.id}";
	});
    $("#tabs").tabs();
    $("#chartContainer").highcharts(${chart});
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/evaluated#section-${section.id}' />">${section.course.code}
  - ${section.number}</a></li>
<li><a class="bc" href="list?assignmentId=${assignment.id}">${assignment.name}</a></li>
<li>${submission.student.name}</li>
</ul>

<c:if test="${not assignment.published}">
<p>This assignment is not published yet.</p>
</c:if>

<c:if test="${assignment.published}">

<c:if test="${not assignment.pastDue}">
<p style="text-align: right;"><button id="evaluate" class="subbutton">Evaluate</button></p>
</c:if>

<div id="tabs">
<ul>
  <li><a href="#tab-summary">Summary</a></li>
  <li><a href="#tab-chart">Chart</a></li>
  <li><a href="#tab-all">All</a></li>
</ul>

<div id="tab-summary">
<c:if test="${not empty iEvalStats}">
<h4>Instructor Evaluations: ${iEvalStats[0].count}</h4>
<c:if test="${iEvalStats[0].count > 0}">
<table class="general2 autowidth">
<tr><th>Indicator</th><th>Mean</th><th>Median</th><th>Min</th><th>Max</th></tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td>
  <td class="center"><fmt:formatNumber pattern=".00" value="${iEvalStats[status.index+1].mean}" /></td>
  <td class="center"><fmt:formatNumber pattern=".0" value="${iEvalStats[status.index+1].median}" /></td>
  <td class="center">${iEvalStats[status.index+1].min}</td>
  <td class="center">${iEvalStats[status.index+1].max}</td>
</tr>
</c:forEach>
<tr>
  <td class="overall" >Overall</td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${iEvalStats[0].mean}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".0" value="${iEvalStats[0].median}" /></td>
  <td class="overall center">${iEvalStats[0].min}</td>
  <td class="overall center">${iEvalStats[0].max}</td>
</tr>
</table>
</c:if>
</c:if>

<c:if test="${not empty sEvalStats}">
<h4>Peer Evaluations: ${sEvalStats[0].count}</h4>
<c:if test="${sEvalStats[0].count > 0}">
<table class="general2 autowidth">
<tr><th>Indicator</th><th>Mean</th><th>Median</th><th>Min</th><th>Max</th></tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td>
  <td class="center"><fmt:formatNumber pattern=".00" value="${sEvalStats[status.index+1].mean}" /></td>
  <td class="center"><fmt:formatNumber pattern=".0" value="${sEvalStats[status.index+1].median}" /></td>
  <td class="center">${sEvalStats[status.index+1].min}</td>
  <td class="center">${sEvalStats[status.index+1].max}</td>
</tr>
</c:forEach>
<tr>
  <td class="overall" >Overall</td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${sEvalStats[0].mean}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".0" value="${sEvalStats[0].median}" /></td>
  <td class="overall center">${sEvalStats[0].min}</td>
  <td class="overall center">${sEvalStats[0].max}</td>
</tr>
</table>
</c:if>
</c:if>

<c:if test="${not empty eEvalStats}">
<h4>External Evaluations: ${eEvalStats[0].count}</h4>
<c:if test="${eEvalStats[0].count > 0}">
<table class="general2 autowidth">
<tr><th>Indicator</th><th>Mean</th><th>Median</th><th>Min</th><th>Max</th></tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td>
  <td class="center"><fmt:formatNumber pattern=".00" value="${eEvalStats[status.index+1].mean}" /></td>
  <td class="center"><fmt:formatNumber pattern=".0" value="${eEvalStats[status.index+1].median}" /></td>
  <td class="center">${eEvalStats[status.index+1].min}</td>
  <td class="center">${eEvalStats[status.index+1].max}</td>
</tr>
</c:forEach>
<tr>
  <td class="overall" >Overall</td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${eEvalStats[0].mean}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".0" value="${eEvalStats[0].median}" /></td>
  <td class="overall center">${eEvalStats[0].min}</td>
  <td class="overall center">${eEvalStats[0].max}</td>
</tr>
</table>
</c:if>
</c:if>
</div> <!--  end of tab-summary -->

<div id="tab-chart">
<div id="chartContainer" style="width: 880px; height: 400px;"></div>
</div> <!--  end of tab-chart -->

<div id="tab-all">
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
  <c:if test="${submission.externalEvaluationCount > 0}">
  <tr>
    <td colspan="${fn:length(rubric.indicators)+1}"
        class="evaluation-type">External Evaluation</td>
  </tr>
  <c:forEach items="${submission.externalEvaluations}" var="eevaluation">
  <c:if test="${eevaluation.completed}">
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
  </c:if>
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
</div> <!--  end of tab-all -->

</div> <!--  end of tabs -->

</c:if> <%-- end of assignment.published --%>
