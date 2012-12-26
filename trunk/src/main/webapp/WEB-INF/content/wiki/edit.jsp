<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
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
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
<csns:wikiBreadcrumbs path="${revision.page.path}" />
<li>Edit</li>
</ul>

<form:form modelAttribute="revision" enctype="multipart/form-data">
<input type="hidden" name="path" value="${param.path}" />
<table class="general">
<tr>
  <th><csns:help name="title">Title</csns:help></th>
  <td>
    <form:input path="subject" cssClass="leftinput" cssStyle="width: 98%;" />
    <div class="error"><form:errors path="subject" /></div>
  </td>
</tr>
<tr>
  <td colspan="2">
    <div class="error"><form:errors path="content" /></div>
    <form:textarea id="textcontent" path="content" rows="10" cols="60" />
  </td>
</tr>
<tr>
  <th>Options</th>
  <td>
    Include Sidebar: <form:checkbox path="includeSidebar" cssStyle="margin-right: 1em;" />
    <csns:help name="locked" img="false">Locked</csns:help>:
      <form:checkbox path="page.locked" cssStyle="margin-right: 1em;" />
    <csns:help name="password" img="false">Password</csns:help>:
      <form:input path="page.password" cssClass="leftinput" /> 
  </td>
</tr>
<tr>
  <th></th>
  <td><input type="submit" class="subbutton" name="submit" value="Save" /></td>
</tr>
</table>
</form:form>

<div id="help-title" class="help">
<em>Title</em> will be displayed in the title bar of a browser window.</div>

<div id="help-locked" class="help">
A locked page can only be edited by its original author.</div>

<div id="help-password" class="help">
<p>You may specify a <em>password</em> to protect this page so that anyone
who tries to access this page must enter the password first. Note that this
password is for this page only, i.e. it has nothing to do with your account
password or anything.</p>
<p>If you don't want this page to be password-protected, simply leave this
field empty.</p></div>
