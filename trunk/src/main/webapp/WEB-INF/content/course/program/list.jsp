<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
function clone( id )
{
    var msg = "Do you want to clone this program?";
    if( confirm(msg) )
        window.location.href = "clone?id=" + id;
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/course/search' />">Courses</a></li>
<li><a class="bc" href="../../courses">${department.name}</a></li>
<li>Programs</li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<li class="align_right"><a href="create"><img alt="[Create Program]"
  title="Create Program" src="<c:url value='/img/icons/report_add.png' />" /></a></li>
</security:authorize>
</ul>

<table class="viewtable autowidth">
<thead>
  <tr><th>Name</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th></th>
  </security:authorize>
  </tr>
</thead>
<tbody>
<c:forEach items="${programs}" var="program">
<tr>
  <td><a href="view?id=${program.id}">${program.name}</a></td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td>
    <a href="javascript:clone(${program.id})"><img title="Clone Program" alt="[Clone Program]"
       border="0" src="<c:url value='/img/icons/report_code.png' />" /></a>
    <a href="edit?id=${program.id}"><img title="Edit Program" alt="[Edit Program]"
       border="0" src="<c:url value='/img/icons/report_edit.png' />" /></a>
  </td>
  </security:authorize>
</tr>
</c:forEach>
</tbody>
</table>
