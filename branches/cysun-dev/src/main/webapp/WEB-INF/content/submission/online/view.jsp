<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${submission.assignment.section}" />
<c:set var="answerSheet" value="${submission.answerSheet}" />
<c:set var="questionSheet" value="${submission.answerSheet.questionSheet}" />

<script>
$(function(){
    $("#prev").click(function(){
        window.location.href = "view?assignmentId=${assignment.id}&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = "view?assignmentId=${assignment.id}&sectionIndex=${sectionIndex+1}"; 
    });
    $("#ok").click(function(){
        window.location.href = "<c:url value='/section/taken#section-${section.id}' />"; 
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taken#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>${submission.assignment.name}</li>
</ul>

<p>Due Date: <csns:dueDate date="${submission.effectiveDueDate.time}"
  datePast="${submission.pastDue}" /></p>

<c:if test="${not empty assignment.totalPoints}">
<p>Total points: ${assignment.totalPoints}</p>
</c:if>

<c:if test="${submission.gradeMailed}">
<h4>Grade</h4>
<div class="editable_input">${submission.grade}</div>
<h4>Comments</h4>
<pre><c:out value="${submission.comments}" escapeXml="true" /></pre>
</c:if>

<c:if test="${assignment.availableAfterDueDate}">
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
</c:if>
