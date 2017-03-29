<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function(){
    $("table.block").tablesorter({
        sortList: [[0,0]]
    });
    /* major and program selections */
    $(".select").hide();
    $(".select").change(function(){
        var field = $(this).attr("data-field");
        var msg = "Are you sure you want to change this student's " + field + "?";
        if( confirm(msg) )
            window.location.href = field + "/set?userId=${user.id}&" + field + "Id=" + $(this).val();
    });
    $(".change").click(function(){
        var field = $(this).attr("data-field");
        $("#"+field+"-display").toggle();
        $("#"+field+"-select").toggle();
        if( $(this).html() == 'Change' )
            $(this).html("Cancel");
        else
            $(this).html("Change");
    });
    if( "${user.major.id}" )
        $("#major-select").val( "${user.major.id}" );
    if( "${user.personalProgram.id}" )
        $("#program-select").val( "${user.personalProgram.program.id}" );
    /* prereq-met toggle */
    $(".prereq").each(function(){
       if( $(this).attr("data-prereq-met") == "true" )
           $(this).addClass("prereq-met");
    });
    $(".prereq").dblclick(function(){
        if( confirm("Toggle prerequisites met?") )
            $.ajax({
                url: "program/entry/update?operation=prereq&entryId=" + $(this).attr("data-entry-id"),
                context: $(this),
                success: function(){
                    $(this).toggleClass("prereq-met");
                }
            });
    });
    /* add entry */
    $("#addEntryDialog").dialog({
        autoOpen: false,
        width: 450,
        title: "Add Course",
        buttons: {
            "Cancel": function(){
                $("#addEntryForm")[0].reset();
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
                $("#addEntryForm").submit();
            }
        }
    });
    $(".block-title").dblclick(function(){
        $("input[name='blockId']").val($(this).attr("data-block-id"));
        $("#addEntryDialog").dialog("open");
    });
    /* delete entry */
    $(".entry-code").dblclick(function(){
       var $parent = $(this).closest("tr");
       var msg = "Are you sure you want to delete this course requirement?";
       if( confirm(msg) )
           $.ajax({
               url: "program/entry/update?operation=delete&entryId=" + $parent.attr("data-entry-id"),
               success: function(){
                   $parent.remove();
               }
           });
    });
    /* drag and drop enrollment */
    $(".enrollment").draggable({
        helper: "clone"
    });
    $(".entry").droppable({
        accept: ".enrollment",
        hoverClass: "ui-state-highlight",
        drop: function( event, ui ){
            window.location.href= "program/entry/update?userId=${user.id}" +
                "&entryId=" + $(this).attr("data-entry-id") +
                "&enrollmentId=" + ui.draggable.attr("data-enrollment-id");
        }
    });
    /* hide the <thead> which is required by tablesorter */
    $(".hidden").hide();
});
</script>
<table class="general autowidth">
<tr>
  <th>Major</th>
  <td style="min-width: 4em;">
    <span id="major-display">${user.major.name}</span>
<c:if test="${fn:length(departments) > 0}">
    <select id="major-select" class="select" data-field="major" name="majorId">
      <option value=""></option>
      <c:forEach items="${departments}" var="department">
      <option value="${department.id}">${department.name}</option>
      </c:forEach>
    </select>
</c:if>
  </td>
  <td><a class="change" data-field="major" href="javascript:void(0)">Change</a></td>
</tr>  
<tr>
  <th>Program</th>
  <td style="min-width: 4em;">
    <span id="program-display">${user.personalProgram.program.name}</span>
<c:if test="${fn:length(programs) > 0}">
    <select id="program-select" class="select" data-field="program" name="programId">
      <option value=""></option>
      <c:forEach items="${programs}" var="program">
      <option value="${program.id}">${program.name}</option>
      </c:forEach>
    </select>
</c:if>
  </td>
  <td><a class="change" data-field="program" href="javascript:void(0)">Change</a></td>
</tr>
</table>

<c:if test="${not empty user.personalProgram}">
<div id="personal-program">

<%-- personal program --%>
<div class="left-block">
<table class="general2 block">
<thead class="hidden"><tr><th colspan="6"></th></tr></thead>
<c:forEach items="${user.personalProgram.blocks}" var="block">
<tbody>
  <tr>
    <th class="block-title" data-block-id="${block.id}" colspan="6" style="text-align: left;">
      ${block.programBlock.name}
      <c:if test="${block.programBlock.requireAll}">(All Courses Required)</c:if>
      <c:if test="${not block.programBlock.requireAll}">(${block.programBlock.unitsRequired} Units Required)</c:if>
    </th>
  </tr>
</tbody>
<tbody>
  <c:forEach items="${block.entries}" var="entry">
  <tr class="entry" data-entry-id="${entry.id}">
    <td class="entry-code">${entry.course.code}</td>
    <td>${entry.course.name}</td>
    <td>${entry.course.units}</td>
    <c:if test="${empty entry.enrollment}">
      <td colspan="3" class="prereq" data-entry-id="${entry.id}" data-prereq-met="${entry.prereqMet}">
      </td>
    </c:if>
    <c:if test="${not empty entry.enrollment}">
      <td>${entry.enrollment.section.term}</td>
      <td>${entry.enrollment.section.course.code}</td>
      <td>${entry.enrollment.grade.symbol}</td>
    </c:if>
  </tr>
  </c:forEach>
</tbody>
</c:forEach>
</table>
</div>

<%-- enrollments --%>
<div class="right-block">
<table class="general2 autowidth">
<thead>
  <tr><th>Term</th><th>Course</th><th>Grade</th></tr>
</thead>
<tbody class="sortable">
  <c:forEach items="${enrollments}" var="enrollment">
  <tr class="enrollment" data-enrollment-id="${enrollment.id}">
    <td>${enrollment.section.term}</td>
    <td>${enrollment.section.course.code}</td>
    <td><span style="margin-left: 1em;">${enrollment.grade.symbol}</span></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</div>

</div> <!-- end of personal program -->

<div id="addEntryDialog">
<form id="addEntryForm" action="program/entry/add">
<input id="addCourse" name="course" type="text" class="forminput" style="width: 96%;"
       placeholder="Search course to add" />
<input name="userId" type="hidden" value="${user.id}" />
<input name="blockId" type="hidden" value="" />
<input name="courseId" type="hidden" value="" />
</form>
</div>
</c:if>
