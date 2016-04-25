<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("#sections").tablesorter({
    	sortList: [[0,0], [1,0]]
    });
});
</script>

<ul id="title">
<li><a class="bc" href="list">Schedules</a></li>
<li>${schedule.term}</li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <li class="align_right"><a href="import?scheduleId=${schedule.id}"><img title="Import Sections"
    alt="[Import Sections]" src="<c:url value='/img/icons/table_import.png' />" /></a></li>
  <li class="align_right"><a href="edit?id=${schedule.id}"><img title="Edit Schedule"
    alt="[Edit Schedule]" src="<c:url value='/img/icons/calendar_edit.png' />" /></a></li>
</security:authorize>
</ul>

<table class="general autowidth" style="margin-bottom: 2em;">
<tr>
  <th>Sections:</th><td>${fn:length(schedule.sections)}</td>
</tr>
<tr>
  <th>Pre-Registration Start</th>
  <td><fmt:formatDate value="${schedule.preregStart}" pattern="yyyy-MM-dd" /></td>
</tr>
<tr>
  <th>Pre-Registration End</th>
  <td><fmt:formatDate value="${schedule.preregEnd}" pattern="yyyy-MM-dd" /></td>
</tr>
</table>

<c:if test="${fn:length(schedule.sections) > 0}">
<table id="sections" class="viewtable autowidth">
<thead>
<tr><th>Course</th><th>Section</th><th>Number</th><th>Time</th><th>Location</th><th>Enrolled</th></tr>
</thead>
<tbody>
<c:forEach items="${schedule.sections}" var="section">
<tr>
  <td><a href="<c:url value='/course/view?id=${section.course.id}' />">${section.course.code}</a></td>
  <td>${section.sectionNumber}</td>
  <td>${section.classNumber}</td>
  <td>${section.days}<c:if test="${not empty section.startTime}">
    ${section.startTime}-${section.endTime}</c:if></td>
  <td>${section.location}</td>
  <td>${fn:length(section.registrations)}/${section.capacity}</td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
