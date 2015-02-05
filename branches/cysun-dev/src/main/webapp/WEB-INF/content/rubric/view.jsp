<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("#add").click(function(){
        window.location.href = "addIndicator?id=${rubric.id}"; 
    });
});
function promote( id )
{
    var msg = "Do you want to promote this rubric to a department rubric?";
    if( confirm(msg) )
        window.location.href = "promote?id=" + id;
}
</script>

<ul id="title">
<li><a href="list" class="bc">Rubrics</a></li>
<li>${rubric.name}</li>
<security:authorize access="principal.isAdmin('${dept}') or principal.id.toString() == '${rubric.creator.id}'">
  <li class="align_right"><a href="edit?id=${rubric.id}"><img alt="[Edit Rubric]"
    title="Edit Rubric" src="<c:url value='/img/icons/table_edit.png' />" /></a></li>
</security:authorize>
<security:authorize access="principal.isAdmin('${dept}')">
<c:if test="${empty rubric.department}">
  <li class="align_right"><a href="javascript:promote(${rubric.id})"><img alt="[Promote Rubric]"
    title="Promote Rubric" src="<c:url value='/img/icons/table_up.png' />" /></a></li>
</c:if>
</security:authorize>
<li class="align_right"><a href="results?id=${rubric.id}"><img alt="[Results]" 
    title="Results" src="<c:url value='/img/icons/table_multiple.png'/>" /></a></li>
</ul>

${rubric.description}

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
    <td colspan="${rubric.scale}" class="indicator-name">
      ${indicator.name}
<security:authorize access="principal.isAdmin('${dept}') or principal.id.toString() == '${rubric.creator.id}'">
      <span style="float: right;"><a href="editIndicator?rubricId=${rubric.id}&amp;indicatorIndex=${status.index}"><img
        alt="[Edit Indicator]" title="Edit Indicator" src="<c:url value='/img/icons/row_edit.png' />" /></a></span>
</security:authorize>
    </td>
  </tr>
  <tr>
  <c:forEach begin="0" end="${rubric.scale-1}" step="1" var="index">
    <td class="indicator-criterion">${indicator.criteria[index]}</td>
  </c:forEach>
  </tr>
</c:forEach>
</tbody>
</table>

<c:if test="${not rubric.published}">
<security:authorize access="principal.isAdmin('${dept}') or principal.id.toString() == '${rubric.creator.id}'">
<p>
  <button id="add" type="button" class="subbutton">Add Performance Indicator</button>
</p>
</security:authorize>
</c:if>
