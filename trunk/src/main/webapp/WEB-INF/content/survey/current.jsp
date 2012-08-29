<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function(){
    $(".help").dialog({
        autoOpen: false,
        modal: true
    });
});
function help( type )
{
    $("#help-" + type).dialog("open");
}
</script>
<ul id="title">
<li><a class="bc" href="<c:url value='/department/${department.abbreviation}/' />">${department.name}</a></li>
<li><a class="bc" href="list">Surveys</a></li>
<li>Open Surveys</li>
</ul>

<c:if test="${fn:length(surveys) == 0}">
  <p>Currently there are no open surveys.</p>
</c:if>

<c:if test="${fn:length(surveys) > 0 }">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th class="shrink">Type</th><th class="date">Close Date</th></tr>
</thead>
<tbody>
  <c:forEach items="${surveys}" var="survey">
  <tr>
    <td><a href="response/edit?surveyId=${survey.id}">${survey.name}</a></td>
    <td class="shrink"><a href="javascript:help('${survey.type}')">${survey.type}</a></td>
    <td class="date"><fmt:formatDate value="${survey.closeDate.time}" pattern="MM/dd/yyyy" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>

<div id="help-Anonymous" class="help">An <em>Anonymous</em> survey is open to
the public, i.e. no CSNS account is required, and the system does not keep
any information about the people who take the survey.</div>

<div id="help-Recorded" class="help">A <em>Recorded</em> survey requires the
users to log in CSNS to take the survey, but the system only records whether a
user has taken a survey or not, i.e. a survey response is not connected to a
particular user so certain level of anonymity is preserved.</div>

<div id="help-Named" class="help">A <em>named</em> survey requires the users
to log in CSNS to take the survey. The system records the identity of the user
for each survey response.</div>
