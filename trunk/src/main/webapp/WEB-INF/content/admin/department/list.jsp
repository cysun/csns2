<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
$(function(){
    $("#add").click(function(){
        window.location.href = "add";
    });
});
</script>

<ul id="title">
<li>Departments</li>
<li class="align_right"><button id="add" class="opbutton">Add</button></li>
</ul>

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
