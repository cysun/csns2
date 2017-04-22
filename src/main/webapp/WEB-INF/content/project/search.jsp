<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
$(function(){
    $("table").tablesorter();
});
</script>

<ul id="title">
<li>Projects</li>
</ul>

<form action="search" method="get">
<p><input id="search" name="text" type="text" class="forminput" size="40" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty projects}">
<table class="general2 autowidth">
<thead>
<tr><th>Year</th><th>Project</th><th>Sponsor</th><th>Students</th><th>Advisors</th></tr>
</thead>
<tbody>
<c:forEach items="${projects}" var="project">
<tr>
  <td><a href="<c:url value='/department/${project.department.abbreviation}/projects?year=${project.year}' />">${project.year}</a></td>
  <td class="nowrap"><a href="<c:url value='/department/${project.department.abbreviation}/project/view?id=${project.id}' />">${project.title}</a></td>
  <td class="center">${project.sponsor}</td>
  <td>
    <c:forEach items="${project.students}" var="student" varStatus="status">
      ${student.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
  <td>
    <c:forEach items="${project.advisors}" var="advisor" varStatus="status">
      ${advisor.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
