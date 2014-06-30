<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script type="text/javascript">
$(function() {
    $("#subfolders").hide();
    $("input[name='destId'][value='']").remove();
});
function toggleSubfolders()
{
    $("#subfolders").toggle();
}
</script>

<ul id="title">
<csns:fileBreadcrumbs file="${src}" />
</ul>

<table class="general">
<tr>
  <th>Copy</th>
  <td>${src.fullPath}</td>
</tr>

<tr>
  <th>To</th>
  <td>
    <c:if test="${empty dest}">/</c:if>
    <c:if test="${not empty dest}">${dest.fullPath}/</c:if>
    <c:if test="${fn:length(subfolders) > 0}">
    (<a href="javascript:toggleSubfolders()">select subfolders</a>)
    <div id="subfolders" style="margin: 0.5em 2em;">
      <c:forEach items="${subfolders}" var="subfolder">
        <c:if test="${subfolder.folder and subfolder.id != file.id}">
          <a href="copy?srcId=${src.id}&amp;destId=${subfolder.id}">${subfolder.name}</a><br />
        </c:if>
      </c:forEach>
    </div>
    </c:if>
  </td>
</tr>

</table>

<form action="copy" method="post" style="margin-top: 10px;">
<input type="hidden" name="srcId" value="${src.id}" />
<input type="hidden" name="destId" value="${dest.id}" />
<input type="submit" name="copy" class="subbutton" value="Copy" />
</form>
