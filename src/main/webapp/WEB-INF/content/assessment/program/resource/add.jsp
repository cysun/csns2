<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
});
</script>

<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li><a href="../edit?id=${program.id}" class="bc">${program.name}</a></li>
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
