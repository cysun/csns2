<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul id="title">
<li>Programs</li>
</ul>

<form action="search" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40"
  value="${param.term}" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty programs}">
<table class="viewtable autowidth">
<tr><th>Department</th><th>Name</th></tr>
<c:forEach items="${programs}" var="program">
<tr>
  <td>${program.department.name}</td>
  <td><a href="../department/${program.department.abbreviation}/program/view?id=${program.id}">${program.name}</a></td>
</tr>
</c:forEach>
</table>
</c:if>
