<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function(){
    $("table").tablesorter({
    	sortList: [[2,1]]
    });
});
</script>

<ul id="title">
<li><a class="bc" href="overview">MFT</a></li>
<li>Scores</li>
<li class="align_right"><a href="import"><img alt="[Import]"
  title="Import" src="<c:url value='/img/icons/table_import.png' />" /></a></li>
</ul>

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
  <tr><th>CIN</th><th>Name</th><th>Score</th><th>Percentile</th></tr>
</thead>
<tbody>
  <c:forEach items="${scores}" var="score">
  <tr>
    <td>${score.user.cin}</td>
    <td>${score.user.lastName}, ${score.user.firstName}</td>
    <td>${score.value}</td>
    <td>${score.percentile}</td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
