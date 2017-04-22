<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
function publish( id )
{
    var msg = "Do you want to start pre-registration now?";
    if( confirm(msg) )
        $("#pdate-"+id).load( "publish?id=" + id );
}
</script>

<ul id="title">
<li>Schedules</li>
<li class="align_right"><a href="create"><img alt="[Create Schedule]"
  title="Create Schedule" src="<c:url value='/img/icons/calendar_add.png' />" /></a></li>
</ul>

<c:if test="${fn:length(schedules) > 0}">
<table class="general2 autowidth">
<thead>
  <tr><th>Term</th><th>Pre-Registration Start</th><th>Pre-Registration End</th><th></th>
</thead>
<tbody>
<c:forEach items="${schedules}" var="schedule">
<tr>
  <td><a href="view?id=${schedule.id}">${schedule.term}</a></td>
  <td class="center"><csns:publishDate itemId="${schedule.id}"
    date="${schedule.preregStart}" datePast="${schedule.preregStarted}" /></td>
  <td class="center"><csns:dueDate datePattern="yyyy-MM-dd"
    date="${schedule.preregEnd}" datePast="${schedule.preregEnded}" /></td>
  <td><a href="edit?id=${schedule.id}"><img alt="[Edit Schedule]" title="Edit Scheduile"
    src="<c:url value='/img/icons/calendar_edit.png'/>" /></a></td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
