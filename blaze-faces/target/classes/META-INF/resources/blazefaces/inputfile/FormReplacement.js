(function() {
	var FormReplacement = function( selectorObj ) {
		this.buttonSelector = selectorObj.button;
		//this.selectSelector = selectorObj.select;
		//this.checkboxSelector = selectorObj.checkbox;
		//this.radiobuttonSelector = selectorObj.radio;
		
		this._init();
	}
	
	

	FormReplacement.prototype = {
		_init : function() {
			this._replaceButtons();
			//this._replaceSelects();
			//this._replaceCheckboxes();
			//this._replaceRadioButton();
		},
		
		_replaceButtons: function() {
			var bttns = $( this.buttonSelector ),
				tBttn = null,
				type  = "";
			
			bttns.each( function( i, n ) {
				type = $( n ).find( "input, a" )[ 0 ].nodeName;
				
				tBttn = new BlazeJS.UI.Button( $( n ), type === "a" ? "anchor" : "submit" );
			});
		},
		
		_replaceSelects : function() {
			var selects = $( this.selectSelector ),
				tselect = null,
				type  = "";
			
			selects.each( function( i, n ) {
				tselect = new BlazeJS.UI.Select( $( n ) );
			});
		},
		
		_replaceCheckboxes : function() {
			var checkboxes = $( this.checkboxSelector ),
				tCheckbox = null;
			
			checkboxes.each( function( i, n ) {
				tCheckbox = new BlazeJS.UI.Checkbox( $( n ) );
			});
		},
		
		_replaceRadioButton : function() {
			var radios = $( this.radiobuttonSelector ),
				tRadio = null;
			
			radios.each( function( i, n ) {
				tRadio = new BlazeJS.UI.Radiobutton( $( n ) );
			});
		}
	}

	BlazeJS.UI.FormReplacement = function( selectorObj ) {
		var instance = new FormReplacement( selectorObj );
	}
})();