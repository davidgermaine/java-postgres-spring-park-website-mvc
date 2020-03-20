<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:import url="/WEB-INF/jsp/common/header.jsp" />

<h1>Survey of the Day!</h1>

<hr>

<h3>Vote for your favorite National Park!</h3>

<!-- Initialize a form -->
<c:url var="surveyInput" value="/surveyInput" />
<form:form method="POST" action="${inputSubmitUrl}" modelAttribute="surveyInput">

	<!-- Loop that creates a dropdown select option for each park in npgeek database -->
	<!-- Blank value by default -->
	<div id="survey-padding">
		<label for="parkCode">Favorite Park: </label>
		<form:select path="parkCode">
			<form:option value=""> select one...</form:option>
			<c:forEach var="park" items="${parks}">
				<form:option value="${park.parkCode}"><c:out value="${park.parkName}"/></form:option>
			</c:forEach>
		</form:select>
		<form:errors path="parkCode" cssClass="error"/>
	</div>
	
	<h4>Personal Information</h4>
	
	<!-- Email field (validations are @NotBlank @Email) -->
	<div id="survey-padding">
		<label for="emailAddress">Email: </label>
		<form:input path="emailAddress" placeholder=" email"/>
		<form:errors path="emailAddress" cssClass="error"/>
	</div>
	
	<!-- Select a state (blank value by default)-->
	<div id="survey-padding">  
		<label for="state">State of Residence: </label>
		<form:select path="state">
			<form:option value=""> select one...</form:option>
			<form:option value="AL">AL</form:option>
			<form:option value="AK">AK</form:option>
			<form:option value="AZ">AZ</form:option>
			<form:option value="AR">AR</form:option>
			<form:option value="CA">CA</form:option>
			<form:option value="CO">CO</form:option>
			<form:option value="CT">CT</form:option>
			<form:option value="DE">DE</form:option>
			<form:option value="FL">FL</form:option>
			<form:option value="GA">GA</form:option>
			<form:option value="HI">HI</form:option>
			<form:option value="ID">ID</form:option>
			<form:option value="IL">IL</form:option>
			<form:option value="IN">IN</form:option>
			<form:option value="IA">IA</form:option>
			<form:option value="KS">KS</form:option>
			<form:option value="KY">KY</form:option>
			<form:option value="LA">LA</form:option>
			<form:option value="ME">ME</form:option>
			<form:option value="MD">MD</form:option>
			<form:option value="MA">MA</form:option>
			<form:option value="MI">MI</form:option>
			<form:option value="MN">MN</form:option>
			<form:option value="MS">MS</form:option>
			<form:option value="MO">MO</form:option>
			<form:option value="MT">MT</form:option>
			<form:option value="NE">NE</form:option>
			<form:option value="NV">NV</form:option>
			<form:option value="NH">NH</form:option>
			<form:option value="NJ">NJ</form:option>
			<form:option value="NM">NM</form:option>
			<form:option value="NY">NY</form:option>
			<form:option value="NC">NC</form:option>
			<form:option value="ND">ND</form:option>
			<form:option value="OH">OH</form:option>
			<form:option value="OK">OK</form:option>
			<form:option value="OR">OR</form:option>
			<form:option value="PA">PA</form:option>
			<form:option value="RI">RI</form:option>
			<form:option value="SC">SC</form:option>
			<form:option value="SD">SD</form:option>
			<form:option value="TN">TN</form:option>
			<form:option value="TX">TX</form:option>
			<form:option value="UT">UT</form:option>
			<form:option value="VT">VT</form:option>
			<form:option value="VA">VA</form:option>
			<form:option value="WA">WA</form:option>
			<form:option value="WV">WV</form:option>
			<form:option value="WI">WI</form:option>
			<form:option value="WY">WY</form:option>	
		</form:select>
		<form:errors path="state" cssClass="error"/>
	</div>
	
	<!-- Select an activity level (blank value by default) -->
	<div id="survey-padding">
		<label for="activityLevel">Level of Activity: </label>
		<form:select path="activityLevel">
			<form:option value=""> select one...</form:option>
			<form:option value="inactive">Inactive</form:option>
			<form:option value="sedentary">Sedentary</form:option>
			<form:option value="active">Active</form:option>
			<form:option value="extremely active">Extremely Active</form:option>
		</form:select>
		<form:errors path="activityLevel" cssClass="error"/>
	</div>
	
	<!-- Button to submit form -->
	<div id="survey-padding">
		<input id="submit-button" type="submit" value="Submit survey and see results" />
	</div>
	
</form:form>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />