<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
$(function(){
   $("table").tablesorter(); 
});
</script>

<ul id="title">
<li>Programs</li>
</ul>

<form action="search" method="get">
<p><input id="search" name="text" type="text" class="forminput" size="40"
  value="${param.text}" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty programs}">
<table class="general2 autowidth">
<tr><th>Department</th><th>Name</th><th>Publish Date</th><th>Published By</th></tr>
<c:forEach items="${programs}" var="program">
<tr>
  <td>${program.department.name}</td>
  <td><a href="../department/${program.department.abbreviation}/program/view?id=${program.id}">${program.name}</a></td>
  <td><fmt:formatDate value="${program.publishDate.time}" pattern="yyyy-MM-dd" /></td>
  <td>${program.publishedBy.name}</td>
</tr>
</c:forEach>
</table>
</c:if>
