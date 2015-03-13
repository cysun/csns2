<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/program/search' />">Programs</a></li>
<li><a class="bc" href="../programs">${program.department.name}</a></li>
<li><csns:truncate value="${program.name}" length="55" /></li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<li class="align_right"><a href="edit?id=${program.id}"><img alt="[Edit Program]"
  title="Edit Program" src="<c:url value='/img/icons/report_edit.png' />" /></a></li>
</security:authorize>
</ul>

<h3>${program.name}</h3>
<div>${program.description}</div>

<h4>Required Courses</h4>
<ul>
<c:forEach items="${program.requiredCourses}" var="course">
<li style="margin: 0 0 5px 0;">${course.code} ${course.name}</li>
</c:forEach>
</ul>


<h4>Elective Courses</h4>
<ul>
<c:forEach items="${program.electiveCourses}" var="course">
<li style="margin: 0 0 5px 0;">${course.code} ${course.name}</li>
</c:forEach>
</ul>
