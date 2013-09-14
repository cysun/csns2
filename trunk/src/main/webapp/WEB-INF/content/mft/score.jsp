<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function(){
	$("#tabs").tabs();
    $("table").tablesorter();
    $("#plot").click(function(event){
        event.preventDefault();
        $.ajax({
            url: "score/chart",
            data: {
                beginYear: $("select[name='beginYear'] option:selected").val(),
                endYear: $("select[name='endYear'] option:selected").val()
            },
            success: function(data){ $("#chartContainer").highcharts(data.chart); }
        });
    });
<c:if test="${not empty years}">
    $("select[name='beginYear']").val("${beginYear}");
    $("select[name='endYear']").val("${endYear}");
</c:if>
});
</script>

<ul id="title">
<li><a class="bc" href="overview">MFT</a></li>
<li>Scores</li>
<li class="align_right"><a href="import"><img alt="[Import Scores]"
  title="Import Scores" src="<c:url value='/img/icons/table_import.png' />" /></a></li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#tab-data">Data</a></li>
  <li><a href="#tab-chart">Chart</a></li>
</ul>

<div id="tab-data">
<c:if test="${empty dates}">
<p>No MFT scores yet.</p>
</c:if>

<c:if test="${not empty dates}">
<form method="get" action="score">
<p><select name="date" onchange="this.form.submit()">
<c:forEach items="${dates}" var="date">
<option value="<fmt:formatDate value='${date}' pattern='yyyy-MM-dd' />"
  <c:if test="${date.time == selectedDate.time}">selected="selected"</c:if>>
  <fmt:formatDate value='${date}' pattern='yyyy-MM-dd' />
</option>
</c:forEach>
</select>
</p>
</form>

<table class="viewtable autowidth">
<thead>
  <tr><th></th><th>CIN</th><th>Name</th><th>Score</th><th>Percentile</th></tr>
</thead>
<tbody>
  <c:forEach items="${scores}" var="score" varStatus="status">
  <tr>
    <td class="right">${status.index+1}</td>
    <td>${score.user.cin}</td>
    <td>${score.user.lastName}, ${score.user.firstName}</td>
    <td>${score.value}</td>
    <td class="center">${score.percentile}</td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
</div> <!--  end of tab-data -->

<div id="tab-chart">
<c:if test="${not empty dates}">
<div style="padding: 10px; margin-bottom: 35px;" class="ui-widget-content ui-corner-all">
From:
  <select name="beginYear">
    <c:forEach items="${years}" var="year">
      <option value="${year}">${year}</option>
    </c:forEach>    
  </select>
<span style="margin-left: 10px;">To:</span>
  <select name="endYear">
    <c:forEach items="${years}" var="year" varStatus="status">
      <option value="${year}">${year}</option>
    </c:forEach>    
  </select>
<span style="margin-left: 10px;"><button id="plot" class="subbutton">Plot</button></span>
</div>
<div id="chartContainer" style="width:100%; height:400px;"></div>
</c:if>
</div> <!--  end of tab-chart -->
</div> <!--  end of tabs -->
