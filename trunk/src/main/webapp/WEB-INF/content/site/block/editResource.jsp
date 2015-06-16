<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
function removeResource()
{
    var msg = "Are you sure you want to remove this resource?";
    if( confirm(msg) )
        window.location.href = "removeResource?_cid=${_cid}&resourceIndex=${param.resourceIndex}";
}
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li><a href="<c:url value='${section.siteUrl}/block/list' />" class="bc">Blocks</a></li>
<li>Edit Additional Resource</li>
<li class="align_right"><a href="javascript:removeResource()"><img title="Remove Resource"
  alt="[Remove Resource]" src="<c:url value='/img/icons/page_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="resource" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th class="shrink">Block</th>
    <td>${block.name}</td>
  </tr>
  
  <tr>
    <th>Item</th>
    <td>${item.name}</td>
  </tr>

  <tr>
    <th>Resource Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>
  
  <tr>
    <th>Item Type</th>
    <td>
      <form:select path="type">
        <form:options items="${resourceTypes}" />
      </form:select>
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

  <tr>
    <th></th>
    <td><input class="subbutton" type="submit" name="save" value="Save" /></td>
  </tr>
</table>
<input type="hidden" name="blockId" value="${block.id}" />
</form:form>
