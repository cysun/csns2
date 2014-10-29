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
<%-- edit syllabus on class site --%>
<c:when test="${view == 'site'}">
<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li>Edit Syllabus</li>
</ul>
</c:when>

<%-- edit syllabus in course journal as instructor --%>
<c:when test="${view == 'journal1'}">
<ul id="title">
<li><a href="<c:url value='/section/taught?quarter=${section.quarter.code}#section-${section.id}' />"
       class="bc" >${section.course.code} - ${section.number}</a></li>
<li><a href="<c:url value='/section/journal/view?sectionId=${section.id}' />"
      class="bc">Course Journal</a></li>
<li>Edit Syllabus</li>
</ul>
</c:when>
</c:choose>

<form:form modelAttribute="syllabus" enctype="multipart/form-data">
<table class="general">
  <tr>
    <td>
      <form:select path="type">
        <form:options items="${resourceTypes}" />
      </form:select>
  </tr>

  <tr id="resTEXT" class="res">
    <td>
      <form:textarea path="text" rows="5" cols="80" />
      <div class="error"><form:errors path="text" /></div>
    </td>
  </tr>

  <tr id="resFILE" class="res">
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
    <td>
      <form:input path="url" cssClass="leftinput" cssStyle="width: 99%;" placeholder="http://" />
      <div class="error"><form:errors path="url" /></div>
    </td>
  </tr>

  <tr>
    <td><input class="subbutton" type="submit" name="save" value="Save" /></td>
  </tr>
</table>
</form:form>
