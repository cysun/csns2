<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
	$(".xcoordinate").click(function(){
		$.ajax({
            context: this,
			url: "removeXCoordinate",
			data: { "coordinate": $(this).text(), "_cid": "${_cid}" },
			success: function(){
				$(this).parent().remove();
			}
		});
	});
	$("#add").click(function(event){
		if( $("#coordinate").val() )
	    {
			$.ajax({
				url: "addXCoordinate",
				data: { "coordinate": $("#coordinate").val(), "_cid": "${_cid}" },
				success: function(){
			        $("<span>").append(
			            $("<a>")
			                .attr({href: "javascript:void(0)"})
			                .text($("#coordinate").val())
			                .click(function(){
			                    $.ajax({
                                    context: this,
			                        url: "removeXCoordinate",
			                        data: { "coordinate": $(this).text(), "_cid": "${_cid}" },
			                        success: function(){
			                            $(this).parent().remove();
			                        }
			                    });
 	                        })
			        ).append(", ").insertBefore($("#coordinate"));
			        $("#coordinate").val("");
				}
			});
	    }
	    event.preventDefault();
	});
});
function deleteChart()
{
	if( confirm("Are you sure you want to delete this chart?") )
		window.location.href = "delete?id=${chart.id}";
}
</script>

<ul id="title">
<li><a class="bc" href="../list">Surveys</a></li>
<li><a class="bc" href="list">Charts</a></li>
<li><a class="bc" href="view?id=${chart.id}" title="${chart.name}"><csns:truncate
  value="${chart.name}" length="60" /></a></li>
<li>Edit</li>
<li class="align_right"><a href="addSeries?chartId=${chart.id}"><img alt="[Add Series]"
  title="Add Series" src="<c:url value='/img/icons/chart_line_add.png' />" /></a></li>
<li class="align_right"><a href="javascript:deleteChart()"><img alt="[Delete Chart]"
  title="Delete Chart" src="<c:url value='/img/icons/chart_bar_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="chart">
<table class="general autowidth">
  <tr>
    <th class="shrink">Chart Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" maxlength="255" cssStyle="width: 99%;" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th>X Axis Label</th>
    <td><form:input path="xLabel" cssClass="leftinput" maxlength="255" /></td>
  </tr>

  <tr>
    <th>X Coordinates</th>
    <td>
      <c:forEach items="${chart.xCoordinates}" var="coordinate" varStatus="status">
        <span><a class="xcoordinate" href="javascript:void(0)">${coordinate}</a>,</span>
      </c:forEach>
      <input id="coordinate" name="coordinate" type="text" class="leftinput" />
      <button id="add" class="subbutton">Add</button>
    </td>
  <tr>
    <th>Y Axis Label</th>
    <td><form:input path="yLabel" cssClass="leftinput" maxlength="255" /></td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Save" /></td></tr>
</table>
</form:form>
