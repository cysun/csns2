<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul id="title">
<li>Projects</li>
</ul>

<form action="search" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty projects}">
<table class="viewtable">
<tr><th>Year</th><th>Project</th><th>Students</th><th>Advisors</th></tr>
<c:forEach items="${projects}" var="project">
<tr>
  <td class="shrink">${project.year}</td>
  <td><a href="<c:url value='/department/${project.department.abbreviation}/project/view?id=${project.id}' />">${project.title}</a></td>
  <td style="width: 250px;">
    <c:forEach items="${project.students}" var="student" varStatus="status">
      ${student.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
  <td style="width: 100px;">
    <c:forEach items="${project.advisors}" var="advisor" varStatus="status">
      ${advisor.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
</tr>
</c:forEach>
</table>
</c:if>
