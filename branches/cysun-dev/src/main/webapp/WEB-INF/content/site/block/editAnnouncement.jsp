<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
function removeAnnouncement()
{
    var msg = "Are you sure you want to remove this announcement?";
    if( confirm(msg) )
        window.location.href = "removeAnnouncement?id=${announcement.id}";
}
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li><a href="<c:url value='${section.siteUrl}/block/list' />" class="bc">Blocks</a></li>
<li>Edit Announcement</li>
<li class="align_right"><a href="javascript:removeAnnouncement()"><img title="Remove Announcement"
  alt="[Remove Announcement]" src="<c:url value='/img/icons/comment_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="announcement">
<form:textarea path="content" />
<div class="error"><form:errors path="content" /></div>
<p><input class="subbutton" type="submit" name="save" value="Save" /></p>
</form:form>

<script type="text/javascript">
  CKEDITOR.replaceAll();
</script>
