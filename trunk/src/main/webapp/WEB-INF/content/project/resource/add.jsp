<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $(".res").hide();
    if($("#type").val() != "None")
        $("#res"+$("#type").val()).show();
    $("#type").click(function(){
        $(".res").hide();
        $("#res"+$(this).val()).show();
    });
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Default"
        });
    });
    $("div.help").dialog({
        autoOpen: false,
        modal: true
    });
});
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/project/search' />">Projects</a></li>
<li><a class="bc" href="../../projects?year=${project.year}">${fn:toUpperCase(dept)}</a></li>
<li><a class="bc" href="../view?id=${project.id}"><csns:truncate value="${project.title}" length="60" /></a></li>
<li>Add Resource</li>
</ul>

<form:form modelAttribute="resource" enctype="multipart/form-data">
<table class="general">

  <tr>
    <th class="shrink">Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th class="shrink">Type</th>
    <td>
      <form:select path="type">
        <form:option label="File" value="FILE" />
        <form:option label="Text" value="TEXT" />
        <form:option label="URL" value="URL" />
      </form:select>
    </td>
  </tr>

  <tr>
    <th><csns:help name="private">Private</csns:help></th>
    <td><form:checkbox path="private" /></td>
  </tr>

  <tr id="resTEXT" class="res">
    <th></th>
    <td>
      <form:textarea path="text" rows="5" cols="80" />
      <div class="error"><form:errors path="text" /></div>
    </td>
  </tr>

  <tr id="resFILE" class="res">
    <th></th>
    <td>
      <input name="uploadedFile" type="file" size="80" style="width: 99%;" class="leftinput">
      <div class="error"><form:errors path="file" /></div>
    </td>
  </tr>

  <tr id="resURL" class="res">
    <th></th>
    <td>
      <form:input path="url" cssClass="leftinput" cssStyle="width: 99%;" placeholder="http://" />
      <div class="error"><form:errors path="url" /></div>
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Add" /></td></tr>
</table>
</form:form>

<div id="help-private" class="help">
A <em>private</em> resource can only be accessed by the people who are involved
in the project, i.e. the students, advisors, and project liaisons.</div>
