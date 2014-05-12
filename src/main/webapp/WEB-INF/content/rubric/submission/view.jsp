<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${assignment.section}" />
<c:set var="rubric" value="${assignment.rubric}" />

<script>
$(function(){
    $("#ok").click(function(){
        window.location.href = "list?assignmentId=${assignment.id}";
    });
    $("table").tablesorter({
       sortList: [[0,0]]
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="list?assignmentId=${assignment.id}"><csns:truncate value="${assignment.name}" length="50" /></a></li>
<security:authorize access="principal.faculty">
<li><a href="<c:url value='/user/view?id=${submission.student.id}' />"><csns:truncate
  value="${submission.student.name}" length="25" /></a></li>
</security:authorize>
<security:authorize access="not principal.faculty">
<li><csns:truncate value="${submission.student.name}" length="25" /></li>
</security:authorize>
</ul>

<c:if test="${not assignment.published}">
<p>This assignment is not published yet.</p>
</c:if>

<c:if test="${assignment.published}">
<h3><a href="../evaluation/view?id=${evaluation.id}">${rubric.name}</a></h3>

<table class="general2 autowidth">
 <tr>
   <c:forEach items="${rubric.indicators}" var="indicator">
   <th>${indicator.name}</th>
   </c:forEach>
 </tr>
 <tr>
   <c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
   <td>
     <c:if test="${evaluation.ratings[status.index] >= 0}">
       ${evaluation.ratings[status.index]}
     </c:if>
   </td>
   </c:forEach>
 </tr>
</table>

<c:if test="${fn:length(submission.instructorEvaluations) > 0}">
<h4>Instructor Evaluations</h4>
<table class="general2 autowidth">
 <c:forEach items="${submission.instructorEvaluations}" var="ievaluation">
 <tr>
   <th>${ievaluation.evaluator.name}</th>
   <c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
   <td>${ievaluation.ratings[status.index]}</td>
   </c:forEach>
 </tr>
 </c:forEach>
</table>
</c:if>

<c:if test="${fn:length(submission.peerEvaluations) > 0}">
<h4>Peer Evaluations</h4>
</c:if>

<c:if test="${fn:length(submission.externalEvaluations) > 0}">
<h4>External Evaluations</h4>
</c:if>

</c:if> <%-- end of assignment.published --%>

<p><button id="ok" class="subbutton">OK</button></p>
