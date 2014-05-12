<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="submission" value="${evaluation.submission}" />
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
			}
		});
	});
    $("#ok").click(function(){
        window.location.href = "../submission/view?id=${submission.id}";
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code}
   - ${section.number}</a></li>
<li><a class="bc" href="../submission/list?assignmentId=${assignment.id}"><csns:truncate
  value="${assignment.name}" length="50" /></a></li>
<li><a class="bc" href="../submission/view?id=${submission.id}"><csns:truncate
  value="${submission.student.name}" length="25" /></a></li>
<li>Evaluate</li>
</ul>

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
      <c:if test="${evaluation.ratings[status.index] == index}">
        class="indicator-criterion selected"
      </c:if>
      <c:if test="${evaluation.ratings[status.index] != index}">
        class="indicator-criterion"
      </c:if>
    >${indicator.criteria[index]}</td>
  </c:forEach>
  </tr>
</c:forEach>
</tbody>
</table>

<p><button id="ok" class="subbutton">OK</button></p>
