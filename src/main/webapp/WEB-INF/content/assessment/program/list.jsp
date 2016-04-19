<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<ul id="title">
<li>Program Assessment</li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<li class="align_right"><a href="add"><img alt="[Add Program]"
  title="Add Program" src="<c:url value='/img/icons/report_add.png' />" /></a></li>
</security:authorize>
</ul>

<ul>
<c:forEach items="${programs}" var="program">
  <li><a class="link" href="view?id=${program.id}"><span class="big">${program.name}</span></a></li>
</c:forEach>
</ul>
