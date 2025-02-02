<%@ page language="java" import="java.sql.*"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="room.Room" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hotel Rooms</title>
    <link href="https://fonts.googleapis.com/css?family=Noto+Sans&display=swap" rel="stylesheet">
    
    <style type="text/css">
        .flexExpand {
            flex: 1;
        }

        h1 {
            font-family: 'Noto Sans', sans-serif;
        }

        #round1 {
            border-radius: 18px;
            width: 130px;
            height: 18px;
        }

        #round2 {
            border-radius: 4px;
            height: 20px;
        }

        #check {
            border-radius: 4px;
            height: 25px;
            width: 100px;
            margin: 8px 0;
            margin-left: 1.5rem;
            font-family: "Noto Sans", sans-serif;

        }

        input[type=text] {
            width: 90%;
            padding: 0.5em;
            margin: 8px 0;
        }

        input[type=submit] {
            width: 90%;
            background-color: #AE91E5;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-family: "Noto Sans", sans-serif;
            margin-left: 3em;
        }

        .dropdown {
            height: 42px;
            width: 100px;
            margin: 8px 0;
            margin-left: 4rem;
            border-radius: 4px;
            font-family: "Noto Sans", sans-serif;
        }

        .button1 {
            letter-spacing: 0.13em;
            font-size: 0.8rem;
            color: white;
            font-weight: bold;
            display: inline-block;
            padding: 0.75em;
            border-radius: 4px;
            text-decoration: none;
            text-transform: uppercase;
            background-color: #AE91E5;
        }

        .container {
            display: flex;
            justify-content: center;
        }

        .center {
            width: 800px;
            height: 220px;
            background:rgba(0, 0, 0, 0.5);
            color: white;
            font-family: 'Noto Sans', sans-serif;
            border-radius: 5px;
        }

        #background img {
            width: 100%;
            height: 100%;
            position: absolute;
            top: 45px;
            left: 0;
            z-index: -1;
        }

        .button:hover {
            background-color: rgba(0, 0, 0, 0.15);
        }

        .button.outlined {
            border: 1px solid rgba(0, 0, 0, 0.5);
        }

        .button.primary {
            background-color: #DAF7A6;
            color: black;
        }

        .button.disabled {
            color: rgba(0, 0, 0, 0.35)
        }

        body {
            margin: 0;
            font-family: 'Noto Sans', sans-serif;
            min-height: 100vh;
            font-size: small;
        }

        header {
            background-color: #AE91E5;
            color: white;
            padding: 0.5em 0.5rem;
            height: 45px;
            display: flex;
            align-items: center;
            border-bottom: 1px solid rgba(0, 0, 0, 0.5);
        }

        header h1 {
            margin: 0;
            margin-left: 0.25em;
        }

        header .button {
            color: white;
        }

        main {
            padding: 0.5em 0.5rem;
            height: 100%;
        }

        footer {
            position: absolute;
            padding: 0.3rem;
            text-align: center;
            opacity: 0.80;
            color: white;
            font-size: medium;
            bottom: 0;
            width: 100%;
            height: 0.1rem;
        }

        .popup {
            position: relative;
            display: inline-block;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }
        .popup .popuptext {
            visibility: hidden;
            width: 160px;
            background-color: #555;
            color: #fff;
            text-align: center;
            border-radius: 6px;
            padding: 8px 0;
            position: absolute;
            z-index: 1;
            bottom: 125%;
            left: 50%;
            margin-left: -80px;
            opacity: 0.8;
        }

        .popup .show {
            visibility: visible;
            -webkit-animation: fadeIn 1s;
            animation: fadeIn 1s;
        }

        @-webkit-keyframes fadeIn {
            from {opacity: 0;} 
            to {opacity: 0.8;}
        }

        @keyframes fadeIn {
            from {opacity: 0;}
            to {opacity:0.8 ;}
        }

        .home {
            color: white;
            font-weight: bold;
            font-size: 12px;
            text-decoration: none;
        }
    
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
	<form id="sign_in_notice_form">
		<div id="background">
        	<img src="https://images.dailyhive.com/20190923083339/shutterstock_1508803526.jpg">
    	<header>
        	<i class="fa fa-building-o" aria-hidden="true" style="font-size:30px;"></i>
        	<a href="hotel-mockup.html" id="home" class="home"><h1>Fake Hotel</h1></a>
        	<div class="flexExpand"></div>
        	<button type="submit" class="button1" onClick="setPageSignIn()">Sign In</button>
        	<input type="hidden" name="signInPage" id="sign_in_page"/>
        	<button type="submit" class="button1" onClick="setPageEvents()">View Current Events</button>
        	<input type="hidden" name="eventPage" id="events_page"/>
    	</header>
    	</div>
    	<main>
    		<div class="container">
    			<div class="center">
    				<p style="font-size:20px;margin-left: 17.5em;">Search Error</p>
    				<p style="font-size:16px;margin-left: 5em;">In order to search for available hotel rooms, or to view current reservations, you will need to sign in first.</p>
    				<p style="font-size:16px;margin-left: 5em;">Please navigate to the "Sign In" page by clicking the button at the top of the screen.</p>
    				<p style="font-size:16px;margin-left: 5em;">If you do not have an account yet, you can register an account at the "Sign In" page.</p>
    			</div>
    		</div>
    	</main>
    	<footer>
        	&copy; 2020 FakeHotel, a subsidiary of OtherFakeHotel. All rights reserved.
    	</footer>
	</form>
	<script>
	function setPageEvents() {
		var set_page = document.getElementById('events_page');
		set_page.value = "events_page";
		submit();
	}
	
	function setPageSignIn() {
		var set_page = document.getElementById('sign_in_page');
		set_page.value = "sign_in";
		submit();
	}
	
	function submit() {
		var form = document.getElementById('rooms_form');
		form.submit();
	}
	</script>
</body>
</html>