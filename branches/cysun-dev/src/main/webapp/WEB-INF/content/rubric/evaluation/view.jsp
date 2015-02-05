<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${assignment.section}" />
<c:set var="rubric" value="${assignment.rubric}" />

<style>
.viewtable tr:hover {
    background-color: #ffffff;
}
.selected {
    background-color: #ffbb33;
}
</style>

<script>
$(function(){
<c:if test="${not empty evaluation}">
	$("td.indicator-criterion").click(function(){
		var cell = $(this);
		var values = $(this).attr("id").split("-");
		$.ajax({
			url: "set",
			data: {
				id: ${evaluation.id},
				index: values[1],
				value: values[2]
			},
			success: function(){
		        cell.siblings().removeClass("selected");
		        cell.addClass("selected");
			},
			cache: false
		});
	});
    $("#comments").editable( "set", {
        submitdata: { "id": ${evaluation.id} },
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
</c:if>
    $("#ok").click(function(){
      <c:choose>
    	<c:when test="${role == 'student'}">
        window.location.href = "../../submission/student/list?assignmentId=${assignment.id}";
        </c:when>
        <c:otherwise>
        window.location.href = "../../submission/${role}/view?id=${submission.id}";
        </c:otherwise>
      </c:choose>
    });
});
</script>

<c:choose>
  <c:when test="${role == 'instructor'}"><c:set var="home" value="taught" /></c:when>
  <c:when test="${role == 'evaluator'}"><c:set var="home" value="evaluated" /></c:when>
  <c:otherwise><c:set var="home" value="taken" /></c:otherwise>
</c:choose>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/${home}#section-${section.id}' />">${section.course.code}
   - ${section.number}</a></li>
<li><a class="bc" href="../../submission/${role}/list?assignmentId=${assignment.id}"><csns:truncate
  value="${assignment.name}" length="50" /></a></li>
<c:if test="${role == 'instructor' or role == 'evaluator'}">
<li><a class="bc" href="../../submission/${role}/view?id=${submission.id}"><csns:truncate
  value="${submission.student.name}" length="25" /></a></li>
<li>Evaluate</li>
</c:if>
<c:if test="${role == 'student'}">
<li>${submission.student.name}</li>
</c:if>
</ul>

<c:if test="${empty evaluation}">
  <c:choose>
    <c:when test="${not assignment.published}">
      <p>This assignment is not published yet.</p>
    </c:when>
    <c:otherwise>
      <p>This assignment is past due.</p>
    </c:otherwise>
  </c:choose>
</c:if>

<c:if test="${not empty evaluation}">
<h3>${rubric.name}</h3>

<table class="viewtable">
<thead>
  <tr>
    <c:forEach begin="1" end="${rubric.scale}" step="1" var="rank">
    <th>${rank}</th>
    </c:forEach>
  </tr>
</thead>
<tbody>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
  <tr id="${indicator.id}">
    <td colspan="${rubric.scale}" class="indicator-name">${indicator.name}</td>
  </tr>
  <tr>
  <c:forEach begin="0" end="${rubric.scale-1}" step="1" var="index">
    <td id="ic-${status.index}-${index}" 
      <c:if test="${evaluation.ratings[status.index] == index+1}">
        class="indicator-criterion selected"
      </c:if>
      <c:if test="${evaluation.ratings[status.index] != index+1}">
        class="indicator-criterion"
      </c:if>
    >${indicator.criteria[index]}</td>
  </c:forEach>
  </tr>
</c:forEach>
</tbody>
</table>

<h4><a id="commentsLink" href="javascript:void(0)">Additional Comments</a></h4>
<pre id="comments"><c:out value="${evaluation.comments}" escapeXml="true" /></pre>
</c:if> <%-- end of "not empty evaluation" --%>

<p><button id="ok" class="subbutton">OK</button></p>
