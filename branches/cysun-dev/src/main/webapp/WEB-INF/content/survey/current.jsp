<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

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
<security:authorize access="authenticated and principal.isFaculty('${dept}')">
<li><a class="bc" href="list">Surveys</a></li>
</security:authorize>
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
    <td class="shrink"><a href="javascript:help('${survey.type}')"><small>${survey.type}</small></a></td>
    <td class="date"><fmt:formatDate value="${survey.closeDate.time}" pattern="MM/dd/yyyy" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>

<div id="help-ANONYMOUS" class="help">An <em>Anonymous</em> survey is open to
the public, i.e. no CSNS account is required, and the system does not keep
any information about the people who took the survey.</div>

<div id="help-RECORDED" class="help">A <em>Recorded</em> survey requires the
users to log in CSNS to take the survey. The system only records whether a
user has taken a survey or not, but does not link a survey response to a
particular user. A <em>Recored</em> survey prevents a user from taking
the survey multiple times to influence the results, and at the same time it
still preserve anonymity so users can share their honest opinions.
</div>

<div id="help-NAMED" class="help">A <em>Named</em> survey requires the users
to log in CSNS to take the survey. The system records the identity of the user
for each survey response. Named surveys may not be suitable for collecting
opinions, but they can be useful for things like voting, class pre-registration,
and so on.</div>
