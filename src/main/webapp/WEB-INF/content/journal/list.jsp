<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("#tabs").tabs({
        cache: false
    });
    $("table").tablesorter();
});
</script>

<ul id="title">
<li>Course Journals</li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#undergraduate">Undergraduate Courses</a></li>
  <li><a href="#graduate">Graduate Courses</a></li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <li><a href="#submitted">Pending Approval</a></li>
</security:authorize>
</ul>

<div id="undergraduate">
<c:if test="${fn:length(department.undergraduateCourses) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Code</th><th>Name</th><th>Quarter</th><th>Instructor</th></tr>
</thead>
<tbody>
<c:forEach items="${department.undergraduateCourses}" var="course">
<tr>
  <td>${course.code}</td>
  <c:choose>
  <c:when test="${not empty course.journal}">
    <td><a href="view?id=${course.journal.id}">${course.name}</a></td>
    <td>${course.journal.section.quarter}</td>
    <td>${course.journal.section.instructors[0].name}</td>
  </c:when>
  <c:otherwise>
    <td>${course.name}</td>
    <td></td>
    <td></td>
  </c:otherwise>
  </c:choose>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
</div>

<div id="graduate">
<c:if test="${fn:length(department.graduateCourses) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Code</th><th>Name</th><th>Quarter</th><th>Instructor</th></tr>
</thead>
<tbody>
<c:forEach items="${department.graduateCourses}" var="course">
<tr>
  <td>${course.code}</td>
  <c:choose>
  <c:when test="${not empty course.journal}">
    <td><a href="view?id=${course.journal.id}">${course.name}</a></td>
    <td>${course.journal.section.quarter}</td>
    <td>${course.journal.section.instructors[0].name}</td>
  </c:when>
  <c:otherwise>
    <td>${course.name}</td>
    <td></td>
    <td></td>
  </c:otherwise>
  </c:choose>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
</div>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<div id="submitted">
<c:if test="${fn:length(submittedJournals) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Code</th><th>Name</th><th>Quarter</th><th>Instructor</th><th>Submitted</th></tr>
</thead>
<tbody>
<c:forEach items="${submittedJournals}" var="journal">
<tr>
  <td>${journal.section.course.code}</td>
  <td><a href="view?id=${journal.id}">${journal.section.course.name}</a></td>
  <td class="nowrap">${journal.section.quarter}</td>
  <td class="nowrap">${journal.section.instructors[0].name}</td>
  <td><fmt:formatDate value="${journal.submitDate}" pattern="M/d/yyyy" /></td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
</div>
</security:authorize>

</div> <!-- end of tabs -->
