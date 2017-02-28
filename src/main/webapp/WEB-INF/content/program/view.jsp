<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
function clone( id )
{
    var msg = "Do you want to clone this program?";
    if( confirm(msg) )
        window.location.href = "clone?id=" + id;
}
function remove( id )
{
    var msg = "Do you want to remove this program?";
    if( confirm(msg) )
        window.location.href = "remove?id=" + id;
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/program/search' />">Programs</a></li>
<li><a class="bc" href="list">${program.department.name}</a></li>
<li>${program.name}</li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <li class="align_right"><a href="block/list?programId=${program.id}"><img title="Edit Blocks"
      alt="[Edit Blocks]" src="<c:url value='/img/icons/bricks.png' />" /></a></li>
  <li class="align_right"><a href="edit?id=${program.id}"><img alt="[Edit Program]"
      title="Edit Program" src="<c:url value='/img/icons/report_edit.png' />" /></a></li>
  <li class="align_right"><a href="javascript:remove(${program.id})"><img alt="[Remove Program]"
      title="Remove Program" src="<c:url value='/img/icons/report_delete.png' />" /></a></li>
  <li class="align_right"><a href="javascript:clone(${program.id})"><img alt="[Clone Program]"
      title="Clone Program" src="<c:url value='/img/icons/report_code.png' />" /></a></li>
</security:authorize>
</ul>

<h3 class="site-title">${program.name}</h3>
<div class="site-description">${program.description}</div>

<c:forEach items="${program.blocks}" var="block">
<div id="block-${block.id}" class="site-block">
<div class="site-block-title">${block.name}
  <span style="margin-left: 1em;">
    <c:if test="${block.requireAll}">(All Courses Required)</c:if>
    <c:if test="${not block.requireAll}">(${block.unitsRequired} Units Required)</c:if>
  </span>
</div>
<div class="site-block-content">
${block.description}
<c:if test="${fn:length(block.courses) > 0}">
<table class="general2 autowidth">
  <tr><th>Code</th><th>Name</th><th>Units</th></tr>
  <c:forEach items="${block.courses}" var="course">
  <tr>
    <td>${course.code}</td>
    <td>${course.name}</td>
    <td class="center">${course.units}</td>
  </c:forEach>
</table>
</c:if>
</div> <!-- end of site-block-content -->
</div> <!-- end of site-block -->
</c:forEach>
