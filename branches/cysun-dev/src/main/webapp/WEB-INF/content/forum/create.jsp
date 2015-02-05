<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#members").hide();
    $("input[name='membersOnly']").change(function(){
        $("#members").toggle();
    });
    $("#search").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
                $("<input>").attr({
                    type: "hidden",
                    name: "userId",
                    value: ui.item.id
                }).appendTo($(this).parent());
        }
    })
    .autocomplete( "instance" )._renderItem = function(ul, item){
        var li = $("<li>");
        var downloadUrl = "<c:url value='/download.html?fileId=' />";
        if( item.thumbnail )
            li.append("<img src='" + downloadUrl + item.thumbnail + "' alt='' style='vertical-align: middle; margin-right: 0.5em;' />");
        return li.append(item.label).appendTo(ul);
    };
    $("#clear").click(function(event){
        event.preventDefault();
        $("input[name='userId']").remove();
        $("#search").val("");
    });
    $("#add").click(function(event){
       event.preventDefault();
       $.ajax({
           url: "addMember",
           data: {
               "userId" : $("input[name='userId']").val(),
               "_cid": "${_cid}"
           },
           cache: false,
           success: function(data){
               $("#membersTable > tbody:last").append(
                 "<tr id='tr-" + data.id + "'>" + 
                 "<td>" + data.firstName + " " + data.lastName + "</td>" +
                 "<td>" + data.username + "</td>" +
                 "<td>" + data.primaryEmail + "</td>" +
                 "<td class='center'><a href='javascript:removeMember(" + data.id + ")'>" +
                 "<img title='Remove' alt='[Remove]' border='0' src='<c:url value='/img/icons/delete.png' />' />" +
                 "</a></td></tr>"
               );
               $("input[name='userId']").remove();
               $("#search").val("");
           }
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
function removeMember( userId )
{
    $.ajax({
       url: "removeMember",
       data: {
           "userId" : userId,
           "_cid": "${_cid}"
       },
       cache: false,
       success: function(){
           $("#tr-"+userId).remove();
       }
    });
}
</script>

<ul id="title">
<li><a class="bc" href="list">Forums</a></li>
<li>Create Forum</li>
</ul>

<form:form modelAttribute="forum">
<table class="general autowidth">
  <tr>
    <th>Department</th>
    <td>${forum.department.name}</td>
  </tr>
  <tr>
    <th>Name *</th>
    <td>
      <form:input path="name" cssClass="forminput" cssStyle="width: 600px;" />
      <div class="error"><form:errors path="name"  /></div>
    </td>
  </tr>
  <tr>
    <th><csns:help name="membersOnly">Members Only</csns:help></th>
    <td><form:checkbox path="membersOnly" /></td>
  </tr>
</table>

<div id="members">
<h4>Members</h4>
<table id="membersTable" class="viewtable autowidth">
<thead><tr><th>Name</th><th>Username</th><th>Email</th><th></th></tr></thead>
<tbody>
<c:forEach items="${forum.members}" var="member">
<tr>
  <td>${member.name}</td>
  <td>${member.username}</td>
  <td>${member.primaryEmail}</td>
  <td class="center"><a href="javascript:removeMember(${member.id})"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
</tr>
</c:forEach>
</tbody>
</table>

<p>
<input id="search" type="text" class="forminput" size="40"
    placeholder="Search for users to add" />
<button id="add" class="subbutton">Add</button>
<button id="clear" class="subbutton">Clear</button>
</p>
</div>

<p><input type="submit" class="subbutton" value="Create" /></p>
</form:form>

<div id="help-membersOnly" class="help">Only the department administrators and
the members of this forum can access this forum.</div>
