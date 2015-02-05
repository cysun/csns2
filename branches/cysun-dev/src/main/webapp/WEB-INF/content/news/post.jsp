<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $('#expireDate').datepicker({
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
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
  <li><a class="bc" href="current">News and Announcements</a></li>
  <li>Post</li>
</ul>

<form:form modelAttribute="news" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th>Category:</th>
    <td>
      <select name="forumId" size="1" class="leftinput">
      <c:forEach items="${news.department.forums}" var="forum">
        <option value="${forum.id}">${forum.name}</option>
      </c:forEach>
      </select>
    </td>
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
