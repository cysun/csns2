<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>
<%@ attribute name="answer" required="true" rtexprvalue="true" type="csns.model.qa.Answer" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<c:set var="question" value="${answer.question}" />
<li>
<div class="question">${question.description}</div><jsp:doBody />
<div class="selection">
<c:choose>
  <c:when test="${question.type == 'CHOICE' and question.singleSelection }">
    <c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
      <input type="radio" 
        <c:if test="${csns:isChoiceSelected(answer,choiceStatus.index)}">checked="checked"</c:if>
      /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${question.type == 'CHOICE' and not question.singleSelection }">
    <c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
      <input type="checkbox"
        <c:if test="${csns:isChoiceSelected(answer,choiceStatus.index)}">checked="checked"</c:if>
      /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${question.type == 'RATING'}">
    ${question.minRating}
    <c:forEach begin="${question.minRating}" end="${question.maxRating}" step="1" var="rating">
      <input type="radio"
        <c:if test="${answer.rating == rating}">checked="checked"</c:if>
      />
    </c:forEach>
    ${question.maxRating}
  </c:when>
  <c:when test="${question.type == 'TEXT'}">
    <div class="display_answer">${answer.text}</div>
  </c:when>
</c:choose>
</div>
</li>
