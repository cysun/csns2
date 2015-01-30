<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $(".add-to-group").autocomplete({
        source: "<c:url value='/autocomplete/course' />",
        select: function(event, ui) {
            var group = $(this).attr("data-group");
            if( ui.item )
                $.ajax({
                    url: "addCourse",
                    data: {
                        "courseId" : ui.item.id,
                        "group": group,
                        "_cid": "${_cid}"
                    },
                    cache: false,
                    success: function(data){
                        $("#"+group).append("<div class='course-mapping-item' " +
                            "id='course-" + ui.item.id + "'>" +
                            "<a href='javascript:removeCourse(" + ui.item.id + ")'>" +
                            ui.item.label + "</a></div>"
                        );
                    },
                    complete: function(){
                        $(".add-to-group").val("");
                    }
                });
        }
    });
});
function removeCourse( courseId )
{
    var msg = "Are you sure you want to remove this course from the mapping?";
    if( confirm(msg) )
        $.ajax({
            url: "removeCourse",
            data: {
                "courseId" : courseId,
                "_cid": "${_cid}"
            },
            cache: false,
            success: function(data){
                $("#course-" + courseId).remove();
            }
        });
}
function deleteMapping()
{
    var msg = "Are you sure you want to delete this course mapping?";
    if( confirm(msg) )
        window.location.href = "delete?id=${mapping.id}";
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/course/search' />">Courses</a></li>
<li><a class="bc" href="../../courses">${department.name}</a></li>
<li><a class="bc" href="list">Course Mappings</a></li>
<li>Edit Mapping</li>
<li class="align_right"><a href="javascript:deleteMapping()"><img alt="[Delete Course Mapping]"
  title="Delete Course Mapping" src="<c:url value='/img/icons/mapping_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="mapping">
<table class="viewtable">
  <tr><th>Group 1</th><th>Group 2</th></tr>
  <tr>
    <td id="group1">
      <c:forEach items="${mapping.group1}" var="course">
        <div class="course-mapping-item" id="course-${course.id}"><a
             href="javascript:removeCourse(${course.id})">${course.code} ${course.name}</a></div>
      </c:forEach>
    </td>
    <td id="group2">
      <c:forEach items="${mapping.group2}" var="course">
        <div class="course-mapping-item" id="course-${course.id}"><a
             href="javascript:removeCourse(${course.id})">${course.code} ${course.name}</a></div>
      </c:forEach>    
    </td>
  </tr>
  <tr>
    <td><input class="add-to-group" data-group="group1" type="text" style="width: 100%;" /></td>
    <td><input class="add-to-group" data-group="group2" type="text" style="width: 100%;" /></td>
  </tr>
  <tr>
    <td colspan="2">
      <input type="submit" class="subbutton" name="save" value="Save" />
    </td>
  </tr>
</table>
</form:form>
