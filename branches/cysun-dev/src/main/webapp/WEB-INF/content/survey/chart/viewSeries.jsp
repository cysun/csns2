<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="../list">Surveys</a></li>
<li><a class="bc" href="list">Charts</a></li>
<li><a class="bc" title="${series.chart.name}" href="view?id=${series.chart.id}"><csns:truncate
  value="${series.chart.name}" length="60" /></a></li>
<li>Series</li>
<li class="align_right"><a href="editSeries?id=${series.id}"><img alt="[Edit Series]"
  title="Edit Series" src="<c:url value='/img/icons/chart_line_edit.png' />" /></a></li>
</ul>

<table class="general autowidth">
  <tr>
    <th class="shrink">Name</th>
    <td>${series.name}</td>
  </tr>
  <tr>
    <th>Stats Type</th>
    <td>${series.statType}</td>
  </tr>
</table>

<table id="points" class="viewtable" style="margin-top: 1em;">
<tr><th></th><th>Survey</th><th class="shrink">Section</th><th class="shrink">Question</th></tr>
<c:forEach items="${series.points}" var="point" varStatus="status">
<c:if test="${not empty point.survey}">
<tr data-id="${point.survey.id}-${point.sectionIndex}-${point.questionIndex}">
  <td class="center">${status.index+1}</td>
  <td>${point.survey.name}</td>
  <td class="center">${point.sectionIndex+1}</td>
  <td class="center">${point.questionIndex+1}</td>
</tr>
</c:if>
<c:if test="${empty point.survey}">
<tr data-id="0-0-0">
  <td class="center">${status.index+1}</td><td>[Empty Point]</td><td></td><td></td>
</tr>
</c:if>
</c:forEach>
</table>
