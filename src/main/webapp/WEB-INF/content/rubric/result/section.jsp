<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<style>
.general2 td.overall {
  border-top: 5px double #dddddd;
}
</style>

<script>
$(function(){
    $("#tabs").tabs();
    $("#chartContainer").highcharts(${chart});
});
</script>

<ul id="title">
<li><a class="bc" href="list">Rubrics</a></li>
<li><a class="bc" href="results?id=${rubric.id}"><csns:truncate
  value="${rubric.name}" length="50" /></a></li>
<li>${section.course.code}, ${section.quarter}</li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#tab-data">Data</a></li>
  <li><a href="#tab-chart">Chart</a></li>
</ul>

<div id="tab-data">
<c:if test="${not empty iEvalStats}">
<h4>Instructor Evaluations: ${iEvalStats[0].count}</h4>
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

<c:if test="${not empty sEvalStats}">
<h4>Peer Evaluations: ${sEvalStats[0].count}</h4>
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

<c:if test="${not empty eEvalStats}">
<h4>External Evaluations: ${eEvalStats[0].count}</h4>
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
</div> <!--  end of tab-data -->

<div id="tab-chart">
<div id="chartContainer" style="width: 880px; height: 400px;"></div>
</div> <!--  end of tab-chart -->

</div> <!--  end of tabs -->
