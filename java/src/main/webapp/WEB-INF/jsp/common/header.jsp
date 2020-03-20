<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>National Park Geek</title>
    <c:url value="/css/nationalParkCss.css" var="cssHref" />
    <link rel="stylesheet" type="text/css" href="${cssHref}">
</head>

<body>

	<!-- Top of page logo and title -->
    <header>
    	<c:url value="/" var="homePageHref" />
    	<c:url value="/img/logo.png" var="logoSrc" />
    	<div id="header-img">
        	<a href="${homePageHref}">
        		<img src="${logoSrc}" alt="National Park Geek logo" class="logo"/>
        	</a>
        </div>
        <h1>National Park Geek</h1>
    </header>
    <hr>
    
    <!-- Navigation bar with clickable buttons -->
    <div id="clickable-bar">
	    <table id="header-bar">
	    	<tr>
	    		<c:url value="/" var="homePage"/>
	    		<a href="${homePage}"><button class="btn info">Home Page</button></a>
	    		
	    		<c:url value="/favorites" var="favorites"/>
	    		<a href="${favorites}"><button class="btn info">Users' Favorite Parks</button></a>
	    		
	    		<c:url value="/surveyInput" var="surveyPage"/>
	    		<a href="${surveyPage}"><button class="btn info">Survey of the Day</button></a>
	    		
	    		<c:url value="/visit" var="visit"/>
	    		<a href="${visit}"><button class="btn info">Parks Online</button></a>
	    		
	    		<c:url value="https://donate.nationalparks.org/page/18127/donate/1" var="donateNow" />
	    		<a href="${donateNow}"><button class="btn info">Give Now!</button></a>
	    	</tr>
	    </table>
   	</div>
    <hr>