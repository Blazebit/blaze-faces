(function() {
	var Button = function( selector, type, text, href ) {
		this.uiid       = BlazeJS.UI.generateUIID();
		this.clickable	= null;
		this.disabled = false;
		this.selector 	= selector;
		this.myself	  	= null;
		this.text 	 	= text;
		this.type 		= type;
		this.href       = href;
		
		this._init();
	}
	
	Button.markup = "<div class='blaze-ui-form blaze-ui-bttn noVisibility'><div class='blaze-ui-bg lft'></div><div class='blaze-ui-bg mdl'></div><div class='blaze-ui-bg rgt'></div></div>";
	

	Button.prototype = {
		_init : function() {
			var that = this;
			
			// if button already exists in DOM
			if ( this.selector ) {
				this.myself = $( this.selector );
				this.clickable = this.myself.find( "input, a" );
				
				if ( this.clickable.attr( "disabled" ) ) {
					this.disable();
				}
			}
			// button needs to be generated first
			else {
				this.myself = $( Button.markup );
				this.clickable = this._insertClickableElement( this.type, this.href );
				this.setText( this.text );
			}
			
			this.myself.mousedown( function() {
				that._handleMouseDown();
			})
			
			// add uiid
			this.myself.attr( "data-uiid", this.uiid );
			
			this._setCalculatedWidth();
			
			BlazeJS.UI.add( this );
		},
		
		_handleMouseDown : function() {
			var elem = this.myself;
			
			if ( !this.disabled ) {
				elem.addClass( "active" );
				
				elem.bind( "mouseleave.active", function() {
					elem.removeClass( "active" );
				});
				
				elem.bind( "mouseenter.active", function() {
					if ( !elem.hasClass( "active" ) ) elem.addClass( "active" );
				});
				
			    $( document ).one('mouseup', function() {
			    	elem.removeClass( "active" );
			    	elem.unbind( "mouseenter.active" );
			        $().unbind();
			    });
			}
		},
		
		_setCalculatedWidth : function() {
			this.myself.width( this._calcWidth() );
		},
		
		_calcWidth : function() {
			var width = 0,
				clone = this.myself.clone(),
				icon  = null;
			
			$( "body" ).append( clone );
			
			width = clone.find( "input, a" ).width() + 28;
			
			// if icon is available, add also it's width 
			icon = clone.find( ".icon" );
			if ( icon.length != 0 ) {
				width += icon.width() + 11;
			}
			
			// remove temporary button again from DOM
			clone.remove();
			
			return width;
		},
		
		_insertClickableElement : function( type, href ) {
			var clickableElement = null;
			
			switch( type ) {
				case "anchor" : 
					clickableElement = $( "<a href='" + ( href ? href : "#" ) + "'>" );
					this.myself.find( ".mdl" ).append( clickableElement );
					break;
				case "submit" : 
					clickableElement = $( "<input type='submit'>" );
					this.myself.find( ".mdl" ).append( $( clickableElement ) );
					break;
				case "button" : 
					clickableElement = $( "<input type='button'>" );
					this.myself.find( ".mdl" ).append( $( clickableElement ) );
			}
			
			return clickableElement;
		},
		
		bind : function( type, fn ) {
			var that = this;
			
			this.clickable.bind( type, function( e ) {
				if ( !that.disabled ) {
					return fn.call( this, e );
				}
				else {
					return false;
				}
			});
		},
	
		disable : function() {
			this.myself.addClass( "disabled" );
			this.clickable.attr( "disabled", true );
					 
			this.disabled = true;
		},
		
		enable : function() {
			this.myself.removeClass( "disabled" );
			this.clickable.removeAttr( "disabled" );
			
			this.disabled = false;
		},
		
		self : function() {
			return this.myself.removeClass( "noVisibility" );
		},
		
		setText : function( value ) {
			if ( this.type === "anchor" ) {
				this.clickable.text( value );
			}
			else {
				this.clickable.val( value );
			}
			
			// recalculate button width depending on the new button text/label
			this._setCalculatedWidth();
		}
	}

	BlazeJS.UI.Button = function( selector, type, href ) {
		var bttn = null;
		
		if ( typeof selector === "object" ) {
			bttn = new Button( selector, type );
		}
		else {
			bttn = new Button( null, type, selector, href );
		}

		return {
			bind    : function( type, fn ) {
				bttn.bind( type, fn );
			},
			
			disable : function() {
				bttn.disable();
			},
			
			enable  : function() {
				bttn.enable();
			},
			
			isDisabled : function() {
				return bttn.disabled;
			},
			
			uiid	: bttn.uiid,
			
			html    : function() {
				return bttn.self();
			},
			
			text    : function( value ) {
				bttn.setText( value );
			}
		};
	}
})();