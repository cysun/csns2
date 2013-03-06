<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
    $(".add").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
            	$("<input>").attr({
            	    type: "hidden",
            	    name: $(this).attr("id"),
            	    value: ui.item.id
            	}).appendTo("form");
            event.preventDefault();
            $(this).before(ui.item.value + ", ");
            $(this).val("");
        }
    });
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Default"
        });
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/project/search' />">Projects</a></li>
<li><a class="bc" href="../projects?year=${project.year}">${project.department.name}</a></li>
<li>Add Project</li>
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
    <td><input id="students" type="text" class="forminput add" name="s" style="width: 150px;" /></td>
  </tr>

  <tr>
    <th>Advisors</th>
    <td><input id="advisors" type="text" class="forminput add" name="a" style="width: 150px;" /></td>
  </tr>

  <tr>
    <th>Year</th>
    <td><form:input path="year" cssClass="smallerinput" /></td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Add" /></td></tr>
</table>
</form:form>
