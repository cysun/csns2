<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function() {
    $("#renameForm").hide();
    $("#replaceForm").hide();
});
function toggleRenameForm()
{
    $("#replaceForm").hide();
    $("#renameForm").toggle();
}
function toggleReplaceForm()
{
    $("#renameForm").hide();
    $("#replaceForm").toggle();
}
function toggleFilePublic( fileId )
{
    $.ajaxSetup({ cache: false });
    $("#file-" + fileId).load("<c:url value='/file/toggle?id=' />" + fileId);
}
function deleteFile()
{
    var msg = "Are you sure you want to delete this file?";
    <c:if test="${file.folder}">
    msg += " ALL files under this folder will be deleted as well.";
    </c:if>

    if( confirm(msg) )
        window.location.href = "delete?id=${file.id}";
}
</script>

<ul id="title">
<csns:fileBreadcrumbs file="${file}" />
</ul>

<p>
<a href="javascript:toggleRenameForm()">Rename</a> |
<c:if test="${not file.folder}">
<a href="javascript:toggleReplaceForm()">Replace</a> |
</c:if>
<a href="copy?srcId=${file.id}">Copy</a> |
<a href="move?srcId=${file.id}">Move</a> |
<a href="javascript:deleteFile()">Delete</a>
</p>

<form id="renameForm" action="rename" method="post">
<p>
<input type="text" style="width: 20em;" name="name" value="${file.name}" class="forminput" />
<input type="hidden" name="id" value="${file.id}" />
<input type="submit" name="submit" class="subbutton" value="Rename" />
</p>
</form>

<form id="replaceForm" action="replace" method="post" enctype="multipart/form-data">
<p>
<input type="file" size="40" name="file" />
<input type="hidden" name="id" value="${file.id}" />
<input type="submit" name="submit" class="subbutton" value="Replace" />
</p>
</form>

<table class="general">

<c:choose>
  <c:when test="${file.folder and file.isPublic()}">
    <c:set var="img" value="open_folder.png" />
  </c:when>
  <c:when test="${file.folder and not file.isPublic()}">
    <c:set var="img" value="closed_folder.png" />
  </c:when>
  <c:when test="${not file.folder and file.isPublic()}">
    <c:set var="img" value="open_book.png" />
  </c:when>
  <c:otherwise>
    <c:set var="img" value="closed_book.png" />
  </c:otherwise>
</c:choose>

<tr>
  <th>Name:</th>
  <td style="min-width: 25em;">
    <span style="margin-right: 0.5em;"><a id="file-${file.id}"
      href="javascript:toggleFilePublic(${file.id})"><img
      border="0" alt="[*]" title="Toggle Public/Private" src="<c:url value='/img/icons/${img}' />" /></a></span>
    <c:if test="${file.folder}">
      <a href="view?id=${file.id}">${file.name}</a>
    </c:if>
    <c:if test="${not file.folder}">
      <a href="<c:url value='/download?fileId=${file.id}' />">${file.name}</a>
    </c:if>
  </td>
</tr>

<tr>
  <th>Date:</th>
  <td>
    <fmt:formatDate value="${file.date}" pattern="yyyy-MM-dd HH:mm:ss" />
  </td>
</tr>

<c:if test="${not file.folder}">
<tr>
  <th>Size:</th>
  <td>
    <csns:fileSize value="${file.size}" />
  </td>
</tr>
</c:if>

<tr>
  <th>Owner:</th>
  <td>
    ${file.owner.username}
  </td>
</tr>

</table>
