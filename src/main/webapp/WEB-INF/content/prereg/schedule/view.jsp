<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#sections").tablesorter({
    	sortList: [[0,0], [1,0]]
    });
});
function publish( id )
{
    var msg = "Do you want to start pre-registration now?";
    if( confirm(msg) )
        $("#pdate-"+id).load( "publish?id=" + id );
}
</script>

<ul id="title">
<li><a class="bc" href="list">Schedules</a></li>
<li>${schedule.term}</li>
  <li class="align_right"><a href="import?scheduleId=${schedule.id}"><img title="Import Sections"
    alt="[Import Sections]" src="<c:url value='/img/icons/table_import.png' />" /></a></li>
  <li class="align_right"><a href="edit?id=${schedule.id}"><img title="Edit Schedule"
    alt="[Edit Schedule]" src="<c:url value='/img/icons/calendar_edit.png' />" /></a></li>
</ul>

<table class="general autowidth">
<tr>
  <th>Sections:</th><td>${fn:length(schedule.sections)}</td>
  <th>Pre-Registration Start</th>
  <td><csns:publishDate itemId="${schedule.id}" date="${schedule.preregStart}"
                        datePast="${schedule.preregStarted}" /></td>
</tr>
<tr>
  <th>Registrations:</th>
  <td><a href="../registration/list?scheduleId=${schedule.id}">${fn:length(registrations)}</a></td>
  <th>Pre-Registration End</th>
  <td><csns:dueDate datePattern="yyyy-MM-dd" date="${schedule.preregEnd}"
                    datePast="${schedule.preregEnded}" /></td>
</tr>
</table>

<div style="margin: 1em 0;">${schedule.description}</div>

<c:if test="${fn:length(schedule.sections) > 0}">
<table id="sections" class="viewtable autowidth">
<thead>
<tr>
  <th>Course</th><th>Section</th><th>Enrolled</th><th>Name</th><th>Type</th>
  <th>Number</th><th>Time</th><th>Location</th></tr>
</thead>
<tbody>
<c:forEach items="${schedule.sections}" var="section">
<tr>
  <td>${section.course.code}</td>
  <td>${section.sectionNumber}</td>
  <td>
    <a href="../registration/list?sectionId=${section.id}">${fn:length(section.registrations)}/${section.capacity}</a>
  </td>
  <td>${section.course.name}</td>
  <td>${section.type}</td>
  <td>${section.classNumber}</td>
  <td>${section.days}<c:if test="${not empty section.startTime}">
    ${section.startTime}-${section.endTime}</c:if></td>
  <td>${section.location}</td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
