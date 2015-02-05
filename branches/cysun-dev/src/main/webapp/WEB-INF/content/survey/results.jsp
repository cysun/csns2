<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#prev").click(function(){
        window.location.href = "results?id=${survey.id}&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = "results?id=${survey.id}&sectionIndex=${sectionIndex+1}"; 
    });
    $("#ok").click(function(){
        window.location.href = "list"; 
    });
});
</script>

<ul id="title">
<li><a class="bc" href="list">Surveys</a></li>
<li><a class="bc" href="view?id=${survey.id}&amp;sectionIndex=${sectionIndex}"><csns:truncate
  value="${survey.name}" length="65" /></a></li>
<li>Result Summary</li>
<li class="align_right"><a href="results?id=${survey.id}&amp;export=excel"><img title="Export to Excel"
    alt="[Export to Excel]" src="<c:url value='/img/icons/export_excel.png' />" /></a></li>
</ul>

<h3>Number of responses: <a href="response/list?surveyId=${survey.id}">${survey.numOfResponses}</a></h3>

<div class="qa_content">
<c:if test="${survey.questionSheet.numOfSections > 1}">
<div id="qa_section">Section <csns:romanNumber value="${sectionIndex+1}" />.</div>
</c:if>

<ol class="qa_list">
<c:forEach items="${survey.questionSheet.sections[sectionIndex].questions}" var="question">
<li>
  <div class="question">${question.description}</div>
  <div class="selection">
  <c:if test="${question.numOfAnswers == 0}">
    No response for this question yet.
  </c:if>
  <c:if test="${question.numOfAnswers > 0}">
<c:choose>
  <c:when test="${question.type == 'CHOICE'}">
    <ul>
    <c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
      <li>
        <c:out value="${choice}" escapeXml="true" />
        <a href="response/list?surveyId=${survey.id}&amp;questionId=${question.id}&amp;selection=${choiceStatus.index}">${question.choiceSelections[choiceStatus.index]}</a>
      </li>
    </c:forEach>
    </ul>
  </c:when>
  <c:when test="${question.type == 'RATING'}">
    <ul>
      <c:forEach begin="${question.minRating}" end="${question.maxRating}" step="1" var="rating">
      <li>${rating}:
        <a href="response/list?surveyId=${survey.id}&amp;questionId=${question.id}&amp;rating=${rating}">${question.ratingSelections[rating-question.minRating]}</a>
      </li>
      </c:forEach>
      <li>Average: <b><fmt:formatNumber value="${question.ratingStats.average}"
                        minFractionDigits="2" maxFractionDigits="2" /></b></li>
      <li>Median: <b><fmt:formatNumber value="${question.ratingStats.median}"
                        minFractionDigits="2" maxFractionDigits="2" /></b></li>
    </ul>
  </c:when>
  <c:when test="${question.type == 'TEXT'}">
    <ul>
      <c:forEach items="${question.answers}" var="answer">
      <c:if test="${not empty answer.text}">
      <li><a href="response/view?answerSheetId=${answer.section.answerSheet.id}&amp;sectionIndex=${answer.section.index}"><c:if
          test="${survey.type == 'NAMED'}">${answer.section.answerSheet.author.name}</c:if><c:if
          test="${survey.type != 'NAMED'}">${answer.section.answerSheet.id}</c:if></a>:
        ${answer.text}
      </li>
      </c:if>
      </c:forEach>
    </ul>
  </c:when>
</c:choose>
  </c:if>
  </div>
</li>
</c:forEach>
</ol>

<p>
<c:if test="${sectionIndex > 0}">
<button id="prev" type="button" class="subbutton">Previous Section</button>
</c:if>
<c:if test="${sectionIndex < survey.questionSheet.numOfSections-1}">
<button id="next" type="button" class="subbutton" >Next Section</button>
</c:if>
<button id="ok" type="button" class="subbutton">OK</button>
</p>

</div>
