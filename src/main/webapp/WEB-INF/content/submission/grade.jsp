<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${submission.assignment.section}" />

<script>
$(function(){
    $("#dueDate").datetimepicker({
        inline: true,
        showSecond: true,
        timeFormat: "hh:mm:ss"
    });
    $("#dueDateLink").click(function(){
        $("#dueDateForm").toggle();
    });
    $("#dueDateForm").hide();
    $("#grade").editable( "edit", {
        submitdata: { "id": ${submission.id} },
        name: "grade",
        placeholder: "&nbsp;",
        width: 80,
        event: "dblclick",
        submit: "Save"
    });
    $("#gradeLink").click(function(){
       $("#grade").trigger("dblclick"); 
    });
    $("#comments").editable( "edit", {
        submitdata: { "id": ${submission.id} },
        name: "comments",
        placeholder: "&nbsp;",
        type: "textarea",
        rows: 10,
        event: "dblclick",
        submit: "Save"
    });
    $("#commentsLink").click(function(){
        $("#comments").trigger("dblclick"); 
    });
    $("#ok").click(function(){
        window.location.href = "list?assignmentId=${assignment.id}";
    });
    $("table").tablesorter({
       sortList: [[0,0]]
    });
});
function toggleFilePublic( fileId )
{
    $("#file-" + fileId).load("<c:url value='/file/toggleFilePublic.html?fileId=' />" + fileId);
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="list?assignmentId=${assignment.id}"><csns:truncate value="${assignment.name}" length="35" /></a></li>
<li><csns:truncate value="${submission.student.name}" length="25" /></li>
<li class="align_right"><a href="email?submissionId=${submission.id}"><img title="Email Grade" alt="[Email Grade]"
  src="<c:url value='/img/icons/email_go.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='/download?submissionId=${submission.id}' />"><img
  title="Download All Files" alt="[Download All Files]" src="<c:url value='/img/icons/download.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='add?id=${submission.id}' />"><img
  title="Upload Additional Files" alt="[Upload Additional Files]" src="<c:url value='/img/icons/upload.png' />" /></a></li>
</ul>

<p><a id="dueDateLink" href="javascript:void(0)">Due Date: </a><csns:dueDate submission="${submission}" /></p>
<form id="dueDateForm" action="edit" method="post">
<p><input id="dueDate" name="dueDate" class="leftinput" size="20" maxlength="20"
  value="<fmt:formatDate value="${submission.effectiveDueDate.time}" pattern="MM/dd/yyyy HH:mm:ss" />" />
<input type="hidden" name="id" value="${submission.id}" />
<input class="subbutton" type="submit" value="OK" /></p>
</form>

<table class="viewtable">
<thead><tr><th>Name</th><th class="shrink">Size</th><th>Date</th></tr></thead>
<tbody>
  <c:forEach items="${submission.files}" var="file">
  <tr>
    <td><a href="<c:url value='/download?fileId=${file.id}' />">${file.name}</a></td>
    <td class="shrink"><csns:fileSize value="${file.size}" /></td>
    <td class="datetime"><fmt:formatDate value="${file.date}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>

<h4><a id="gradeLink" href="javascript:void(0)">Grade</a></h4>
<div id="grade" class="editable_input">${submission.grade}</div>

<h4><a id="commentsLink" href="javascript:void(0)">Comments</a></h4>
<pre id="comments"><c:out value="${submission.comments}" escapeXml="true" /></pre>

<button id="ok" class="subbutton">OK</button>
