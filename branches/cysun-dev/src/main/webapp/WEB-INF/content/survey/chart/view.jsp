<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#chartContainer").highcharts(${highchart});
});
</script>

<ul id="title">
<li><a class="bc" href="../list">Surveys</a></li>
<li><a class="bc" href="list">Charts</a></li>
<li title="${chart.name}"><csns:truncate
  value="${chart.name}" length="75" /></li>
<li class="align_right"><a href="addSeries?chartId=${chart.id}"><img alt="[Add Series]"
  title="Add Series" src="<c:url value='/img/icons/chart_line_add.png' />" /></a></li>
<li class="align_right"><a href="edit?id=${chart.id}"><img alt="[Edit Chart]"
  title="Edit Chart" src="<c:url value='/img/icons/chart_bar_edit.png' />" /></a></li>
</ul>

<table class="viewtable autowidth">
<tr>
  <th>Series</th>
  <c:forEach items="${chart.xCoordinates}" var="xcoordinate">
  <th>${xcoordinate}</th>
  </c:forEach>
</tr>
<c:forEach items="${chart.series}" var="serie">
<tr>
  <td><a href="viewSeries?id=${serie.id}">${serie.name}</a></td>
  <c:forEach items="${serie.values}" var="value">
  <td><fmt:formatNumber value="${value}" pattern=".00" /></td>
  </c:forEach>
</tr>
</c:forEach>
</table>

<div id="chartContainer" style="width: 880px; height: 400px; margin-top: 2em;"></div>
