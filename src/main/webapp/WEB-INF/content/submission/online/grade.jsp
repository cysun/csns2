<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${submission.assignment.section}" />
<c:set var="answerSheet" value="${submission.answerSheet}" />
<c:set var="questionSheet" value="${submission.answerSheet.questionSheet}" />

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
    $("#grade").editable( "<c:url value='/submission/edit' />", {
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
    $("#comments").editable( "<c:url value='/submission/edit' />", {
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
    $("#prev").click(function(){
        window.location.href = "grade?id=${submission.id}&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = "grade?id=${submission.id}&sectionIndex=${sectionIndex+1}"; 
    });
    $("#ok").click(function(){
        window.location.href = "<c:url value='/submission/list?assignmentId=${assignment.id}' />"; 
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
    $("#file-" + fileId).load("<c:url value='/file/toggleFilePublic.html?fileId=' />" + fileId);
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="<c:url value='/submission/list?assignmentId=${assignment.id}' />"><csns:truncate
  value="${assignment.name}" length="35" /></a></li>
<security:authorize access="principal.faculty">
<li><a href="<c:url value='/user/view?id=${submission.student.id}' />"><csns:truncate
  value="${submission.student.name}" length="25" /></a></li>
</security:authorize>
<security:authorize access="not principal.faculty">
<li><csns:truncate value="${submission.student.name}" length="25" /></li>
</security:authorize>
<li class="align_right"><a href="../email?submissionId=${submission.id}"><img title="Email Grade" alt="[Email Grade]"
  src="<c:url value='/img/icons/email_go.png' />" /></a></li>
<c:if test="${submission.pastDue}">
  <li class="align_right"><a href="autograde?id=${submission.id}"><img
    title="Auto Grade" alt="[Auto Grade]" src="<c:url value='/img/icons/table_check.png' />" /></a></li>
</c:if>
</ul>

<p><a id="dueDateLink" href="javascript:void(0)">Due Date: </a><csns:dueDate
  date="${submission.effectiveDueDate.time}" datePast="${submission.pastDue}" />
<span style="float: right; margin-bottom: 5px;">
<button class="prev subbutton">Prev</button>
<button class="next subbutton">Next</button>
</span> 
</p>
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

<c:if test="${not empty answerSheet and (submission.saved or submission.finished)}">
<div class="qa_content">
${questionSheet.description}

<c:if test="${questionSheet.numOfSections > 1}">
<div id="qa_section">Section <csns:romanNumber value="${sectionIndex+1}" /></div>
${questionSheet.sections[sectionIndex].description}
</c:if>

<ol class="qa_list">
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
<span style="float: right;">
<button class="prev subbutton">Prev</button>
<button class="next subbutton">Next</button>
</span> 
</p>
</div>
</c:if>
