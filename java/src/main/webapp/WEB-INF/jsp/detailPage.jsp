<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="/WEB-INF/jsp/common/header.jsp" />

<h1>Park Details</h1>

<!-- First large section of page, contains park image and table of 
park facts and info found in npgeek database -->
<div class="park-parent">

	<!-- Park image overlayed by a transparent text box with name, state, and quote of park -->
	<div id="park-display">
		<div id="detail-div">
			<c:url value="/img/parks/${park.parkCode}.jpg" var="parkPicture" />
				<img src= "${parkPicture}" id="detail-img">
			<div class="transbox">
				<div class= "name">
       				<c:out value="${park.parkName}"/>
    			</div>
    			<div class="state">
        			<c:out value="${park.state}"/>
    			</div>
    			<div class="italic">
        			<br>
        			"<c:out value="${park.quote}"/>"
        			<br>
        			~<c:out value="${park.quoteSource}"/>
    			</div>
			</div>
		</div>
	</div>

	<!-- Table of all extra park information from npgeek database -->
	<div id="park-info">
	    <h3 id="facts-table-header">${park.parkName} Facts!</h3>
	    <table id="facts-table">
	        <tr>
	            <th> Acreage</th>
	            <td> <fmt:formatNumber type="number" maxFractionDigits="3" value="${park.acreage}" /> acres</td>
	        </tr>
	        <tr>
	            <th> Elevation</th>
	            <td> <fmt:formatNumber type="number" maxFractionDigits="3" value="${park.elevation}" /> feet</td>
	        </tr>
	        <tr>
	            <th> Miles of Trail</th>
	            <td> <c:out value="${park.milesOfTrail}" /> miles</td>
	        </tr>
	        <tr>
	            <th> Number of Campsites</th>
	            <td> <c:out value="${park.numberOfCampsites}" /></td>
	        </tr>
	        <tr>
	            <th> Climate</th>
	            <td> <c:out value="${park.climate}" /></td>
	        </tr>
	        <tr>
	            <th> Year Founded</th>
	            <td> <c:out value="${park.yearFounded}" /></td>
	        </tr>
	        <tr>
	            <th> Annual Visitors</th>
	            <td> <fmt:formatNumber type="number" maxFractionDigits="3" value="${park.annualVisitorCount}" /></td>
	        </tr>
	        <tr>
	            <th> Entry Fee</th>
	            <td> $<c:out value="${park.entryFee}" /></td>
	        </tr>
	        <tr>
	            <th> Number of Animal Species</th>
	            <td> <c:out value="${park.numberOfSpecies}" /></td>
	        </tr>
	    </table>
	</div>
	
</div>

<div>
	<br>
	<c:out value="${park.description}"/>
</div>

<!-- Weather forecast container -->
<div>
	<hr>
	<h2>Five Day Forecast</h2>
	
	<!-- Small form that lets the user create a session based on their desired
	view scale of the temperatures in either Farenheit or Celcius -->
	<c:url value="/detail?parkCode=${park.parkCode}" var="temp" />
	<form method="POST" action="${temp}">
		<div class="inline-block">
			<p>View temperatures in:</p>
		</div>
		<div class="inline-block">
			<select name="temperature" id="temperature">
				<option value="F">&deg;F</option>
				<option value="C">&deg;C</option>
			</select>
		</div>
		<div class="inline-block">
			<input type="submit" value="Refresh" />
		</div>
	</form>
	
	<!-- Table that displays weather information for five days of each park -->
	<table>
		<tr>
			<th id="forecast-th">TODAY</th>
			<th id="forecast-th">Tomorrow</th>
			<th id="forecast-th">2 days out</th>
			<th id="forecast-th">3 days out</th>
			<th id="forecast-th">4 days out</th>
		</tr>
		<tr>
			<c:forEach var="weather" items="${forecast}">
				<td class="tabletop" id="detail-table">
					<c:url value="/img/weather/${weather.forecast}.png" var="weatherPicture" />
					<c:url value="/img/weather/partlyCloudy.png" var="partlyCloudy" />
					
					<!-- Choose method to get weather image -->
					<c:choose>
						<c:when test="${weather.forecast eq 'partly cloudy'}">
							<img src="${partlyCloudy}" class="weather-img" />
						</c:when>
						<c:otherwise>
							<img src="${weatherPicture}" class="weather-img" />
						</c:otherwise>
					</c:choose>
					
					<div id="forecast-text">${weather.forecast}</div><hr></td>
			</c:forEach>
		</tr>
		
		<!-- Display weather temperatures in user's selected scale (Farenheit by default) -->
		<tr>
			<c:forEach var="weather" items="${forecast}">
				<td id="detail-table" class="tablemid">

					<table id="temp-table">
						<tr>
							<th class="hot">High</th>
							<th class="cold">Low</th>
						</tr>
						<tr>
							<c:choose>
								<c:when test="${temperature eq 'F'}">
									<td class="text-center"><c:out value="${weather.high}" />&deg;F</td>
									<td class="text-center"><c:out value="${weather.low}" />&deg;F</td>
								</c:when>
								<c:when test="${temperature eq 'C'}">
									<td class="text-center"><c:out value="${weather.high}" />&deg;C</td>
									<td class="text-center"><c:out value="${weather.low}" />&deg;C</td>
								</c:when>
							</c:choose>
						</tr>
					</table>

				</td>
			</c:forEach>
		</tr>

		<!-- Pull in generated advisory string created based on weather attributes -->
		<tr>
			<c:forEach var="warning" items="${advisory}">
				<td class="tablebottom" id="detail-table"><c:out
						value="${warning}" /></td>
			</c:forEach>
		</tr>

	</table>
</div>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />