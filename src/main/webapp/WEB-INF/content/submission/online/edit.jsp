<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${submission.assignment.section}" />
<c:set var="answerSheet" value="${submission.answerSheet}" />

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taken#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><csns:truncate value="${assignment.name}" length="70" /></li>
</ul>

<div class="qa_content">
<div>${answerSheet.questionSheet.description}</div>
<c:if test="${answerSheet.numOfSections > 1}">
<div id="qa_section">Section <csns:romanNumber value="${sectionIndex+1}" /></div>
${answerSheet.questionSheet.sections[sectionIndex].description}
</c:if>
</div>

<form:form modelAttribute="submission">
<ol class="qa_list">
<c:forEach items="${answerSheet.sections[sectionIndex].answers}" var="answer" varStatus="answerStatus">
<li>
<div class="question"><div class="qa_content">${answer.question.description}</div></div>
<div class="selection">
<c:choose>
  <c:when test="${answer.question.type == 'CHOICE' and answer.question.singleSelection }">
    <c:forEach items="${answer.question.choices}" var="choice" varStatus="choiceStatus">
      <form:radiobutton path="answerSheet.sections[${sectionIndex}].answers[${answerStatus.index}].selections"
                        value="${choiceStatus.index}" /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${answer.question.type == 'CHOICE' and not answer.question.singleSelection }">
    <c:forEach items="${answer.question.choices}" var="choice" varStatus="choiceStatus">
      <form:checkbox path="answerSheet.sections[${sectionIndex}].answers[${answerStatus.index}].selections"
                        value="${choiceStatus.index}" /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${answer.question.type == 'RATING'}">
    ${answer.question.minRating}
    <c:forEach begin="${answer.question.minRating}" end="${answer.question.maxRating}" step="1" var="rating">
      <form:radiobutton path="answerSheet.sections[${sectionIndex}].answers[${answerStatus.index}].rating" value="${rating}" />
    </c:forEach>
    ${answer.question.maxRating}
  </c:when>
  <c:when test="${answer.question.type == 'TEXT'}">
    <c:if test="${answer.question.textLength <= 60}">
      <form:input path="answerSheet.sections[${sectionIndex}].answers[${answerStatus.index}].text"
                  cssStyle="width: ${answer.question.textLength}em;" />
    </c:if>
    <c:if test="${answer.question.textLength > 60}">
      <form:textarea path="answerSheet.sections[${sectionIndex}].answers[${answerStatus.index}].text" rows="10" cols="80" />
    </c:if>
  </c:when>
</c:choose>
</div>
</li>
</c:forEach>
</ol>

<input type="hidden" name="sectionIndex" value="${sectionIndex}" />

<div style="margin-top: 20px;">
<input type="submit" class="subbutton" name="save" value="Save" />
<c:if test="${sectionIndex > 0}">
  <input type="submit" class="subbutton" name="prev" value="Previous Section" />
</c:if>
<c:if test="${sectionIndex < answerSheet.numOfSections-1}">
  <input type="submit" class="subbutton" name="next" value="Next Section" />
</c:if>
<c:if test="${sectionIndex == answerSheet.numOfSections-1}">
  <input type="submit" class="subbutton" name="finish" value="Finish" />
</c:if>
</div>
</form:form>

<script type="text/javascript">
  CKEDITOR.replaceAll();
</script>
