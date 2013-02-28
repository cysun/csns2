<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $(".add").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
            {
                $("<span>").attr({
                    id: $(this).attr("id") + "-" + ui.item.id
                }).append(
                    $("<input>").attr({
                        type: "hidden",
                        name: $(this).attr("id"),
                        value: ui.item.id
                    })
                ).append(
                    $("<a>").attr({
                        href: "javascript:delete" + $(this).attr("id") + "(" + ui.item.id + ")"
                    }).text(ui.item.value)
                ).append(", ").insertBefore($(this));
                event.preventDefault();
                $(this).val("");
            }
        }
    });
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Full"
        });
    });
});
function deletestudents( memberId )
{
    var msg = "Are you sure you want to remove this student?";
    if( confirm(msg) )
      $("#students-"+memberId).remove();
}
function deleteadvisors( memberId )
{
    var msg = "Are you sure you want to remove this advisor?";
    if( confirm(msg) )
      $("#advisors-"+memberId).remove();
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/project/search' />">Projects</a></li>
<li><a class="bc" href="../projects">${project.department.name}</a></li>
<li><a class="bc" href="../project/view?id=${project.id}"><csns:truncate value="${project.title}" length="50" /></a></li>
<li>Edit</li>
</ul>

<form:form modelAttribute="project">
<table class="general">
  <tr>
    <th class="shrink">Title</th>
    <td>
      <form:input id="prj_title" path="title" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="title" /></div>
    </td>
  </tr>

  <tr>
    <th>Description</th>
    <td><form:textarea path="description" rows="5" cols="80" /></td>
  </tr>

  <tr>
    <th>Students</th>
    <td>
      <c:forEach items="${project.students}" var="student" varStatus="status">
        <span id="students-${student.id}">
          <a href="javascript:deletestudents(${student.id})">${student.name}</a>,
          <input type="hidden" name="students" value="${student.id}" />
        </span>
      </c:forEach>
      <input id="students" type="text" class="forminput add" name="s" style="width: 150px;" />
    </td>
  </tr>

  <tr>
    <th>Advisors</th>
    <td>
      <c:forEach items="${project.advisors}" var="advisor" varStatus="status">
        <span id="advisors-${advisor.id}">
          <a href="javascript:deleteadvisors(${advisor.id})">${advisor.name}</a>,
          <input type="hidden" name="advisors" value="${advisor.id}" />
        </span>
      </c:forEach>
      <input id="advisors" type="text" class="forminput add" name="a" style="width: 150px;" /></td>
  </tr>

  <tr>
    <th>Year</th>
    <td><form:input path="year" cssClass="smallerinput" /></td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Save" /></td></tr>
</table>
</form:form>
