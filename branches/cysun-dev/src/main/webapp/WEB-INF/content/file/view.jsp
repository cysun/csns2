<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function() {
    $("#newFolderForm").hide();
    $("#uploadFileForm").hide();
    $("table").tablesorter();
    $("input[name='parentId'][value='']").remove();
});
function toggleNewFolderForm()
{
	$("#uploadFileForm").hide();
    $("#newFolderForm").toggle();
}
function toggleUploadFileForm()
{
	$("#newFolderForm").hide();
    $("#uploadFileForm").toggle();
}
function toggleFilePublic( fileId )
{
    $.ajaxSetup({ cache: false });
    $("#file-" + fileId).load("<c:url value='/file/toggle?id=' />" + fileId);
}
</script>

<ul id="title">
<csns:fileBreadcrumbs file="${folder}" />
<c:if test="${not empty folder}">
  <li class="align_right"><a href="<c:url value='/download?folderId=${folder.id}' />"><img
    title="Download All Files" alt="[Download All Files]" src="<c:url value='/img/icons/download.png' />" /></a></li>
</c:if>
</ul>

<c:if test="${folder == null && user != null or folder.owner.id == user.id}">
<p>
<a href="javascript:toggleNewFolderForm()">New Folder</a> |
<a href="javascript:toggleUploadFileForm()">Upload File</a>
</p>

<form id="newFolderForm" action="create" method="post">
<p>
<input type="text" style="width: 20em;" name="name" class="forminput" />
<input type="hidden" name="parentId" value="${folder.id}" />
<input type="submit" name="submit" class="subbutton" value="Create Folder" />
</p>
</form>

<form id="uploadFileForm" action="upload" enctype="multipart/form-data" method="post">
<p>
<input type="file" size="40" name="file" />
<input type="hidden" name="parentId" value="${folder.id}" />
<input type="submit" name="submit" class="subbutton" value="Upload File" />
</p>
</form>
</c:if>

<table class="viewtable">
<thead>
<tr>
  <th>Name</th><th>Date</th><th>Size</th><th>Owner</th>
  <c:if test="${folder == null && user != null || user.id == folder.owner.id}"><th></th></c:if>
</tr>
</thead>
<tbody>
<c:forEach items="${files}" var="file">
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
  <td style="min-width: 25em;">
    <span style="margin-right: 0.5em;"><a id="file-${file.id}"
      href="javascript:toggleFilePublic(${file.id})"><img border="0" alt="[*]" title="Toggle Public/Private"
      src="<c:url value='/img/icons/${img}' />" /></a></span>
    <c:if test="${file.folder}">
      <a href="view?id=${file.id}">${file.name}</a>
    </c:if>
    <c:if test="${not file.folder}">
      <a href="<c:url value='/download?fileId=${file.id}' />">${file.name}</a>
    </c:if>
  </td>
  
  <td style="min-width: 9em;">
    <fmt:formatDate value="${file.date}" pattern="yyyy-MM-dd HH:mm:ss" />
  </td>
  
  <td style="white-space: nowrap; text-align: right;">
    <c:if test="${not file.folder}"><csns:fileSize value="${file.size}" /></c:if>
  </td>
  
  <td>
    ${file.owner.username}
  </td>
  
  <c:if test="${folder == null && user != null or folder.owner.id == user.id}">
  <td style="white-space: nowrap;">
    <a href="edit?id=${file.id}"><img alt="[Edit]" title="Edit"
       src="<c:url value='/img/icons/edit.gif'/>" border="0" /> Edit</a>
  </td>
  </c:if>
</tr>
</c:forEach>
</tbody>
</table>
