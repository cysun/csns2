<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>
<%@ attribute name="question" required="true" rtexprvalue="true" type="csns.model.qa.Question" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<li id="${question.id}"> <jsp:doBody /> 
<div class="question">${question.description}</div>
<div class="selection">
<c:choose>
  <c:when test="${question.type == 'CHOICE' and question.singleSelection }">
    <c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
      <input type="radio" 
        <c:if test="${csns:isSelectionCorrect(question,choiceStatus.index)}">checked="checked"</c:if>
      /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${question.type == 'CHOICE' and not question.singleSelection }">
    <c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
      <input type="checkbox"
        <c:if test="${csns:isSelectionCorrect(question,choiceStatus.index)}">checked="checked"</c:if>
      /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${question.type == 'RATING'}">
    ${question.minRating}
    <c:forEach begin="${question.minRating}" end="${question.maxRating}" step="1" var="rating">
      <input type="radio" />
    </c:forEach>
    ${question.maxRating}
  </c:when>
  <c:when test="${question.type == 'TEXT'}">
    <c:if test="${question.textLength <= 60}">
      <input style="width: ${question.textLength}em;" />
    </c:if>
    <c:if test="${question.textLength > 60}">
      <textarea rows="10" cols="80"></textarea>
    </c:if>
  </c:when>
</c:choose>
</div>
</li>
