(function() {
	var UI = function() {
		this.elements = {};
		
                this.init();
		this.listenToClick();
		this.listenToHover();
	}
	
	UI.prototype = {
		init : function() {
                    //new BlazeJS.UI.Tooltip( ".tooltip" );
		},
		
		listenToClick : function() {
			var target			= null,
				relatedTarget   = null,
				formType        = "";
			
			$( document ).bind( "click", function( e ) {
				// ensure that just the left mouse key is clicked
				if ( ( $.browser.msie && e.which === 0 ) || ( !$.browser.msie && e.which === 1 ) ) {
					target = $( e.target );
					relatedTarget = target.closest( ".blaze-ui-form" );
					
					// buttons
					if ( relatedTarget.hasClass( "blaze-ui-bttn" ) ) {
						// close all select dropdowns
						$( ".blaze-ui-select.active .dropdown").addClass( "noDisplay" ).parent().removeClass( "active" );
					}
                                        else if ( relatedTarget.hasClass( "blaze-ui-icon" ) ) {
                                            if ( relatedTarget.hasClass( "disabled" ) ) e.preventDefault();
                                        }
					// select 
					else if ( relatedTarget.hasClass( "blaze-ui-select" ) ) {
						// "clickable" element clicked
						if ( target.closest( ".clickable" ).length > 0 ) {
							var uiid 		= relatedTarget.attr( "data-uiid" ),
								instance 	= null;
							
							instance = BlazeJS.UI.get( uiid );
							
							if ( instance ) {
								// hide dropdown
								if ( relatedTarget.hasClass( "active" ) ) {
									instance.hideOptions();
								}
								// show dropdown
								else {
									instance.showOptions();
								}
							}
						}
						// item selected
						if ( target[ 0 ].nodeName === "LI" ) {
							var uiid 		= relatedTarget.attr( "data-uiid" ),
								instance 	= null;
							
							instance = BlazeJS.UI.get( uiid );
							
							if ( instance ) {
								instance.setValue( target.attr( "rel" ) );
								instance.hideOptions();
								relatedTarget.removeClass( "hover" );
							}
						}
					}
					// checkbox
					else if ( relatedTarget.hasClass( "blaze-ui-cb" ) ) {
						var uiid 		= relatedTarget.attr( "data-uiid" ),
							anchor		= relatedTarget.find( "a" ),
							instance 	= null;
							
							instance = BlazeJS.UI.get( uiid );
							
							if ( anchor.hasClass( "checked" ) ) {
								instance.setValue( false );
							}
							else if ( anchor.hasClass( "undefined" ) ) {
								instance.setValue( "undefined" );
							}
							else {
								instance.setValue( true );
							}
						
						e.preventDefault();
					}
					// radio button
					else if ( relatedTarget.hasClass( "blaze-ui-radio" ) ) {
						var uiid 		= relatedTarget.attr( "data-uiid" ),
							anchor		= relatedTarget.find( "a" ),
							instance 	= null;
							
							instance = BlazeJS.UI.get( uiid );
							
							if ( anchor.hasClass( "checked" ) ) {
								
							}
							else {
								instance.setValue( true );
							}
						
						e.preventDefault();
					}
					// hide all select dropdowns
					else {
						$( ".blaze-ui-select.active .dropdown").addClass( "noDisplay" ).parent().removeClass( "active" );
					}
				}
			});
		},
		
		listenToHover : function() {
			var target			= null,
				relatedTarget   = null,
				formType        = "";
			
			$( document ).bind( "mouseover", function( e ) {
				target = $( e.target );
				relatedTarget = target.closest( ".blaze-ui-form" );
				
				if ( !relatedTarget.hasClass( "disabled" ) ) {
					relatedTarget.addClass( "hover" );
				}
			})
			
			$( document ).bind( "mouseout", function( e ) {
				if ( relatedTarget ) {
					relatedTarget.removeClass( "hover" );
				}
			})
		},
		
		add : function( element ) {
			if ( !this.get( element.uiid ) ) {
				this.elements[ element.uiid ] = element;
			}
		},
		
		get : function( uiid ) {
			return this.elements[ uiid ];
		},
		
		generateUIID : function() {
			return 'xxxxxxxxx'.replace( /[xy]/g, function(c) { var r = Math.random()*16|0,v=c=='x'?r:r&0x3|0x8;return v.toString(16); } );
		}	
	}
	
	BlazeJS.UI = {};
	
	uiinstance = new UI();
	
	BlazeJS.UI.get = function( uiid ) {
		return uiinstance.get( uiid );
	}
	
	BlazeJS.UI.add = function( element ) {
		return uiinstance.add( element );
	}
	
	BlazeJS.UI.generateUIID = function() {
		return uiinstance.generateUIID();
	}
})();