<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
	$(".type").hide()
	$("#type").keyup(function(){
		$(this).val( $(this).val().toUpperCase() );
	});
	$("#type").change(function(){
		$(".type").hide();
		if($(this).val() == "RUBRIC")
        {
		    $("#rubrics").show();
		    $("#name").val( $("#rubrics option:selected").text() );
		}
		else if($(this).val() == "SURVEY")
	    {
			$("#surveys").show();
            $("#name").val( $("#surveys option:selected").text() );
	    }
		else
			$("#name").val("");
	});
	$("#rubrics").change(function(){
		$("#name").val( $("#rubrics option:selected").text() );
	});
    $("#surveys").change(function(){
        $("#name").val( $("#surveys option:selected").text() );
    });
    $(".res").hide();
    if($("#description\\.type").val() != "None")
        $("#res"+$("#description\\.type").val()).show();
    $("#description\\.type").change(function(){
        $(".res").hide();
        $("#res"+$(this).val()).show();
    });
	$("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Default"
        });
    });
})
</script>

<ul id="title">
<li><a href="../../list" class="bc">Program Assessment</a></li>
  <c:choose>
  <c:when test="${not empty outcome}">
  <li><a href="../../view?id=${outcome.program.id}#outcomes" class="bc">${outcome.program.name}</a></li>
  <li><a href="../measures?fieldId=${outcome.id}&edit=true" class="bc">Student Outcome #${outcome.index+1}</a>
  </c:when>
  </c:choose>
<li>Add Measure</li>
</ul>

<form:form modelAttribute="measure" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th class="shrink">Type</th>
    <td><form:input path="type" cssClass="leftinput" maxlength="255" list="types"
                    cssStyle="width: 25%; text-transform: uppercase;" />
      <datalist id="types">
        <option>RUBRIC</option>
        <option>SURVEY</option>
      </datalist>
      <div class="error"><form:errors path="type" /></div>
    </td>
  </tr>

  <tr id="rubrics" class="type">
    <th class="shrink">Rubric</th>
    <td>
      <form:select path="rubric">
        <form:options items="${rubrics}" itemLabel="name" />
      </form:select>
    </td>
  </tr>
  
  <tr id="surveys" class="type">
    <th class="shrink">Survey</th>
    <td>
      <form:select path="surveyChart">
        <form:options items="${surveyCharts}" itemLabel="name" />
      </form:select>
    </td>
  </tr>

  <tr>
    <th class="shrink">Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th class="shrink">Description</th>
    <td>
      <form:select path="description.type">
        <form:options items="${resourceTypes}" />
      </form:select>
    </td>
  </tr>

  <tr id="resTEXT" class="res">
    <th></th>
    <td>
      <form:textarea path="description.text" rows="5" cols="80" />
      <div class="error"><form:errors path="description.text" /></div>
    </td>
  </tr>

  <tr id="resFILE" class="res">
    <th></th>
    <td>
      <input name="file" type="file" size="80" style="width: 99%;" class="leftinput">
      <div class="error"><form:errors path="description.file" /></div>
    </td>
  </tr>

  <tr id="resURL" class="res">
    <th></th>
    <td>
      <form:input path="description.url" cssClass="leftinput" cssStyle="width: 99%;" placeholder="http://" />
      <div class="error"><form:errors path="description.url" /></div>
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Add" /></td></tr>
</table>
</form:form>
