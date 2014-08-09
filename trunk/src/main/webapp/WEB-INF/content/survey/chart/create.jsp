<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
</script>

<ul id="title">
<li><a class="bc" href="../list">Surveys</a></li>
<li><a class="bc" href="list">Charts</a></li>
<li>Create</li>
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
        <a class="xcoordinate" href="javascript:void(0)">${coordinate}</a>,
      </c:forEach>
      <input id="coordinate" name="coordinate" type="text" class="leftinput" />
      <button id="add" class="subbutton">Add</button>
    </td>
  <tr>
    <th>Y Axis Label</th>
    <td><form:input path="yLabel" cssClass="leftinput" maxlength="255" /></td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Create" /></td></tr>
</table>
</form:form>
