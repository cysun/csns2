<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("#editStandingForm").hide();

    $("#editStandingLink").click( function() {
        $("#editStandingForm").toggle();
    });

    var currentYear = (new Date()).getFullYear();
    for( var i=0 ; i < 10 ; ++i )
        $("select[name='year']").append( $("<option></option>").attr("value", currentYear-i).text(currentYear-i) ); 
});

function deleteStanding( id )
{
    var msg = "Are you sure you want to delete this standing?";
    if( confirm(msg) )
        window.location.href = "standing/delete?id=" + id;
}
</script>

<div> <!-- for tab -->

<c:if test="${fn:length(currentStandings) == 0}">
<p>No standing information on record.</p>
</c:if>

<c:if test="${fn:length(currentStandings) > 0}">
<h4>Current Standings</h4>
<ul>
  <c:forEach items="${currentStandings}" var="currentStanding">
  <li>${currentStanding.standing.symbol} - ${currentStanding.standing.name} in ${currentStanding.department.name}</li>
  </c:forEach>
</ul>

<h4>Standing History</h4>
<table class="viewtable autowidth">
  <tr>
    <th>Department</th><th>Standing</th><th>Quarter</th>
    <c:if test="${isFaculty}"><th></th></c:if>
  </tr>
  <c:forEach items="${academicStandings}" var="academicStanding">
  <tr id="standing-${academicStanding.id}">
    <td>${academicStanding.department.name}</td>
    <td>${academicStanding.standing.symbol}</td>
    <td>${academicStanding.quarter}</td>
    <c:if test="${isFaculty}">
    <td class="center">
      <security:authorize access="principal.isFaculty('${academicStanding.department.abbreviation}')">
      <a href="javascript:deleteStanding(${academicStanding.id})"><img title="Delete Standing"
         alt="[Delete Standing]" src="<c:url value='/img/icons/delete.png' />" /></a>
      </security:authorize>
    </td>
    </c:if>
  </tr>
  </c:forEach>
</table>
</c:if>

<security:authorize access="principal.isFaculty('${dept}')">
<p><a id="editStandingLink" href="javascript:void(0)">Edit</a></p>

<form id="editStandingForm" action="standing/edit" method="post">
<p>${department.name}</p>
  <select name="standingId">
  <c:forEach items="${standings}" var="standing">
    <option value="${standing.id}">${standing.symbol}</option>
  </c:forEach>
  </select>
  <select name="year"></select>
  <select name="quarterSuffix">
    <option value="9">Fall</option>
    <option value="1">Winter</option>
    <option value="3">Spring</option>
    <option value="6">Summer</option>
  </select>
  <input type="hidden" name="departmentId" value="${department.id}" />
  <input type="hidden" name="userId" value="${user.id}" />
  <input type="submit" name="submit" class="subbutton" value="OK" />
</form>
</security:authorize>

</div>
