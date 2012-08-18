<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>Online Assignments</li>
<li class="align_right"><a href="create?sectionId=${section.id}"><img alt="[Create Online Assignment]"
  title="Create Online Assignment" src="<c:url value='/img/icons/script_add.png' />" /></a></li>
</ul>

<c:if test="${fn:length(assignments) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Publish Date</th><th>Due Date</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${assignments}" var="assignment">
  <tr>
    <td>
      ${assignment.name}
    </td>
    <td class="fixedwidth"><csns:publishDate assignment="${assignment}" /></td>
    <td class="fixedwidth"><csns:dueDate assignment="${assignment}" /></td>
    <td class="action">
      <a href="clone?id=${assignment.id}' />"><img alt="[Clone Assignment]" 
         title="Clone Assignment" src="<c:url value='/img/icons/script_code.png'/>" />Clone</a>
      <a href="edit?id=${assignment.id}"><img alt="[Edit Assignment]"
         title="Edit Assignment" src="<c:url value='/img/icons/script_edit.png'/>" />Edit</a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>

<form action="list" method="get">
<p><input name="search" type="text" class="forminput" size="40" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${fn:length(results) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Quarter</th><th>Course</th><th>Name</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${results}" var="assignment">
  <tr>
    <td>${assignment.section.quarter}</td>
    <td>${assignment.section.course}</td>
    <td>${assignment.name}</td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
