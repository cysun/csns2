<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function() {
    $("#generate_cin").click(function(event){
        event.preventDefault();
        var cin = "G" + (100000000 + Math.floor(Math.random()*100000000));
        $("#cin").val( cin );
    });
    $("#user").validate({
        errorPlacement: function(error, element){
            error.appendTo( element.parent("td").find("div") );
        }
    });
    $("#search").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
                window.location.href = "add?userId=" + ui.item.id + "&sectionId=${param.sectionId}";
        }
    })
    .autocomplete( "instance" )._renderItem = function(ul, item){
        var li = $("<li>");
        var downloadUrl = "<c:url value='/download.html?fileId=' />";
        if( item.thumbnail )
            li.append("<img src='" + downloadUrl + item.thumbnail + "' alt='' style='vertical-align: middle; margin-right: 0.5em;' />");
        return li.append(item.label).appendTo(ul);
    };
});
</script>

<p>Here you may add a student to the class if the student is not on the
GET roster. Please first use the search function to check if the student
already has an account on CSNS.</p>

<p>
<input id="search" name="term" type="text" class="forminput" size="40" />
<button class="subbutton">Search</button>
</p>

<p>If the search function does not find the student, please fill out the
following form to create an account for the student and add the student to
your class. Note that you should only generate a CIN for the student if the
student does not have one issued by the university.</p>

<form:form modelAttribute="user">
<table class="general">
  <tr>
    <th>First Name</th>
    <td>
      <form:input path="firstName" cssClass="forminput required" />
      <div class="error"></div>
    </td>
  </tr>
  <tr>
    <th>Last Name</th>
    <td>
      <form:input path="lastName" cssClass="forminput required" />
      <div class="error"></div>
    </td>
  </tr>
  <tr>
    <th>CIN</th>
    <td>
      <form:input path="cin" cssClass="forminput required" />
      <button id="generate_cin" class="subbutton">Generate</button>
      <div class="error"></div>
    </td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" value="Add" />
    </td>
  </tr>
</table>
<input type="hidden" name="sectionId" value="${param.sectionId}" />
</form:form>
