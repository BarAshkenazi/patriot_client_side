

var http = require('https');
var mongojs     =   require('mongojs');
var db          =   mongojs('patriot',['Areas','AreaPolygon']);



var func = function(error,results){
    
    var final = [];
    results.forEach(function(result){
        
        var reduced = {};
        reduced.polygon = result.polygon;
        reduced.area = result.area;
        reduced.time = result.time;
        
        reduced.locality = "";
        if(result.locality){
        reduced.locality = result.locality;
         }
        
        final.push(reduced);
        
       
        
    });
    
    
 db.AreaPolygon.insert(final);
    
};

db.Areas.find({},func);




