<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("table").tablesorter();
});
function publish( id )
{
    var msg = "Do you want to publish this program now?";
    if( confirm(msg) )
        window.location.href = "publish?id=" + id;
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/program/search' />">Programs</a></li>
<li>${department.name}</li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<li class="align_right"><a href="create"><img alt="[Create Program]"
  title="Create Program" src="<c:url value='/img/icons/report_add.png' />" /></a></li>
</security:authorize>
</ul>

<c:if test="${fn:length(programs) > 0}">
<table class="general2 autowidth">
<thead>
  <tr><th>Name</th><th>Publish Date</th><th>Published By</th></tr>
</thead>
<tbody>
<c:forEach items="${programs}" var="program">
<tr>
  <td><a href="view?id=${program.id}">${program.name}</a></td>
  <c:if test="${program.published}">
    <td><fmt:formatDate value="${program.publishDate.time}" pattern="yyyy-MM-dd" /></td>
    <td>${program.publishedBy.name}</td>
  </c:if>
  <c:if test="${not program.published}">
    <td colspan="2" class="center">
      <security:authorize access="authenticated and principal.isAdmin('${dept}')">
      <a href="javascript:publish(${program.id})">Publish</a>
      </security:authorize>
    </td>
  </c:if>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
