<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
  <li><a href="#submitted">Pending Approval</a></li>
</ul>

<div id="undergraduate">
<c:if test="${fn:length(department.undergraduateCourses) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Code</th><th>Name</th><th>Course Journal</th></tr>
</thead>
<tbody>
<c:forEach items="${department.undergraduateCourses}" var="course">
<tr>
  <td>${course.code}</td>
  <td>${course.name}</td>
  <td class="center">
    <c:if test="${not empty course.syllabus}">
      <a href="view?id=${course.courseJournal.id}">${course.courseJournal.section.quarter}</a>
    </c:if>
  </td>
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
  <tr><th>Code</th><th>Name</th><th>Course Journal</th></tr>
</thead>
<tbody>
<c:forEach items="${department.graduateCourses}" var="course">
<tr>
  <td>${course.code}</td>
  <td>${course.name}</td>
  <td class="center">
    <c:if test="${not empty course.syllabus}">
      <a href="view?id=${course.courseJournal.id}">${course.courseJournal.section.quarter}</a>
    </c:if>
  </td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
</div>

<div id="submitted">
<c:if test="${fn:length(submittedJournals) > 0}">
<table class="viewtable">
<thead><tr>
  <th>Code</th><th>Name</th><th>Course Journal</th><th>Instructor</th><th>Submitted</th>
</tr></thead>
<tbody>
<c:forEach items="${submittedJournals}" var="journal">
<tr>
  <td>${journal.section.course.code}</td>
  <td>${journal.section.course.name}</td>
  <td>${journal.section.quarter}</td>
  <td>${journal.section.instructors[0]}</td>
  <td><fmt:formatDate value="${journal.submitDate}" pattern="yyyy-MM-dd" /></td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
</div>

</div> <!-- end of tabs -->
