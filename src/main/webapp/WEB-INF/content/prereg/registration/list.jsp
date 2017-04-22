<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function(){
    $("#registrations").tablesorter();
    $(".comments-dialog").dialog({
        autoOpen : false,
        modal : true,
        width : 600
    });
    $(".view-comments").click(function(){
        var regId = $(this).attr("data-registration-id");
        $(".comments-dialog[data-registration-id='" + regId + "']").dialog("open");
    });
    $("#selectAll").click(function(){
        var checked = $("#selectAll").is(":checked");
        $(":checkbox[name='userId']").prop("checked",checked);
    });
    $("#email").click(function(){
        if( $(":checkbox[name='userId']:checked").length == 0 )
            alert( "Please select the student(s) to contact." );
        else
            $("#studentsForm").attr("action", "<c:url value='/email/compose' />").submit();
    });
    $("#drop").click(function(){
        if( $(":checkbox[name='userId']:checked").length == 0 )
            alert( "Please select the student(s) you want to drop." );
        else
            if( confirm("Are you sure you want to drop these students from the section?") )
                $("#studentsForm").attr("action", "delete").submit();
    });
});
</script>

<c:set var="qstring" value="scheduleId=${schedule.id}" />
<c:if test="${not empty section}">
  <c:set var="schedule" value="${section.schedule}" />
  <c:set var="qstring" value="sectionId=${section.id}" />
</c:if>

<ul id="title">
<li><a class="bc" href="../schedule/list">Schedules</a></li>
<li><a class="bc" href="../schedule/view?id=${schedule.id}">${schedule.term}</a></li>
<c:choose>
  <c:when test="${not empty section}">
    <li>${section.course.code}-${section.sectionNumber}</li>
  </c:when>
  <c:otherwise>
    <li>Registrations</li>
  </c:otherwise>
</c:choose>
<li class="align_right"><a href="export?${qstring}"><img title="Export to Excel"
    alt="[Export to Excel]" src="<c:url value='/img/icons/export_excel.png' />" /></a></li>
<li class="align_right"><a id="drop" href="javascript:void(0)"><img title="Drop Students"
    alt="[Drop Students]" src="<c:url value='/img/icons/user_delete.png' />" /></a></li>
<li class="align_right"><a id="email" href="javascript:void(0)"><img title="Email Students"
    alt="[Email Students]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
</ul>

<p>Total: ${fn:length(registrations)}</p>

<c:if test="${fn:length(registrations) > 0}">
<form id="studentsForm" method="post">
<table id="registrations" class="general2 autowidth">
<thead>
<tr>
  <th><input id="selectAll" type="checkbox" /></th><th>CIN</th>
  <th>Name</th><th>Timestamp</th><th>Sections</th>
</tr>
</thead>
<tbody>
<c:forEach items="${registrations}" var="registration">
<tr>
  <td class="center"><input type="checkbox" name="userId" value="${registration.student.id}" /></td>
  <td>${registration.student.cin}</td>
  <td class="nowrap">
    <a href="<c:url value='/user/view?id=${registration.student.id}' />">${registration.student.lastName},
    ${registration.student.firstName}</a>
    <c:if test="${not empty registration.comments}">
      <a href="javascript:void(0)" class="view-comments" data-registration-id="${registration.id}"><img
        title="View Comments" alt="[View Comments]" src="<c:url value='/img/icons/comment.png' />" /></a>
      <pre class="comments-dialog" data-registration-id="${registration.id}">${registration.comments}</pre>
    </c:if>
  </td>
  <td class="nowrap"><fmt:formatDate value="${registration.date}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
  <td>
    <c:forEach items="${registration.sectionRegistrations}" var="sectionRegistration">
      ${sectionRegistration.sectionString}
    </c:forEach>
  </td>
</tr>
</c:forEach>
</tbody>
</table>
<input type="hidden" name="sectionId" value="${section.id}" />
<input type="hidden" name="backUrl" value="/department/${dept}/prereg/registration/list?${qstring}" />
</form>
</c:if>
