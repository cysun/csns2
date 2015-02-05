<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#expireDate").datepicker({
        inline: true
    });
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Default"
        });
    });
    $("div.help").dialog({
        autoOpen: false,
        modal: true
    });
});
function addAttachment()
{
    $("#attachments").append("<br /><input name='file' class='leftinput' style='width: 100%;' type='file' size='75' />");
}
function deleteAttachment( fileId )
{
    var msg = "Are you sure you want to remove this attachment?";
    if( confirm(msg) )
        $.ajax({
            url: "deleteAttachment",
            data: { "newsId": ${news.id}, "fileId": fileId, "_cid": "${_cid}" },
            success: function(){
                $("#attachment-"+fileId).remove();
            },
            cache: false
        });
}
function deleteNews()
{
    var msg = "Do you want to remove this news entry (i.e. expire it immediately)?";
    if( confirm(msg) )
        window.location.href = "delete?id=${news.id}";
}
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
  <li><a class="bc" href="current">News and Announcements</a></li>
  <li><csns:truncate value="${news.topic.firstPost.subject}" /></li>
  <li class="align_right"><a href="javascript:deleteNews()"><img title="Delete News"
  alt="[Delete News]" src="<c:url value='/img/icons/newspaper_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="news" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th>Category:</th>
    <td>${news.topic.forum.name}</td>
  </tr>
  <tr>
    <th>Subject</th>
    <td>
      <form:input path="topic.firstPost.subject" cssClass="leftinput" cssStyle="width: 99%;" />
      <div class="error"><form:errors path="topic.firstPost.subject" /></div>
    </td>
  </tr>
  <tr>
    <th>Content</th>
    <td>
      <form:textarea id="textcontent" path="topic.firstPost.content" cssStyle="width: 99%;" rows="15" cols="80" />
      <div class="error"><form:errors path="topic.firstPost.content" /></div>
    </td>
  </tr>
  <tr>
    <th><csns:help name="expdate">Expiration Date</csns:help></th>
    <td>
      <form:input path="expireDate" cssClass="smallinput" size="10" maxlength="10" />
    </td>
  </tr>
  <tr>
    <th>Attachments
      (<a href="javascript:addAttachment()">+</a>)
    </th>
    <td id="attachments">
      <input name="file" class="leftinput" style="width: 100%;" type="file" size="80" />
    </td>
  </tr>
<c:if test="${fn:length(news.topic.firstPost.attachments) > 0}">
  <tr>
    <th></th>
    <td>
      <table class="viewtable autowidth">
        <c:forEach items="${news.topic.firstPost.attachments}" var="attachment">
        <tr id="attachment-${attachment.id}">
          <td><a href="<c:url value='/download?fileId=${attachment.id}' />">${attachment.name}</a></td>
          <td><a href="javascript:deleteAttachment(${attachment.id})"><img alt="[Delete Attachment]"
                 title="Delete This Attachment" src="<c:url value='/img/icons/delete.png' />" /></a></td>
        </tr>
        </c:forEach>
      </table>
    </td>
  </tr>
</c:if>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" name="submit" value="Submit" />
    </td>
  </tr>
</table>
</form:form>

<div id="help-expdate" class="help">
Each news entry should have an <em>expiration date</em>, after which the
entry will be removed automatically from the front page.</div>
