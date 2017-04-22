<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#sections").tablesorter({
    	sortList: [[0,0], [1,0]]
    });
    $(".notes-dialog").dialog({
        autoOpen : false,
        modal : true,
        width : 450
    });
    $(".view-notes").click(function(){
        var sectionId = $(this).attr("data-section-id");
        $(".notes-dialog[data-section-id='" + sectionId + "']").dialog("open");
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
  <li class="align_right"><a href="../section/add?scheduleId=${schedule.id}"><img title="Add Section"
    alt="[Add Section]" src="<c:url value='/img/icons/page_add.png' />" /></a></li>
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
<table id="sections" class="general2 autowidth">
<thead>
<tr>
  <th>Course</th><th>Section</th><th>Enrolled</th><th>Name</th><th>Type</th>
  <th>Number</th><th>Time</th><th>Location</th><th>Linked By</th><th></th></tr>
</thead>
<tbody>
<c:forEach items="${schedule.sections}" var="section">
<tr id="${section.id}">
  <td><a href="<c:url value='/course/view?id=${section.course.id}' />">${section.course.code}</a></td>
  <td>${section.sectionNumber}
    <c:if test="${not empty section.notes}">
      <a href="javascript:void(0)" class="view-notes" data-section-id="${section.id}"><img
        title="View Notes" alt="[View Notes]" src="<c:url value='/img/icons/comment.png' />" /></a>
      <pre class="notes-dialog" data-section-id="${section.id}">${section.notes}</pre>
    </c:if>
  </td>
  <td>
    <a href="../registration/list?sectionId=${section.id}">${fn:length(section.registrations)}/${section.capacity}</a>
  </td>
  <td>${section.course.name}</td>
  <td>${section.type}</td>
  <td>${section.classNumber}</td>
  <td>${section.days}<c:if test="${not empty section.startTime}">
    ${section.startTime}-${section.endTime}</c:if></td>
  <td>${section.location}</td>
  <td class="nowrap">
    <c:if test="${not empty section.linkedBy}">
      ${section.linkedBy.course.code}-${section.linkedBy.sectionNumber}
    </c:if>
  </td>
  <td><a href="../section/edit?id=${section.id}"><img title="Edit Section"
    alt="[Edit Section]" src="<c:url value='/img/icons/page_edit.png' />" /></a>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
