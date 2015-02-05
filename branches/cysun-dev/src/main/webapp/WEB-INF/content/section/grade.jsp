<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${enrollment.section}" />

<script>
$(function(){
<c:if test="${not enrollment.gradeMailed}">
    $("#grade").addClass("bold");
</c:if>
    $("#grade").editable( "grade", {
        submitdata: {"enrollmentId": ${enrollment.id} },
        name: "gradeId",
        placeholder: "&nbsp;",
        type: "select",
        data: "${grades}",
        style: "width: 150px;",
        event: "dblclick",
        submit: "Save",
        onblur: "submit"
    });
    $("#gradeLink").click(function(){
       $("#grade").trigger("dblclick"); 
    });
    $("#comments").editable( "grade", {
        submitdata: { "enrollmentId": ${enrollment.id} },
        name: "comments",
        placeholder: "&nbsp;",
        type: "textarea",
        rows: 10,
        event: "dblclick",
        submit: "Save",
        onblur: "submit"
    });
    $("#commentsLink").click(function(){
        $("#comments").trigger("dblclick"); 
    });
    $("#ok").click(function(){
        window.location.href = "roster?id=${section.id}";
    });
    $("table").tablesorter({
    	sortList: [[0,0]]
    });
});
</script>

<ul id="title">
<li><a class="bc" href="taught#section-${section.id}">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="roster?id=${section.id}">Students</a></li>
<security:authorize access="principal.faculty">
<li><a href="<c:url value='/user/view?id=${enrollment.student.id}' />">${enrollment.student.name}</a></li>
</security:authorize>
<security:authorize access="not principal.faculty">
<li>${enrollment.student.name}</li>
</security:authorize>
<li class="align_right"><a href="email?enrollmentId=${enrollment.id}"><img title="Email Grade" alt="[Email Grade]"
  src="<c:url value='/img/icons/email_go.png' />" /></a></li>
</ul>

<table class="viewtable halfwidth">
<thead><tr><th>Assignment</th><th>Grade</th><th>Total</th></tr></thead>
<tbody>
  <c:forEach items="${submissions}" var="submission">
  <tr>
    <td><a href="<c:url value='/submission/grade?id=${submission.id}' />">${submission.assignment.name}</a></td>
    <td class="center">${submission.grade}</td>
    <td class="center">${submission.assignment.totalPoints}</td>
  </tr>
  </c:forEach>
</tbody>
</table>

<h4><a id="gradeLink" href="javascript:void(0)">Grade</a></h4>
<div id="grade" class="editable_input">${enrollment.grade.symbol}</div>

<h4><a id="commentsLink" href="javascript:void(0)">Comments</a></h4>
<pre id="comments"><c:out value="${enrollment.comments}" escapeXml="true" /></pre>

<button id="ok" class="subbutton">OK</button>
