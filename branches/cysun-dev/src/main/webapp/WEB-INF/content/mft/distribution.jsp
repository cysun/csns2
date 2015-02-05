<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ul id="title">
<li><a class="bc" href="overview">MFT</a></li>
<li>National Distributions</li>
<li class="align_right"><a href="distribution/add"><img alt="[Add Distribution]"
  title="Add Distribution" src="<c:url value='/img/icons/table_add.png' />" /></a></li>
</ul>

<c:if test="${empty years}">
<p>No MFT distribution yet.</p>
</c:if>

<c:if test="${not empty years}">
<form method="get" action="distribution">
<select name="year" onchange="this.form.submit()">
<c:forEach items="${years}" var="year">
<option value="${year}" <c:if test="${year == selectedYear}">selected="selected"</c:if>>
  ${year}
</option>
</c:forEach>
</select>
</form>

<p></p>

<table class="viewtable autowidth">
  <tr><th>Name</th><th>Time Period</th><th>Sample Size</th></tr>
  <c:forEach items="${distributions}" var="distribution">
  <tr>
    <td><a href="distribution/view?id=${distribution.id}">${distribution.type.name}</a></td>
    <td>
      <fmt:formatDate value="${distribution.fromDate}" pattern="MM/yyyy" /> -
      <fmt:formatDate value="${distribution.toDate}" pattern="MM/yyyy" />
    </td>
    <td class="center">${distribution.numOfSamples}</td>
  </tr>
  </c:forEach>
</table>
</c:if>
