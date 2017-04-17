<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
function updateAbsence( $cell, value )
{
    var $absence = $cell.closest("tr").children("td.absence-count");
    var count = Number( $absence.html() ) + value;
    if( count > 0 )
        $absence.html(count);
    else
        $absence.html("");
}
$(function(){
    var newEventDialog = $("#newEventForm").dialog({
       autoOpen: false,
       resizable: false,
       modal: true,
       width: 'auto',
       buttons: {
           "Create": function(){
               $("#newEventForm").submit();
           }
       }
    });
    $("#newEventBtn").click(function(){
       newEventDialog.dialog("open"); 
    });
    var editEventDialog = $("#editEventForm").dialog({
        autoOpen: false,
        resizable: false,
        modal: true,
        width: 'auto',
        buttons: {
            "Remove": function(){
                $("#editEventForm").attr("action", "attendance/removeEvent");
                $("#editEventForm").submit();
            },
            "Save": function(){
                $("#editEventForm").attr("action", "attendance/editEvent");
                $("#editEventForm").submit();
            }
        }
    });
    $(".event").off(); // remove tablesorter's event handler on event <th>
    $(".event").click(function(event){
        $("#editEventForm > input[name='name']").val($(this).text());
        $("#editEventForm > input[name='eventId']").val($(this).attr("data-event-id"));
        editEventDialog.dialog("open");
    });
    $(".record").click(function(){
        $.ajax({
            url: "attendance/toggle",
            data: {
                "eventId" : $(this).attr("data-event-id"),
                "userId" : $(this).attr("data-user-id")
            },
            cache: false,
            context: $(this),
            success: function(){
                if( $(this).hasClass("attended"))
                {
                    $(this).removeClass("attended");
                    $(this).addClass("absent");
                    updateAbsence($(this), 1);
                }
                else if( $(this).hasClass("absent") )
                {
                    $(this).removeClass("absent");
                    updateAbsence($(this), -1);
                }
                else
                    $(this).addClass("attended");
            }
        });
    });
    $("tr.student").each(function(index,row){
        var count = $(row).children("td.absent").length;
        if( count > 0 ) $(row).children("td.absence-count").html(count);
    });
    $("table").tablesorter({
        sortList: [[0,0]]
    });
})
</script>

<ul id="title">
<li><a class="bc" href="taught#section-${section.id}">${section.course.code} - ${section.number}</a></li>
<li>Attendance</li>
</ul>

<c:if test="${fn:length(section.attendanceEvents) == 0}">
<p>To take attendance, first click on the New Event button to create an <em>event</em> that
the students are supposed to attend. The name of the event can be anything, e.g. <code>Lab 1</code>,
<code>Midterm</code>, and so on. After an event is created, click on the cell that corresponds
to a student to indicate whether the student attended the event:</p>
<ul>
<li>Green means the student attended the event.</li>
<li>Red means the student was absent.</li>
<li>White means there is no information about the student's attendance.</li>
</ul>
<p>You can click on an event to edit or remove it.</p>
</c:if>
<p><button id="newEventBtn" class="subbutton">New Event</button></p>


<c:if test="${fn:length(section.attendanceEvents) > 0}">
<div style="overflow-x: auto; transform: rotateX(180deg);">
<table class="viewtable autowidth" style="transform:rotateX(180deg);">
<thead>
<tr>
  <th>Student</th>
  <th class="sorter-digit">Absence</th>
  <c:forEach items="${section.attendanceEvents}" var="event">
    <th class="event sorter-false" data-event-id="${event.id}">${event.name}</th>
  </c:forEach>
</tr>
</thead>
<tbody>
<c:forEach items="${section.enrollments}" var="enrollment">
<tr class="student">
  <td>${enrollment.student.lastName}, ${enrollment.student.firstName}</td>
  <td class="absence-count center"></td>
  <c:forEach items="${section.attendanceEvents}" var="event">
  <c:set var="attended" value="${event.isAttended(enrollment.student)}" />
  <c:choose>
    <c:when test="${empty attended}">
    <td data-event-id="${event.id}" data-user-id="${enrollment.student.id}" class="record"></td>
    </c:when>
    <c:when test="${attended}">
    <td data-event-id="${event.id}" data-user-id="${enrollment.student.id}" class="record attended"></td>
    </c:when>
    <c:otherwise>
    <td data-event-id="${event.id}" data-user-id="${enrollment.student.id}" class="record absent"></td>
    </c:otherwise>
  </c:choose>
  </c:forEach>
</tr>
</c:forEach>
</tbody>
</table>
</div>
</c:if>

<form id="newEventForm" action="attendance/addEvent" method="post">
<input type="text" style="width: 10em;" name="name"
  placeholder="Event Name" maxlength="10" class="forminput" />
<input type="hidden" name="sectionId" value="${section.id}" />
</form>

<form id="editEventForm" method="post">
<input type="text" style="width: 10em;" name="name"
  placeholder="Event Name" maxlength="10" class="forminput" />
<input type="hidden" name="sectionId" value="${section.id}" />
<input type="hidden" name="eventId" value="" />
</form>
