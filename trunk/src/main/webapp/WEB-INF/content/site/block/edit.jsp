<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
function removeBlock( id )
{
    var msg = "Are you sure you want to remove this block?";
    if( confirm(msg) )
        window.location.href = "remove?id=" + id;
}
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li><a href="<c:url value='${section.siteUrl}/block/list' />" class="bc">Blocks</a></li>
<li>Edit Block</li>
<li class="align_right"><a href="javascript:removeBlock(${block.id})"><img title="Remove Block"
  alt="[Remove Block]" src="<c:url value='/img/icons/brick_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="block">
<table class="general">
  <tr>
    <th class="shrink">Block Type</th>
    <td>
      <form:select path="type">
        <form:options items="${blockTypes}" />
      </form:select>
  </tr>
  <tr>
    <th>Block Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>
  <tr>
    <th></th>
    <td><input class="subbutton" type="submit" name="save" value="Save" /></td>
  </tr>
</table>
</form:form>
