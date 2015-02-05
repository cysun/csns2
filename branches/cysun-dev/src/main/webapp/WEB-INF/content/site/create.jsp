<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
$(function(){
    $("table").tablesorter();
});
</script>

<p>Please choose to create a new site or clone an existing site:</p>

<p><a href="create?sectionId=${section.id}">New Site</a></p>

<table class="viewtable">
<thead>
  <tr><th>Quarter</th><th>Course</th><th>Section</th><th></th></tr>
</thead>
<tbody>
<c:forEach items="${sites}" var="site">
  <tr>
    <td>${site.section.quarter}</td>
    <td>${site.section.course.code}</td>
    <td>${site.section.number}</td>
    <td>
      <a href="<c:url value='${site.url}' />">View</a> |
      <a href="clone?id=${site.id}">Clone</a>
    </td>
  </tr>
</c:forEach>
</tbody>
</table>
