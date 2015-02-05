<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li><a href="<c:url value='${section.siteUrl}/block/list' />" class="bc">Blocks</a></li>
<li>Add Announcement</li>
</ul>

<form:form modelAttribute="announcement">
<form:textarea path="content" />
<div class="error"><form:errors path="content" /></div>
<p><input class="subbutton" type="submit" name="add" value="Add" /></p>
</form:form>

<script type="text/javascript">
  CKEDITOR.replaceAll();
</script>
