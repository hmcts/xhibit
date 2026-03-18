<!--
	// This file contains the data validation JavaScript functions
	// It is included in the HTML pages with forms that need these
	// data validation routines.


// DEFINE VARIABLES

// whitespace characters
var whitespace = " \t\n\r";

/****/
// Check whether string s is empty.

function isEmpty(s)
{   return ((s == null) || (s.length == 0))
}

/****************************************************************/


// Returns true if string s is empty or 
// whitespace characters only.

function isWhitespace (s)

{   var i;

    // Is s empty?
    if (isEmpty(s)) return true;

    // Search through string's characters one by one
    // until we find a non-whitespace character.
    // When we do, return false; if we don't, return true.

    for (i = 0; i < s.length; i++)
    {   
	// Check that current character isn't whitespace.
	var c = s.charAt(i);

	if (whitespace.indexOf(c) == -1) return false;
    }

    // All characters are whitespace.
    return true;
}

/****************************************************************/

function isNumber(strFieldIn)
{
	var strField = new String(strFieldIn);
	
	if (isWhitespace(strField)) return true;

	var i = 0;

	for (i = 0; i < strField.length; i++)
		if (strField.charAt(i) < '0' || strField.charAt(i) > '9') {
			return false;
		}

	return true;
}

/****************************************************************/

// PURPOSE:  Check to see if the string passed in is a valid time.
//	A valid time is defined as a string which is in the format [h]h:mm

	function isTime(strTime)
	{
		var strTestTime = new String(strTime);

		if (isWhitespace(strTestTime)) return true;
		if (strTestTime.indexOf(":",0) == 0) return false;

		var nColonPlace = strTestTime.indexOf(":",1);
		var hh = strTestTime.substr(0, nColonPlace);
		var mm = strTestTime.substr(nColonPlace+1);
//		alert(hh + "/" + mm);
		if (!isNumber(hh)) return false;
		if (mm.length != 2 || !isNumber(mm)) return false;
		var i_hh = parseInt(hh);
		var i_mm = parseInt(mm);
		if (hh < 0 || hh > 23) return false;
		if (mm < 0 || mm > 59) return false;

		// Check if time is in valid range (>18:30 and <06:00)
		if (hh < 18 && hh > 6) return false;
		if (hh == 18 && mm < 30) return false;
		if (hh == 6 && mm > 0) return false;
		return true;
	}

/****************************************************************/
	
/**** Some AJAX functions ***/

	function createRequestObject() {
		var tmpXmlHttpObject;
		
		// depending on browser support, use correct way to create XmlHttpRequest object
		if (window.XmlHttpRequest) {
			// Mozilla, Safari use this
			tmpXmlHttpObject = new XmlHttpRequest();
		} else if (window.ActiveXObject) {
			// IE would use this method
			tmpXmlHttpObject = new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		return tmpXmlHttpObject;
	}
	
	function makeGetRequest(selectedValue, url) {
		
		if (url.length==0) {
			alert('Error: url is empty');
			return;
		}
		
		http = createRequestObject();
		if (http == null) {
			alert('Browser does not support HTTP Request');
			return;
		}
		
		// build the url
		url = url+"?dropDown="+selectedValue;
		url = url+"&sid="+Math.random();
		
		// make connection to the server
		http.open('get', url);
		
		// assign a handler for the response
		http.onreadystatechange = processResponse;
		
		// actually send the request to the server
		http.send(null);
	}
	
	function processResponse() {
		// check if response has been received
		if (http.readyState == 4) {
			
			// read and assign the response from the server
			var response = http.responseText;
			
			return response;
		}
	}