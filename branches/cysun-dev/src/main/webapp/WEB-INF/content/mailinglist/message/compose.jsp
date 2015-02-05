<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
    $("#cancel").click(function(event){
       event.preventDefault();
       window.location.href = "<c:url value='${backUrl}' />";
    });
});
function addAttachment()
{
    $("#attachments").append("<br /><input name='file' class='leftinput' style='width: 100%;' type='file' size='75' />");
}
</script>

<ul id="title">
<li><a class="bc" href="../list">Mailing Lists</a></li>
<li><a class="bc" href="../view?id=${message.mailinglist.id}">${message.mailinglist.name}</a></li>
<li>Email</li>
</ul>

<form:form modelAttribute="message" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th>To:</th>
    <td><span class="tt">${message.mailinglist.name}</span></td>
  </tr>
  <tr>
    <th>Subject:</th>
    <td>
      <form:input path="subject" cssClass="leftinput" cssStyle="width: 99%;" />
      <div class="error"><form:errors path="subject" /></div>
    </td>
  </tr>
  <tr>
    <th>Content:</th>
    <td>
      <form:textarea id="textcontent" path="content" cssClass="leftinput" cssStyle="width: 99%;" rows="15" cols="75" />
      <div class="error"><form:errors path="content" /></div>
    </td>
  </tr>
  <tr>
    <th>Attachments (<a href="javascript:addAttachment()">+</a>)</th>
    <td id="attachments">
      <input name="file" class="leftinput" style="width: 100%;" type="file" size="75" /> 
    </td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" name="send" value="Send" />
    </td>
  </tr>
</table>
</form:form>
