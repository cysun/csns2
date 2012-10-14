<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div id="csns_menu">
<div id="menu">
<ul class="menu">

<security:authorize access="hasRole('ROLE_ADMIN')">
  <li><a href="<c:url value='/admin/department/list' />">Departments</a></li>
  <li><a href="<c:url value='/user/search' />">Users</a></li>
</security:authorize>

<security:authorize access="not hasRole('ROLE_ADMIN')">
<security:authorize access="authenticated">
<li><a href="#">Home</a>
  <div><ul>
    <security:authorize access="principal.instructor">
    <li><a href="<c:url value='/section/taught' />"><img alt=""
         src="<c:url value='/img/icons/instructor.png' />" />Instructor</a></li>
    </security:authorize>
    <li><a href="<c:url value='/section/taken' />"><img alt=""
           src="<c:url value='/img/icons/student.png' />" />Student</a></li>
  </ul></div>
</li>
</security:authorize>

<c:if test="${not empty dept}">
<security:authorize access="authenticated and principal.isFaculty('${dept}')">
<li><a href="#">Department</a>
  <div><ul>
    <li><a href="<c:url value='/department/${dept}/people'/>"><img alt=""
           src="<c:url value='/img/icons/users.png' />" />People</a></li>
  </ul></div>
</li>
</security:authorize>

<li><a href="#">Curriculum</a>
  <div><ul>
    <li><a href="<c:url value='/department/${dept}/courses' />"><img alt=""
           src="<c:url value='/img/icons/courses.png' />" />Courses</a></li>
    <li><a href="<c:url value='/department/${dept}/projects'/>"><img alt=""
           src="<c:url value='/img/icons/projects.png' />" />Projects</a></li>
  </ul></div>
</li>

<li><a href="#">Resources</a>
  <div><ul>
    <li><a href="<c:url value='/department/${dept}/forums' />"><img alt=""
           src="<c:url value='/img/icons/forums.png' />" />Forums</a></li>
    <li><a href="<c:url value='/department/${dept}/survey/current' />"><img alt=""
           src="<c:url value='/img/icons/surveys.png' />" />Surveys</a></li>
  </ul></div>
</li>
</c:if> <%-- end of <c:if test="${not empty dept}"> --%>

<li><a href="<c:url value='/wiki/content/csns/help' />">Help</a></li>
</security:authorize>

</ul>
</div> <!-- end of menu -->
</div> <!-- end of csns-menu -->
