<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${submission.assignment.section}" />

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
$(function(){
    $("#dueDate").datetimepicker({
        timeFormat: "HH:mm:ss"
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
        submit: "Save",
        onblur: "submit"
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
        submit: "Save",
        onblur: "submit"
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
    $(".prev,.next").hide();
    if( testLocalStorage() && localStorage.getItem("submissionIds") != null
            && localStorage.getItem("assignmentId") == "${assignment.id}" )
    {
        var submissionIds = localStorage.getItem("submissionIds").split(",");
        var currentIndex = submissionIds.indexOf("${submission.id}");
        if( currentIndex > 0 )
          $(".prev").show().click(function(){
              window.location.href = "grade?id=" + submissionIds[currentIndex-1];
          });
        if( currentIndex >=0 && currentIndex < submissionIds.length-1 )
          $(".next").show().click(function(){
              window.location.href = "grade?id=" + submissionIds[currentIndex+1];
          });
    }
});
function toggleFilePublic( fileId )
{
    $.ajaxSetup({ cache: false });
    $("#file-" + fileId).load("<c:url value='/file/toggle?id=' />" + fileId);
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="list?assignmentId=${assignment.id}"><csns:truncate value="${assignment.name}" length="35" /></a></li>
<security:authorize access="principal.faculty">
<li><a href="<c:url value='/user/view?id=${submission.student.id}' />"><csns:truncate
  value="${submission.student.name}" length="25" /></a></li>
</security:authorize>
<security:authorize access="not principal.faculty">
<li><csns:truncate value="${submission.student.name}" length="25" /></li>
</security:authorize>
<li class="align_right"><a href="email?submissionId=${submission.id}"><img title="Email Grade" alt="[Email Grade]"
  src="<c:url value='/img/icons/email_go.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='/download?submissionId=${submission.id}' />"><img
  title="Download All Files" alt="[Download All Files]" src="<c:url value='/img/icons/download.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='add?id=${submission.id}' />"><img
  title="Add/Remove Files" alt="[Add/Remove Files]" src="<c:url value='/img/icons/upload.png' />" /></a></li>
</ul>

<p><a id="dueDateLink" href="javascript:void(0)">Due Date: </a><csns:dueDate
  date="${submission.effectiveDueDate.time}" datePast="${submission.pastDue}" />
<span style="float: right; margin-bottom: 5px;">
<button class="prev subbutton">Prev</button>
<button class="next subbutton">Next</button>
</span> 
</p>
<form id="dueDateForm" action="edit" method="post">
<p><input id="dueDate" name="dueDate" class="leftinput" size="20" maxlength="20"
  value="<fmt:formatDate value="${submission.effectiveDueDate.time}" pattern="MM/dd/yyyy HH:mm:ss" />" />
<input type="hidden" name="id" value="${submission.id}" />
<input class="subbutton" type="submit" value="OK" /></p>
</form>

<c:if test="${fn:length(submission.files) > 0}">
<table class="viewtable">
<thead><tr><th>Name</th><th class="shrink">Size</th><th>Date</th></tr></thead>
<tbody>
  <c:forEach items="${submission.files}" var="file">
    <c:choose>
      <c:when test="${file.isPublic()}"><c:set var="img" value="open_book.png" /></c:when>
      <c:otherwise><c:set var="img" value="closed_book.png" /></c:otherwise>
    </c:choose>
  <tr>
    <td>
      <span style="margin-right: 0.5em;"><a id="file-${file.id}"
            href="javascript:toggleFilePublic(${file.id})"><img border="0" alt="[*]" title="Toggle Public/Private"
            src="<c:url value='/img/icons/${img}' />" /></a></span>
      <a href="<c:url value='/download?fileId=${file.id}' />">${file.name}</a>
    </td>
    <td class="shrink"><csns:fileSize value="${file.size}" /></td>
    <td class="datetime"><fmt:formatDate value="${file.date}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>

<h4><a id="gradeLink" href="javascript:void(0)">Grade</a></h4>
<div id="grade" class="editable_input">${submission.grade}</div>

<h4><a id="commentsLink" href="javascript:void(0)">Comments</a></h4>
<pre id="comments"><c:out value="${submission.comments}" escapeXml="true" /></pre>

<p>
<button id="ok" class="subbutton">OK</button>
<span style="float: right;">
<button class="prev subbutton">Prev</button>
<button class="next subbutton">Next</button>
</span> 
</p>
