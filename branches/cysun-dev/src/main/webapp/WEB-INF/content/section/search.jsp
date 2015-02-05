<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("table").tablesorter();
});
</script>

<ul id="title">
<li>Sections</li>
</ul>

<form action="search" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty sections}">
<table class="viewtable">
<thead>
<tr>
  <th>Year</th><th>Quarter</th><th>Code</th><th>Name</th><th>Instructor</th>
  <security:authorize access="authenticated and principal.admin">
    <th></th>
  </security:authorize>
</tr>
</thead>
<tbody>
<c:forEach items="${sections}" var="section">
<tr>
  <td>${section.quarter.year}</td>
  <td>${section.quarter.quarterName}</td>
  <td>
    ${section.course.code}
    <c:if test="${section.number != 1}">(${section.number})</c:if>
  </td>
  <td>${section.course.name}</td>
  <td>
    <c:forEach items="${section.instructors}" var="instructor" varStatus="status">
      ${instructor.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
  <security:authorize access="authenticated and principal.admin">
  <td class="center"><a href="view?id=${section.id}"><img
    title="Grade Sheet" alt="[Grade Sheet]" src="<c:url value='/img/icons/table.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
