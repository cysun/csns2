<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}" />

<script>
function testLocalStorage(){
    try
    {
        localStorage.setItem("test", "test");
        localStorage.removeItem("test");
        return true;
    }
    catch(e) {
        return false;
    }
}
function saveSubmissionIds(){
    if( ! testLocalStorage() ) return;

    var submissionIds = [];
    $(".submission").each(function(){
        submissionIds.push($(this).attr("data-submission-id"));
    });
    localStorage.setItem( "assignmentId", "${assignment.id}" );
    localStorage.setItem( "submissionIds", submissionIds.join() );
}
$(function(){
   $("table").tablesorter({
       sortList: [[1,0]]
   });
   $("table").bind("sortEnd", function(){
       saveSubmissionIds();
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
   saveSubmissionIds();
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><csns:truncate value="${assignment.name}" length="60" /></li>
<li class="align_right"><a href="email?assignmentId=${assignment.id}"><img title="Email Grades" alt="[Email Grades]"
  src="<c:url value='/img/icons/email_go.png' />" /></a></li>
<c:if test="${not assignment.online}">
  <li class="align_right"><a href="<c:url value='/download?assignmentId=${assignment.id}' />"><img
    title="Download All Files" alt="[Download All Files]" src="<c:url value='/img/icons/download.png' />" /></a></li>
</c:if>
<c:if test="${assignment.online and assignment.pastDue}">
  <li class="align_right"><a href="online/autograde?assignmentId=${assignment.id}"><img
    title="Auto Grade" alt="[Auto Grade]" src="<c:url value='/img/icons/table_multiple_check.png' />" /></a></li>
</c:if>
<c:if test="${assignment.online and assignment.published}">
  <li class="align_right"><a href="online/summary?assignmentId=${assignment.id}"><img
    title="Submission Summary" alt="[Submission Summary]" src="<c:url value='/img/icons/table_multiple.png' />" /></a></li>
</c:if>
</ul>

<p>Due Date: <csns:dueDate date="${assignment.dueDate.time}"
  datePast="${assignment.pastDue}" /></p>

<c:if test="${not empty assignment.totalPoints}">
<p>Total points: ${assignment.totalPoints}</p>
</c:if>

<table class="viewtable autowidth">
<thead><tr><th></th><th>Name</th><c:if test="${not assignment.online}"><th># of Files</th></c:if><th>Grade</th></tr></thead>
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
    <td class="submission" data-submission-id="${submission.id}">
      <c:if test="${not empty submission.grade and not submission.gradeMailed}"><img
        src="<c:url value='/img/icons/email.png' />" alt="[Email]" /></c:if>
      <c:if test="${not assignment.online}">
        <a href="grade?id=${submission.id}">${submission.student.lastName}, ${submission.student.firstName}</a>
      </c:if>
      <c:if test="${assignment.online}">
        <a href="online/grade?id=${submission.id}">${submission.student.lastName}, ${submission.student.firstName}</a>
      </c:if>
    </td>
    <c:if test="${not assignment.online}">
    <td class="center">${submission.fileCount}</td>
    </c:if>
    <td class="center">${submission.grade}</td>
  </tr>
  </c:forEach>
</tbody>
</table>
