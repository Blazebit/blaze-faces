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
	},
        
        toJson : function (o){
            var parse = function(_o){
                var a = [], t;

                for(var p in _o){
                    if(_o.hasOwnProperty(p)){
                        t = _o[p];
                        if(t && typeof t == "object"){
                            a[a.length]= p + ":{ " + arguments.callee(t).join(", ") + "}";
                        }
                        else {
                            if(typeof t == "string"){
                                a[a.length] = [ p+ ": \"" + t.toString() + "\"" ];
                            }
                            else{
                                a[a.length] = [ p+ ": " + t.toString()];
                            }
                        }
                    }
                }

                return a;
            }

            return "{" + parse(o).join(", ") + "}";
        },
        
        getBooleanProperties : function(a){
            var props = {};

            for ( var o in a ) {
                if ( typeof a[ o ] === "boolean" ) {
                    props[ o ] = a[ o ]
                }
            }
            
            return props;
        },

        escapeClientId : function( id ) {
            return "#" + id.replace(/:/g,"\\:");
        },

        addSubmitParam : function( parent, name, value ) {
            $( this.escapeClientId( parent ) ).append( "<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>" );

            return this;
        },

        submit : function( formId ) {
            $( this.escapeClientId( formId ) ).submit();
        },

        reset : function( formId ) {
            $( this.escapeClientId( formId ) ).reset();
        },

        getCookie : function( name ) {
            return $.cookie( name );
        },

        setCookie : function( name, value ) {
            $.cookie( name, value );
        },
        
        PARTIAL_REQUEST_PARAM : "javax.faces.partial.ajax",

        PARTIAL_UPDATE_PARAM : "javax.faces.partial.render",

        PARTIAL_PROCESS_PARAM : "javax.faces.partial.execute",

        PARTIAL_SOURCE_PARAM : "javax.faces.source",

        BEHAVIOR_EVENT_PARAM : "javax.faces.behavior.event",

        PARTIAL_EVENT_PARAM : "javax.faces.partial.event",

        VIEW_STATE : "javax.faces.ViewState"
}

BlazeJS.EventHandler = {
    add : function(selector, event, fn){
        $(selector).bind(event, fn);
    },
    
    create : function(type, actions){
        return function(){
            var retVal = null;
            var tempVal = null;
            // @todo: Handle type == 'parallel'
            for(var i = 0; i < actions.length; i++){
                tempVal = actions[i]();
                
                if(tempVal != null)
                    retVal = tempVal;
            }
        
            return retVal;
        }
    }
}

BlazeJS.Modules = {}



/* General Utilities */

/**
 * jQuery Cookie plugin
 *
 * Copyright (c) 2010 Klaus Hartl (stilbuero.de)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */
jQuery.cookie = function (key, value, options) {

    // key and value given, set cookie...
    if (arguments.length > 1 && (value === null || typeof value !== "object")) {
        options = jQuery.extend({}, options);

        if (value === null) {
            options.expires = -1;
        }

        if (typeof options.expires === 'number') {
            var days = options.expires, t = options.expires = new Date();
            t.setDate(t.getDate() + days);
        }

        return (document.cookie = [
            encodeURIComponent(key), '=',
            options.raw ? String(value) : encodeURIComponent(String(value)),
            options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
            options.path ? '; path=' + options.path : '',
            options.domain ? '; domain=' + options.domain : '',
            options.secure ? '; secure' : ''
        ].join(''));
    }

    // key and possibly options given, get cookie...
    options = value || {};
    var result, decode = options.raw ? function (s) {return s;} : decodeURIComponent;
    return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null;
};

/**
 * Extension to remove item(s) from an array
 */
Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};
