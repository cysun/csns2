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
    $("#evalType").val("${evalType}");
    $("#beginYear").val("${beginYear}").change(function(){
    	if( $("#endYear").val() < $("#beginYear").val() )
    	    $("#endYear").val($("#beginYear").val());
    });
    $("#endYear").val("${endYear}").change(function(){
        if( $("#endYear").val() < $("#beginYear").val() )
            $("#beginYear").val($("#endYear").val());
    });
    $("#select").click(function(){
        window.location.href = "?rubricId=${rubric.id}&courseId=${course.id}"
            + "&beginYear=" + $("#beginYear").val()
            + "&endYear=" + $("#endYear").val()
            + "&evalType=" + $("#evalType").val();
    });
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
<c:if test="${ratingCountsByYear.keySet().size() > 1}">    
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
<li>${course.code}</li>
</ul>

<div style="padding: 10px; margin-bottom: 1em;" class="ui-widget-content ui-corner-all">
Evaluation Type:
  <select id="evalType" name="evalType">
    <option>INSTRUCTOR</option>
    <option>PEER</option>
    <option>EXTERNAL</option>
  </select>
<span style="margin-left: 2em;">From:</span>
  <select id="beginYear" name="beginYear">
    <c:forEach items="${years}" var="year">
      <option value="${year}">${year}</option>
    </c:forEach>    
  </select>
<span style="margin-left: 1em;">To:</span>
  <select id="endYear" name="endYear">
    <c:forEach items="${years}" var="year" varStatus="status">
      <option value="${year}">${year}</option>
    </c:forEach>    
  </select>
<span style="margin-left: 2em;"><button id="select" class="subbutton">Select</button></span>
</div>

<div id="tabs">
<ul>
  <li><a href="#tab1">Student</a></li>
  <li><a href="#tab2">Average</a></li>
</ul>

<div id="tab1">
<div id="chart1Container" style="width: 880px; height: 400px;"></div>
<c:if test="${not empty ratingCountsByYear}">
<c:forEach items="${ratingCountsByYear.keySet()}" var="year">
  <h4>${year}</h4>
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
    <c:forEach items="${ratingCountsByYear.get(year)[status.index]}" var="count">
    <td class="center">${count}</td>
    </c:forEach>
  </tr>
  </c:forEach>
  </table>
</c:forEach>
</c:if>
</div> <!--  end of tab1 -->

<div id="tab2">
<div id="chart2Container" style="width: 880px; height: 400px;"></div>
<table class="general2 autowidth">
<tr>
  <th>Indicator</th>
  <c:forEach begin="${beginYear}" end="${endYear}" step="1" var="year">
    <th>${year}</th>
  </c:forEach>
</tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td>
  <c:forEach begin="${beginYear}" end="${endYear}" step="1" var="year">
    <td class="center"><fmt:formatNumber pattern=".00"
      value="${meansByYear.get(year)[status.index+1]}" /></td>
  </c:forEach>
</tr>
</c:forEach>
<tr>
  <td class="overall">Overall</td>
  <c:forEach begin="${beginYear}" end="${endYear}" step="1" var="year">
    <td class="overall center"><fmt:formatNumber pattern=".00"
      value="${meansByYear.get(year)[0]}" /></td>
  </c:forEach>
</tr>
</table>
</div> <!--  end of tab2 -->

</div> <!--  end of tabs -->
