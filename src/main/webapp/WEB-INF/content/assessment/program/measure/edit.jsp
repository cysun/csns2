<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    measureTypeCheck();
    $("#type").keyup(function(){
        $(this).val( $(this).val().toUpperCase() );
    });
    $("#type").change(function(){
        measureTypeCheck();
        if($(this).val() == "RUBRIC")
        {
            $("#name").val( "RUBRIC: " + $("#rubrics option:selected").text() );
            $("#resource\\.type").val("NONE");
        }
        else if($(this).val() == "SURVEY")
        {
            $("#name").val( "SURVEY: " + $("#surveys option:selected").text() );
            $("#resource\\.type").val("NONE");
        }
        else
        {
            $("#name").val("");
        }
    });
    $("#rubrics").change(function(){
        $("#name").val( "RUBRIC: " + $("#rubrics option:selected").text() );
    });
    $("#surveys").change(function(){
        $("#name").val( "SURVEY: " + $("#surveys option:selected").text() );
    });
    $("#resource\\.type").change(function(){
        $(".res").hide();
        $("#res"+$(this).val()).show();
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
function measureTypeCheck()
{
    $(".type").hide();
    $(".res").hide();
    if($("#type").val() == "RUBRIC")
    {
        $("#rubrics").show();
        $("#dataType").hide();
    }
    else if($("#type").val() == "SURVEY")
    {
        $("#surveys").show();
        $("#dataType").hide();
    }
    else
    {
        $("#dataType").show();
        $("#res"+$("#resource\\.type").val()).show();
    }
}
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
<li><a href="../../list" class="bc">Program Assessment</a></li>
  <c:choose>
  <c:when test="${not empty outcome}">
  <li><a href="../../view?id=${outcome.program.id}#outcomes" class="bc">${outcome.program.name}</a></li>
  <li><a href="../measures?fieldId=${outcome.id}&edit=true" class="bc">Student Outcome #${outcome.index+1}</a>
  </c:when>
  </c:choose>
<li>Edit Measure</li>
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

  <tr id="dataType">
    <th class="shrink"><csns:help name="data">Data</csns:help></th>
    <td>
      <form:select path="resource.type">
        <form:options items="${resourceTypes}" />
      </form:select>
    </td>
  </tr>

  <tr id="resFILE" class="res">
    <th></th>
    <td>
      <input name="file" type="file" size="80" style="width: 99%;" class="leftinput">
      <div class="error"><form:errors path="resource.file" /></div>
    </td>
  </tr>

  <tr id="resURL" class="res">
    <th></th>
    <td>
      <form:input path="resource.url" cssClass="leftinput" cssStyle="width: 99%;" placeholder="http://" />
      <div class="error"><form:errors path="resource.url" /></div>
    </td>
  </tr>

  <tr>
    <th>Description</th>
    <td>
      <form:textarea path="description" rows="5" cols="80" />
      <div class="error"><form:errors path="description" /></div>
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Save" /></td></tr>
</table>
</form:form>

<div id="help-data" class="help">
A file or the URL of a resource that contains the raw data of this measure.
Other information about the measure, and preferably, a summary of the results,
should be included in the <em>description</em> field.</div>
