(function() {
	window.BlazeJS = {};
	
})();

BlazeJS.Util = {
	getValueFromParameter : function( type, args ) {
		for ( var i = 0; i < args.length; i++ ) {
			if ( typeof args[ i ] === type ) {
				return args[ i ];
			}
		}
		
		return undefined;
	},
	
	switchText : function( $elem, value, duration ) {
		duration = duration || 400;
		
		$elem.fadeOut( duration / 2, function() {
			$( this ).text( value );
		});
		
		$elem.fadeIn( duration );
	},
	
	getPagePosition : function( event ) {
		if ( event.pageX || event.pageY ) {
	        return { 
	        	x : event.pageX,
	        	y : event.pageY
	        }
		}
		else if ( event.clientX || event.clientY ) {
	        return {
	        	x : event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft,
	        	y : event.clientY + document.body.scrollTop + document.documentElement.scrollTop
	        }
		}
		
		return undefined;
	}
}

BlazeJS.Core = {
	Class : (function() {
		var extending         = {},
			excludeProperties = [ "extend" ];
		
		//o - Object, which contains Initialize Function, Methods
		return function( o ) {
			var options = o.options || {},
				 parent = o.extend || null,
				 properties;
			
			//Helper function to build prototype inheritance
			function F() {
				var options = arguments[ 0 ];
				
				if ( typeof options === "object" ) {
					for ( var p in options ) {
						//if ( this.options && this.options[ p ] != null ){
							this.options[ p ] = options[ p ];
					}
				}
				//initialize function get called, needs to be implemented in every class
				this.initialize.apply( this, arguments );
			}
			
			function subclass() {};
			F.superclass = parent;
			F.subclasses = [];
			
			//If extending
			if ( parent ) {
				subclass.prototype = parent.prototype;
				F.prototype = new subclass;
				F.prototype.$super = parent;
				if ( parent.subclasses ) {
					parent.subclasses.push(F);
				}
			}
			
			//Copy all properties (and methods) to the prototype of the helper function 'F'
			for ( var p in o ) {
				if ( jQuery.inArray( p, excludeProperties ) === -1 ) {
					F.prototype[ p ] = o[ p ];
				}
			}
			
			F.prototype.constructor = F;
			
			return F;
		}
	})()
}

BlazeJS.FX = {
	scrollTo : function( hor, ver, duration, callback ) {
		var currentX  = window.pageXOffset,
			currentY  = window.pageYOffset,
			startX 	  = currentX,
			startY    = currentY,
			startTime = ( new Date ).getTime(),
			stepsX    = hor ? 10 : 0,
			stepsY    = currentY > ver ? -10 : 10;
		
		if ( !( ( currentY + 20 >= ver - 5 ) && ( currentY + 20 <= ver + 5 ) ) ) {
			var scrollInterval = window.setInterval( function() {
				window.scrollBy( stepsX, stepsY );
				
				// Update new position
				currentX = currentX + stepsX;
				currentY = currentY + stepsY;
				
				if ( ( currentY + 20 >= ver - 5 ) && ( currentY + 20 <= ver + 5 ) ) {
					clearInterval( scrollInterval );
					callback();
				}
			}, duration )
		}
	}
};


/**
 * Replace the <input type="file"> HTML Tag with a custom FileChooser Wrapper.
 * This allows you to style it with custom CSS Styles
 *
 * @param: 
*/
BlazeJS.Modules = {
	fileChooser : new BlazeJS.Core.Class({
		options : {
			selector : "",
			markup : "<div class='input-file-wrapper'><input class='input' type='text' /><div class='input-file'><span class='replace'></span></div></div>"
		},
		
		initialize : function() {
			var options = this.options,
				   elem = options.selector;
				
			if ( elem ) {
				elem = $( elem );
				
				elem.each( function() {
					var curEl = $( this );
					//Insert HTML Markup
					var newEl = $( options.markup ).insertBefore( curEl );
					//Replace the Placeholder with the current <input type="file" />
					newEl.find( ".replace" ).replaceWith( curEl );
					
					//Get the input where the path will be stored
					var input = curEl.parent().parent().find( "input[type='text']" )
					
					curEl.bind( "change", function() {
						if ( input[ 0 ].value !== this.value ) {
							input[ 0 ].value = this.value;
						}
					});
				});
			}
		}
	}),
	
	/*formReplacement : new BlazeJS.Core.Class({
		formElementClass : {
			button   : "blaze-ui-bttn",
			checkbox : "blaze-ui-cb",
			radio    : "blaze-ui-radio",
			icon	 : "blaze-ui-icon",
			select   : "blaze-ui-select"
		},
		
		options : {
		},
		
		initialize : function() {
			var selector = "",		//current selector
				elems,              //all matching elements
				that = this,
				dropdown,
				mousedown = false;				
			
			//Register Hover Event Handler
			$( ".blaze-ui-form" ).hover( function( e ) {
				var $root = $( e.target ).closest( ".blaze-ui-form" );
				
				//Select
				if ( $root.hasClass( that.formElementClass.select ) ) {
					//reset mousedown flag to prevent width bug
					mousedown = false;
					//Set new width on this element, because the Hover Button is larger
					//this.style.width = $( this ).width() + 4 + "px";
					
					//Also Fix dropdown width
					$( this ).find( ".dropdown" ).width( $( this ).width() - 10 );
				}
				
				if ( !$root.hasClass( "disabled" ) ) {
					$( this ).addClass( "hover" );
				}
			}, function( e ) {
				//Select
				if ( $( e.target ).closest( ".blaze-ui-form" ).hasClass( that.formElementClass.select ) ) {
					//only decrease the with, when button isn't pressed right now
					if ( !mousedown ) {
						//decrease the with again
						//this.style.width = $( this ).width() - 4 + "px";
					}
				}
				
				$( this ).removeClass( "hover" );
			});
			
			$( ".blaze-ui-form a" ).focus( function() {
				var wrapper = $( this ).closest( ".blaze-ui-form" );
				
				// add focus to checkbox & radio buttons
				if ( ( wrapper.hasClass( that.formElementClass.checkbox ) && !wrapper.hasClass( "disabled" ) ) || wrapper.hasClass( that.formElementClass.radio ) ) {
					wrapper.addClass( "focus" );
				}
			});
			
			for ( var p in this.options ) {
				//save current selector in variable
				selector = this.options[ p ];
				elems = $( selector );
				
				//@TODO: mit substring, denn wenn man nur die Buttons in einem Panel neu rendern will, dann kann sich der Button spezifische Selector auch nach einer ID befinden
				
				//If we're dealing with input buttons
				if ( selector == "." + this.formElementClass.button ) {
					var $curElem = undefined,
						width = 0,
						zIndexStart = 100,
						icon = null,
						mousedown = false;		//Indicates, if the button is pressed right now
					
					elems.each( function() {
						$curElem = $( this );
						
						//Get length of input value + 20px for the padding
						width = $curElem.find( "input, a" ).width() + 28;
						
						// if icon is available, add also it's width 
						icon = $curElem.find( ".icon" );
						if ( icon.length != 0 ) {
							width += icon.width() + 8;
						}
						
						//Set new width on this element
						this.style.width = width + "px";
						
						// set 'disabled' class if input is disabled
						if ( $curElem.find( "input" ).attr( "disabled" ) ) {
							$curElem.addClass( "disabled" );
						}
					} );
					
					//Register Mouse Pressed Event Handler
					elems.mousedown( that.mouseUpAfterDrag );
				}
				//If we're dealing with select dropdowns
				else if ( selector == "." + this.formElementClass.select ) {
					var width = 0,
						mousedown = false;		//Indicates, if the button is pressed right now
					
					elems.each( function() {
						var selectEl = $( this ).find( "select" ),
							longest = "",
							pWidth = 0;
						
						// if width for element is defined, then skip the following calculation of the new width
						if ( selectEl.css( "width" ).match( /%/gi ) === null ) {
							//Get length of input value + ,depending on the length of the selected option tag + 23px for padding
							width = selectEl.width() + 23;
							
							// get width, based on the longest value
							selectEl.find( "option" ).each( function() {
								if ( $( this ).text().length > longest.length ) {
									longest = $( this ).text();
								}
							});
							
							pWidth = Math.round( longest.length * 5.5 + 40 );
							
							// if pWidth (possible width) is greater than 'width', then apply pWidth to the element
							if ( pWidth > width ) {
								width = pWidth;
							}
							
							// ensure min width
							width = width < 65 ? "65px" : width + "px";
						}
						else {
							width = selectEl.css( "width" );
						}
						
						//Set new width on this element
						this.style.width = width;
						
						//Set text from Select
						$( this ).find( ".blaze-ui-bg.mdl span" ).text( selectEl.find( "option:selected" ).text() );
						
						//Set z-index value to allow dropdown to overlap other select
						$( this ).css( "zIndex", zIndexStart-- );
						
						// check if select tag is disabled, then apply class name 'disabled
						if ( selectEl.attr( "disabled" ) ) {
							$( this ).addClass( "disabled" );
						}
					} );
					
					$( selector + " .clickable" ).click( function() {
						var curSelect = $( this ).parent();
						
						// only open dropdown, if element isn't disabled
						if ( !curSelect.hasClass( "disabled" ) ) {
							dropdown = curSelect.find( ".dropdown" );
							
							if ( !curSelect.hasClass( "active" ) ) {
								//Hide all other active Dropdowns
								that.hideAllSelectDropdowns();
								
								//SlideDown the Dropdown with the option elements
								dropdown.show();
								curSelect.addClass( "active" );
							}
							else {
								dropdown.hide();
								curSelect.removeClass( "active" );
							}
						}
					});
					
					//Register Click Event for <li>'s
					$( selector + " .dropdown li" ).click( function() { 
						var curLi = $( this ),
							curSelect = curLi.closest( selector ),
							label = curSelect.find( ".clickable span" );
						
						//Update Label
						label.text( curLi.text() );
						//Update hidden <select>
						curSelect.find( "select option[selected]" ).removeAttr( "selected" );
						curSelect.find( "select option[value='" + curLi.attr( "rel" ) + "']" ).attr( "selected", "selected" );
						
						//Update '.selected' class
						curLi.parent().find( ".selected" ).removeClass( "selected" );
						curLi.addClass( "selected" );
						
						curSelect.find( ".dropdown" ).hide();
						curSelect.removeClass( "active" );
					});
				}
				//If we're dealing with Checkboxes
				else if ( selector == "." + this.formElementClass.checkbox ) {
					$( elems ).each( function() {
						//If input-checkbox is checked
						var cur = $( this );

						if ( cur.find( "input" ).attr( "checked" ) ) {
							cur.find( "a" ).addClass( "checked" );
						}
						if ( cur.find( "input" ).attr( "disabled" ) ) {
							cur.addClass( "disabled" );
						}
					});
					
					$( "a", elems ).click( function( e ) {
						var el = $( this );
						
						//If checkbox isn't disabled
						if ( !el.parent().hasClass( "disabled" ) ) {
							if ( el.hasClass( "checked" ) ) {
								el.removeClass( "checked" );
								el.next().attr( "checked", false );
								
								//data_table uncheck all other checkboxes
								if ( el.closest( ".select_all_cb" ).length > 0 ) {
									el.closest( "thead" ).next().find( ".select_cb .blaze-ui-cb a" ).each( function() {
										var el = $( this );
										el.removeClass( "checked" );
										el.next().attr( "checked", false );
										
										//Remove Row Highlight
										el.closest( "tr" ).removeClass( "selected" );
									});
									
								}
							}
							else {
								el.addClass( "checked" );
								el.next().attr( "checked", true );
								
								//data_table Check all other checkboxes
								if ( el.closest( ".select_all_cb" ).length > 0 ) {
									el.closest( "thead" ).next().find( ".select_cb .blaze-ui-cb a" ).each( function() {
										var el = $( this );
										el.addClass( "checked" );
										el.next().attr( "checked", true );
										
										//Add Row Highlight
										el.closest( "tr" ).addClass( "selected" );
									});
								}
							}
						}
						
						e.preventDefault();
					});
					
					// allow clicking on label to change value of checkbox
					$( "input", elems ).closest( "li" ).find( "label" ).click( function() {
						var $that = $( this );
						
						$that.closest( "li" ).find( ".blaze-ui-cb a" ).toggleClass( "checked" );
					});
					
					// hover event for checkbox
					$( "a", elems ).hover( function() {
						var el = $( this );
						
						//If checkbox isn't disabled
						if ( !el.parent().hasClass( "disabled" ) ) {
							el.parent().addClass( "hover" );
						}
					}, function() {
						var el = $( this );
						
						//If checkbox isn't disabled
						if ( !el.parent().hasClass( "disabled" ) ) {
							el.parent().removeClass( "hover" );
						}
					});
				}
				//If we're dealing with radio inputs
				else if ( selector == "." + this.formElementClass.radio ) {
					$( elems ).each( function() {
						//If input-checkbox is checked
						var cur = $( this );

						if ( cur.find( "input" ).attr( "checked" ) ) {
							cur.find( "a" ).addClass( "checked" );
						}
						if ( cur.find( "input" ).attr( "disabled" ) ) {
							cur.addClass( "disabled" );
						}
					});
					
					$( "a", elems ).click( function( e ) {
						var el = $( this );
						
						//If checkbox isn't disabled
						if ( !el.parent().hasClass( "disabled" ) ) {
							if ( !el.hasClass( "checked" ) ) {
								// uncheck all other radio inputs with the same 'name', 
								// because only one radio input can be checked at the same time in the same group
								uncheckAll( el.next().attr( "name" ) );
								
								el.addClass( "checked" );
								el.next().attr( "checked", true );
							}
						}
						
						function uncheck( $el ) {
							$el.removeClass( "checked" );
							$el.next().attr( "checked", false );
						}
						
						function uncheckAll( name ) {
							$( "input[type='radio'][name='" + name + "']" ).each( function() {
								uncheck( $( this ).prev() );
							});
						}
						
						e.preventDefault();
					});
					
					// hover event for radio
					$( "a", elems ).hover( function() {
						var el = $( this );
						
						//If radio isn't disabled
						if ( !el.parent().hasClass( "disabled" ) ) {
							el.parent().addClass( "hover" );
						}
					}, function() {
						var el = $( this );
						
						//If radio isn't disabled
						if ( !el.parent().hasClass( "disabled" ) ) {
							el.parent().removeClass( "hover" );
						}
					});
				}
			}
		},
		
		mouseUpAfterDrag : function(e) {
		    var curEl = $( this );
		    
			//add class name 'active'
			curEl.addClass( "active" );
			
			curEl.bind( "mouseleave.active", function() {
				curEl.removeClass( "active" );
			});
			
			curEl.bind( "mouseenter.active", function() {
				if ( !curEl.hasClass( "active" ) ) curEl.addClass( "active" );
			});
			
		    $( document ).one('mouseup', function() {
		    	curEl.removeClass( "active" );
		    	curEl.unbind( "mouseenter.active" );
		        $().unbind();
		        $( document ).mousedown(BlazeJS.Modules.formReplacement.mouseUpAfterDrag);
		    });
		},
		
		hideAllSelectDropdowns : function() {
			$( ".blaze-ui-select .dropdown" ).slideUp( 50 )
											 .parent().removeClass( "active" );
		}
	}),*/
	
	dataTable : new BlazeJS.Core.Class({
		options : {
			selector : "",
			renderFilter : true
		},
		
		initialize : function() {
			var elems = $( this.options.selector ),
				that = this;
			
			/*elems.each( function() {
				that.addColumnSorter( $( this ), [ "titel", "autor" ] );
			});*/
		},
		
		addColumnSorter : function( el, columns ) {
			el.find( "thead th" ).each( function() {
				var curEl = $( this );
				
				//if column is specified in array, to be hoverable
				if ( $.inArray( curEl.attr( "class" ), columns ) > -1 ) {
					curEl.addClass( "sortable" );
					
					//insert sortable indicator (arrow)
					curEl.append( "<span class='sortable-icon'></span>" );
				}
			});
			
			el.find( "thead th" ).hover( function() {
				var curEl = $( this );
				
				//if column is specified in array, to be hoverable
				if ( curEl.hasClass( "sortable" ) ) {
					curEl.addClass( "hover" );
				}
			}, function() {
				if ( $( this ).hasClass( "hover" ) )
					$( this ).removeClass( "hover" );
			});
			
			el.find( "thead th" ).click( function() {
				var curEl = $( this );
				
				if ( !curEl.hasClass( "desc" )  && !curEl.hasClass( "asc" ) )
					curEl.addClass( "asc" );
				else if ( curEl.hasClass( "asc" ) )
					curEl.removeClass( "asc" ).addClass( "desc" );
				else
					curEl.removeClass( "desc" );
			});
		}
	}),
	
	/**
	 * Replace the standard tooltip with a custom tooltip. 
	 * Elements displaying tooltips must have a title attribute, which contains the displayed
	 * tooltip text
	 *
	 * Usage:    new BlazeJS.Modules.Tooltip( { selector : ".selector" } );
	 *
	 * @options: 
	*/	
	Tooltip        : new BlazeJS.Core.Class( {
		options : {
			selector : "",
			markup : "<div class='blaze-ui-tooltip'></div>",
			delay : 500
		},
		
		initialize : function() {
			var elems = $( this.options.selector ),
				clicked = false,
				that = this,
				timeout = null,
				mouseMoveTimeout = null,
				mouseStopped = false,
				mouseLeft = false,
				tooltip,
				title = "";
			
			elems.live( "mouseenter mouseleave", function( e ) {
				var $el = $( this );

				if ( e.type == "mouseenter" ) {	
					//Store title attribute of the current element (to prevent browser tooltip)
					storeTitleAttribute( $el );	
					
					// check if mouse doesn't move upon the element which triggers the tooltip
					$el.mousemove( function( eMouseMove ) {
						// allow user to move mouse 15px to the left, right, up and down without hiding the tooltip (when tooltip is displayed already)
						if ( mouseStopped && tooltip ) {
							var tooltipOffset = tooltip.offset();
							
							if ( ( eMouseMove.clientX < tooltipOffset.left - 12 ) || ( eMouseMove.clientX > tooltipOffset.left + 12 ) || 
								 ( eMouseMove.clientY < tooltipOffset.top - 25  ) || ( eMouseMove.clientY > tooltipOffset.top + 25  ) ) {
								// if title attribute has changed during execution, store the new title attribute
								if ( $el.attr( "title" ) !== title && $el.attr( "title" ) !== "" ) {
									storeTitleAttribute( $el );
								}
								
								//Remove Tooltip from document
								if ( tooltip != undefined ) {
									tooltip.remove(); 
									tooltip = undefined;
								}
							}
						}
						if ( !mouseLeft && clicked && tooltip === undefined ) {
							storeTitleAttribute( $el );	
							clicked = false;
							mouseLeft = false;
						}
						// ensure, that the tooltip doesn't show up more than once while hovering upon the element
						if ( mouseStopped === false ) {	
							// Clear mouseMoveTimeout whenever mouse moves again and doesn't stop.
							// If mouse stop's moving, then the timeout will be triggered
							window.clearTimeout( mouseMoveTimeout );

							mouseMoveTimeout = window.setTimeout( function() {
								//Store title attribute of the current element (to prevent browser tooltip)
								
								
								mouseStopped = true;
								
								timeout = window.setTimeout( function() {
									var coords = BlazeJS.Util.getPagePosition( eMouseMove );
									
									// calc tooltip position
									var tPosition = that.calcNewPosition( coords.x, coords.y, title );
									
									// finally show the tooltip
									tooltip = that.show( title, tPosition );
								}, that.options.delay );
							}, 200);
						}
					});
					// add click event, that when user clicks on the element with the tooltip the tooltip fades out
					$el.bind( "mousedown", function() {
						removeTooltip( $el );
						clicked = true;
					});
				}
				else {
					mouseLeft = true;
					removeTooltip( $el );
				}
			});
			
			function removeTooltip( $elem ) {
				//Clear Timeout & reset title attribute
				window.clearTimeout( timeout );
				window.clearTimeout( mouseMoveTimeout );
				mouseStopped = false;
				
				//Reset title attribute ONLY IF no title attribute is specified on the element
				if ( $elem.attr( "title" ) === "" ) $elem.attr( "title", title );

				//Remove Tooltip from document
				if ( tooltip != undefined ) {
					tooltip.remove();tooltip = undefined;
				}
			}
			
			function storeTitleAttribute( el ) {
				title = el.attr( "title" );
				//Remove title attribute
				el.removeAttr( "title" );
			}
		},
		
		/**
		 * Calculates the position, where the tooltip will be displayed. It also ensures, that the tooltip
		 * will not be displayed too close to the window boundings.
		 * 
		 * @param {int} currentMouseX	Current position of mouse on the X axis
		 * @param {int} currentMouseY	Current position of mouse on the Y axis
		 * @param {string} text			Text, that the tooltip will display
		 */
		calcNewPosition : function( currentMouseX, currentMouseY, text ) {
			var possibleWidth = 0;
			
			// calc possible width of the tooltip
			possibleWidth = Math.round( text.length * 5 ) + 10;
			
			// if tooltip is too close to the window boundings, display the tooltip to the left of the mouse cursor
			if ( ( currentMouseX + possibleWidth ) > window.innerWidth ) {
				return {
					left : currentMouseX - possibleWidth - 15,
					top  : currentMouseY + 13
				}
			}
			
			// if tooltip is not too close the the window boundings, just return mouse position
			return {
				left : currentMouseX - 10,
				top  : currentMouseY + 13
			}
		},
		
		/**
		 * Shows the tooltip with the passed text
		 * 
		 * @param {String} text			Text, that the tooltip will display
		 * @param {Object} position		Position in the window, where the tooltip should be displayed (position.left, position.top)
		 */
		show : function( text, position ) {
			var tooltip = $( this.options.markup );
			
			//Set tooltip text
			tooltip.html( text );
			//Insert HTML into document body
			tooltip.css( { display : "none" } ).appendTo( document.body );
			//Set tooltip position
			tooltip.css( { top : position.top + 5, left : position.left + 10 } );
			//Show Tooltip
			tooltip.show();
			
			return tooltip;
		}
	}),
	
	NumericStepper : new BlazeJS.Core.Class( {
		options : {
			selector : "",
			steps : 1,
			max : ""
		},
		
		initialize : function() {
			var elems = $( this.options.selector ),
				that = this;
			
			//If associated input is disabled
			elems.each( function() {
				if ( $( this ).prev().attr( "disabled" ) ) {
					$( this ).remove();
				}
			});
			
			elems.find( ".inc" ).click( function( e ) {
				var input = $( this ).parent().prev();
				
				if ( input.val() == "" ) {
					input.val( that.options.steps );
				}
				else {
					//Get start/end position of user selection
					var selectedStart = input[ 0 ].selectionStart,
						selectedEnd = input[ 0 ].selectionEnd,
						selection = "",
						value = input.val();
					
					//If value in input is selected
					if ( ( selection = value.substring( selectedStart, selectedEnd ) ).length != 0 ) {
						var unselectedTextUntilSelectionStart = value.substring( 0, selectedStart ),
							unselectedTextAfterSelectionEnd = value.substring( selectedEnd, value.length );
						
						//check if value is smaller than max
						if ( that.options.max != "" && parseInt( selection ) < that.options.max ) {
							//Increase selected value
							input.val( unselectedTextUntilSelectionStart + ( parseInt( selection ) + that.options.steps ) + unselectedTextAfterSelectionEnd );
						}
						//value bigger than defined max value
						else {
						
						}
					}
					else
						input.val( parseInt( value ) + that.options.steps );
				}
				
				e.preventDefault();
			});
			elems.find( ".dec" ).click( function( e ) {
				var input = $( this ).parent().prev();
				
				if ( input.val() == "" ) {
					input.val( "-" + that.options.steps );
				}
				else {
					input.val( parseInt( input.val() ) - that.options.steps );
				}
				
				e.preventDefault();
			});
		}
	}),
	
	stickyControls : new BlazeJS.Core.Class( {
		options : {
		},
		
		initialize : function() {
			var elems = $( this.options.selector ),
				that = this;
			
			//event that will be triggered, if user scrolls within the document
			$( window ).scroll( function(e) {
				that.slideToViewport( elems, $( document ).scrollTop() );
			});
		},
		
		slideToViewport : function( elems, documentScrollTop ) {
			var that = this;
			
			elems.each( function() {
				//Save current widget reference for performance improvement
				var curEl = $( this );
				
				//if the treeview and the header are not visible in the current viewport,
				//the sticky control panel will be allways on top left
				if ( documentScrollTop > ( that.getVisibleHeight( curEl ) + 20 ) ) {
					if ( curEl.next().length > 0 && $( this ).parent().find( ".widget" ).length > 0 ) {
						//22, because of the bottom margin & 2px for the border
						var bottomPosition = curEl.position().top + curEl.height() + 22,
							ancestorEl = curEl.next();

						//If sticky widget & next widget are overlapping
						if ( bottomPosition >= ancestorEl.position().top && curEl.position().top <= ( ancestorEl.position().top + ancestorEl.height() ) ) {
							curEl.remove();
							curEl.insertAfter( ancestorEl );
							curEl.show();
							console.log( "overlapping" );
							//Checks if current browser is MS Internet Explorer and forces it to reflow/repaint
							//the whole document to get rid of margin errors
							if ( $.browser.msie )
								document.body.className = document.body.className;
						}
					}
					else {
						var siblingsLength = curEl.prevAll().length;
						var distance = documentScrollTop - that.getVisibleHeight( curEl ) - ( siblingsLength * 20 );
						
						$( this ).css( "position", "relative" );
						$( this ).animate( { top :   distance + 8 + "px" }, 50 );
					}
				}
				//If header & treeview is back in viewport
				else {
					$( this ).css( { top : "0px" } );
				}
			});
		},
		
		getVisibleHeight : function( sticky ) {
			var baseHeight = $( ".header" ).height();
			sticky.prevAll().each( function() {
				baseHeight += $( this ).height();
			});

			return baseHeight;
		}
	}),

	Treeview       : new BlazeJS.Core.Class( {
		options : {
			selector : ""
		},
		
		initialize : function() {
			var trees = $( this.options.selector ),
				that = this;
			
			//traversing through all single trees
			trees.each( function() {
				var currentEl = $( this );
				
				//traversing all <li>'s 
				currentEl.find( "li" ).each( function() {
					var currentLi = $( this ),
						subMenues = currentLi.find( "ul" );
					
					//if <li> has a submenue
					if ( subMenues.length > 0 ) {
						//insert toggle arrow
						currentLi.find( "a:first" ).before( $( "<a class='toggle open'></a>" ) );
						
						//add submenue class name
						subMenues.addClass( "sub" );
					}
				});
				
				//add event handler (delegation)
				that.addEventHandler( currentEl );
			});
		},
		
		addEventHandler : function( el ) {
			el.click( function( e ) {
				//if event is triggered on the toggle <a> element
				if ( e.target.className.indexOf( "toggle" ) >= 0) {
					var clickedEl = $( e.target ),
						subMenue = clickedEl.parent().parent().find( "ul" );
					
					if ( subMenue.is( ":hidden" ) ) {
						//make submenue visible
						subMenue.slideDown( 300 );
						//set arrow in other direction
						clickedEl.removeClass( "close" )
								 .addClass( "open" );
					}
					else {
						subMenue.slideUp( 300 );
						//set arrow in other direction
						clickedEl.removeClass( "open" )
								 .addClass( "close" );
					}
				}
			});
		}
	}),
	
	
	/**
	 * Replace the standard tooltip with a custom tooltip. 
	 * Elements displaying tooltips must have a title attribute, which contains the displayed
	 * tooltip text
	 *
	 * Usage:    new BlazeJS.Modules.Tooltip( { selector : ".selector" } );
	 *
	 * @options: 
	*/	
	FileUpload	: new BlazeJS.Core.Class( {
		options : {
			selector : ""
		},
		
		initialize : function() {
			var elems = $( this.options.selector ),
				that  = this;
			
			elems.each( function() {
				var $fileUploadWrapper = $( this );
				
				that.addEvents( $fileUploadWrapper );
			});
		},
		
		addEvents : function( $fileUploadWrapper ) {
			var that = this;
			
			// enable show/hide all added files in the upload queue
			
			
			$fileUploadWrapper.find( ".file-table li" ).live( "mouseenter mouseleave", function( e ) {
				var $cancelUploadAnchor = null,
					$that               = $( this );
				
				if ( e.type == "mouseenter" ) {
					// add 'cancel' tag
					$that.prepend( $( "<a class='cancel icon-cancel-rounded tooltip' href='#' title='Datei Upload Abbrechen'>" ) );
					
					$that.addClass( "hover" );
				}
				else {
					$cancelUploadAnchor = $that.find( "a.cancel" );
					
					// remove 'cancel' tag
					if ( $cancelUploadAnchor.length != 0 ) {
						$cancelUploadAnchor.remove();
					}
					
					$that.removeClass( "hover" );
				}
			});
		},
		
		toggleFileTable : function( $fileTable ) {
			// slide file table down, if it's hidden
			if ( $fileTable[ 0 ].style.display === "none" ) {
				$fileTable.slideDown( 300 );
				return 1;
			}
			else {
				$fileTable.slideUp( 300 );
				return 0;
			}
		}
	}),
	
	AddPanelToDashboard : function( selector ) {
		var addDashboardClicked = false,
			$elem 				= $( selector )
			action				= "add";
		
		$elem.click( function( e ) {
			var $that = $( this ),
				$msg  = $that.find( ".msg" );
			
			if ( !addDashboardClicked ) {
				var $iconTag = $that.find( ".icon" );
				
				// set 'a' tag as inactive
				$that.addClass( "inactive" );
				
				addDashboardClicked = true;
					
				// modul will be appended on the dashboard
				if ( action === "add" ) {
					// show ajax loader
					$iconTag.addClass( "ajaxloader-radial-blue-small" );
					
					// Send Request ( currently just demo )
					setTimeout( function() {
						$msg.fadeOut( 200, function() {
							$msg.text( "Von Startseite Entfernen" );
							
							// update css classes & replace ajax loader with success icon
							$iconTag.removeClass( "ajaxloader-radial-blue-small" );
							$iconTag.parent().removeClass( "inactive" );
							$iconTag.parent().addClass( "remove" );
							
							addDashboardClicked = false;
							
							// update title attribute
							$that[ 0 ].setAttribute( "title", "Modul von Startseite wieder entfernen" );
						} );
						
						// fade in message
						$msg.fadeIn( 500 );
					}, 4000);
				}
				// modul will be removed from dashboard
				else if ( action === "remove" ) {
					// show ajax loader
					$iconTag.addClass( "ajaxloader-radial-small" );
					
					setTimeout( function() {
						$msg.fadeOut( 200, function() {
							// add success message
							$msg.text( "Zur Startseite Hinzufügen" );
							
							// update css classes & replace ajax loader with success icon
							$iconTag.removeClass( "ajaxloader-radial-small" );
							$iconTag.parent().removeClass( "inactive" );
							$iconTag.parent().removeClass( "remove" );
							
							addDashboardClicked = false;
							
							// update title attribute
							$that[ 0 ].setAttribute( "title", "Modul zu Startseite hinzufügen" );
						} );
						
						// fade in message
						$msg.fadeIn( 500 );
					}, 4000);
				}
				
				if ( action === "add" ) {
					action = "remove";
				}
				else if ( action === "remove" ) {
					action = "add";
				}
			}
			
			// instead of 'return false' use this statement, to allow other click events on this anchor element
			e.preventDefault();
		});
		
		$elem.parent().hover( function() {
			$( this ).toggleClass( "hover" );
		});
	},
	
	ContentPanelSidebar : function( selector ) {
		var $elem 				= $( selector ),
			$listItems 			= undefined,
			calculatedHeight 	= 0;
		
		$elem.each( function() {
			$listItems = $elem.find( ".side ul:first > li" );
			
			$listItems.each( function() {
				calculatedHeight += $( this ).height();
			});
			
			$elem.find( ".main" ).css( "minHeight" , calculatedHeight );
			
			calculatedHeight = 0;
		});
	}
}
/*BlazeJS.Modules.superFileChooser = new BlazeJS.Core.Class({
	options : {
		selector : "",
		name : "super"
	},
	
	extend : BlazeJS.Modules.fileChooser,
	
	initialize : function() {
		new this.super( this.options );
	}
});*/


//new BlazeJS.Modules.dataTable( { selector : "table.data-table" } );







