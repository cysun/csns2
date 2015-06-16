<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
function removeItem( blockId, itemId )
{
    var msg = "Are you sure you want to delete this item?";
    if( confirm(msg) )
        window.location.href = "removeItem?blockId=${block.id}&itemId=${item.id}";
}
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li><a href="<c:url value='${section.siteUrl}/block/list' />" class="bc">Blocks</a></li>
<li>Edit Item</li>
<li class="align_right"><a href="javascript:removeItem()"><img title="Remove Item"
  alt="[Remove Item]" src="<c:url value='/img/icons/plugin_delete.png' />" /></a></li>
<li class="align_right"><a href="addResource.html?_cid=${_cid}"><img
  title="Add Additional Resource" alt="[Add Additional Resource]"
  src="<c:url value='/img/icons/page_add.png' />" /></a></li>
</ul>

<form:form modelAttribute="item" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th class="shrink">Block</th>
    <td>
      <select name="newBlockId">
        <c:forEach items="${section.site.blocks}" var="siteBlock">
          <c:if test="${siteBlock.type == 'REGULAR'}">
          <option value="${siteBlock.id}" <c:if test="${siteBlock.id == block.id}">selected</c:if>>
            ${siteBlock.name}
          </option>
          </c:if>
        </c:forEach>
      </select>
    </td>
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
      <input name="uploadedFile" type="file" class="leftinput">
      <c:if test="${item.resource.type == 'FILE' and item.resource.file != null}">
        <span style="margin-left: 2em;">
        <a href="<c:url value='/download?fileId=${item.resource.file.id}' />">${item.resource.file.name}</a>
        </span>
      </c:if>
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

<c:if test="${fn:length(item.additionalResources) > 0}">
  <tr>
    <th>Additional Resources</th>
    <td>
      <table class="viewtable autowidth">
        <tbody>
        <c:forEach items="${item.additionalResources}" var="resource" varStatus="status">
        <tr data-block-id="${block.id}" data-item-id="${item.id}" data-resource-index="${status.index}">
          <td><a href="../resource/${resource.id}">${resource.name}</a></td>
          <td><a href="editResource?_cid=${_cid}&amp;resourceIndex=${status.index}"><img
            title="Edit Resource" alt="[Edit Resource]" src="<c:url value='/img/icons/page_edit.png' />" /></a></td>
        </tr>
        </c:forEach>
        </tbody>
      </table>
    </td>
  </tr>
</c:if>

  <tr>
    <th></th>
    <td><input class="subbutton" type="submit" name="save" value="Save" /></td>
  </tr>
</table>
</form:form>
