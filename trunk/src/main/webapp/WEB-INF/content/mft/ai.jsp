<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $('#date').datepicker({
        inline: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-10:+00"
    });
	$(".op").hide();
	$("#edit").click(function(){
		$(".op").toggle();
	});
});
function deleteIndicator( id )
{
    var msg = "Are you sure you want to delete this set of indicators?";
    if( confirm(msg) )
        window.location.href = "ai/delete?id=" + id;
}
</script>

<ul id="title">
<li><a class="bc" href="overview">MFT</a></li>
<li>Assessment Indicators</li>
<li class="align_right"><a id="edit" href="javascript:void(0)"><img alt="[Edit]"
  title="Edit" src="<c:url value='/img/icons/table_edit.png' />" /></a></li>
</ul>

<p>MFT Assessment Indicators:</p>
<ul>
  <li>AI-1: Programming</li>
  <li>AI-2: Discrete Structures and Algorithms</li>
  <li>AI-3: Systems (Architecture, Operating Systems, Networking, Database)</li>
</ul>

<p>For each indicator the table shows two numbers. The first number is the mean
score of the students for that indicator. This score is measured in the percentage
of the questions that are answered correctly. The second number is the national
percentile the institution is in based on the mean score of the students.</p>

<form:form modelAttribute="indicator" action="ai/update">
<table class="viewtable autowidth">
  <tr><th>Date</th><th>AI-1</th><th>AI-2</th><th>AI-3</th><th class="op"></th></tr>
  <tr class="op">
    <td><form:input path="date" cssClass="forminput" cssStyle="width: 94px;"
                    placeholder="MM/DD/YYYY" required="true" /></td>
    <td><form:input path="ai1" cssClass="smallerinput" required="true" /></td>
    <td><form:input path="ai2" cssClass="smallerinput" required="true" /></td>
    <td><form:input path="ai3" cssClass="smallerinput" required="true" /></td>
    <td><input type="submit" name="add" value="Add or Update" class="subbutton" /></td>
  </tr>
  <c:forEach items="${indicators}" var="indicator">
  <tr>
    <td><fmt:formatDate pattern="yyyy-MM-dd" value="${indicator.date}" /></td>
    <td>${indicator.ai1} (${indicator.ai1Percentile})</td>
    <td>${indicator.ai2} (${indicator.ai2Percentile})</td>
    <td>${indicator.ai3} (${indicator.ai3Percentile})</td>
    <td class="op"><a href="javascript:deleteIndicator(${indicator.id})"><img title="Delete"
      alt="[Delete]" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </tr>
  </c:forEach>
</table>
</form:form>
