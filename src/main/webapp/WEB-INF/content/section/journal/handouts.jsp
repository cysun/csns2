<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
function removeHandout( resourceId )
{
    var msg = "Are you sure you want to remove this handout?";
    if( confirm(msg) )
        window.location.href = "removeHandout?sectionId=${section.id}&resourceId="
                + resourceId;
}
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught?quarter=${section.quarter.code}#section-${section.id}' />"
       class="bc" >${section.course.code} - ${section.number}</a></li>
<li><a href="view?sectionId=${section.id}" class="bc">Course Journal</a></li>
<li>Handouts</li>
<li class="align_right"><a href="addHandout?sectionId=${section.id}"><img title="Add Handout" alt="[Add Handout]"
    src="<c:url value='/img/icons/plugin_add.png' />" /></a></li>
</ul>

<table class="viewtable autowidth">
  <tbody class="sortableElements">
  <c:forEach items="${section.journal.handouts}" var="handout">
  <tr data-handout-id="${handout.id}">
    <td><a href="<c:url value='/resource/view?id=${handout.id}' />">${handout.name}</a></td>
    <td>
      <a href="javascript:removeHandout(${handout.id})"><img title="Remove Handout"
         alt="[Remove Handout]" src="<c:url value='/img/icons/plugin_delete.png' />" /></a>
      <a href="editHandout?sectionId=${section.id}&amp;resourceId=${handout.id}"><img title="Edit Handout"
         alt="[Edit Item]" src="<c:url value='/img/icons/plugin_edit.png' />" /></a>
    </td>
  </tr>
  </c:forEach>
  </tbody>
</table>
