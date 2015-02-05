<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
    $(".res").hide();
    if($("#type").val() != "None")
        $("#res"+$("#type").val()).show();
    $("#type").click(function(){
        $(".res").hide();
        $("#res"+$(this).val()).show();
    });
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Default"
        });
    });
});
</script>

<c:choose>
<%-- add handout to course journal --%>
<c:when test="${view == 'journal_handout'}">
<ul id="title">
<li><a href="<c:url value='/section/taught?quarter=${section.quarter.code}#section-${section.id}' />"
       class="bc" >${section.course.code} - ${section.number}</a></li>
<li><a href="view?sectionId=${section.id}" class="bc">Course Journal</a></li>
<li><a href="handouts?sectionId=${section.id}" class="bc">Handouts</a></li>
<li>Add Handout</li>
</ul>
</c:when>
</c:choose>

<form:form modelAttribute="resource" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th class="shrink">Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th>Type</th>
    <td>
      <form:select path="type">
        <form:options items="${resourceTypes}" />
      </form:select>
  </tr>

  <tr id="resTEXT" class="res">
    <th></th>
    <td>
      <form:textarea path="text" rows="5" cols="80" />
      <div class="error"><form:errors path="text" /></div>
    </td>
  </tr>

  <tr id="resFILE" class="res">
    <th></th>
    <td>
      <input name="uploadedFile" type="file" class="leftinput">
      <c:if test="${syllabus.type == 'FILE' and syllabus.file != null}">
        <span style="margin-left: 2em;">
        <a href="<c:url value='/download?fileId=${syllabus.file.id}' />">${syllabus.file.name}</a>
        </span>
      </c:if>
      <div class="error"><form:errors path="file" /></div>
    </td>
  </tr>

  <tr id="resURL" class="res">
    <th></th>
    <td>
      <form:input path="url" cssClass="leftinput" cssStyle="width: 99%;" placeholder="http://" />
      <div class="error"><form:errors path="url" /></div>
    </td>
  </tr>

  <tr>
    <th></th>
    <td><input class="subbutton" type="submit" name="save" value="Save" /></td>
  </tr>
</table>
</form:form>
