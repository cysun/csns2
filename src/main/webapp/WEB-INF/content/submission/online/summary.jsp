<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}" />

<script>
$(function(){
    $("#prev").click(function(){
        window.location.href = "summary?assignmentId=${assignment.id}&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = "summary?assignmentId=${assignment.id}&sectionIndex=${sectionIndex+1}"; 
    });
    $("#ok").click(function(){
        window.location.href = "<c:url value='/submission/list?assignmentId=${assignment.id}' />"; 
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="<c:url value='/submission/list?assignmentId=${assignment.id}' />"><csns:truncate
  value="${assignment.name}" length="35" /></a></li>
<li>Submission Summary</li>
</ul>

<h3>Number of submissions: <a href="list?assignmentId=${assignment.id}">${fn:length(submissions)}</a></h3>

<div class="qa_content">
<c:if test="${assignment.questionSheet.numOfSections > 1}">
<div id="qa_section">Section <csns:romanNumber value="${sectionIndex+1}" />.</div>
</c:if>

<ol class="qa_list">
<c:forEach items="${assignment.questionSheet.sections[sectionIndex].questions}" var="question">
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
        <a href="list?assignmentId=${assignment.id}&amp;questionId=${question.id}&amp;selection=${choiceStatus.index}">${question.choiceSelections[choiceStatus.index]}</a>
      </li>
    </c:forEach>
    </ul>
  </c:when>
  <c:when test="${question.type == 'RATING'}">
    <ul>
      <c:forEach begin="${question.minRating}" end="${question.maxRating}" step="1" var="rating">
      <li>${rating}:
        <a href="list?assignmentId=${assignment.id}&amp;questionId=${question.id}&amp;rating=${rating}">${question.ratingSelections[rating-question.minRating]}</a>
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
      <li><a href="grade?answerSheetId=${answer.section.answerSheet.id}&amp;sectionIndex=${answer.section.index}">${answer.section.answerSheet.author.name}</a>:
        <c:if test="${fn:length(answer.text) <= 500}">${answer.text}</c:if>
        <c:if test="${fn:length(answer.text) > 500}"><p>... ...</p></c:if>
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
<c:if test="${sectionIndex < assignment.questionSheet.numOfSections-1}">
<button id="next" type="button" class="subbutton" >Next Section</button>
</c:if>
<button id="ok" type="button" class="subbutton">OK</button>
</p>

</div>
