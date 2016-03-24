<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${assignment.section}" />
<c:set var="rubric" value="${assignment.rubric}" />

<script>
$(function(){
	$("#evaluate").click(function(){
		window.location.href = "../../evaluation/instructor/view?submissionId=${submission.id}";
	});
    $("#tabs").tabs();
    $("#chartContainer").highcharts(${chart});
    $(".comments-dialog").dialog({
        autoOpen : false,
        modal : true
    });
    $(".view-comments").click(function(){
        var evalId = $(this).attr("data-eval-id");
        $(".comments-dialog[data-eval-id='" + evalId + "']").dialog("open");
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code}
  - ${section.number}</a></li>
<li><a class="bc" href="list?assignmentId=${assignment.id}">${assignment.name}</a></li>
<security:authorize access="principal.faculty">
<li><a href="<c:url value='/user/view?id=${submission.student.id}' />">${submission.student.name}</a></li>
</security:authorize>
<security:authorize access="not principal.faculty">
<li>${submission.student.name}</li>
</security:authorize>
</ul>

<c:if test="${not assignment.published}">
<p>This assignment is not published yet.</p>
</c:if>

<c:if test="${assignment.published}">

<c:if test="${not assignment.pastDue}">
<p style="text-align: right;"><button id="evaluate" class="subbutton">Evaluate</button></p>
</c:if>

<div id="tabs">
<ul>
  <li><a href="#tab-summary">Summary</a></li>
  <li><a href="#tab-chart">Chart</a></li>
  <li><a href="#tab-all">All</a></li>
</ul>
<%@include file="_summary.jsp" %>
<%@include file="_chart.jsp" %>
<%@include file="_all.jsp" %>
</div>

</c:if> <%-- end of assignment.published --%>
