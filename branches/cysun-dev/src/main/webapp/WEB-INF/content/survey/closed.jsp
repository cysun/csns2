<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<div id="closed">
<c:if test="${fn:length(closedSurveys) == 0}">
<p>No closed surveys.</p>
</c:if>

<c:if test="${fn:length(closedSurveys) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Author</th><th>Published</th><th>Closed</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${closedSurveys}" var="survey">
  <tr>
    <td><a href="view?id=${survey.id}">${survey.name}</a></td>
    <td class="shrink">${survey.author.username}</td>
    <td class="date"><csns:publishDate date="${survey.publishDate.time}"
        datePast="${survey.published}" itemId="${survey.id}" /></td>
    <td class="date"><csns:closeDate survey="${survey}" /></td>
    <td class="action">
      <a href="results?id=${survey.id}"><img alt="[Results]" 
         title="Results" src="<c:url value='/img/icons/table_multiple.png'/>" /></a>
      <a href="javascript:clone(${survey.id})"><img alt="[Clone Survey]" 
         title="Clone Survey" src="<c:url value='/img/icons/script_code.png'/>" /></a>
      <a href="edit?id=${survey.id}"><img alt="[Edit Survey]"
         title="Edit Survey" src="<c:url value='/img/icons/script_edit.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
</div>
