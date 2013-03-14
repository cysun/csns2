<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="backUrl" value="${param.backUrl}" />
<c:if test="${empty backUrl}">
  <c:set var="backUrl" value="/" />
</c:if>

<script>
$(function(){
    $(".add").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
            {
                $("<span>").attr({
                    id: $(this).attr("id") + "-" + ui.item.id
                }).append(
                    $("<input>").attr({
                        type: "hidden",
                        name: "userId",
                        value: ui.item.id
                    })
                ).append(
                    $("<a>").attr({
                        href: "javascript:delete" + $(this).attr("id") + "(" + ui.item.id + ")"
                    }).text(ui.item.value)
                ).append(", ").insertBefore($(this));
                event.preventDefault();
                $(this).val("");
            }
        }
    });
    $("#cancel").click(function(event){
       event.preventDefault();
       window.location.href = "<c:url value='${backUrl}' />";
    });
});
function deleterecipient( recipientId )
{
    var msg = "Are you sure you want to remove this recipient?";
    if( confirm(msg) )
      $("#recipient-"+recipientId).remove();
}
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
    <td style="line-height: 200%;">
      <c:forEach items="${email.recipients}" var="recipient" varStatus="status">
        <span id="recipient-${recipient.id}">
          <csns:verify user="${recipient}">${recipient.name}</csns:verify>,
          <input name="userId" type="hidden" value="${recipient.id}" />
        </span>
      </c:forEach>
      <input id="recipient" type="text" class="forminput add" name="r" style="width: 150px;" />
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
