(function() {
    var Panel = function( selector ) {
        this.uiid           = BlazeJS.UI.generateUIID();
        this.$selector 	= $( selector );
        this.$toggleIcon	= this.$selector.find( ".panel-title .toggle" );
        this.isDataTable    = this.$selector.find( ".data_table" ).length > 0 ? true : false;
        
        if ( this.$selector.length > 0 ) {
            this._init();
        }
    }

    Panel.prototype = {
        _init : function() {
            // add toggle event
            if ( this.$toggleIcon.length > 0 ) {
                this.addToggleEvent();
            }
            if ( this.isDataTable ) {
                this._initDataTable();
            }
            
            // add uiid
            this.$selector.attr( "data-uiid", this.uiid );

            BlazeJS.UI.add( this );
        },
        
        _initDataTable : function() {
            this.$dataTable = this.$selector.find( ".data_table" );
            
            this._addTableRowClasses();
            this._addSelectAllRowsEvent();
        },
        
        _addTableRowClasses : function() {
            var $row = null;
            
            this.$dataTable.find( "tbody tr" ).each( function( i, n ) {
                $row = $( this );
                
                if ( ( ( i + 1 ) % 2 === 0 ) && !$row.hasClass( "even" ) ) {
                    $( this ).addClass( "even" );
                }
                else if ( !$row.hasClass( "odd" ) ){
                    $( this ).addClass( "odd" );
                }
            })
        },
        
        _addSelectAllRowsEvent : function() {
            var cbID = this.$dataTable.find( ".select_all_cb .blaze-ui-cb" ).attr( "data-uiid"),
                cb   = BlazeJS.UI.get( cbID ),
                that = this;
            
            if ( cb ) {
                cb.onChange( function( value ) {
                    that._handleTableRowSelection( value );
                })
            }
        },
        
        selectAllTableRows : function() {
            this._handleTableRowSelection( true );
        },
        
        deselectAllTableRows : function() {
            this._handleTableRowSelection( false );
        },
        
        _handleTableRowSelection : function( value ) {
            var cb = this.$dataTable.find( ".select_cb .blaze-ui-cb" );
						
            cb.each( function( i, n ) {
                BlazeJS.UI.get( n.getAttribute( "data-uiid" ) ).setValue( value );

                value ? $( this ).closest( "tr" ).addClass( "selected" ) : $( this ).closest( "tr" ).removeClass( "selected" );
            })
        },

        toggle : function() {
            if ( this.$toggleIcon.length > 0 ) {
                this.handleToggleEvent();
            }
        },

        addToggleEvent : function() {
            var that = this;

            this.$toggleIcon.click( function( e ) {
                that.handleToggleEvent();

                e.preventDefault();
            })
        },

        handleToggleEvent : function() {
            if ( this.$selector.hasClass( "closed" ) ) {
                this.open();
            }
            else {
                this.close();
            }
        },

        open : function() {
            this.$selector.removeClass( "closed" );

            this.$selector.find( ".panel-body" )
                          .slideDown( 300, function() {
                              $( this ).removeClass( "noDisplay" );
                          })
        },

        close : function() {
            var that = this;

            this.$selector.find( ".panel-body" )
                          .slideUp( 300, function() {
                              that.$selector.addClass( "closed" );
                          })
        }
    }

    BlazeJS.UI.Panel = function( selector ) {
        var panel = new Panel( selector );

        return {
            toggle : panel.toggle
        };
    }
    
    jQuery(document).ready(function() {
        new BlazeJS.UI.Panel( ".panel-wrapper" );
        
    })
})();