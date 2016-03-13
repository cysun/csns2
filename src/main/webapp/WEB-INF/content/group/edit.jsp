<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
var nameValid = false;
var origName = "";
$(function(){
	origName = $("#name").val().toLowerCase();
	checkName();
    $("#add-button").click(function() {
    	$("#addUserId").val($("#input-user").attr('user-id'));
    });
    
    $(".add").each(function(){
        $(this).autocomplete({
            source: "<c:url value='/autocomplete/user' />",
            select: function(event, ui) {
                if( ui.item ){
                	$(this).attr('user-id', ui.item.id);
                }
            }
        });
    });
    
    $(".clear").each(function(){
       $(this).click(function(event){
           event.preventDefault();
           $("input[name='userId']").remove();
           $(".add").each(function(){
              $(this).val("");
           });
       });
    });
    
    $("#form").submit(function(e){
    	if(!nameValid){
    		e.preventDefault();
    		return false;
    	}else{
	    	$("#form").submit();
	    	return true;
    	}
    });
});

function removeUser( id ) {
	$("#removeUserId").val(id);
}
function checkName() {
	var url = "<c:url value='/department/${dept}/group/check' />";
	var name = $("#name").val();
	if(name.length > 0){
		if(name.toLowerCase() != origName)	
			check(name, url, onCheckNameComplete);
		else{
			$("#name").removeClass('valid');
			$("#name").removeClass('invalid');
			nameValid = true;
		}
	}
	else{
		$("#name").removeClass('valid');
		$("#name").removeClass('invalid');
	}
}
function onCheckNameComplete(valid){
	nameValid = valid;
	if(!valid){
		$("#name").removeClass('valid');
		$("#name").addClass('invalid');
	}else{
		$("#name").removeClass('invalid');
		$("#name").addClass('valid');
	}
}
</script>
<script src="<c:url value='/js/group.js' />" type="text/javascript"></script>

<ul id="title">
  <li><a class="bc" href="<c:url value='/department/${dept}/people#group' />">Group</a></li>
  <li>Edit</li>
  <li class="align_right"><a href="<c:url value='/department/${dept}/group/user/import?id=${group.id}' />"><img alt="[Import Students]"
  title="Import Students" src="<c:url value='/img/icons/table_import.png' />" /></a></li>
</ul>

<form:form modelAttribute="group" id="form">
<table class="general">
  <tr>
    <th>Name</th>
    <td>
      <form:input path="name" id="name" cssClass="leftinput" cssStyle="width: 99%;" onkeyup="checkName();" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>
  
  <tr>
    <th>Description</th>
    <td>
      <form:input path="description" cssClass="leftinput" cssStyle="width: 99%;"/>
      <div class="error"><form:errors path="description" /></div>
    </td>
  </tr>

  <tr>
	<th>Add Users</th>
	<td>	
		<p>
		<input type="text" class="forminput add" name="userName" size="40" 
		    placeholder="Search for users to add" id="input-user" />
		<input type="submit" class="subbutton" name="add" value="Add" id="add-button" />
		<button class="subbutton clear">Clear</button>
		</p>
		<input type="hidden" id="addUserId" name="addUserId" />
		<input type="hidden" id="removeUserId" name="removeUserId" /> 
		<c:if test="${fn:length(group.users) > 0}">
	    <table class="target-users-table">
			<tr class="text-left">
			  <th style="padding-left:3px">CIN</th><th>Name</th><th>Primary Email</th>
			  <th class=""></th>
			</tr>
			<c:forEach items="${group.users}" var="user">
			<tr>
			  <td>${user.cin}</td>
			  <td>${user.name}</td>
			  <td>${user.primaryEmail}</td>
			  <td class="text-top"><input alt="[Remove]" title="Remove" type="image" src="<c:url value='/img/icons/delete.png' />" onclick="removeUser(${user.id})" />
			  </td>
			</tr>
			</c:forEach>
		</table>
		</c:if>
	</td>
  </tr>
  
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" name="submit" value="Edit" />
    </td>
  </tr>
</table>
</form:form>
