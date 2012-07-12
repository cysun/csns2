<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<c:if test="${empty dept}">
  <c:set var="dept" value="${cookie['default-dept'].value}" scope="request" />
</c:if> 

<div id="csns_menu">
<div id="menu">
<ul class="menu">

<c:if test="${not empty dept}">

<security:authorize access="hasRole('ROLE_ADMIN')" var="sysadmin" />
<security:authorize access="authenticated and principal.isAdmin('${dept}')" var="admin" />
<security:authorize access="authenticated and principal.isFaculty('${dept}')" var="faculty" />
<security:authorize access="authenticated and principal.isInstructor('${dept}')" var="instructor" />
<security:authorize access="authenticated and principal.isReviewer('${dept}')" var="reviewer" />
<security:authorize access="authenticated" var="student" />

<%-- Note that the order of <c:when> is important. --%>
<c:choose>
  <c:when test="${sysadmin}">
    <li><a href="<c:url value='/user/search' />">Users</a></li>
    <li><a href="<c:url value='/admin/department/list' />">Departments</a></li>
  </c:when>
  <c:when test="${reviewer}">
    <li><a href="<c:url value='/wiki/content/${dept}/program_assessment/' />">Home</a></li>
  </c:when>
  <c:when test="${faculty or instructor}">
    <li><a href="<c:url value='/instructor/section/list' />">Home</a>
      <div><ul>
        <li><a href="<c:url value='/instructor/section/list' />"><img alt=""
               src="<c:url value='/img/icons/instructor.png' />" />Instructor</a></li>
        <li><a href="<c:url value='/student/section/list' />"><img alt=""
               src="<c:url value='/img/icons/student.png' />" />Student</a></li>
      </ul></div>
    </li>
  </c:when>
  <c:when test="${admin}">
    <li><a href="<c:url value='/user/search' />">Home</a></li>
  </c:when>
  <c:when test="${student}">
    <li><a href="<c:url value='/student/section/list' />">Home</a></li>
  </c:when>
</c:choose>

<li><a href="#">Department</a>
  <div><ul>
    <li><a href="<c:url value='/department/${dept}/news' />"><img alt=""
           src="<c:url value='/img/icons/news.png' />" />News</a></li>
    <li><a href="<c:url value='/department/${dept}/people'/>"><img alt=""
           src="<c:url value='/img/icons/users.png' />" />People</a></li>
    <li><a href="<c:url value='/department/${dept}/sections'/>"><img alt=""
           src="<c:url value='/img/icons/sections.png' />" />Sections</a></li>
    <li><a href="<c:url value='/department/${dept}/courses' />"><img alt=""
           src="<c:url value='/img/icons/courses.png' />" />Courses</a></li>
    <li><a href="<c:url value='/department/${dept}/projects'/>"><img alt=""
           src="<c:url value='/img/icons/projects.png' />" />Projects</a></li>
    <li><a href="<c:url value='/department/${dept}/data'/>"><img alt=""
           src="<c:url value='/img/icons/data.png' />" />Data Import</a></li>
  </ul></div>
</li>

<li><a href="#">Assessment</a>
  <div><ul>
    <li><a href="<c:url value='/department/${dept}/mft' />"><img alt=""
           src="<c:url value='/img/icons/mft.png' />" />MFT</a></li>
    <li><a href="<c:url value='/department/${dept}/queries' />"><img alt=""
           src="<c:url value='/img/icons/queries.png' />" />Queries</a></li>
  </ul></div>
</li>

<li><a href="#">Resources</a>
  <div><ul>
    <li><a href="<c:url value='/wiki/content/${dept}/' />"><img alt=""
           src="<c:url value='/img/icons/wiki.png' />" />Wiki</a></li>
    <li><a href="<c:url value='/forum/viewForums.html' />"><img alt=""
            src="<c:url value='/img/icons/forums.png' />" />Forums</a></li>
    <li><a href="<c:url value='/department/${dept}/surveys' />"><img alt=""
           src="<c:url value='/img/icons/surveys.png' />" />Surveys</a></li>
    <li><a href="<c:url value='/mailinglist/viewMailinglists.html' />"><img alt=""
           src="<c:url value='/img/icons/mailinglists.png' />" />Mailing Lists</a></li>
    <li><a href="<c:url value='/file/viewFolder.html' />"><img alt=""
           src="<c:url value='/img/icons/file-manager.png' />" />File Manager</a></li>
 </ul></div>
</li>

</c:if> <%-- end of <c:if test="${not empty dept}"> --%>

<li><a href="<c:url value='/wiki/content/csns/help' />">Help</a></li>   

</ul>
</div> <!-- end of menu -->
</div> <!-- end of csns-menu -->
