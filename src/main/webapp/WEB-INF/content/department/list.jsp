<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul id="title">
<li>Departments</li>
<li class="align_right"><a href="add"><img title="Add" alt="[Add]"
    src="<c:url value='/img/icons/table_add.png' />" /></a></li>
</ul>

<table class="viewtable">
<tr>
  <th>Abbreviation</th><th>Name</th><th>Full Name</th><th>Administrators</th><th><br /></th>
</tr>
<c:forEach items="${departments}" var="department">
<tr>
  <td class="tt">${department.abbreviation}</td>
  <td>${department.name}</td>
  <td>${department.fullName}</td>
  <td>
    <c:forEach items="${department.administrators}" var="administrator" varStatus="status">
    ${administrator.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
  <td class="center"><a href="edit?id=${department.id}"><img title="Edit" alt="[Remove]" border="0"
    src="<c:url value='/img/icons/table_edit.png' />" /></a>
  </td>
</tr>
</c:forEach>
</table>
