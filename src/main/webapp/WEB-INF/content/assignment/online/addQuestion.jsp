<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="section" value="${assignment.section}" />

<script>
$(function(){
    $("input[name='numChoices']").change(function(){
        var newNumChoices = $(this).val();
        var oldNumChoices = $("#choicesInput").children("input").length / 2;
        if( newNumChoices > oldNumChoices )
            for( var i = oldNumChoices+1 ; i <= newNumChoices ; ++i )
                $("#choicesInput").append( "<span>Choice #" + i + ":</span> " +
                    "<input name='choices' type='text' class='leftinput' style='width: 70%;' /> " + 
                    "<input name='correctSelections' type='checkbox' value='" + (i-1) + "' />" +
                    "<br /><br />" );
        if( newNumChoices < oldNumChoices )
        {
            $("#choicesInput span:gt(" + (newNumChoices-1) +")").remove();
            $("#choicesInput input:gt(" + (newNumChoices*2-1) +")").remove();
            $("#choicesInput br:gt(" + (newNumChoices*2-1) +")").remove();
        }
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="edit?id=${assignment.id}&sectionIndex=${param.sectionIndex}">${assignment.name}</a></li>
<li>Add Question</li>
</ul>

<form:form modelAttribute="question">
<h4>Question description:</h4>
<form:textarea path="description" cssStyle="width: 80%; height: 8em;" />
<div class="error"><form:errors path="description" /></div>

<c:choose>

<c:when test="${question.type == 'CHOICE'}">
<h4>Number of choices:
<input name="numChoices" type="text" class="forminput" value="4" style="width: 2em;"/>
</h4>
<div id="choicesInput">
<span>Choice #1:</span>
  <input name="choices" type="text" class="leftinput" style="width: 70%;" />
  <input name="correctSelections" type="checkbox" value="0" /> <br /><br />
<span>Choice #2:</span>
  <input name="choices" type="text" class="leftinput" style="width: 70%;" />
  <input name="correctSelections" type="checkbox" value="1" /> <br /><br />
<span>Choice #3:</span>
  <input name="choices" type="text" class="leftinput" style="width: 70%;" />
  <input name="correctSelections" type="checkbox" value="2" /> <br /><br />
<span>Choice #4:</span>
  <input name="choices" type="text" class="leftinput" style="width: 70%;" />
  <input name="correctSelections" type="checkbox" value="3" /> <br /><br />
</div>
<h4>Number of selections:
<form:input path="minSelections" cssClass="forminput" cssStyle="width: 1em;" /> -&gt;
<form:input path="maxSelections" cssClass="forminput" cssStyle="width: 1em;" /></h4>
</c:when>

<c:when test="${question.type == 'RATING'}">
<h4>Rating:
<form:input path="minRating" cssClass="forminput" cssStyle="width: 2em;" /> -&gt;
<form:input path="maxRating" cssClass="forminput" cssStyle="width: 2em;" /></h4>
</c:when>

<c:when test="${question.type == 'TEXT'}">
<h4>Text Length:
<form:input path="textLength" cssClass="forminput" cssStyle="width: 4em;" />
</h4>
</c:when>

</c:choose>

<h4>Point Value:
<form:input path="pointValue" cssClass="forminput" cssStyle="width: 2em;" />
</h4>

<input type="hidden" name="assignmentId" value="${assignment.id}" />
<input type="hidden" name="sectionIndex" value="${param.sectionIndex}" />
<input class="subbutton" type="submit" value="Add" />
</form:form>

<script type="text/javascript">
  CKEDITOR.replaceAll();
</script>
