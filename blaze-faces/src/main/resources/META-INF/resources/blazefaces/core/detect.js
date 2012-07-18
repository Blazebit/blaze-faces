function featureJson (o){
    var parse = function(_o){
        var a = [], t;

        for(var p in _o){
            if(typeof _o[p] !== "function" && p[0] != "_" && _o.hasOwnProperty(p)){
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
}
        
var req = null;

try {
    req = new XMLHttpRequest();
} catch(e) {
    try{
        req = new ActiveXObject('Microsoft.XMLHTTP');
    }catch(e){
        req = new ActiveXObject('Msxml2.XMLHTTP')
    }
}

req.open('POST', document.URL, true);

if (typeof req.setRequestHeader !== 'undefined') {
    req.setRequestHeader('Cache-Control', 'no-cache');
    req.setRequestHeader('Faces-Request', 'partial/ajax');
    req.setRequestHeader('Content-type', 'application/x-www-form-urlencoded;charset=UTF-8');
}

        
        
req.send('javax.faces.partial.ajax=true&features=' + encodeURIComponent(featureJson(Modernizr)));