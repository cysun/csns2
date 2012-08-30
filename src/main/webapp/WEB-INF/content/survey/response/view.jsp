<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="survey" value="${response.survey}" />
<c:set var="answerSheet" value="${response.answerSheet}" />

<script>
$(function(){
    $("#prev").click(function(){
        window.location.href = "view?answerSheetId=${answerSheet.id}&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = "view?answerSheetId=${answerSheet.id}&sectionIndex=${sectionIndex+1}"; 
    });
    $("#ok").click(function(){
        window.location.href = "../results?id=${survey.id}&sectionIndex=${sectionIndex}"; 
    });
});
</script>

<ul id="title">
<li><a class="bc" href="../list">Surveys</a></li>
<li><a class="bc" href="../results?id=${survey.id}"><csns:truncate
  value="${survey.name}" length="65" /></a></li>
<li>
<c:if test="${empty answerSheet.author}">Response #${answerSheet.id}</c:if>
<c:if test="${not empty answerSheet.author}"><csns:truncate
  value="${answerSheet.author.name}" length="25" /></c:if>
</li>
</ul>

<div class="qa_content">
<c:if test="${answerSheet.numOfSections > 1}">
<div id="qa_section">Section <csns:romanNumber value="${sectionIndex+1}" />.</div>
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
<button id="ok" type="button" class="subbutton">OK</button>
</p>
</div>
