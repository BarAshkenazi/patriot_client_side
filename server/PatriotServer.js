var restify     =   require('restify');
var mongojs     =   require('mongojs');

restify.CORS.ALLOW_HEADERS.push('dataType');
var server      =   restify.createServer();
var request = require('request');


var dbUrl = "mongodb://Admin:Cc12341234@ds047040.mongolab.com:47040/mobileoref";
var db    =  mongojs(dbUrl,['Areas','Users','AreaPolygon']);

var areas = {};
areas.areas = [];

var sortedAreas = [];

   var func = function(error,results){
    sortedAreas = results.sort(function(a,b){
                        
                        if(a.area > b.area)
                            return 1;
                        if(b.area > a.area)
                            return -1;
                        return 0;
    });
       
       
    sortedAreas.forEach(function(area){
        
        areas.areas.push(area.area);
        
    });
   };

    db.AreaPolygon.find({},func);



server.use(restify.acceptParser(server.acceptable));
server.use(restify.queryParser());
server.use(restify.bodyParser());

var count = 20;
// CORS  
server.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', "*");
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Content-Type');
    next();
}); 

var http = require('http');

db.Users.ensureIndex({
    regid: 1
}, {
    unique: true
})

server.listen(process.env.PORT || 80, function () {
 //   console.log("Server started @ ",process.env.PORT || 3000);
});

 server.get("/getAreas", function (req, res, next) {
    res.writeHead(200, {
        'Content-Type': 'application/json; charset=utf-8'});
        
    //res.end(JSON.stringify(results));
    res.end(JSON.stringify(areas));
      return next(); 
 });


 server.post("/setUserAreas", function (req, res, next) {

        return next(); 
     
 });

 server.post("/setUserLocation", function (req, res, next) {

        return next(); 
     
 });

 server.post("/registerUser", function (req, res, next) {
     
     var user = {};
     user.regid = req.params.regid;
     db.Users.insert(user,function(err,dbUser){
         

        if (err && err.code !== 11000)// code 11000 = duplicate regid -> its fine by me  { 
           {
                res.writeHead(400, {
                    'Content-Type': 'application/json; charset=utf-8'
                });
                res.end(JSON.stringify({
                    error: err,
                    message: "A problem occured registering the user for the service"
                }));
            }
         else {
            res.writeHead(200, {
                'Content-Type': 'application/json; charset=utf-8'
            });
            res.end();
        }   
         
     });
     

     return next(); 
     
 });


 server.get("/send",function(req,res,next){
     
        var gcm     = require('C:\\Users\\Bar2.Bar\\AppData\\Roaming\\npm\\node_modules\\node-gcm\\index.js');
        var message = new gcm.Message();

        //API Server Key
        var sender = new gcm.Sender('AIzaSyCUN2RNwoFvL6BWm0F11uejIdM4yFHfSEQ');
        var registrationIds = [];    

        // Value the payload data to send...
        message.addData('message',"hello push");
        message.addData('title','Push Notification Sample' );
        message.addData('msgcnt','3'); // Shows up in the notification in the status bar

        count++;
        message.addData('notId',count);

        message.timeToLive = 3000;// Duration in seconds to hold in GCM and retry before timing out.

        db.Users.find(function(error,users){

             users.forEach(function(user){

                // At least one reg id required
                registrationIds.push(user.regid);

             });

             //Parameters: message-literal, registrationIds-array, No. of retries, callback-function
             sender.send(message, registrationIds, 4, function (result) {
                 console.log(result);
             });

        });
     
     
           
           
   });


 
 console.log("wtf ",process.env.PORT || 3000);



