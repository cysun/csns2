<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("table").tablesorter();
    $("#selectAll").click(function(){
        var checked = $("#selectAll").is(":checked");
        $(":checkbox[name='userId']").prop("checked",checked);
    });
    $("#email").click(function(){
        if( $(":checkbox[name='userId']:checked").length == 0 )
            alert( "Please select the respondent(s) to contact." );
        else
            $("#respondentsForm").attr("action", "<c:url value='/email/compose' />").submit();
    });
});
</script>

<ul id="title">
<li><a class="bc" href="../list">Surveys</a></li>
<li><a class="bc" href="../results?id=${survey.id}"><csns:truncate
  value="${survey.name}" length="55" /></a></li>
<li>List of Responses</li>
<c:if test="${survey.type == 'NAMED'}">
<li class="align_right"><a id="email" href="javascript:void(0)"><img title="Email Respondents"
    alt="[Email Respondents]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
</c:if>
</ul>

<h3>Number of responses: ${fn:length(answerSheets)}</h3>

<c:if test="${not empty question}">
<div>${question.description}</div>
<p>
<c:choose>
  <c:when test="${question.type == 'CHOICE' and question.singleSelection }">
    <c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
      <input type="radio" 
        <c:if test="${choiceStatus.index == param.selection}">checked="checked"</c:if>
      /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${question.type == 'CHOICE' and not question.singleSelection }">
    <c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
      <input type="checkbox"
        <c:if test="${choiceStatus.index == param.selection}">checked="checked"</c:if>
      /> <c:out value="${choice}" escapeXml="true" /> <br />
    </c:forEach>
  </c:when>
  <c:when test="${question.type == 'RATING'}">
    ${question.minRating}
    <c:forEach begin="${question.minRating}" end="${question.maxRating}" step="1" var="rating">
      <input type="radio" <c:if test="${rating == param.rating}">checked="checked"</c:if> />
    </c:forEach>
    ${question.maxRating}
  </c:when>
</c:choose>
</p>
</c:if>


<c:if test="${fn:length(answerSheets) > 0}">
<form id="respondentsForm" method="post">
<table class="viewtable autowidth">
<thead>
  <tr>
<c:if test="${survey.type == 'NAMED'}">
    <th><input id="selectAll" type="checkbox" /></th><th>CIN</th><th>Name</th>
</c:if>
    <th>Response ID</th><th>Timestamp</th>
  </tr>
</thead>
<tbody>
<c:forEach items="${answerSheets}" var="answerSheet">
  <tr>
<c:if test="${survey.type == 'NAMED'}">
    <td class="center"><input type="checkbox" name="userId" value="${answerSheet.author.id}" /></td>
    <td>${answerSheet.author.cin}</td>
    <td>${answerSheet.author.name}</td>
</c:if>
    <td><a href="view?answerSheetId=${answerSheet.id}">${answerSheet.id}</a></td>
    <td>
      <fmt:formatDate value="${answerSheet.date}" pattern="yyyy-MM-dd hh:mm:ss a" />
    </td>
  </tr>
</c:forEach>
</tbody>
</table>
<input type="hidden" name="backUrl" value="/department/${dept}/survey/results?id=${survey.id}" />
</form>
</c:if>
