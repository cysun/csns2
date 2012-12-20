<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
function deleteSection( id )
{
    var msg = "Are you sure you want to delete this section?";
    if( confirm(msg) )
        window.location.href = "delete?id=" + id;
}
function removeInstructor( instructorId )
{
    var msg = "Are you sure you want to remove this instructor?"
    if( confirm(msg) )
        window.location.href = "edit?id=${section.id}&instructor=remove&instructorId=" + instructorId;
}
$(function() {
    var instructors = ${instructors};
    $(".add").each(function(){
        $(this).autocomplete({
            source: instructors,
            select: function(event, ui) {
                if( ui.item )
                    $("<input>").attr({
                        type: "hidden",
                        name: "instructorId",
                        value: ui.item.id
                    }).appendTo($(this).parent());
            }
        });
    });
    $(".clear").each(function(){
       $(this).click(function(event){
           event.preventDefault();
           $("input[name='instructorId']").remove();
           $(".add").each(function(){
              $(this).val("");
           });
       });
    });
    $("#backBtn").click(function(){
        window.location.href="taught#section-" + ${section.id}; 
    });
});
</script>

<ul id="title">
<li><a class="bc" href="taught#section-${section.id}">${section.course.code} - ${section.number}</a></li>
<li>Edit Section</li>
<li class="align_right"><a href="javascript:deleteSection(${section.id})"><img title="Delete Section"
    alt="[Delete Section]" src="<c:url value='/img/icons/table_delete.png' />" /></a></li>
</ul>

<table class="general">
  <tr><th>Quarter</th><td>${section.quarter}</td>
  <tr><th>Section</th><td>${section.course.code} ${section.course.name} - ${section.number}</td></tr>
  <tr>
    <th rowspan="2" style="vertical-align: top;">Instructor(s)</th>
    <td>
      <c:forEach items="${section.instructors}" var="instructor" varStatus="status">
        <a href="javascript:removeInstructor(${instructor.id})">${instructor.name}</a><c:if
           test="${not status.last}">, </c:if>
      </c:forEach>
    </td>
  </tr>
  <tr>
    <td>
      <form id="addInstructorForm" action="edit?instructor=add" method="post">
        <input type="text" class="forminput add" name="name" size="40" />
        <input type="hidden" name="id" value="${section.id}" />
        <input type="submit" class="subbutton" name="add" value="Add" />
        <button class="subbutton clear">Clear</button>
      </form>
    </td>
  </tr>
  <tr>
    <th></th>
    <td>
      <button id="backBtn" class="subbutton">Done</button>
    </td>
  </tr>
</table>
