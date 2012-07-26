/**
 * BlazeFaces Effect Widget
 */
BlazeFaces.widget.Effect = BlazeFaces.widget.BaseWidget.extend({
    
    init: function(cfg) {
        this.cfg = cfg;
        this.id = this.cfg.id;
        this.jqId = BlazeFaces.escapeClientId(this.id);
        this.source = $(BlazeFaces.escapeClientId(this.cfg.source));
        var _self = this;

        this.runner = function() {
            //avoid queuing multiple runs
            if(_self.timeoutId) {
                clearTimeout(_self.timeoutId);
            }

            _self.timeoutId = setTimeout(_self.cfg.fn, _self.cfg.delay);
        };

        if(this.cfg.event == 'load') {
            this.runner.call();
        } 
        else {
            this.source.bind(this.cfg.event, this.runner);
        }
        
        $(this.jqId + '_s').remove();
    }
    
});