<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}" />
<c:set var="questionSheet" value="${assignment.questionSheet}"/>

<script>
$(function(){
    $("#prev").click(function(){
        window.location.href = "edit?id=${assignment.id}&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = "edit?id=${assignment.id}&sectionIndex=${sectionIndex+1}"; 
    });
    $("#done").click(function(){
        window.location.href = "<c:url value='/section/taught#section-${section.id}' />"; 
    });
    $("select[name='questionType']").change(function(){
        $(this).closest("form").submit();
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="view?id=${assignment.id}">${assignment.name}</a></li>
<li>Questions</li>
</ul>

<div class="qa_content">
${questionSheet.description}

<c:if test="${questionSheet.numOfSections > 1}">
<div id="qa_section">Section <csns:romanNumber value="${sectionIndex+1}" />
<span class="qa_action"><a href="editSection?assignmentId=${assignment.id}&amp;sectionIndex=${sectionIndex}"><img
  title="Edit Section Description" alt="[Edit Section Description]"
  src="<c:url value='/img/icons/page_edit.png' />" /></a></span>
</div>
${questionSheet.sections[sectionIndex].description}
</c:if>

<ol id="sortable" class="qa_list">
<c:forEach items="${questionSheet.sections[sectionIndex].questions}" var="question">
<csns:displayQuestion question="${question}">
<div class="edit">(${question.pointValue}pt)
<a class="qa_action" href="editQuestion.html?assignmentId=${assignment.id}&amp;sectionIndex=${sectionIndex}&amp;questionId=${question.id}">Edit</a>
</div>
</csns:displayQuestion>
</c:forEach>
</ol>

<form method="get" action="addQuestion">
<input type="hidden" name="assignmentId" value="${assignment.id}" />
<input type="hidden" name="sectionIndex" value="${sectionIndex}" />
<select name="questionType" size="1">
  <option value="CHOICE" selected="selected">Choice Question</option>
  <option value="RATING">Rating Question</option>
  <option value="TEXT">Text Question</option>
</select>
<input type="submit" class="subbutton" name="add" value="Add" />
</form>

<p>
<c:if test="${sectionIndex > 0}">
  <button id="prev" type="button" class="subbutton">Previous Section</button>
</c:if>
<c:if test="${sectionIndex < questionSheet.numOfSections-1}">
  <button id="next" type="button" class="subbutton">Next Section</button>
</c:if>
<c:if test="${sectionIndex == questionSheet.numOfSections-1}">
  <button id="done" type="button" class="subbutton">Done</button>
</c:if>
</p>

</div> <!-- qa_content -->
