<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${submission.assignment.section}" />
<c:set var="answerSheet" value="${submission.answerSheet}" />
<c:set var="questionSheet" value="${submission.answerSheet.questionSheet}" />

<script>
$(function(){
    var url = "viewSubmission?id=${submission.id}&enrollmentId=${param.enrollmentId}";
    $("#prev").click(function(){
        window.location.href = url + "&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = url + "&sectionIndex=${sectionIndex+1}"; 
    });
});
</script>

<ul id="title">
<li><a href="list" class="bc">Course Journals</a></li>
<li><a href="view?id=${section.journal.id}#students"
       class="bc">${section.course.code}</a></li>
<li><a href="viewStudent?enrollmentId=${param.enrollmentId}"
       class="bc">${submission.student.name}</a></li>
<li>${submission.assignment.name}</li>
</ul>

<h4>Grade</h4>
<div style="margin-left: 20px;">${submission.grade}</div>

<h4>Comments</h4>
<pre><c:out value="${submission.comments}" escapeXml="true" /></pre>

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
</p>
</div>
