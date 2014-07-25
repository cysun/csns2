<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
.general2 td.overall {
  border-top: 5px double #dddddd;
}
</style>

<h4>Mean Ratings of ${param.type} Evaluation</h4>

<table class="general2 autowidth">
<tr>
  <th>Indicator</th>
  <c:forEach begin="${param.beginYear}" end="${param.endYear}" step="1" var="year">
    <th>${year}</th>
  </c:forEach>
</tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td>
  <c:forEach begin="${param.beginYear}" end="${param.endYear}" step="1" var="year">
    <td class="center"><fmt:formatNumber pattern=".00"
      value="${meansByYear.get(year)[status.index+1]}" /></td>
  </c:forEach>
</tr>
</c:forEach>
<tr>
  <td class="overall">Overall</td>
  <c:forEach begin="${param.beginYear}" end="${param.endYear}" step="1" var="year">
    <td class="overall center"><fmt:formatNumber pattern=".00"
      value="${meansByYear.get(year)[0]}" /></td>
  </c:forEach>
</tr>
</table>