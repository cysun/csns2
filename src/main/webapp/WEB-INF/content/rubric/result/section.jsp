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
    var chart1 = ${chart1};
    chart1.plotOptions = {
        column: {
            stacking: 'percent'
        }
    };
    chart1.colors = ['green', 'greenyellow', 'yellow', 'orange', 'red'];
    chart1.yAxis = {
        title: {
            text: "Percent"
        }
<c:if test="${ratingCountsByType.keySet().size() > 1}">    
        ,
        max: 105,
        endOnTick: false,
        stackLabels: {
            enabled: true,
            style: {
                fontWeight: 'bold',
                color: 'gray'
            },
            formatter: function() {
                return  this.stack;
            }
        }
</c:if>
    };
    $("#chart1Container").highcharts(chart1);
    $("#chart2Container").highcharts(${chart2});
});
</script>

<ul id="title">
<li><a class="bc" href="list">Rubrics</a></li>
<li><a class="bc" href="results?id=${rubric.id}"><csns:truncate
  value="${rubric.name}" length="50" /></a></li>
<li>${section.course.code}, ${section.term}</li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#tab1">Student</a></li>
  <li><a href="#tab2">Average</a></li>
</ul>

<div id="tab1">
<div id="chart1Container" style="width: 880px; height: 400px;"></div>

<c:if test="${not empty ratingCountsByType.get('INSTRUCTOR')}">
<h4>Instructor Evaluations</h4>
<table class="general2 autowidth">
<tr>
  <th>Indicator</th>
  <c:forEach begin="1" end="${rubric.scale}" var="rank">
    <th>${rank}</th>
  </c:forEach>
</tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td> 
  <c:forEach items="${ratingCountsByType.get('INSTRUCTOR')[status.index]}" var="count">
  <td class="center">${count}</td>
  </c:forEach>
</tr>
</c:forEach>
</table>
</c:if>

<c:if test="${not empty ratingCountsByType.get('PEER')}">
<h4>Peer Evaluations</h4>
<table class="general2 autowidth">
<tr>
  <th>Indicator</th>
  <c:forEach begin="1" end="${rubric.scale}" var="rank">
    <th>${rank}</th>
  </c:forEach>
</tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td> 
  <c:forEach items="${ratingCountsByType.get('PEER')[status.index]}" var="count">
  <td class="center">${count}</td>
  </c:forEach>
</tr>
</c:forEach>
</table>
</c:if>

<c:if test="${not empty ratingCountsByType.get('EXTERNAL')}">
<h4>Instructor Evaluations</h4>
<table class="general2 autowidth">
<tr>
  <th>Indicator</th>
  <c:forEach begin="1" end="${rubric.scale}" var="rank">
    <th>${rank}</th>
  </c:forEach>
</tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td> 
  <c:forEach items="${ratingCountsByType.get('EXTERNAL')[status.index]}" var="count">
  <td class="center">${count}</td>
  </c:forEach>
</tr>
</c:forEach>
</table>
</c:if>
</div> <!--  end of tab1 -->

<div id="tab2">
<div id="chart2Container" style="width: 880px; height: 400px;"></div>

<c:if test="${not empty iEvalStats}">
<h4>Instructor Evaluations</h4>
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
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${iEvalStats[0].median}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${iEvalStats[0].min}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${iEvalStats[0].max}" /></td>
</tr>
</table>
</c:if>

<c:if test="${not empty sEvalStats}">
<h4>Peer Evaluations</h4>
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
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${sEvalStats[0].median}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${sEvalStats[0].min}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${sEvalStats[0].max}" /></td>
</tr>
</table>
</c:if>

<c:if test="${not empty eEvalStats}">
<h4>External Evaluations</h4>
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
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${eEvalStats[0].median}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${eEvalStats[0].min}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${eEvalStats[0].max}" /></td>
</tr>
</table>
</c:if>
</div> <!--  end of tab2 -->

</div> <!--  end of tabs -->
