<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${submission.assignment.section}"/>
<c:set var="assignment" value="${submission.assignment}"/>

<script>
$(function(){
    $("table").tablesorter({
        sortList: [[0,0]]
    });
});
function remove( fileId )
{
    var msg = "Are you sure you want to remove this file?";
    if( confirm(msg) )
        $.ajax({
            url: "remove?fileId=" + fileId,
            success: function(){
                $("#row-" + fileId).remove();
            },
            cache: false
        });
}
function toggleFilePublic( fileId )
{
    $.ajaxSetup({ cache: false });
    $("#file-" + fileId).load("<c:url value='/file/toggle?id=' />" + fileId);
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="list?assignmentId=${assignment.id}">${assignment.name}</a></li>
<li><a class="bc" href="grade?id=${submission.id}">${submission.student.name}</a></li>
<li>Files</li>
</ul>

<form method="post" action="upload" enctype="multipart/form-data"><p>
File: <input type="file" name="uploadedFile" size="50" />
<input type="submit" class="subbutton" value="Upload" />
<input type="hidden" name="id" value="${submission.id}" />
<input type="hidden" name="additional" value="true" />
</p></form>

<c:if test="${fn:length(submission.files) > 0}">
<table class="viewtable">
<thead><tr><th>Name</th><th class="shrink">Size</th><th class="datetime">Date</th><th></th></tr></thead>
<tbody>
  <c:forEach items="${submission.files}" var="file">
    <c:choose>
      <c:when test="${file.isPublic()}"><c:set var="img" value="open_book.png" /></c:when>
      <c:otherwise><c:set var="img" value="closed_book.png" /></c:otherwise>
    </c:choose>
  <tr id="row-${file.id}">
    <td>
      <span style="margin-right: 0.5em;"><a id="file-${file.id}"
            href="javascript:toggleFilePublic(${file.id})"><img border="0" alt="[*]" title="Toggle Public/Private"
            src="<c:url value='/img/icons/${img}' />" /></a></span>    
      <a href="<c:url value='/download?fileId=${file.id}' />">${file.name}</a>
    </td>
    <td class="shrink"><csns:fileSize value="${file.size}" /></td>
    <td class="datetime"><fmt:formatDate value="${file.date}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
    <td class="action">
      <a href="javascript:remove(${file.id})"><img alt="[Remove File]"
         title="Remove File" src="<c:url value='/img/icons/script_delete.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
