<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="backUrl" value="${param.backUrl}" />
<c:if test="${empty backUrl}">
  <c:set var="backUrl" value="/" />
</c:if>

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
<li>Email</li>
</ul>

<form:form action="compose" modelAttribute="email" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th>From</th>
    <td>${email.author.name}</td>
  </tr>
  <tr>
    <th>To</th>
    <td>
      <c:forEach items="${email.recipients}" var="recipient" varStatus="status">
      ${recipient.name}<c:if test="${not status.last}">, </c:if>
      <input name="userId" type="hidden" value="${recipient.id}" />
      </c:forEach>
      <div class="error"><form:errors path="recipients" /></div>
    </td>
  </tr>
  <tr>
    <th>Subject</th>
    <td>
      <form:input path="subject" cssClass="leftinput" cssStyle="width: 99%;" />
      <div class="error"><form:errors path="subject" /></div>
    </td>
  </tr>
  <tr>
    <th>Content</th>
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
      <button id="cancel" type="button" class="subbutton">Cancel</button> &nbsp; 
      <input type="submit" class="subbutton" name="send" value="Send" />
    </td>
  </tr>
</table>
<input type="hidden" name="backUrl" value="${backUrl}" />
</form:form>
