<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script>
$(function(){
    $("#sortableSections").sortable({
        update: function(event,ui){
            $.ajax({
                type: "POST",
                url:  "section/reorder?programId=${program.id}",
                data: {
                    "fieldId": ui.item.attr("data-section-id"),
                    "newIndex": ui.item.index()
                }
            });
        }
    });
    $("#sortableSections").disableSelection();
    $(".sortableElements").sortable({
        update: function(event,ui){
            $.ajax({
                type: "POST",
                url:  "resource/reorder?programId=${program.id}",
                data: {
                    "sectionId": ui.item.attr("data-section-id"),
                    "resourceId": ui.item.attr("data-resource-id"),
                    "newIndex": ui.item.index()
                }
            });
        }
    });
    $(".sortableElements").disableSelection();
});
function removeProgram()
{
    var msg = "Are you sure you want to remove this program?";
    if( confirm(msg) )
        window.location.href = "remove?id=${program.id}";
}
</script>

<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li><a href="view?id=${program.id}" class="bc">${program.name}</a></li>
<li>Edit</li>
<li class="align_right"><a href="javascript:removeProgram()"><img alt="[Remove Program]"
  title="Remove Program" src="<c:url value='/img/icons/report_delete.png' />" /></a></li>
<li class="align_right"><a href="section/add?programId=${program.id}"><img alt="[Add Section]"
  title="Add Section" src="<c:url value='/img/icons/brick_add.png' />" /></a></li>
<li class="align_right"><a href="name/edit?programId=${program.id}"><img alt="[Edit Name]"
  title="Edit Name" src="<c:url value='/img/icons/page_edit.png' />" /></a></li>
</ul>

<div class="site-block">
<div class="site-block-title">Vision Statement
  <div class="site-block-operations">
    <a href="vision/edit?programId=${program.id}"><img title="Edit Vision Statement"
       alt="[Edit Vision Statement]" src="<c:url value='/img/icons/page_edit.png' />" /></a>
  </div>
</div>
<div class="site-block-content">${program.vision}</div>
</div>

<div class="site-block">
<div class="site-block-title">Mission Statement
  <div class="site-block-operations">
    <a href="mission/edit?programId=${program.id}"><img title="Edit Mission Statement"
       alt="[Edit Mission Statement]" src="<c:url value='/img/icons/page_edit.png' />" /></a>
  </div>
</div>
<div class="site-block-content">${program.mission}</div>
</div>

<div class="site-block">
<div class="site-block-title">Educational Objectives
  <div class="site-block-operations">
    <a href="objective/add?programId=${program.id}"><img title="Add Objective"
       alt="[Add Objective]" src="<c:url value='/img/icons/page_add.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
<table class="viewtable autowidth">
  <c:forEach items="${program.objectives}" var="objective" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${objective.text}</td>
    <td>
      <a href="objective/edit?programId=${program.id}&index=${status.index}"><img
         title="Edit Objective" alt="[Edit Objective]"
         src="<c:url value='/img/icons/page_edit.png' />" /></a>
    </td>
  </tr>
  </c:forEach>
</table>
</div>
</div>

<div class="site-block">
<div class="site-block-title">Student Outcomes
  <div class="site-block-operations">
    <a href="outcome/add?programId=${program.id}"><img title="Add Outcome"
       alt="[Add Outcome]" src="<c:url value='/img/icons/page_add.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
<table class="viewtable autowidth">
  <c:forEach items="${program.outcomes}" var="outcome" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${outcome.text}</td>
    <td>
      <a href="outcome/edit?programId=${program.id}&index=${status.index}"><img
         title="Edit Outcome" alt="[Edit Outcome]"
         src="<c:url value='/img/icons/page_edit.png' />" /></a>
      <a href="outcome/description?fieldId=${outcome.id}&edit=true"><img
         title="Edit Outcome Measures" alt="[Edit Outcome Measures]"
         src="<c:url value='/img/icons/table_chart_edit.png' />" /></a>
    </td>
  </tr>
  </c:forEach>
</table>
</div>
</div>

<div id="sortableSections">
<c:forEach items="${program.sections}" var="section" varStatus="status">
<div class="site-block" data-section-id="${section.id}">
<div id="s${section.id}" class="site-block-title">${section.name}
  <div class="site-block-operations">
    <a href="section/edit?programId=${program.id}&fieldId=${section.id}"><img
       title="Edit Section" alt="[Edit Section]" src="<c:url value='/img/icons/brick_edit.png' />" /></a>
    <a href="resource/add?programId=${program.id}&sectionId=${section.id}"><img
       title="Add Resource" alt="[Add Resource]" src="<c:url value='/img/icons/plugin_add.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
<c:if test="${fn:length(section.resources) > 0}">
<table class="viewtable autowidth">
  <tbody class="sortableElements">
  <c:forEach items="${section.resources}" var="resource">
  <tr data-section-id="${section.id}" data-resource-id="${resource.id}">
    <td><a href="resource/view?programId=${program.id}&resourceId=${resource.id}"
           id="r${resource.id}">${resource.name}</a></td>
    <td><a href="resource/edit?programId=${program.id}&sectionId=${section.id}&resourceId=${resource.id}"><img
           title="Edit Resource" alt="[Edit Resource]"
           src="<c:url value='/img/icons/plugin_edit.png' />" /></a></td>
  </tr>
  </c:forEach>
  </tbody>
</table>
</c:if>
</div>
</div>
</c:forEach>
</div>
