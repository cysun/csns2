<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}" />

<script>
$(function(){
    $("input[name='numOfChoices']").change(function(){
        var newNumOfChoices = $(this).val();
        var oldNumOfChoices = $("#choicesInput").children("input[type='text']").length;
        if( newNumOfChoices > oldNumOfChoices )
        {
            for( var i = oldNumOfChoices ; i < newNumOfChoices ; ++i )
                $("#choicesInput").append( "<span>Choice #" + (i+1) + ":</span> " +
                    "<input id='choices" + i + "' name='choices[" + i + "]' type='text' class='leftinput' style='width: 70%;' /> " + 
                    "<input id='correctSelections" + (i+1) + "' name='correctSelections' type='checkbox' value='" + i + "' />" +
                    "<input type='hidden' name='_correctSelections' value='off' /> <br /><br />" );
            if( $("input[name='maxSelections']").val() == oldNumOfChoices )
                $("input[name='maxSelections']").val( newNumOfChoices );
        }
        if( newNumOfChoices < oldNumOfChoices )
        {
            $("#choicesInput span:gt(" + (newNumOfChoices-1) +")").remove();
            $("#choicesInput input[type='text']:gt(" + (newNumOfChoices-1) +")").remove();
            $("#choicesInput input[type='checkbox']:gt(" + (newNumOfChoices-1) +")").remove();
            $("#choicesInput input[type='hidden']:gt(" + (newNumOfChoices-1) +")").remove();
            $("#choicesInput br:gt(" + (newNumOfChoices*2-1) +")").remove();
            if( $("input[name='maxSelections']").val() > newNumOfChoices )
                $("input[name='maxSelections']").val( newNumOfChoices );
        }
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
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="editQuestionSheet?assignmentId=${assignment.id}&amp;sectionIndex=${param.sectionIndex}"><csns:truncate
  value="${assignment.name}" length="60" /></a></li>
<li>Add Question</li>
</ul>

<form:form modelAttribute="question">
<h4>Question description:</h4>
<form:textarea path="description" cssStyle="width: 80%; height: 8em;" />
<div class="error"><form:errors path="description" /></div>

<c:choose>

<c:when test="${question.type == 'CHOICE'}">
<h4>Number of choices:
<form:input path="numOfChoices" cssClass="forminput" cssStyle="width: 2em;" />
</h4>
<div id="choicesInput">
<c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
<span>Choice #${choiceStatus.index+1}:</span>
  <form:input path="choices[${choiceStatus.index}]" cssClass="leftinput" cssStyle="width: 70%;" />
  <form:checkbox path="correctSelections" value="${choiceStatus.index}" /> <br /><br />
</c:forEach>
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
<h4><csns:help name="txtlength" img="false">Text Length</csns:help>:
<form:input path="textLength" cssClass="forminput" cssStyle="width: 4em;" />
</h4>
</c:when>

</c:choose>

<h4><csns:help name="point" img="false">Point Value</csns:help>:
<form:input path="pointValue" cssClass="forminput" cssStyle="width: 2em;" />
</h4>

<input type="hidden" name="assignmentId" value="${assignment.id}" />
<input type="hidden" name="sectionIndex" value="${param.sectionIndex}" />
<input class="subbutton" type="submit" value="Add" />
</form:form>

<div id="help-txtlength" class="help">
<em>Text length</em> determines whether a text field or a text area will be
used for the answer of this question. A text field will be used if the text
length is 60 or less; otherwise a text area will be used.
</div>

<div id="help-point" class="help">
<em>Point value</em> is the number of points (i.e. credits) this question is
worth. It tells the auto grader how many points to deduct if the answer to this
question is wrong.</div>
