<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}" />

<script>
$(function(){
   $("table").tablesorter({
      sortList: [[1,0]]
   });
   $(".thumbnails").click(function(){
       $(".ui-dialog-content").dialog("close");
       var downloadUrl = "<c:url value='/download.html?fileId=' />" + $(this).attr("name");
       $("<div>").append("<img src='" + downloadUrl + "' alt='' />").dialog({
           autoOpen:       true,
           height:         400,
           width:          350
       });
   });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/evaluated#section-${section.id}' />">${section.course.code}
  - ${section.number}</a></li>
<li><csns:truncate value="${assignment.name}" length="60" /></li>
</ul>

<p>Due Date: <csns:dueDate date="${assignment.dueDate.time}"
  datePast="${assignment.pastDue}" /></p>

<table class="viewtable autowidth">
<thead><tr><th></th><th>Name</th>
  <th>Instructor Evaluations</th>
  <c:if test="${assignment.evaluatedByExternal}"><th>External Evaluations</th></c:if>
  <c:if test="${assignment.evaluatedByStudents}"><th>Peer Evaluations</th></c:if>
</tr></thead>
<tbody>
  <c:forEach items="${assignment.submissions}" var="submission">
  <tr>
    <td>
      <c:if test="${not empty submission.student.profileThumbnail}">
        <img src="<c:url value='/download.html?fileId=${submission.student.profileThumbnail.id}' />"
             alt="[Profile Thumbnail]" class="thumbnails" name="${submission.student.profilePicture.id}"
             width="24" height="24" />
      </c:if>
    </td>
    <td><a href="view?id=${submission.id}">${submission.student.lastName},
        ${submission.student.firstName}</a></td>
    <td class="center">${submission.instructorEvaluationCount}</td>
    <c:if test="${assignment.evaluatedByExternal}">
      <td class="center">${submission.externalEvaluationCount}</td>
    </c:if>
    <c:if test="${assignment.evaluatedByStudents}">
      <td class="center">${submission.peerEvaluationCount}</td>
    </c:if>
  </tr>
  </c:forEach>
</tbody>
</table>
