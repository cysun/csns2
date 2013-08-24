<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("textarea").each(function(){
        var cke = CKEDITOR.instances[$(this).attr("id")];
        if( cke ) CKEDITOR.remove(cke);
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "BasicWithAttach"
        });
    });
    $("div.help").dialog({
        autoOpen: false,
        modal: true
    });
    $("#cancel").click(function(){
        window.location.href = "../view?id=${record.student.id}#ui-tabs-3";
    });
});
function deleteAttachment( fileId )
{
    var msg = "Are you sure you want to remove this attachment?";
    if( confirm(msg) )
        $.ajax({
            url: "deleteAttachment",
            data: { "fileId": fileId, "_cid": "${_cid}" },
            success: function(){
                $("#attachment-"+fileId).remove();
            },
            cache: false
        });
}
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
<li><a class="bc" href="search">Users</a></li>
<li><a class="bc" href="../view?id=${record.student.id}#ui-tabs-3">${record.student.name}</a></li>
<li>Edit Advisement Record</li>
</ul>

<form:form modelAttribute="record" enctype="multipart/form-data">
  <p><csns:help name="fao" img="false">For advisors only</csns:help>:
  <form:checkbox path="forAdvisorsOnly"/></p>
  <form:textarea path="comment" />
  <c:if test="${fn:length(record.attachments) > 0}"><p></p>
  <table class="viewtable autowidth">
    <c:forEach items="${record.attachments}" var="attachment">
    <tr id="attachment-${attachment.id}">
      <td><a href="<c:url value='/download?fileId=${attachment.id}' />">${attachment.name}</a></td>
      <td><a href="javascript:deleteAttachment(${attachment.id})"><img alt="[Delete Attachment]"
             title="Delete This Attachment" src="<c:url value='/img/icons/cross.png' />" /></a></td>
    </tr>
    </c:forEach>
  </table>
  </c:if>
  <p><button id="cancel" type="button" class="subbutton">Cancel</button> &nbsp;
  <input type="submit" name="submit" class="subbutton" value="OK" /></p>
</form:form>

<div id="help-fao" class="help">
The student won't be able to see the comments marked as <em>for advisors
only</em>.</div>
