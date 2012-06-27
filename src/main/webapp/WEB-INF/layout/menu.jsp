<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div id="csns_menu">
<div id="menu">
  <ul class="menu">
 
    <security:authorize access="isAuthenticated()">
    <li><a href="<c:url value='/home.html' />">Home</a>
      <div><ul>
        <security:authorize access="hasRole('ROLE_STUDENT')">
        <li><a href="<c:url value='/student/viewSections.html' />"><img alt=""
            src="<c:url value='/img/icons/home-student.png' />" />Student</a></li>
        </security:authorize>
        <security:authorize access="hasRole('ROLE_INSTRUCTOR')">
        <li><a href="<c:url value='/instructor/viewSections.html' />"><img alt=""
            src="<c:url value='/img/icons/home-instructor.png' />" />Instructor</a></li>
        </security:authorize>
        <security:authorize access="hasRole('ROLE_ADMIN')">
        <li><a href="<c:url value='/user/searchUsers.html' />"><img alt=""
            src="<c:url value='/img/icons/home-administrator.png' />" />Administrator</a></li>
        </security:authorize>
        <security:authorize access="hasRole('ROLE_PROGRAM_REVIEWER')">
        <li><a href="<c:url value='/wiki/content/assessment/' />"><img alt=""
            src="<c:url value='/img/icons/home-administrator.png' />" />Program Reviewer</a></li>
        </security:authorize>
      </ul></div>
    </li>
    </security:authorize>

    <security:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_ADMIN')">
    <li><a href="#">Administration</a>
      <div><ul>
        <li><a href="<c:url value='/user/searchUsers.html'/>"><img alt=""
            src="<c:url value='/img/icons/administration-users.png' />" />Users</a></li>
        <li><a href="<c:url value='/section/viewSections.html'/>"><img alt=""
            src="<c:url value='/img/icons/administration-sections.png' />" />Sections</a></li>
        <li><a href="<c:url value='/program/viewPrograms.html'/>"><img alt=""
            src="<c:url value='/img/icons/administration-programs.png' />" />Programs</a></li>
        <li><a href="<c:url value='/assessment/mft/viewMFTScores.html' />"><img alt=""
            src="<c:url value='/img/icons/administration-mft-data.png' />" />MFT Data</a></li>
        <security:authorize access="hasRole('ROLE_ADMIN')">
        <li><a href="<c:url value='/admin/importGrades.html'/>"><img alt=""
            src="<c:url value='/img/icons/administration-data-import.png' />" />Data Import</a></li>
        </security:authorize>
      </ul></div>
    </li>
    </security:authorize>

    <li><a href="#">Curriculum</a>
      <div><ul>
        <li><a href="<c:url value='/courses.html' />"><img alt=""
            src="<c:url value='/img/icons/curriculum-undergraduate-courses.png' />" />Courses</a></li>
        <li><a href="<c:url value='/projects.html'/>"><img alt=""
            src="<c:url value='/img/icons/curriculum-senior-projects.png' />" />Senior Projects</a></li>
      </ul></div>
    </li>

    <li><a href="#">Resources</a>
      <div><ul>
        <li><a href="<c:url value='/wiki/content/' />"><img alt=""
            src="<c:url value='/img/icons/resources-wiki.png' />" />Wiki</a></li>
        <li><a href="<c:url value='/news.html' />"><img alt=""
            src="<c:url value='/img/icons/resources-news.png' />" />News</a></li>
        <li><a href="<c:url value='/forum/viewForums.html' />"><img alt=""
            src="<c:url value='/img/icons/resources-forums.png' />" />Forums</a></li>
        <li><a href="<c:url value='/surveys.html' />"><img alt=""
            src="<c:url value='/img/icons/resources-surveys.png' />" />Surveys</a></li>
        <li><a href="<c:url value='/mailinglist/viewMailinglists.html' />"><img alt=""
            src="<c:url value='/img/icons/resources-mailing-lists.png' />" />Mailing Lists</a></li>
        <security:authorize ifAnyGranted="ROLE_USER">
        <li><a href="<c:url value='/file/viewFolder.html' />"><img alt=""
            src="<c:url value='/img/icons/resources-file-manager.png' />" />File Manager</a></li>
        </security:authorize>
        <security:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_ADMIN')">
        <li><a href="<c:url value='/assessment/viewStoredQueries.html' />"><img alt=""
            src="<c:url value='/img/icons/resources-stored-queries.png' />" />Stored Queries</a></li>
        </security:authorize>
      </ul></div>
    </li>

    <li><a href="<c:url value='/wiki/content/csns/help' />">Help</a></li>   

  </ul>
</div>
</div>
