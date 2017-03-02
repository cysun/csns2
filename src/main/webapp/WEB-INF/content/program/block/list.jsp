<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function(){
    $("#addCourseDialog").dialog({
        autoOpen: false,
        width: 450,
        title: "Add Course",
        buttons: {
            "Cancel": function(){
                $("#addCourseForm")[0].reset();
                $(this).dialog("close");
            }
        }
    });
    $("#addCourse").autocomplete({
        source: "<c:url value='/autocomplete/course' />",
        select: function(event, ui) {
            if( ui.item )
            {
                $("input[name='courseId']").val(ui.item.id);
                $("#addCourseForm").submit();
            }
        }
    });
    $(".add-course").click(function(){
        $("input[name='blockId']").val($(this).attr("data-block-id"));
        $("#addCourseDialog").dialog("open");
    });
    $("#sortableBlocks").sortable({
        update: function(event,ui){
            $.ajax({
                type: "POST",
                url:  "reorder",
                data: {
                    "programId": ui.item.attr("data-program-id"),
                    "blockId": ui.item.attr("data-block-id"),
                    "newIndex": ui.item.index()
                }
            });
        }
    });
    $("#sortableBlocks").disableSelection();
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/program/search' />">Programs</a></li>
<li><a class="bc" href="../list">${program.department.name}</a></li>
<li><a class="bc" href="../view?id=${program.id}">${program.name}</a></li>
<li>Blocks</li>
<li class="align_right"><a href="add?programId=${program.id}"><img title="Add Block"
    alt="[Add Block]" src="<c:url value='/img/icons/brick_add.png' />" /></a></li>
</ul>

<h3 class="site-title">${program.name}</h3>
<div class="site-description">${program.description}</div>

<div id="sortableBlocks">
<c:forEach items="${program.blocks}" var="block">
<div id="block-${block.id}" class="site-block" data-program-id="${program.id}" data-block-id="${block.id}">
<div class="site-block-title">${block.name}
  <span style="margin-left: 1em;">
    <c:if test="${block.requireAll}">(All Courses Required)</c:if>
    <c:if test="${not block.requireAll}">(${block.unitsRequired} Units Required)</c:if>
  </span>
  <div class="site-block-operations">
    <a href="edit?id=${block.id}&programId=${program.id}"><img
       title="Edit Block" alt="[Edit Block]" src="<c:url value='/img/icons/brick_edit.png' />" /></a>
    <a class="add-course" href="javascript:void(0)" data-block-id="${block.id}"><img
       title="Add Course" alt="[Add Course]" src="<c:url value='/img/icons/plugin_add.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
${block.description}
<c:if test="${fn:length(block.courses) > 0}">
<table class="general2 autowidth">
  <tr><th>Code</th><th>Name</th><th>Units</th><th></th></tr>
  <c:forEach items="${block.courses}" var="course">
  <tr>
    <td>${course.code}</td>
    <td>${course.name}</td>
    <td class="center">${course.units}</td>
    <td><a href="removeCourse?programId=${program.id}&blockId=${block.id}&courseId=${course.id}"><img
           title="Remove Course" alt="[Remove Course]" src="<c:url value='/img/icons/plugin_delete.png' />" /></a>
  </c:forEach>
</table>
</c:if>
</div> <!-- end of site-block-content -->
</div> <!-- end of site-block -->
</c:forEach>
</div> <!-- end of sortableBlocks -->

<div id="addCourseDialog">
<form id="addCourseForm" action="addCourse">
<input id="addCourse" name="course" type="text" class="forminput" style="width: 96%;"
       placeholder="Search course to add" />
<input name="programId" type="hidden" value="${program.id}" />
<input name="blockId" type="hidden" value="" />
<input name="courseId" type="hidden" value="" />
</form>
</div>
