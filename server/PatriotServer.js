var restify     =   require('restify');
var mongojs     =   require('mongojs');
var morgan      =   require('morgan');
var db          =   mongojs('patriot',['Areas']);
restify.CORS.ALLOW_HEADERS.push('dataType');
var server      =   restify.createServer();
var request = require('request');

server.use(restify.acceptParser(server.acceptable));
server.use(restify.queryParser());
server.use(restify.bodyParser());
server.use(morgan('dev')); // LOGGER
var count = 20;
// CORS  
server.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', "*");
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Content-Type');
    next();
}); 

var http = require('http');
//
server.listen(process.env.PORT || 3000, function () {
 //   console.log("Server started @ ",process.env.PORT || 3000);
});

 server.get("/getAreas", function (req, res, next) {
     
   var func = function(error,results){
    areas = results.sort(function(a,b){
                        
                        if(a.area > b.area)
                            return 1;
                        if(b.area > a.area)
                            return -1;
                        return 0;
    });
       
    var final = [];
       
    areas.forEach(function(area){
        
        final.push(area.area);
        
    });
       
    res.writeHead(200, {
        'Content-Type': 'application/json; charset=utf-8'});
        
    res.end(JSON.stringify(final));

   };

    db.Areas.find({},func);

    return next(); 
     
 });


 server.post("/setUserAreas", function (req, res, next) {

        return next(); 
     
 });

 server.post("/setUserLocation", function (req, res, next) {

        return next(); 
     
 });

 server.post("/registerUser", function (req, res, next) {

     
     res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8'});
     res.end(JSON.stringify());
     return next(); 
     
 });



 
 console.log("wtf ",process.env.PORT || 3000);



