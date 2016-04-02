<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script>
$(function(){
    $(".sortableElements").sortable({
        update: function(event,ui){
            $.ajax({
                type: "POST",
                url:  "measure/reorder?fieldId=${outcome.id}",
                data: {
                    "measureId": ui.item.attr("data-measure-id"),
                    "newIndex": ui.item.index()
                }
            });
        }
    });
    $(".sortableElements").disableSelection();
});
function removeMeasure( measureId )
{
    var msg = "Are you sure you want to remove this measure?";
    if( confirm(msg) )
        window.location.href = "measure/remove?fieldId=${outcome.id}&measureId=" + measureId;
}
</script>
<ul id="title">
<li><a href="../list" class="bc">Program Assessment</a></li>
<li><a href="../view?id=${outcome.program.id}#outcomes" class="bc">${outcome.program.name}</a></li>
<c:if test="${empty param.edit}">
<li>Outcome Measures</li>
<li class="align_right"><a href="?fieldId=${outcome.id}&edit=true"><img alt="[Edit Outcome Measures]"
  title="Edit Outcome Measures" src="<c:url value='/img/icons/table_chart_edit.png' />" /></a></li>
</c:if>
<c:if test="${not empty param.edit}">
<li><a href="measures?fieldId=${outcome.id}" class="bc">Outcome Measures</a></li>
<li>Edit</li>
<li class="align_right"><a href="measure/add?fieldId=${outcome.id}"><img alt="[Add Outcome Measure]"
  title="Add Outcome Measure" src="<c:url value='/img/icons/table_chart_add.png' />" /></a></li>
</c:if>
</ul>

<h3>Student Outcome #${outcome.index+1}</h3>
<p>${outcome.text}</p> <br />

<c:if test="${not empty param.edit and fn:length(outcome.measures) > 0}">
<table class="viewtable autowidth">
<thead>
<tr><th>Type</th><th>Name</th><th></th></tr>
</thead>
<tbody class="sortableElements">
<c:forEach items="${outcome.measures}" var="measure">
<tr data-measure-id="${measure.id}">
  <td>${measure.type}</td>
  <td>${measure.name}</td>
  <td>
    <a href="measure/edit?fieldId=${outcome.id}&measureId=${measure.id}"><img alt="[Edit Measure]" 
       title="Edit Measure" src="<c:url value='/img/icons/table_chart_edit.png'/>" /></a>
    <a href="javascript:removeMeasure(${measure.id})"><img alt="[Remove Measure]" 
       title="Remove Measure" src="<c:url value='/img/icons/table_chart_delete.png'/>" /></a>
  </td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>

<c:if test="${empty param.edit and fn:length(outcome.measures) > 0}">
<table class="general">
<c:forEach items="${outcome.measures}" var="measure">
<tr>
  <th class="shrink">${measure.type}</th>
  <td>
    <c:choose>
      <c:when test="${measure.type == 'RUBRIC'}">
      <a href="<c:url value='/department/${dept}/rubric/results?id=${measure.rubric.id}' />">${measure.name}</a>
      </c:when>
      <c:when test="${measure.type == 'SURVEY'}">
      <a href="<c:url value='/department/${dept}/survey/chart/view?id=${measure.surveyChart.id}' />">${measure.name}</a>
      </c:when>
      <c:when test="${measure.type == 'OTHER' and measure.description.type == 'FILE'}">
      <a href="<c:url value='/download?fileId=${measure.description.file.id}' />">${measure.name}</a>
      </c:when>
      <c:when test="${measure.type == 'OTHER' and measure.description.type == 'URL'}">
      <a href="${measure.description.url}">${measure.name}</a>
      </c:when>
      <c:otherwise>
      ${measure.name}
      </c:otherwise>
    </c:choose>
  </td>
</tr>
<c:if test="${measure.description.type == 'TEXT'}">
<tr><td colspan="2">${measure.description.text}</td></tr>
</c:if>
</c:forEach>
</table>
</c:if>
