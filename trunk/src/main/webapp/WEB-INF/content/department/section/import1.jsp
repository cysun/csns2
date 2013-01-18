<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="department" value="${importer.department}" />
<c:set var="section" value="${importer.section}" />

<script>
function setGrades( number )
{
    if( number > 0 )
    {
        $.ajax({
            url: "grades",
            data: {
                quarter: "${section.quarter.code}",
                course: "${section.course.id}",
                number: number
            },
            success: function(data){ $("#text").val(data); }
        });
    }
    else
        $("#text").val("");
}
$(function(){
<c:if test="${empty importer.text}">
    setGrades( $("#section\\.number").val() );
</c:if>
    $("#section\\.number").change(function(){
        setGrades( $("#section\\.number").val() );
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/search' />">Sections</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/sections?quarter=${section.quarter.code}' />">${department.name}</a></li>
<li>Import Section</li>
</ul>

<p>Please select the section number. Do NOT select <span class="tt">NEW</span>
if the section already exists. Note that CSNS section numbers do not
match GET section numbers, so please choose the section number
<em>very carefully</em>.</p>

<form:form modelAttribute="importer">
<table class="viewtable autowidth">
  <tr><th>Quarter</th><th>Course</th><th>Instructor</th><th>Section</th></tr>
  <tr>
    <td>${section.quarter}</td>
    <td>${section.course.code}</td>
    <td>${section.instructors[0].name}</td>
    <td>
      <form:select path="section.number">
        <form:option value="-1" label="NEW" />
        <form:options items="${importer.sectionNumbers}" />
      </form:select>
    </td>
  </tr>
</table>

<p><form:textarea path="text" rows="20" cols="80" cssStyle="width: 100%; border: 1px solid;" /></p>

<p><input type="hidden" name="_page" value="1" />
<input type="submit" name="_target0" value="Back" class="subbutton" />
<input type="submit" name="_target2" value="Next" class="subbutton" /></p>
</form:form>
