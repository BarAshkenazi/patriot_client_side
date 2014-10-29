

var http = require('https');
var mongojs     =   require('mongojs');
var db          =   mongojs('patriot',['Areas']);
//






//User = {
//    constructor: User,s
//    saveScore:function (theScoreToAdd)  {
//        this.quizScores.push(theScoreToAdd)
//    },
//    showNameAndScores:function ()  {
//        var scores = this.quizScores.length > 0 ? this.quizScores.join(",") : "No Scores Yet";
//        return this.name + " Scores: " + scores;
//    },
//    changeEmail:function (newEmail)  {
//        this.email = newEmail;
//        return "New Email Saved: " + this.email;
//    }
//}




    
    var areas = [];
    
    areas.push(      

    );


var getLocality = 
    function(addressComps){
        
        var address;
        addressComps.every(function(addressComp){
            
            var bFound = false;
            
                        addressComp.types.every(function(type){

                                        if( type == "locality" || type == 'sublocality') {
                                               bFound = true;
                                               return false;
                                        }
                                            return true;

                            });
            
                        if(bFound){
                            address = addressComp.long_name;
                            return false;
                        }
                        return true;
            
                    });
        
        return address;


   };

var rowsInserted = 0;
var googleApiKey = "AIzaSyBhWhP-wRFvopUevSx9PJWjwA_95iExbsA";
var addKey = '&key=';
    
    var createArea = function(area,rowNum){
        
        var finalResult = {};
        var headers = {};
        var options = {
          hostname: 'maps.googleapis.com',
          port: 443,
          method: 'GET',
          header : headers,
          path:'/maps/api/geocode/json?address='+area.area.replace(/ /g,'%20')+'&language=HE',
          rejectUnauthorized:false
        };


        var req = http.request(options, function(res) {
          res.setEncoding('utf8');
          res.fullBody = [];
          res.area = 
          res.on('data', function (chunk) {
              
              res.fullBody.push(chunk);

           // chunk = chunk.replace(/(\r\n|\n|\r|\ufeff)/gm,"");

          });

            res.on('end',function(){

                var parsed = JSON.parse(res.fullBody.join(""));
                var finalResult = area;
                var localities = [];
                var lat;
                var lng;
                
                 parsed.results.forEach(function(result){
                                        
                                        var locality = getLocality(result.address_components);
                
                                        if(locality && (localities.indexOf(locality)  == -1))
                                        {
                                         localities.push(locality); 
                                            if (localities.length == 1)
                                            {
                                             lat = result.geometry.location.lat;   
                                             lng = result.geometry.location.lng;   
                                            }
                                        }
                                        
                                    });
            
              finalResult.hits = localities.length;
              finalResult.results = parsed.results; 
            
              if(finalResult.hits != 0)
              {
                  finalResult.locality = localities[0];
                  finalResult.lat = lat;
                  finalResult.lng = lng;
                   
                  
              }
                
                

                db.Areas.update({_id:area._id},finalResult,{},function(error,row){
                                            
                              
                                if (error)
                                {
                                 console.log(error);   
                                }
                                else
                                {
                                    rowNum++;
                                    console.log(++rowsInserted + ' rows inserted (' + (rowNum - 1) + ')');
                                    if(rowNum != areas.length)
                                    {
                                //        
                                       createArea(areas[rowNum],rowNum);
                                    }
                                    else
                                    {
                                        console.log("finished");
                                    }

                                }
                    
                           }
                        );

                

            });

        }); 
       req.on('error', function(e) {
          console.log('problem with request: ' + e.message);
        });

       req.end();

        
    };


var func = function(error,results){
    areas = results.sort(function(a,b){
                        
                        if(a.area > b.area)
                            return 1;
                        if(b.area > a.area)
                            return -1;
                        return 0;
    });
    
    createArea(areas[0],0);
     

    
    
    
};

db.Areas.find({},func);





     
var start_from = 0;

for( i = 0; i<areas.length; i++)
{
//createArea(areas[i],i);
}






