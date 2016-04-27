<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function(){
    $("#registrations").tablesorter();
    $(".comments-dialog").dialog({
        autoOpen : false,
        modal : true
    });
    $(".view-comments").click(function(){
        var regId = $(this).attr("data-registration-id");
        $(".comments-dialog[data-registration-id='" + regId + "']").dialog("open");
    });
});
</script>

<c:if test="${not empty section}">
  <c:set var="schedule" value="${section.schedule}" />
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
</ul>

<p>Total: ${fn:length(registrations)}</p>

<c:if test="${fn:length(registrations) > 0}">
<table id="registrations" class="viewtable autowidth">
<thead>
<tr>
  <th></th><th>CIN</th><th>Name</th><th>Timestamp</th><th>Sections</th>
</tr>
</thead>
<tbody>
<c:forEach items="${registrations}" var="registration" varStatus="status">
<tr>
  <td>${status.index+1}</td>
  <td>${registration.student.cin}</td>
  <td class="nowrap">
    <a href="<c:url value='/user/view?id=${registration.student.id}' />">${registration.student.lastName},
    ${registration.student.firstName}</a>
    <c:if test="${not empty registration.comments}">
      <a href="javascript:void(0)" class="view-comments" data-registration-id="${registration.id}"><img
        title="View Comments" alt="[View Comments]" src="<c:url value='/img/icons/comment.png' />" /></a>
      <div class="comments-dialog" data-registration-id="${registration.id}">${registration.comments}</div>
    </c:if>
  </td>
  <td class="nowrap"><fmt:formatDate value="${registration.date}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
  <td>
    <c:forEach items="${registration.sections}" var="section">
      ${section.course.code}-${section.sectionNumber}
    </c:forEach>
  </td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
