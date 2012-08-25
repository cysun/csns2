<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/department/${department.abbreviation}/' />">${department.name}</a></li>
<li><a class="bc" href="list">Surveys</a></li>
<li>Open Surveys</li>
</ul>

<c:if test="${fn:length(surveys) == 0}">
  <p>Currently there are no open surveys.</p>
</c:if>

<c:if test="${fn:length(surveys) > 0 }">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Type</th><th>Close Date</th></tr>
</thead>
<tbody>
  <c:forEach items="${surveys}" var="survey">
  <tr>
    <td><a href="take?id=${survey.id}">${survey.name}</a></td>
    <td><a href="javascript:help('${survey.type}')">${survey.type}</a></td>
    <td><fmt:formatDate value="${survey.closeDate}" pattern="MM/dd/yyyy" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
