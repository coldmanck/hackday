<?php
include 'class/db.class.php';
$db = new stdb();

?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>find me</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/jumbotron.css" rel="stylesheet">

    <script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="https://www.google.com/jsapi"></script>
    <script src="js/bootstrap.min.js"></script>

    <script src="fancybox/lib/jquery.mousewheel-3.0.6.pack.js"></script>
    <script src="fancybox/source/jquery.fancybox.js?v=2.1.5"></script>
    <link rel="stylesheet" href="fancybox/source/jquery.fancybox.css?v=2.1.5" media="screen" />
    <link rel="stylesheet" href="fancybox/source/helpers/jquery.fancybox-buttons.css?v=1.0.5" />
    <script src="fancybox/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>
    <link rel="stylesheet" href="fancybox/source/helpers/jquery.fancybox-thumbs.css?v=1.0.7" />
    <script src="fancybox/source/helpers/jquery.fancybox-thumbs.js?v=1.0.7"></script>
    <script src="fancybox/source/helpers/jquery.fancybox-media.js?v=1.0.6"></script>
    <style> 
    #google-map{
      width: 450px;
      height: 400px;
    };
    </style>
  </head>
  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">findme</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <form class="navbar-form navbar-right">
            <div class="form-group">
              <input type="text" placeholder="Email" class="form-control">
            </div>
            <div class="form-group">
              <input type="password" placeholder="Password" class="form-control">
            </div>
            <button type="submit" class="btn btn-success">Sign in</button>
          </form>
        </div><!--/.navbar-collapse -->
      </div>
    </nav>
    
    
    <div id="myCarousel" class="carousel slide" data-ride="carousel">
      <div class="carousel-inner" id="navibar_header_button">
        <div class="item active">
          <img src="pic/b.png" height="200" width="1250"/>
          <div class="container">
            <div class="carousel-caption">
              <h1>Diminish the tragedy.</h1>
              <p>To the families with their elders suffer Alzheirmer's disease, who worry about their lovely elders might get lost on their way home. The system helps us to keep the elders' track under our control. </p>
              <p><a class="btn btn-lg btn-primary" href="#about" id="effect_btn" role="button" >find me!</a></p>
            </div>
          </div>
        </div>
      </div>
    </div>


    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-md-3">
          <h2>Date</h2>
          <div id="location-logs"></div>
        </div>
        <div class="col-md-3" id="photos">
          <h2>Photos</h2>
        </div>
        <div class="col-md-6">
          <h2>Map</h2>
          <div id="google-map"></div>
       </div>
      </div>
    </div>
    
  </body>
</html>
<script>
$(document).ready(function(){

// fancybox
$(".fancybox").fancybox({
  openEffect  : 'none',
  closeEffect : 'none'
});

var google_map = 0;
if(google_map == 0){
  google_map = new google.maps.Map(document.getElementById('google-map'), {
      zoom: 16,
      center: new google.maps.LatLng(35.6654816, 139.7307837),
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      panControl: false,
      streetViewControl: false,
      mapTypeControl: false
    });
}

var date_to_string = function(date){
  return '' + date.getFullYear() +
         (date.getMonth() > 9 ? '/' : '/0') + date.getMonth() +
         (date.getDate() > 9 ? '/' : '/0') + date.getDate() +
         (date.getHours() > 9 ? ' ' : ' 0') + date.getHours() +
         (date.getMinutes() > 9 ? ':' : ':0') + date.getMinutes() +
         (date.getSeconds() > 9 ? ':' : ':0') + date.getSeconds();
}

var add_location_log = function(lat, lng, date, i){
  var div = $('<div style="border: 1px solid black;border-radius: 4px; padding: 1px 5px 1px 5px; text-align: center; margin-top: 5px; cursor: pointer; font-size: 120%;">' + date_to_string(date) + '</div>');
  var marker = new google.maps.Marker({
    map: null,
    position: new google.maps.LatLng(lat, lng)
  });
  marker.setMap(google_map);
  marker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
  div.hover(function(){
    marker.setIcon('http://maps.google.com/mapfiles/ms/icons/red-dot.png');
    $('#pic_'+i).css('outline', '3px red solid');
  }, function(){
    marker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
    $('#pic_'+i).css('outline', '0px red solid');
  });
  google.maps.event.addListener(marker, 'mouseover', function(){
    //marker.infowindow = new google.maps.InfoWindow({
    //  content: '<div>' + date.toString() + ' at ' + '</div>'
    //});
  });
  google.maps.event.addListener(marker, 'mouseout', function(){
    console.log(123);
  })
  $('#location-logs').append(div);
};

<?php
$s = $db -> query('select * from notify;');
$n = 1;
foreach($s as $i){
  echo 'add_location_log(1, 2, new Date("'.$i['datetime'].'"), '.$n.');'."\n";
  $n++;
}
?>

var add_photo = function(url, id){
  var a = $('<a class="fancybox" rel="gallery1" href="'+url+'"><img style="width: 100%; margin-top: 5px;" src="'+url+'" id="pic_'+id+'"></a>');
  $('#photos').append(a);
}

<?php
$s = $db -> query('select * from photo;');
$n = 1;
foreach($s as $i){
  $tmpfname = '/var/www/html/tmp/a'.$n.'.jpg';
  $handle = fopen($tmpfname, "w");
  fwrite($handle, $i['data']);
  fclose($handle);
  echo "add_photo('".'tmp/a'.$n.'.jpg'."', ".$n.");\n";
  $n++;
}

?>

var new_effect = function(){

$('#location-logs >div').remove();
add_location_log(35.6655293, 139.7300153, new Date("2015-03-09 11:19:38"), 1);
add_location_log(35.6653293, 139.7320153, new Date("2015-03-08 13:12:27"), 2);
add_location_log(35.6654293, 139.7330153, new Date("2015-03-08 12:09:36"), 3);
add_location_log(35.6641293, 139.7319153, new Date("2015-03-08 15:05:11"), 4);
add_location_log(35.6642293, 139.7325153, new Date("2015-03-09 08:23:08"), 5);
add_location_log(35.6645293, 139.7332153, new Date("2015-03-09 11:09:38"), 6);
add_location_log(35.6639293, 139.7332953, new Date("2015-03-09 11:12:38"), 7);

// $('').

};
$('#effect_btn').click(new_effect);

});
</script>


