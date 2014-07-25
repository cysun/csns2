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
    $("select[name='beginYear']").val("${beginYear}");
    $("select[name='endYear']").val("${endYear}");
});
function showStats( evalType )
{ 
    $("#statsContainer").load( "result/stats", {
        rubricId: ${rubric.id},
        courseId: ${course.id},
        type: evalType,
        beginYear: ${beginYear},
        endYear: ${endYear}
    });
    $.ajax({
        url: "result/chart",
        data: {
            rubricId: ${rubric.id},
            courseId: ${course.id},
            type: evalType,
            beginYear: ${beginYear},
            endYear: ${endYear}
        },
        success: function(data){ $("#chartContainer").highcharts(data.chart); }
    });
}
</script>

<ul id="title">
<li><a class="bc" href="list">Rubrics</a></li>
<li><a class="bc" href="results?id=${rubric.id}"><csns:truncate
  value="${rubric.name}" length="50" /></a></li>
<li>${course.code}</li>
</ul>

<div style="padding: 10px; margin-bottom: 1em;" class="ui-widget-content ui-corner-all">
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
<span style="margin-left: 10px;"><button id="select" class="subbutton">Select</button></span>
</div>

<table class="general2 autowidth" style="margin-bottom: 1em;">
<tr>
  <th>Evaluation Type</th>
  <c:forEach begin="${beginYear}" end="${endYear}" step="1" var="year">
  <th>${year}</th>
  </c:forEach>
  <th></th>
</tr>
<c:forEach items="${countsByType}" var="counts">
<tr>
  <td>${counts.key}</td>
  <c:forEach items="${counts.value}" var="count">
  <td class="center">${count}</td>
  </c:forEach>
  <td class="center">
    <a href="javascript:showStats('${counts.key}')"><img title="Show Stats" alt="[Show Stats]"
       src="<c:url value='/img/icons/table_chart.png' />" /></a>
  </td>
</tr>
</c:forEach>
</table>

<div id="tabs">
<ul>
  <li><a href="#tab-data">Data</a></li>
  <li><a href="#tab-chart">Chart</a></li>
</ul>

<div id="tab-data">
<div id="statsContainer"></div>
</div> <!--  end of tab-data -->

<div id="tab-chart">
<div id="chartContainer" style="width: 880px; height: 400px;"></div>
</div> <!--  end of tab-chart -->

</div> <!--  end of tabs -->
