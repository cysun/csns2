<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}" />
<c:set var="questionSheet" value="${assignment.questionSheet}"/>

<script>
$(function(){
    $("#prev").click(function(){
        window.location.href = "view?id=${assignment.id}&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = "view?id=${assignment.id}&sectionIndex=${sectionIndex+1}"; 
    });
    $("#ok").click(function(){
        window.location.href = "<c:url value='/section/taught#section-${section.id}' />"; 
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><csns:truncate value="${assignment.name}" /></li>
<li class="align_right"><a href="../edit?id=${assignment.id}"><img title="Edit Assignment"
  alt="[Edit Assignment]" src="<c:url value='/img/icons/script_edit.png' />" /></a></li>
</ul>

<div class="qa_content">
${questionSheet.description}

<c:if test="${questionSheet.numOfSections > 1}">
<div id="qa_section">Section <csns:romanNumber value="${sectionIndex+1}" /></div>
${questionSheet.sections[sectionIndex].description}
</c:if>

<ol class="qa_list">
<c:forEach items="${questionSheet.sections[sectionIndex].questions}" var="question">
<csns:displayQuestion question="${question}">
<div class="edit">(${question.pointValue}pt)</div>
</csns:displayQuestion>
</c:forEach>
</ol>

<p>
<c:if test="${sectionIndex > 0}">
  <button id="prev" type="button" class="subbutton">Previous Section</button>
</c:if>
<c:if test="${sectionIndex < questionSheet.numOfSections-1}">
  <button id="next" type="button" class="subbutton">Next Section</button>
</c:if>
<c:if test="${sectionIndex == questionSheet.numOfSections-1}">
  <button id="ok" type="button" class="subbutton">OK</button>
</c:if>
</p>
</div>
