(function() {
    if ( !BlazeJS.Ajax ) {
        BlazeJS.Ajax = {};
    }
    
    BlazeJS.Ajax = {
        encodeViewState : function() {
            var viewstateValue = document.getElementById( BlazeJS.Util.VIEW_STATE ).value;
            var re = new RegExp( "\\+", "g" );
            var encodedViewState = viewstateValue.replace( re, "\%2B" );
		
            return encodedViewState;
        },
	
        updateState: function( value ) {
            var viewstateValue = $.trim( value );
        
            $( "form" ).each( function() {
                var form = $( this ),
                formViewStateElement = form.children( "input[name='javax.faces.ViewState']" ).get( 0 );

                if( formViewStateElement ) {
                    $( formViewStateElement ).val( viewstateValue );
                }
                else
                {
                    form.append( '<input type="hidden" name="javax.faces.ViewState" id="javax.faces.ViewState" value="' + viewstateValue + '" autocomplete="off" />' );
                }
            });
        },
        
        serialize: function( params ) {
            var serializedParams = "";

            for( var param in params ) {
                serializedParams = serializedParams + "&" + param + "=" + params[ param ];
            }

            return serializedParams;
        },
        
        updateElement: function( id, content ) {
            if( id === BlazeJS.Util.VIEW_STATE ) {
                BlazeJS.Ajax.updateState(content);
            }
            else {
                $( BlazeJS.Util.escapeClientId( id ) ).replaceWith( content );

                //Blazefaces Mobile
                if( $.mobile ) {
                    var controls = $( BlazeJS.Util.escapeClientId(id) ).parent().find( "input, textarea, select, button, ul" );

                    //input and textarea
                    controls
                    .filter("input, textarea")
                    .not("[type='radio'], [type='checkbox'], [type='button'], [type='submit'], [type='reset'], [type='image'], [type='hidden']")
                    .textinput();
                    
                    //lists
                    controls.filter("[data-role='listview']").listview();
                
                    //buttons
                    controls.filter("button, [type='button'], [type='submit'], [type='reset'], [type='image']" ).button();

                    //slider
                    controls.filter("input, select")
                    .filter("[data-role='slider'], [data-type='range']")
                    .slider();
                
                    //selects
                    controls.filter("select:not([data-role='slider'])" ).selectmenu();
                }
            }
        },

        /**
        *  Handles response handling tasks after updating the dom
        **/
        handleResponse: function( xmlDoc ) {
            var redirect = xmlDoc.find( "redirect" ),
                extensions = xmlDoc.find( "extension[ln='blazefaces'][type='args']"),
                scripts = xmlDoc.find( "eval" );

            if( redirect.length > 0 ) {
                window.location = redirect.attr( "url" );
            }
            else {
                //callback arguments
                this.args = extensions.length > 0 ? $.parseJSON( extensions.text() ) : {};

                //scripts to execute
                for( var i=0, length = scripts.length; i < length; i++ ) {
                    $.globalEval( scripts.eq( i ).text() );
                }
            }
        },
    
        send : function( cfg, param ) {
            var form = null,
                encodedURLField = null,
                postURL = null,
                postParams = null,
                process = [],
                update = [];
            
            if( cfg.onstart ) {
                var retVal = cfg.onstart.call( this );
                
                if( retVal == false ) {
                    return;  //cancel request
                }
            }
            
            if( cfg.formId ) {
                form = $( BlazeJS.Util.escapeClientId( cfg.formId ) );                    //Explicit form is defined
            }
            else {
                form = $( BlazeJS.Util.escapeClientId( cfg.source ) ).parents( "form:first" );     //look for a parent of source

                //source has no parent form so use first form in document
                if( form.length === 0 ) {
                    form = $( "form" ).eq( 0 );
                }
            }
    
            postURL = form.attr( "action" );
            postParams = form.serialize();
            encodedURLField = form.children( "input[name='javax.faces.encodedURL']" );

            //portlet support
            if( encodedURLField.length > 0 ) {
                postURL = encodedURLField.val();
            }
    
            //partial ajax
            postParams = postParams + "&" + BlazeJS.Util.PARTIAL_REQUEST_PARAM + "=true";

            //source
            if( typeof cfg.source === "string" ) {
                postParams = postParams + "&" + BlazeJS.Util.PARTIAL_SOURCE_PARAM + "=" + cfg.source;
            }
            else {
                postParams = postParams + "&" + BlazeJS.Util.PARTIAL_SOURCE_PARAM + "=" + cfg.source.id;
            }
            
            //process
            if( cfg.process ) {
                process.push( cfg.process );
            }
            if( param && param.process ) {
                process.push( param.process );
            }
            if( process.length > 0 ) {
                postParams = postParams + "&" + BlazeJS.Util.PARTIAL_PROCESS_PARAM + "=" + process.join( " " );
            }
            
            //update
            if( cfg.update ) {
                update.push( cfg.update );
            }
            if( param && param.update ) {
                update.push( param.update );
            }
            if( update.length > 0 ) {
                postParams = postParams + "&" + BlazeJS.Util.PARTIAL_UPDATE_PARAM + "=" + update.join( " " );
            }
            
            //behavior event
            if( cfg.event ) {
                postParams = postParams + "&" + BlazeJS.Util.BEHAVIOR_EVENT_PARAM + "=" + cfg.event;
                var domEvent = cfg.event;

                if( cfg.event == "valueChange" ) {
                    domEvent = "change";
                }
                else if(cfg.event == "action") {
                    domEvent = "click";
                }

                postParams = postParams + "&" + BlazeJS.Util.PARTIAL_EVENT_PARAM + "=" + domEvent;
            } else {
                postParams = postParams + "&" + cfg.source + "=" + cfg.source;
            }
    
            //params
            if( cfg.params ) {
                postParams = postParams + BlazeJS.Ajax.serialize( cfg.params );
            }
            if( param && param.params ) {
                postParams = postParams + BlazeJS.Ajax.serialize( param.params );
            }

            var xhrOptions = {
                url : postURL,
                type : "POST",
                cache : false,
                dataType : "xml",
                data : postParams,
                beforeSend: function( xhr ) {
                    xhr.setRequestHeader('Faces-Request', 'partial/ajax');
                },
                error: function( xhr, status, errorThrown ) {
                    if( cfg.onerror ) {
                        cfg.onerror.call( xhr, status, errorThrown );
                    }
                },
                success : function(data, status, xhr) {
                    var parsed;

                    //call user callback
                    if( cfg.onsuccess ) {
                        parsed = cfg.onsuccess.call( this, data, status, xhr );
                    }

                    //extension callback that might parse response
                    if(param && param.onsuccess && !parsed) {
                        parsed = param.onsuccess.call(this, data, status, xhr); 
                    }

                    //do not execute default handler as response already has been parsed
                    if( parsed ) {
                        return;
                    } 
                    else {
                        BlazeJS.Ajax.response.call( this, data, status, xhr );
                    }
                },
                complete : function( xhr, status ) {
                    if( cfg.oncomplete ) {
                        cfg.oncomplete.call( this, xhr, status, this.args );
                    }
          
                    BlazeJS.Ajax.requestManager.poll();
                }
            };
	
            xhrOptions.global = cfg.global == true || cfg.global == undefined ? true : false;

            if( cfg.async ) {
                $.ajax( xhrOptions );
            } else {
                BlazeJS.Ajax.requestManager.offer( xhrOptions );
            }
        },
        
        response : function( response ) {
            var xmlDoc  = $( response.documentElement ),
                updates = xmlDoc.find( "update" ),
                update  = null,
                id      = null,
                content = null;

            for( var i=0; i < updates.length; i++ ) {
                update = updates.eq(i);
                id = update.attr( "id" );
                content = update.text();

                BlazeJS.Ajax.updateElement(id, content);
            }

            BlazeJS.Ajax.handleResponse.call(this, xmlDoc);
        },
        
        requestManager : {
            requests : new Array(),

            offer : function( req ) {
                this.requests.push( req );

                if( this.requests.length == 1 ) {
                    var retVal = $.ajax( req );
                    
                    if( retVal === false ) {
                        this.poll();
                    }
                }
            },

            poll : function() {
                if( this.isEmpty() ) {
                    return null;
                }

                var processedRequest = this.requests.shift();
                var nextRequest = this.peek();
                
                if( nextRequest !== null ) {
                    $.ajax( nextRequest );
                }

                return processedRequest;
            },

            peek : function() {
                if( this.isEmpty() ) {
                    return null;
                }

                var nextRequest = this.requests[ 0 ];

                return nextRequest;
            },

            isEmpty : function() {
                return this.requests.length === 0;
            }
        }
    }
})()