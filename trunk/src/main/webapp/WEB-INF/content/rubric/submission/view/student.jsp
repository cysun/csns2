<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
</ul>

<c:if test="${not assignment.published}">
<p>This assignment is not published yet.</p>
</c:if>

<c:if test="${assignment.published}">

<div id="tabs">
<ul>
  <li><a href="#tab-summary">Summary</a></li>
  <li><a href="#tab-chart">Chart</a></li>
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

</div> <!--  end of tabs -->

</c:if> <%-- end of assignment.published --%>
