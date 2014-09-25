<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script>
$(function(){
    $(".res").hide();
    if($("#resource\\.type").val() != "None")
        $("#res"+$("#resource\\.type").val()).show();
    $("#resource\\.type").click(function(){
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
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li><a href="<c:url value='${section.siteUrl}/block/list' />" class="bc">Blocks</a></li>
<li>Add Item</li>
</ul>

<form:form modelAttribute="item" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th class="shrink">Block</th>
    <td>${block.name}</td>
  </tr>
  
  <tr>
    <th>Item Name</th>
    <td>
      <form:input path="resource.name" cssClass="leftinput" cssStyle="width: 99%;" />
      <div class="error"><form:errors path="resource.name" /></div>
    </td>
  </tr>
  
  <tr>
    <th>Item Type</th>
    <td>
      <form:select path="resource.type">
        <form:options items="${resourceTypes}" />
      </form:select>
  </tr>

  <tr id="resTEXT" class="res">
    <th></th>
    <td>
      <form:textarea path="resource.text" rows="5" cols="80" />
      <div class="error"><form:errors path="resource.text" /></div>
    </td>
  </tr>

  <tr id="resFILE" class="res">
    <th></th>
    <td>
      <input name="uploadedFile" type="file" size="80" style="width: 99%;" class="leftinput">
      <div class="error"><form:errors path="resource.file" /></div>
    </td>
  </tr>

  <tr id="resURL" class="res">
    <th></th>
    <td>
      <form:input path="resource.url" cssClass="leftinput" cssStyle="width: 99%;" placeholder="http://" />
      <div class="error"><form:errors path="resource.url" /></div>
    </td>
  </tr>

  <tr>
    <th></th>
    <td><input class="subbutton" type="submit" name="add" value="Add" /></td>
  </tr>
</table>
<input type="hidden" name="blockId" value="${param.blockId}" />
</form:form>
