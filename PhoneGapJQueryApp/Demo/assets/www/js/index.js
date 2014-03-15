document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    var options = new ContactFindOptions();
    options.filter = "HCove";
    options.multiple = true; 
    var fields = ["displayName", "name", "phoneNumbers"];
    navigator.contacts.find(fields, onSuccess, onError, options);
}

function onSuccess(contacts) {
	  for(var i = 0; i < contacts.length; i++) {
	    var html = '<div data-role="collapsible" data-collapsed-icon="arrow-d" data-expanded-icon="arrow-u" data-inset="false">';

	    html += '<h4>' + contacts[i].displayName + '</h4>';
	    html += '<ul data-role="listview" data-inset="true">'

	    var contact = contacts[i];

	    for(var j = 0; j < contact.phoneNumbers.length; j++) {
	    	console.log("Display Name = " + contacts[i].displayName);

	      html += '<li>' + contact.phoneNumbers[j].type + 
	        ": " + contact.phoneNumbers[j].value + '</li>';
	    }

	    html += '</ul></div>';

	    $('#contactsList').append(html);
	  }

	  $('[data-role=collapsible]').collapsible().trigger('create');
	}

	function onError(contactError) {
	  alert('onError!');
	}
	
	//$("#contactsPage").on("pageshow", function(e) {
//	$(document).on("pageshow","#contactsPage", function(e) {
//		console.log("Autocomplete");
//		alert("hahaha");
//		var sugList = $("#suggestions");
//		$("#searchField").on("input", function(e) {
//			alert("hehehe");
//	        var text = $(this).val();
//		}
//	});
	
	
// $( document ).on( "pageinit", "#demo-page", function() {
//	    $( document ).on( "swipeleft swiperight", "#demo-page", function( e ) {
//	        // We check if there is no open panel on the page because otherwise
//	        // a swipe to close the left panel would also open the right panel (and v.v.).
//	        // We do this by checking the data that the framework stores on the page element (panel: open).
//	        if ( $.mobile.activePage.jqmData( "panel" ) !== "open" ) {
//	            if ( e.type === "swipeleft"  ) {
//	                $( "#rightPanel" ).panel( "open" );
//	            } else if ( e.type === "swiperight" ) {
//	                $( "#leftPanel" ).panel( "open" );
//	            }
//	        }
//	    });
//	});