<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/project/search' />">Projects</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/projects?year=${project.year}' />">${fn:toUpperCase(dept)}</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/project/view?id=${project.id}' />"><csns:truncate
  value="${project.title}" length="20" /></a></li>
<li><csns:truncate value="${resource.name}" length="50" /></li>
<c:if test="${project.isMember(user) or user.isFaculty(dept)}">
<li class="align_right"><a href="edit?projectId=${project.id}&amp;resourceId=${resource.id}"><img
  alt="[Edit Resource]" title="Edit Resource" src="<c:url value='/img/icons/plugin_edit.png' />" /></a></li>
</c:if>
</ul>

${resource.text}
