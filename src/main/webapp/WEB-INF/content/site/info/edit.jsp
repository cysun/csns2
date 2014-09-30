<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
function deleteEntry()
{
    var msg = "Are you sure you want to delete this entry?";
    if( confirm(msg) )
        window.location.href = "delete?index=${param.index}";
}
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li><a href="<c:url value='${section.siteUrl}/info/list' />" class="bc">Class Info</a></li>
<li>Edit Entry</li>
<li class="align_right"><a href="javascript:deleteEntry()"><img title="Delete Entry"
  alt="[Delete Entry]" src="<c:url value='/img/icons/table_delete.png' />" /></a></li>
</ul>

<div class="site-title">${site.section.course.code} ${site.section.course.name}</div>
<div class="site-quarter">${site.section.quarter}</div>

<form:form modelAttribute="infoEntry">
<table class="general autowidth" style="margin-left: 10px">
<tr>
  <th><form:input path="name" cssClass="forminput" cssStyle="width: 150px;" /></th>
  <td><form:input path="value" cssClass="forminput" cssStyle="width: 600px;" /></td>
  <td><input type="submit" class="subbutton" name="save" value="Save" /></td>
</tr>
</table>
<input type="hidden" name="index" value="${param.index}" />
</form:form>
