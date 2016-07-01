<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
var selectedClasses = ${selectedClasses};
var numOfClasses = selectedClasses.length;
function gotoSelectedSections()
{
	var offset = $("#selected-classes").offset();
	$("html, body").animate({
	    scrollTop: offset.top,
	    scrollLeft: offset.left
	});
}
function selectClass( sectionId )
{
	var row = $("#all-sections tr[data-section-id='" + sectionId + "']");
	var linkedRow = $("#all-sections tr[data-section-id='" + row.attr("data-linked-by") + "']");
	row.hide();
    linkedRow.hide();

    row = $("#selected-sections tr[data-section-id='" + sectionId + "']");
    var linkedRow = $("#selected-sections tr[data-section-id='" + row.attr("data-linked-by") + "']");
    row.find("input[type='checkbox']").attr("disabled", false).prop("checked",true);
    row.show();
    linkedRow.show();
}
function deselectClass( sectionId )
{
    var row = $("#all-sections tr[data-section-id='" + sectionId + "']");
    var linkedRow = $("#all-sections tr[data-section-id='" + row.attr("data-linked-by") + "']");
    row.find("input[type='checkbox']").attr("disabled", false).prop("checked",false);
    row.show();
    linkedRow.show();

    row = $("#selected-sections tr[data-section-id='" + sectionId + "']");
    var linkedRow = $("#selected-sections tr[data-section-id='" + row.attr("data-linked-by") + "']");
    row.hide();
    linkedRow.hide();
}
function updateNumOfClasses(value)
{
	numOfClasses += value;
	$("#numOfClasses").html(numOfClasses);
}
$(function(){
    $("#all-sections,#selected-sections").tablesorter({
        sortList: [[1,0], [2,0]]
    });
    $("#selected-sections .section").hide();
    selectedClasses.forEach(function(s){
    	selectClass(s);
    })
    $("input[type='checkbox']").change(function(){
        $(this).attr("disabled",true);
        var sectionId = $(this).attr("data-section-id");
    	if($(this).is(':checked'))
    	{
    		if( numOfClasses >= ${registration.regLimit} )
    	    {
    	        $(this).attr("checked",false).attr("disabled",false);
    	        alert("You have already reached your registration limit.");
    	        return;
    	    }
            $.ajax({
                type: "POST",
                url:  "registration/addClass",
                data: {
                    "registrationId": ${registration.id},
                    "sectionId": sectionId
                },
                success: function(){
                	selectClass(sectionId);
                    gotoSelectedSections();
                    updateNumOfClasses(1);
                },
                error: function(){
                	$(this).attr("disabled", false);
                	$(this).attr("checked", false);
                }
            });
    	}
    	else
    	{
            $.ajax({
                type: "GET",
                url:  "registration/removeClass",
                data: {
                    "registrationId": ${registration.id},
                    "sectionId": sectionId
                },
                success: function(){
                    deselectClass(sectionId);
                    updateNumOfClasses(-1);
                },
                error: function(){
                    $(this).attr("disabled", false);
                    $(this).attr("checked", true);
                }
            });
    	}
    });
    $("#comments").editable( "registration/editComments", {
        submitdata: { "registrationId": ${registration.id} },
        name: "comments",
        placeholder: "&nbsp;",
        type: "textarea",
        rows: 4,
        event: "dblclick",
        submit: "Save",
        onblur: "submit"
    });
    $("#commentsLink").click(function(){
        $("#comments").trigger("dblclick"); 
    });
    $(".notes-dialog").dialog({
        autoOpen : false,
        modal : true,
        width : 450
    });
    $(".view-notes").click(function(){
        var sectionId = $(this).attr("data-section-id");
        $(".notes-dialog[data-section-id='" + sectionId + "']").dialog("open");
    });
});
</script>

<c:set var="schedule" value="${registration.schedule}" />
<c:set var="sections" value="${schedule.sections}" />

<ul id="title">
<li>Pre-Registration</li>
</ul>

<table class="general autowidth" style="margin-bottom: 2em;">
<tr>
  <th>Term</th>
  <td>${schedule.term}</td>
  <th>Pre-Registration Start</th>
  <td><fmt:formatDate value="${schedule.preregStart}" pattern="yyyy-MM-dd" /></td>
</tr>
<tr>
  <th>Student</th>
  <td>${registration.student.name}</td>
  <th>Pre-Registration End</th>
  <td><fmt:formatDate value="${schedule.preregEnd}" pattern="yyyy-MM-dd" /></td>
</tr>
</table>

<div style="margin: 1em 0;">${schedule.description}</div>

<h4 id="selected-classes">Selected Classes:
<span id="numOfClasses">${fn:length(registration.sectionRegistrations)}</span>
/ ${registration.regLimit}</h4>

<table id="selected-sections" class="viewtable autowidth">
<thead>
<tr>
  <th></th><th>Course</th><th>Section</th><th>Name</th><th>Type</th>
  <th>Number</th><th>Time</th><th>Location</th>
</tr>
</thead>
<tbody>
<c:forEach items="${sections}" var="section">
<tr class="section" data-section-id="${section.id}"
    <c:if test="${section.linkedBy != null}">data-linked-by="${section.linkedBy.id}"</c:if>>
  <td>
    <c:if test="${empty section.linkedTo}">
    <input type="checkbox"  data-section-id="${section.id}" checked />
    </c:if>
  </td>
  <td>
    <a href="<c:url value='/course/view?id=${section.course.id}' />">${section.course.code}</a>
  </td>
  <td>${section.sectionNumber}
    <c:if test="${not empty section.notes}">
      <a href="javascript:void(0)" class="view-notes" data-section-id="s${section.id}"><img
        title="View Notes" alt="[View Notes]" src="<c:url value='/img/icons/comment.png' />" /></a>
      <pre class="notes-dialog" data-section-id="s${section.id}">${section.notes}</pre>
    </c:if>
  </td>
  <td>${section.course.name}</td>
  <td>${section.type}</td>
  <td>${section.classNumber}</td>
  <td>${section.days}<c:if test="${not empty section.startTime}">
    ${section.startTime}-${section.endTime}</c:if></td>
  <td>${section.location}</td>
</tr>
</c:forEach>
</tbody>
</table>

<h4><a id="commentsLink" href="javascript:void(0)">Additional Comments</a></h4>
<pre id="comments">${registration.comments}</pre>

<p>Please select from the following the classes you plan to register for the
${schedue.term} term. You may select a max number of <em>${registration.regLimit}</em>
classes.</p>

<table id="all-sections" class="viewtable autowidth">
<thead>
<tr>
  <th></th><th>Course</th><th>Section</th><th>Name</th><th>Type</th>
  <th>Number</th><th>Time</th><th>Location</th>
</tr>
</thead>
<tbody>
<c:forEach items="${sections}" var="section">
<tr data-section-id="${section.id}"
    <c:if test="${section.linkedBy != null}">data-linked-by="${section.linkedBy.id}"</c:if>>
  <td>
    <c:if test="${empty section.linkedTo}">
    <input type="checkbox" data-section-id="${section.id}" />
    </c:if>
  </td>
  <td>
    <a href="<c:url value='/course/view?id=${section.course.id}' />">${section.course.code}</a>
  </td>
  <td>${section.sectionNumber}
    <c:if test="${not empty section.notes}">
      <a href="javascript:void(0)" class="view-notes" data-section-id="a${section.id}"><img
        title="View Notes" alt="[View Notes]" src="<c:url value='/img/icons/comment.png' />" /></a>
      <pre class="notes-dialog" data-section-id="a${section.id}">${section.notes}</pre>
    </c:if>
  </td>
  <td>${section.course.name}</td>
  <td>${section.type}</td>
  <td>${section.classNumber}</td>
  <td>${section.days}<c:if test="${not empty section.startTime}">
    ${section.startTime}-${section.endTime}</c:if></td>
  <td>${section.location}</td>
</tr>
</c:forEach>
</tbody>
</table>
