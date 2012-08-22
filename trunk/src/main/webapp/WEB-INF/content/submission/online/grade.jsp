<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${submission.assignment.section}" />
<c:set var="answerSheet" value="${submission.answerSheet}" />
<c:set var="questionSheet" value="${submission.answerSheet.questionSheet}" />

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
    $("#grade").editable( "<c:url value='/submission/edit' />", {
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
    $("#comments").editable( "<c:url value='/submission/edit' />", {
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
    $("#prev").click(function(){
        window.location.href = "grade?id=${submission.id}&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = "grade?id=${submission.id}&sectionIndex=${sectionIndex+1}"; 
    });
    $("#ok").click(function(){
        window.location.href = "<c:url value='/submission/list?assignmentId=${assignment.id}' />"; 
    });
});
function toggleFilePublic( fileId )
{
    $("#file-" + fileId).load("<c:url value='/file/toggleFilePublic.html?fileId=' />" + fileId);
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="<c:url value='/submission/list?assignmentId=${assignment.id}' />">${assignment.name}</a></li>
<li>${submission.student.name}</li>
<li class="align_right"><a href="email?submissionId=${submission.id}"><img title="Email Grade" alt="[Email Grade]"
  src="<c:url value='/img/icons/email_go.png' />" /></a></li>
</ul>

<p><a id="dueDateLink" href="javascript:void(0)">Due Date: </a><csns:dueDate submission="${submission}" /></p>
<form id="dueDateForm" action="<c:url value='/submission/edit' />" method="post">
<p><input id="dueDate" name="dueDate" class="leftinput" size="20" maxlength="20"
  value="<fmt:formatDate value="${submission.effectiveDueDate.time}" pattern="MM/dd/yyyy HH:mm:ss" />" />
<input type="hidden" name="id" value="${submission.id}" />
<input class="subbutton" type="submit" value="OK" /></p>
</form>

<h4><a id="gradeLink" href="javascript:void(0)">Grade</a></h4>
<div id="grade" class="editable_input">${submission.grade}</div>

<h4><a id="commentsLink" href="javascript:void(0)">Comments</a></h4>
<pre id="comments"><c:out value="${submission.comments}" escapeXml="true" /></pre>

<div class="qa_content">
${questionSheet.description}

<c:if test="${questionSheet.numOfSections > 1}">
<div id="qa_section">Section <csns:romanNumber value="${sectionIndex+1}" /></div>
${questionSheet.sections[sectionIndex].description}
</c:if>

<ol>
<c:forEach items="${answerSheet.sections[sectionIndex].answers}" var="answer">
<csns:displayAnswer answer="${answer}" />
</c:forEach>
</ol>

<p>
<c:if test="${sectionIndex > 0}">
  <button id="prev" type="button" class="subbutton">Previous Section</button>
</c:if>
<c:if test="${sectionIndex < answerSheet.numOfSections-1}">
  <button id="next" type="button" class="subbutton">Next Section</button>
</c:if>
<c:if test="${sectionIndex == answerSheet.numOfSections-1}">
  <button id="ok" type="button" class="subbutton">OK</button>
</c:if>
</p>
</div>
