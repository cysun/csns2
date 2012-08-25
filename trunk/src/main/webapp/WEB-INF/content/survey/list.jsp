<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/department/${department.abbreviation}/' />">${department.name}</a></li>
<li>Surveys</li>
<li class="align_right"><a href="create"><img alt="[Create Survey]"
  title="Create Survey" src="<c:url value='/img/icons/script_add.png' />" /></a></li>
</ul>

<c:if test="${fn:length(surveys) == 0}">
<p>No surveys found.</p>
</c:if>

<c:if test="${fn:length(surveys) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Author</th><th>Published</th><th>Closed</th><th>Results</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${surveys}" var="survey">
  <tr>
    <td><a href="view?id=${survey.id}">${survey.name}</a></td>
    <td class="shrink">${survey.author.username}</td>
    <td class="date"><csns:publishDate survey="${survey}" /></td>
    <td class="date"><csns:closeDate survey="${survey}" /></td>
    <td class="shrink">
      <c:if test="${survey.published}"><a href="summary?id=${survey.id}">Summary</a></c:if>
      <c:if test="${not survey.published}">Summary</c:if>
    </td>
    <td class="shrink">
      <a href="clone?id=${survey.id}' />"><img alt="[Clone Survey]" 
         title="Clone Survey" src="<c:url value='/img/icons/script_code.png'/>" /></a>
      <a href="edit?id=${survey.id}"><img alt="[Edit Survey]"
         title="Edit Survey" src="<c:url value='/img/icons/script_edit.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
