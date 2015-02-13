<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

 <script>
$(function(){
    $("table").tablesorter();
    $(".add").each(function(){
        $(this).autocomplete({
            source: "<c:url value='/autocomplete/course' />",
            select: function(event, ui) {
                if( ui.item )
                    $("<input>").attr({
                        type: "hidden",
                        name: $(this).attr("name") + "Id",
                        value: ui.item.id
                    }).addClass("added").appendTo($(this).parent());
            }
        });
    });
    $(".clear").each(function(){
       $(this).click(function(event){
           event.preventDefault();
           $(".added").remove();
           $(".add").each(function(){
              $(this).val("");
           });
       });
    });
});
</script>

<h4>Courses Taken</h4>
<c:if test="${fn:length(coursesTaken) > 0}">
<table class="viewtable autowidth">
<thead>
  <tr><th>Quarter</th><th>Course</th><th>Instructor</th><th>Grade</th></tr>
</thead>
<tbody>
  <c:forEach items="${coursesTaken}" var="courseTaken">
  <tr>
    <td>${courseTaken.section.quarter}</td>
    <td>${courseTaken.section.course.code}</td>
    <td>${courseTaken.section.instructors[0].name}</td>
    <td><span style="margin-left: 1em;">${courseTaken.grade.symbol}</span></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
<c:if test="${fn:length(coursesTaken) == 0}">
<p>No course on record.</p>
</c:if>

<h4>Course Substitutions</h4>
<c:if test="${fn:length(courseSubstitutions) > 0}">
<table class="viewtable autowidth">
<thead>
  <tr><th>Course</th><th>Substitute</th><th>Advisor</th><th>Date</th></tr>
</thead>
<tbody>
  <c:forEach items="${courseSubstitutions}" var="courseSubstitution" varStatus="status">
  <tr <c:if test="${status.index % 2 == 1}">class="even"</c:if>>
    <td>${courseSubstitution.original.code}</td>
    <td>${courseSubstitution.substitute.code}</td>
    <td>${courseSubstitution.advisementRecord.advisor.name}</td>
    <td><fmt:formatDate pattern="yyyy-MM-dd" value="${courseSubstitution.advisementRecord.date}" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>

<p><a class="toggle" id="addCourseSubstitution" href="javascript:void(0)">Add</a></p>
<form class="default-hide" id="addCourseSubstitutionForm" action="course/substitute"
      enctype="multipart/form-data" method="post">
<p>Substitute
<input type="text" class="add center" name="original" size="15"/> with
<input type="text" class="add center" name="substitute" size="15" />.</p>
<p>Additional Comment:</p>
<textarea id="ta1" name="comment" rows="5" cols="80"></textarea>
<p><input type="hidden" name="userId" value="${user.id}" />
<button class="clear subbutton">Clear</button>
<input type="submit" name="submit" class="subbutton" value="OK" /></p>
</form>

<h4>Course Transfers</h4>
<c:if test="${fn:length(courseTransfers) > 0}">
<table class="viewtable autowidth">
<thead>
  <tr><th>Course</th><th>Advisor</th><th>Date</th></tr>
</thead>
<tbody>
  <c:forEach items="${courseTransfers}" var="courseTransfer" varStatus="status">
  <tr <c:if test="${status.index % 2 == 1}">class="even"</c:if>>
    <td>${courseTransfer.course.code}</td>
    <td>${courseTransfer.advisementRecord.advisor.name}</td>
    <td><fmt:formatDate pattern="yyyy-MM-dd" value="${courseTransfer.advisementRecord.date}" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>

<p><a class="toggle" id="addCourseTransfer" href="javascript:void(0)">Add</a></p>
<form class="default-hide" id="addCourseTransferForm" action="course/transfer"
      enctype="multipart/form-data" method="post">
<p>Transfer credits for <input type="text" class="add center" name="course" size="15"/>.</p>
<p>Additional Comment:</p>
<textarea id="ta2" name="comment" rows="5" cols="80"></textarea>
<p><input type="hidden" name="userId" value="${user.id}" />
<button class="clear subbutton">Clear</button>
<input type="submit" name="submit" class="subbutton" value="OK" /></p>
</form>

<h4>Course Waivers</h4>
<c:if test="${fn:length(courseWaivers) > 0}">
<table class="viewtable autowidth">
<thead>
  <tr><th>Course</th><th>Advisor</th><th>Date</th></tr>
</thead>
<tbody>
  <c:forEach items="${courseWaivers}" var="courseWaiver" varStatus="status">
  <tr <c:if test="${status.index % 2 == 1}">class="even"</c:if>>
    <td>${courseWaiver.course.code}</td>
    <td>${courseWaiver.advisementRecord.advisor.name}</td>
    <td><fmt:formatDate pattern="yyyy-MM-dd" value="${courseWaiver.advisementRecord.date}" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>

<p><a class="toggle" id="addCourseWaiver" href="javascript:void(0)">Add</a></p>
<form class="default-hide" id="addCourseWaiverForm" action="course/waive"
      enctype="multipart/form-data" method="post">
<p>Waive the requirement of <input type="text" class="add center" name="course" size="15" />.</p>
<p>Additional Comment:</p>
<textarea id="ta3" name="comment" rows="5" cols="80"></textarea>
<p><input type="hidden" name="userId" value="${user.id}" />
<button class="clear subbutton">Clear</button>
<input type="submit" name="submit" class="subbutton" value="OK" /></p>
</form>
