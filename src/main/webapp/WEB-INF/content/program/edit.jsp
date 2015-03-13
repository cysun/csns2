<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $(".add-course").autocomplete({
        source: "<c:url value='/autocomplete/course' />",
        select: function(event, ui) {
            var courseType = $(this).attr("data-course-type");
            if( ui.item )
                $.ajax({
                    url: "addCourse",
                    data: {
                        "courseId" : ui.item.id,
                        "courseType": courseType,
                        "_cid": "${_cid}"
                    },
                    cache: false,
                    success: function(data){
                        $("#"+courseType+"Table > tbody:last").append(
                            "<tr id='course-" + ui.item.id + "'>" + 
                            "<td>" + ui.item.label + "</td>" +
                            "<td class='center'><a href='javascript:removeCourse(" + ui.item.id + ",\"" + courseType + "\")'>" +
                            "<img title='Remove' alt='[Remove]' border='0' src='<c:url value='/img/icons/delete.png' />' />" +
                            "</a></td></tr>"
                        );
                    },
                    complete: function(){
                        $(".add-course").val("");
                    }
                });
        }
    });
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Default"
        });
    });
});
function removeCourse( courseId, courseType )
{
    var msg = "Are you sure you want to remove this course from the mapping?";
    if( confirm(msg) )
        $.ajax({
            url: "removeCourse",
            data: {
                "courseId" : courseId,
                "courseType" : courseType,
                "_cid": "${_cid}"
            },
            cache: false,
            success: function(data){
                $("#course-" + courseId).remove();
            }
        });
}
function removeProgram()
{
    var msg = "Are you sure you want to remove this program?";
    if( confirm(msg) )
        window.location.href = "remove?id=${program.id}";
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/program/search' />">Programs</a></li>
<li><a class="bc" href="../programs">${program.department.name}</a></li>
<li><a class="bc" href="view?id=${program.id}"><csns:truncate value="${program.name}" length="50" /></a></li>
<li>Edit</li>
<li class="align_right"><a href="javascript:removeProgram()"><img alt="[Remove Program]"
  title="Remove Program" src="<c:url value='/img/icons/report_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="program">
<table class="general">
  <tr>
    <th class="shrink">Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>
  <tr>
    <th>Description</th>
    <td><form:textarea path="description" rows="5" cols="80" /></td>
  </tr>
</table>

<h4>Required Courses</h4>
<table id="requiredTable" class="viewtable autowidth">
<thead><tr><th>Course</th><th></th></tr></thead>
<tbody>
<c:forEach items="${program.requiredCourses}" var="course">
<tr id="course-${course.id}">
  <td>${course.code} ${course.name}</td>
  <td class="center"><a href="javascript:removeCourse(${course.id},'required')"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
</tr>
</c:forEach>
</tbody>
</table>
<p><input type="text" class="forminput add-course" data-course-type="required"
          size="40" placeholder="Search for courses to add" /></p>

<h4>Elective Courses</h4>
<table id="electiveTable" class="viewtable autowidth">
<thead><tr><th>Course</th><th></th></tr></thead>
<tbody>
<c:forEach items="${program.electiveCourses}" var="course">
<tr id="course-${course.id}">
  <td>${course.code} ${course.name}</td>
  <td class="center"><a href="javascript:removeCourse(${course.id},'elective')"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
</tr>
</c:forEach>
</tbody>
</table>
<p><input type="text" class="forminput add-course" data-course-type="elective"
          size="40" placeholder="Search for courses to add" /></p>

<p><input type="submit" class="subbutton" value="Save" /></p>
</form:form>
