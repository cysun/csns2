<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="../list">Surveys</a></li>
<li>Charts</li>
<li class="align_right"><a href="create"><img alt="[Create Chart]"
  title="Create Chart" src="<c:url value='/img/icons/chart_bar_add.png' />" /></a></li>
</ul>

<c:if test="${fn:length(charts) == 0}">
<p>No charts yet.</p>
</c:if>

<c:if test="${fn:length(charts) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Author</th><th>Updated</th></tr>
</thead>
<tbody>
  <c:forEach items="${charts}" var="chart">
  <tr>
    <td><a href="view?id=${chart.id}">${chart.name}</a></td>
    <td class="shrink">${chart.author.username}</td>
    <td class="shrink"><fmt:formatDate value="${chart.date}" pattern="MM/dd/yyyy" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
