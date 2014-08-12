<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#search").autocomplete({
        source: "../autocomplete",
        select: function(event, ui) {
            if( ui.item )
                $("#survey").val(ui.item.id);
        }
    });
});
function addPoint()
{
	if( $("#survey").val() && $("#section").val() && $("#question").val() )
	{
		var s = "<a href='javascript:removePoint(" + $("#survey").val() + ","
		        + ($("#section").val()-1) + "," + ($("#question").val()-1)
		        + ")'><img alt='[Delete Point]' title='Delete Point' "
		        + "src='<c:url value='/img/icons/delete.png' />' /></a>";
	    $.ajax({
		    url: "addPoint",
		    data: {
			    "surveyId": $("#survey").val(),
			    "sectionIndex": $("#section").val()-1,
			    "questionIndex": $("#question").val()-1,
			    "_cid": "${_cid}"
		    },
		    success: function(){
		    	$("#addRow").before(
		    	    $("<tr>").attr("id","p"+$("#survey").val()+"-"+($("#section").val()-1)+"-"+($("#question").val()-1))
		    	    .append($("<td>").text($("#search").val()))
		            .append($("<td>").text($("#section").val()).addClass("center"))
		            .append($("<td>").text($("#question").val()).addClass("center"))
		            .append($("<td>").html(s))
		        );
		    	$("#search").val("");
		    	$("#survey").val("");
		    	$("#section").val("");
		    	$("#question").val("");
		    }
	    });
	}
}
function removePoint( surveyId, sectionIndex, questionIndex )
{
    $.ajax({
        url: "removePoint",
        data: {
            "surveyId": surveyId,
            "sectionIndex": sectionIndex,
            "questionIndex": questionIndex,
            "_cid": "${_cid}"
        },
        success: function(){
        	$("#p" + surveyId + "-" + sectionIndex + "-" + questionIndex).remove();
        }
    });
}
</script>

<ul id="title">
<li><a class="bc" href="../list">Surveys</a></li>
<li><a class="bc" href="list">Charts</a></li>
<li><a class="bc" href="view?id=${series.chart.id}"><csns:truncate
  value="${series.chart.name}" length="50" /></a></li>
<li>Add Series</li>
</ul>

<form:form modelAttribute="series">
<table class="general">
  <tr>
    <th class="shrink">Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" maxlength="255" style="width: 99%" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>
  <tr>
    <th>Stats Type</th>
    <td>
      <form:select path="statType">
        <form:option value="Average" />
        <form:option value="Median" />
        <form:option value="Min" />
        <form:option value="Max" />
      </form:select>
    </td>
  </tr>
</table>

<table id="points" class="viewtable" style="margin-top: 1em;">
<tr><th>Survey</th><th class="shrink">Section</th><th class="shrink">Question</th>
  <th class="shrink"></th></tr>
<c:forEach items="${series.points}" var="point">
<tr id="p${point.survey.id}-${point.sectionIndex}-${point.questionIndex}">
  <td>${point.survey.name}</td>
  <td class="center">${point.sectionIndex+1}</td>
  <td class="center">${point.questionIndex+1}</td>
  <td><a href="javascript:removePoint(${point.survey.id},${point.sectionIndex},${point.questionIndex})"><img
         alt="[Delete Point]" title="Delete Point" src="<c:url value='/img/icons/delete.png' />" /></a></td>
</tr>
</c:forEach>
<tr id="addRow">
  <td><input id="search" type="text" class="leftinput" style="width: 99%"
             placeholder="Search for survey" />
      <input id="survey" type="hidden" />
  </td>
  <td><input id="section" type="text" class="smallerinput" /></td>
  <td><input id="question" type="text" class="smallerinput" /></td>
  <td><a href="javascript:addPoint()"><img alt="[Add Point]" title="Add Point"
         src="<c:url value='/img/icons/add.png' />" /></a></td>
</tr>
</table>

<p><button class="subbutton">Add</button></p>
</form:form>
