<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/project/search' />">Projects</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/projects' />">${department.name}</a></li>
<li>${project.title}</li>
<li class="align_right"><a href="edit?id=${project.id}"><img alt="[Edit Project]"
  title="Edit Project" src="<c:url value='/img/icons/brick_edit.png' />" /></a></li>
</ul>

<div class="project-title">${project.title}</div>

<div class="project-members">
  <c:forEach items="${project.students}" var="student" varStatus="status">
    ${student.name}<c:if test="${not status.last}">, </c:if>
  </c:forEach>
</div>

<div class="project-advisors"> Advisor(s): 
  <c:forEach items="${project.advisors}" var="advisor" varStatus="status">
    ${advisor.name}<c:if test="${not status.last}">, </c:if>
  </c:forEach>
</div>

${project.description}
