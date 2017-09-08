<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${assignment.section}" />
<c:set var="rubric" value="${assignment.rubric}" />

<script>
$(function(){
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
<li><a href="list" class="bc">Course Journals</a></li>
<li><a href="view?id=${section.journal.id}#students"
       class="bc">${section.course.code}</a></li>
<li><a href="viewStudent?enrollmentId=${param.enrollmentId}"
       class="bc">${submission.student.name}</a></li>
<li>${submission.assignment.name}</li>
</ul>

<c:if test="${not assignment.published}">
<p>This assignment is not published yet.</p>
</c:if>

<c:if test="${assignment.published}">

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

</c:if>
