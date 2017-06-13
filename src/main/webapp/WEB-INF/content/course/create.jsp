<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $(".add").autocomplete({
        source: "<c:url value='/autocomplete/course' />",
        select: function(event, ui) {
            if( ui.item )
            {
                $("<span>").attr({
                    id: $(this).attr("id") + "-" + ui.item.id
                }).append(
                    $("<input>").attr({
                        type: "hidden",
                        name: $(this).attr("id"),
                        value: ui.item.id
                    })
                ).append(
                    $("<a>").attr({
                        href: "javascript:delete" + $(this).attr("id") + "(" + ui.item.id + ")"
                    }).text(ui.item.value)
                ).append(", ").insertBefore($(this));
                event.preventDefault();
                $(this).val("");
            }
        }
    });
    $("#coordinator").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
                $("<input>").attr({
                    type: "hidden",
                    name: "coordinator",
                    value: ui.item.id
                }).appendTo("form");
        }
    });
    $("div.help").dialog({
        autoOpen: false,
        modal: true
    });
    $("#code").blur(function(){
       var code = $(this).val().replace(/\s+/g, '');
       $(this).val(code);
       if( code.search(/[0-9]{4}/) >= 0 )
       {
           $("#units").val("3");
           $("#unitFactor").val("1.0");
       }
       else
       {
           $("#units").val("4");
           $("#unitFactor").val("0.667");
       }
    });
});
function help( name )
{
    $("#help-"+name).dialog("open");
}
function deleteprerequisites( prereqId )
{
    var msg = "Are you sure you want to remove this prerequisite?";
    if( confirm(msg) )
      $("#prerequisites-"+prereqId).remove();
}
</script>

<ul id="title">
<c:choose>
  <c:when test="${not empty dept}">
    <li><a class="bc" href="<c:url value='/department/${dept}/courses' />">Courses</a></li>
  </c:when>
  <c:otherwise>
    <li><a class="bc" href="search">Courses</a></li>
  </c:otherwise>
</c:choose>
<li>Create Course</li>
</ul>

<form:form modelAttribute="course" enctype="multipart/form-data">
<table class="general autowidth">
  <tr>
    <th><csns:help name="code">Code</csns:help> *</th>
    <td>
      <form:input path="code" cssClass="forminput" cssStyle="width: 150px;" required="required" />
      <div class="error"><form:errors path="code" /></div>
    </td>
  </tr>
  <tr>
    <th>Name *</th>
    <td>
      <form:input path="name" cssClass="forminput" cssStyle="width: 600px;" required="required" />
      <div class="error"><form:errors path="name"  /></div>
    </td>
  </tr>
  <tr>
    <th>Units *</th>
    <td>
      <form:input path="units" cssClass="forminput" cssStyle="width: 4em;" required="required" />
      <div class="error"><form:errors path="units"  /></div>
    </td>
  </tr>
  <tr>
    <th><csns:help name="unit-factor">Unit Factor</csns:help> *</th>
    <td>
      <form:input path="unitFactor" cssClass="forminput" cssStyle="width: 4em;" required="required" />
      <div class="error"><form:errors path="unitFactor"  /></div>
    </td>
  </tr>
  <tr>
    <th>Prerequisites</th>
    <td><input id="prerequisites" type="text" class="forminput add" name="a" style="width: 150px;" /></td>
  <tr>
    <th>Coordinator</th>
    <td>
      <input id="coordinator" name="cname" class="forminput" style="width: 600px;" />
    </td>
  </tr>
  <tr>
    <th>Description</th>
    <td>
      <input name="file" type="file" class="forminput" style="width: 600px;" />
    </td>
  </tr>
  <tr>
    <th>Catalog Description</th>
    <td>
      <form:textarea path="catalogDescription" cssStyle="width: 99%;" rows="6" />
    </td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" value="Create" />
    </td>
  </tr>
</table>
</form:form>

<div id="help-code" class="help">A course code must consist of uppercase letters
followed by a number, and optionally, followed by another uppercase letter, e.g.
<span class="tt">CS101</span> or <span class="tt">CS496A</span>.</div>

<div id="help-unit-factor" class="help">Unit factor is used to make quarter
units and semester units equivalent for the purpose of calculating GPA. The
unit factor for a semester course is 1.0, and the unit factor for a quarter
course is 0.667.</div>
