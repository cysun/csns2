<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function(){
    $(".select").hide();
    $(".select").change(function(){
        var field = $(this).attr("data-field");
        var msg = "Are you sure you want to change this student's " + field + "?";
        if( confirm(msg) )
            window.location.href = field + "/set?userId=${user.id}&" + field + "Id=" + $(this).val();
    });
    $(".change").click(function(){
        var field = $(this).attr("data-field");
        $("#"+field+"-display").toggle();
        $("#"+field+"-select").toggle();
        if( $(this).html() == 'Change' )
            $(this).html("Cancel");
        else
            $(this).html("Change");
    });
    if( "${user.major.id}" )
        $("#major-select").val( "${user.major.id}" );
    if( "${user.personalProgram.id}" )
        $("#program-select").val( "${user.personalProgram.program.id}" );
});
</script>
<table class="general autowidth">
<tr>
  <th>Major</th>
  <td style="min-width: 4em;">
    <span id="major-display">${user.major.name}</span>
<c:if test="${fn:length(departments) > 0}">
    <select id="major-select" class="select" data-field="major" name="majorId">
      <option value=""></option>
      <c:forEach items="${departments}" var="department">
      <option value="${department.id}">${department.name}</option>
      </c:forEach>
    </select>
</c:if>
  </td>
  <td><a class="change" data-field="major" href="javascript:void(0)">Change</a></td>
</tr>  
<tr>
  <th>Program</th>
  <td style="min-width: 4em;">
    <span id="program-display">${user.personalProgram.program.name}</span>
<c:if test="${fn:length(programs) > 0}">
    <select id="program-select" class="select" data-field="program" name="programId">
      <option value=""></option>
      <c:forEach items="${programs}" var="program">
      <option value="${program.id}">${program.name}</option>
      </c:forEach>
    </select>
</c:if>
  </td>
  <td><a class="change" data-field="program" href="javascript:void(0)">Change</a></td>
</tr>
</table>
