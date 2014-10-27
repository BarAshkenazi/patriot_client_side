var restify     =   require('restify');
var mongojs     =   require('mongojs');
var morgan      =   require('morgan');
var db          =   mongojs('bucketlistapp', ['appUsers','bucketLists' ,'Users']);
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

 server.get("/pull", function (req, res, next) {
     
//     $.ajax({
//       type: "GET",
//       url: "http://www.oref.org.il/WarningMessages/alerts.json",
//       contentType: "application/json",
//       async: true,
//       dataType: "json",
//       success: function (o0,o1,o2,o3) {
//             console.log("good");
//           },
//       error: function (o0,o1,o2,o3) {
//            console.log("bad");
//           },
//   }); 

     
var headers = {};
     
    
var options = {
  hostname: 'www.oref.org.il',
  port: 80,
  method: 'GET',
  header : headers,
  path:'/WarningMessages/alerts.json'
};

var req = http.request(options, function(res) {
  res.setEncoding('utf16le');
  res.on('data', function (chunk) {

    chunk = chunk.replace(/(\r\n|\n|\r|\ufeff)/gm,"");
    var parsed = JSON.parse(chunk);
  });
});

req.on('error', function(e) {
  console.log('problem with request: ' + e.message);
});

req.end();
     
//   request('http://www.oref.org.il/WarningMessages/alerts.json', function (error, response, body) {
//  if (!error && response.statusCode == 200) {
//    console.log(body) // Print the google web page.
//    
//        res.writeHead(200, {
//        'Content-Type': 'application/json; charset=utf-8'
//    });
//    var toCharset = 'UTF-16LE';
//    var fromCharset = 'UTF-8';
//      while(true){
//    var converted = encoding.convert(body, toCharset, fromCharset);
//      }
//          
//    
//  }
//})
   

     
     
     return next();
     
     
     
 });
 console.log("wtf ",process.env.PORT || 3000);



