<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul id="title">
<li>Departments</li>
</ul>

<p><a href="add">Add</a></p>

<table class="viewtable">
<tr>
  <th>Abbreviation</th><th>Name</th><th>Administrators</th><th><br /></th>
</tr>
<c:forEach items="${departments}" var="department">
<tr>
  <td class="tt">${department.abbreviation}</td>
  <td>${department.name}</td>
  <td>
    <c:forEach items="${department.administrators}" var="administrator" varStatus="status">
    ${administrator.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
  <td class="centered"><a href="edit?id=${department.id}">Edit</a></td>
</tr>
</c:forEach>
</table>
