/* USE 

var p = new BlazeJS.Modules.Progressbar( SELECTOR, DEFAULT_VALUE, IS_WAITING, EVENTS: complete, change );

To increase progress value:
p.value( VALUE );


Getting progressbar value:
p.value();

*/

(function() {
	var Progressbar = function( selector, value, isWaiting, events ) {
		this.complete = undefined;
		this.change   = undefined;
		this.events   = events;
		this.selector = selector;
		this.$elem    = $( selector );
		this.value    = value;
		this.waiting  = isWaiting || false;
		this.triggerComplete = true;
		
		// show waiting '.gif' placeholder
		this._setWaiting( isWaiting );
	}
	
	Progressbar.prototype = {
		_setValue : function( value ) {
			var that = this;
			
			function roundNumber(num, dec) {
				var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
				return result;
			}
			
			// interrupt progress bar from waiting state if '_setValue' isn't called from '_init()' method
			if ( this.waiting === true ) {
				this._setWaiting( false );
			}
			
			if ( value > 100 ) {
				this.value = 100;
			}
			else {
				this.value = roundNumber( value, 0 );
			}
			
			// trigger 'change' event
			if ( this.change ) {				
				this.change.call( null, { elem : this.$elem, value : parseInt( this.value ) } );
			}
				
			// update in DOM
			this.$elem.find( ".progress" ).stop().animate( { "width" : parseInt( this.value ) + "%", "overflow" : "visible" }, 400, function() {
				// trigger 'complete' event, if value reaches 100
				if ( that.triggerComplete && that.complete && that.value === 100 ) {
					that.complete.call( null, { elem : that.$elem, value : that.value } );
					
					// prevent calling complete callback function more than once
					that.triggerComplete = false;
				}
			});
		},
		
		_getValue : function() {
			return this.value;
		},
		
		_setWaiting : function( isWaiting ) {
			if ( isWaiting === true ) {
				this.waiting = true;
				
				// hide progress bar
				this.$elem.find( ".progress" ).hide();
				
				this.$elem.addClass( "waiting" );
			}
			else {
				this.waiting = false;
				
				// progress bar not in waiting state anymore, so try initializing
				this._init();
				
				this.$elem.find( ".progress" ).show();
				this.$elem.removeClass( "waiting" );
			}
		},
		
		_init : function() {
			// set default value if given by user
			if ( this.value != undefined ) {
				this._setValue( this.value );
			}
			
			// add custom callback methods
			if ( this.events ) {
				this.complete = this.events.complete || undefined;
				this.change   = this.events.change || undefined;
			}
		}
	}

	BlazeJS.Modules.Progressbar = function( selector ) {
		var value  = 0,
			isWaiting = false,
			events = {};
		
		if ( arguments.length > 1 ) {
			value = BlazeJS.Util.getValueFromParameter( "number", arguments );
			isWaiting = BlazeJS.Util.getValueFromParameter( "boolean", arguments );
			events = arguments[ arguments.length - 1 ];
		}
			
		var p = new Progressbar( selector, value, isWaiting, events );
		
		return {
			value : function() {
				if ( arguments.length === 1 ) {
					p._setValue( arguments[ 0 ] );
				}
				else {
					return p._getValue();
				}
			},
			
			wait : function( b ) {
				p._setWaiting( b );
			}
		}
	};
})();