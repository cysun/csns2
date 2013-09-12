<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
function deleteDistribution( id )
{
    var msg = "Are you sure you want to delete this distribution?";
    if( confirm(msg) )
        window.location.href = "delete?id=" + id;
}
</script>

<ul id="title">
<li><a class="bc" href="../overview">MFT</a></li>
<li><a class="bc" href="../distribution?year=${distribution.year}">National Distributions</a></li>
<li>${distribution.type.name}</li>
<li class="align_right"><a href="edit?id=${distribution.id}"><img alt="[Edit Distribution]"
  title="Edit Distribution" src="<c:url value='/img/icons/table_edit.png' />" /></a></li>
<li class="align_right"><a href="javascript:deleteDistribution(${distribution.id})"><img alt="[Delete Distribution]"
  title="Delete Distribution" src="<c:url value='/img/icons/table_delete.png' />" /></a></li>
</ul>

<table class="viewtable autowidth">
  <tr><th>Year</th><th>Time Period</th><th>Sample Size</th><th>Mean</th><th>Median</th><th>StdDev</th></tr>
  <tr>
    <td>${distribution.year}</td>
    <td>
      <fmt:formatDate value="${distribution.fromDate}" pattern="MM/yyyy" /> -
      <fmt:formatDate value="${distribution.toDate}" pattern="MM/yyyy" />
    </td>
    <td class="center">${distribution.numOfSamples}</td>
    <td class="center">${distribution.mean}</td>
    <td class="center">${distribution.median}</td>
    <td class="center">${distribution.stdev}</td>
  </tr>
</table>

<p></p>

<table class="viewtable autowidth">
  <tr><th>${distribution.type.valueLabel}</th><th>Percentile</th></tr>
  <c:forEach items="${distribution.entries}" var="entry">
  <tr>
    <td class="center">${entry.value}</td>
    <td class="center">${entry.percentile}</td>
  </tr>
  </c:forEach>
</table>
