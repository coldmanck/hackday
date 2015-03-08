<!DOCTYPE html>
<?php

$file = fopen('data/restaurants', 'r');
$ary = array();
$restaurant_ary = array();
while(!feof($file)){array_push($ary, trim(fgets($file)));}
fclose($file);
foreach($ary as $val){
array_push($restaurant_ary, explode("\t", $val));
}
# print_r($restaurant_ary); ###
$restaurant_counties = array_unique(
  array_map(function($s){
    return substr($s[0], 0, 6);
  }, $restaurant_ary)
);
# print_r($restaurant_counties); ###
$restaurant_types = array_unique(
  array_map(function($a){
    return $a[3];
  }, $restaurant_ary)
);
# print_r($restaurant_types); ###

$file = fopen('data/county_latlng', 'r');
$ary = array();
$county_latlng = array();
while(!feof($file)){array_push($ary, trim(fgets($file)));}
fclose($file);
foreach($ary as $val){array_push($county_latlng, explode("\t", $val));}
# print_r($county_latlng); ###

$file = fopen('data/foods', 'r');
$ary = array();
while(!feof($file)){array_push($ary, trim(fgets($file)));}
# print_r($ary); ###
$healthy_foods_ruby_ary = array_map(
  function($s){
    $a = array();
    $t = explode("\t", $s);
    array_push($a, isset($t[2]) ? $t[2] : '');
    array_push($a, explode(",", isset($t[7]) ? $t[7] : ''));
    array_push($a, isset($t[8]) ? $t[8] : '');
    array_push($a, isset($t[4]) ? $t[4] : '');
    array_push($a, isset($t[9]) ? $t[9] : '');
    array_push($a, isset($t[10]) ? $t[10] : '');
    return $a;
  }, $ary
);
# print_r($healthy_foods_ruby_ary); ###
$ary = array();
foreach(array_map(function($a){return $a[1];}, $healthy_foods_ruby_ary) as $val){
  foreach($val as $v){
    array_push($ary, $v);
  }
}
# print_r($ary); ###
$healthy_foods_ruby_func_ary = array_unique($ary);
# print_r($healthy_foods_ruby_func_ary); ###

?>
<html>
  <head>
    <title>搜尋健康的關鍵字</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="image/favicon.ico">
    
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/carousel.css">
    <link rel="stylesheet" href="css/welcome.css">
    
    <script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="https://www.google.com/jsapi"></script>
    <script src="script/bootstrap.min.js"></script>
    <script src="script/docs.min.js"></script>
    <script src="script/ie-emulation-modes-warning.js"></script>
    <script src="script/ie10-viewport-bug-workaround.js"></script>
  </head>
  <body>
    <div id="trans_board" style="position: fixed; display: none;"></div>
    <div id="white_board_1" style="position: fixed; display: none;"></div>
    <div id="white_board_2" style="position: fixed; display: none;"></div>

    <div class="navbar-wrapper">
      <div class="container">
        <div class="navbar navbar-inverse navbar-static-top" role="navigation">
          <div class="container">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="#">吃出健康與快樂－搜尋健康的關鍵字</a>
            </div>
            <div class="navbar-collapse collapse">
              <ul class="nav navbar-nav" id="navbar_header_ui">
                <li class="active"><a href="#">首頁</a></li>
                <li><a href="#about">探索本服務</a></li>
                <li><a href="#restaurant">我愛優良餐廳</a></li>
                <li><a href="#food">我愛優良食品</a></li>
                <li><a href="#contact">關於 VirtualArts</a></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div id="myCarousel" class="carousel slide" data-ride="carousel">
      <ol class="carousel-indicators">
        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
        <li data-target="#myCarousel" data-slide-to="1"></li>
        <li data-target="#myCarousel" data-slide-to="2"></li>
      </ol>
      <div class="carousel-inner" id="navibar_header_button">
        <div class="item active">
          <img src="image/about1-crop.jpg"/>
          <div class="container">
            <div class="carousel-caption">
              <h1>還記得食物的原味嗎？</h1>
              <p>本平台源於衛生福利部食品藥物管理署之開放式資料庫，為追求健康的消費者迅速找到喜愛的餐廳及食物。</p>
              <p><a class="btn btn-lg btn-primary" href="#about" role="button" >馬上探索</a></p>
            </div>
          </div>
        </div>
        <div class="item">
          <img src="image/rest1-crop.jpg"/>
          <div class="container">
            <div class="carousel-caption">
              <h1>今天吃哪家？</h1>
              <p>由FDA慎重挑選全台上千家優良餐飲店家，保證要讓你吃得安心、吃出健康。</p>
              <p><a class="btn btn-lg btn-primary" href="#restaurant" role="button">我要吃出美味</a></p>
            </div>
          </div>
        </div>
        <div class="item">
          <img src="image/food1-crop.jpg"/>
          <div class="container">
            <div class="carousel-caption">
              <h1>你可以吃得更好。</h1>
              <p>琳瑯滿目的健康食品卻不知從何下手？FDA嚴選健康食品，等你來發掘！</p>
              <p><a class="btn btn-lg btn-primary" href="#food" role="button">我要吃出健康</a></p>
            </div>
          </div>
        </div>
      </div>
      <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
      <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
    </div>

    <div class="container marketing">
      <div class="row featurette" id="about">
        <div class="col-md-7">
          <h2 class="featurette-heading">探索本服務！<p><span class="text-muted">Explore!</span></p></h2><br>
          <p class="lead">生活在忙碌的現代，外食族已經成為一般人的生活型態。如何避開飲食三高－「高油、高鹽、高糖」，選擇健康的飲食是現代人的一大課題。<br><br>
          本平台源於衛生福利部食品藥物管理署之開放式資料庫，不僅善用現有的資料，更延伸其應用，致力於幫助追求健康的消費者迅速找到喜愛的餐廳及食物。</p>
          <p class="pull-right" style="align: 100% 0% 0% 100%"><a href="#" class="a_back_to_top">Back to top</a></p>
        </div>
        <div class="col-md-5">
          <img src="image/about2-crop.jpg" class="pic"/>
        </div>
      </div>
      <hr class="featurette-divider">
      <div class="row featurette" id="restaurant">
        <div class="col-md-5">
          <img src="image/rest2-crop.jpg" class="pic"/>
        </div>
        <div class="col-md-7">
          <h2 class="featurette-heading">我愛優良餐廳。 <p><span class="text-muted">Love for restaurant.</span></p></h2>
          <p class="lead">由FDA慎重挑選全台上千家優良餐飲店家，保證要讓你吃得安心、吃出健康。</p>
          <p><a class="btn btn-default" role="button" id="choose_restaurant_button">馬上尋找 &raquo;</a></p>
          <p class="pull-right" style="align: 100% 100% 0% 0%"><a href="#" class="a_back_to_top">Back to top</a></p>
          <div id="choose_restaurant" style="display: none;">
            <table>
              <tr>
                <td><div id="google_map_restaurant"></div></td>
                <td>
                  <div class="wb_panel_div">
                    <div class="whitebaord_div">
                      <span class="wb_panel_div_text">所在縣市</span>
                      <select id="select_county" class="select btn">
                        <option value="null">請選擇所在縣市</option>
                        <?php
                        foreach($restaurant_counties as $val){
                          echo '<option value="'.$val.'">'.$val.'</option>'."\n";}
                        ?>
                      </select>
                    </div>
                    <div class="whitebaord_div">
                      <span class="wb_panel_div_text">餐廳類型</span>
                      <select id="select_restaurant_type" class="select btn">
                        <option value="all">所有餐廳類型</option>
                        <?php
                        foreach($restaurant_types as $val){
                          echo '<option value="'.$val.'">'.$val.'</option>'."\n";}
                        ?>
                      </select>
                    </div>
                    <div class="whitebaord_div">
                      <form action="javascript:void(0);">
                        <span class="wb_panel_div_text">依關鍵字</span>
                        <input type="text" id="select_restaurant_name_text" class="text"/>
                        <!--
                        <input type="submit" class="btn" value="找餐廳" id="select_restaurant_name_btn"/>
                        -->
                      </form>
                    </div>
                  </div>
                  <div id="restaurant_info_div">
                    <div id="restaurant_info">
                      <div><span style="font-size: 24px;" id="restaurant_info_name">請點選餐廳看詳細資訊</span></div>
                      <div><span style="font-size: 20px;" id="restaurant_info_addr"></span></div>
                      <div style="font-size: 16px;" class="biao_kai">搜尋更多資訊：<a id="restaurant_info_a1" href="#" target="_blank"></a></div>
                      <div style="font-size: 16px;" class="biao_kai">尋找店家評價：<a id="restaurant_info_a2" href="#" target="_blank"></a></div>
                    </div>
                  </div>
                </td>
              </tr>
            </table>
          </div>
        </div>
      </div>
      <hr class="featurette-divider">
      <div class="row featurette" id="food">
        <div class="col-md-7">
          <h2 class="featurette-heading">我愛優良食品。<p><span class="text-muted">Love for food.</span></p></h2>
          <p class="lead">琳瑯滿目的健康食品卻不知從何下手？FDA嚴選健康食品，等你來發掘！</p>
          <p><a id="choose_food_button" class="btn btn-default" role="button">馬上尋找 &raquo;</a></p>
          <p class="pull-right" style="align: 100% 0% 0% 100%"><a href="#" class="a_back_to_top">Back to top</a></p>
          <div id="choose_food" style="display: none;">
            <table style="height: 100%;">
              <tr>
                <td style="padding: 10px 10px 10px 10px; width: 450px;" valign="top">
                  <div class="wb_panel_div">
                    <div class="whitebaord_div">
                      <span class="wb_panel_div_text">依保健功效</span>
                      <select id="select_food" class="select btn">
                        <option disable>請選擇</option>
                        <?php
                        foreach($healthy_foods_ruby_func_ary as $val){
                          echo '<option value="'.$val.'">'.$val.'</option>'."\n";}
                        ?>
                      </select>
                    </div>
                    <div class="whitebaord_div">
                      <form action="javascript:void(0);" style="padding: 10px 0 0 0">
                        <span class="wb_panel_div_text">依關鍵字</span>
                        <input type="text" id="select_food_text" class="text" placeholder="例：靈芝、綠茶..."/>
                        <!--
                        <input type="button" class="btn" value="找健康食品" id="select_food_btn"/>
                        -->
                      </form>
                    </div>
                  </div>
                  <div id="select_food_info_div"></div>
                </td>
                <td style="padding: 10px 10px 10px 10px" valign="top">
                  <div style="overflow-y: auto; height: 400px">
                    <table id="healty_food_table" border="1"></table>
                  </div>
                </td>
              </tr>
            </table>
          </div>
        </div>
        <div class="col-md-5">
          <img src="image/food2-crop.jpg" class="pic"/>
        </div>
      </div>
      <hr class="featurette-divider"/>
      <div class="row featurette" id="contact">
        <div class="col-md-12">
          <h2 class="featurette-heading">關於 VirtualArts 團隊<p><span class="text-muted">About Team VirtualArts</span></p></h2>
          <p class="lead">本團隊由國立清華大學與國立交通大學的學生所組成，藉由發掘<a href="http://www.fda.gov.tw/">衛生福利部食品藥物管理署（FDA）</a>
          所提供的<a href="http://data.fda.gov.tw/">開放式資料（Open data）</a>，分析人們所需要的餐廳與飲食資訊，幫助人們在尋求美味之餘，也能吃的更健康。
          若有任何建議與指教，非常歡迎<a href="mailto:coldmanck@gmail.com">聯絡我們</a>。</p>
        </div>
      </div>
      <hr class="featurette-divider"/>
      <footer>
        <p class="pull-right"><a href="#" class="a_back_to_top">Back to top</a></p>
        <p>&copy; 2014 Team VirtualArts.</p>
      </footer>
    </div>
  </body>
</html>


<script>
$(function(){

$('#navbar_header_ui li a, #navibar_header_button a')
  .css('color', 'rgb(200, 200, 200)')
  .click(function(){
    $('html, body').animate({scrollTop: $($(this).attr('href')).offset().top}, 'slow');
    return false;
  });
$('.a_back_to_top').click(function(){
  $('html, body').animate({scrollTop: 0}, 1000);
  return false;
});

$('#trans_board').css('display', 'none');
$('#white_board_1, #white_board_2').css('display', 'none');

$('#trans_board').click(function(){
  $('#white_board_1, #white_board_2, #choose_restaurant, #choose_food').css('display', 'none');
  $(this).css('display', 'none');
}).scroll(function(){return false;});

$('#choose_restaurant_button').click(function(){
  $(this).blur();
  $('#trans_board').css('display', 'block');
  $('#white_board_1')
    .css('display', 'block')
    .animate({opacity: 1}, 700)
    .html($('#choose_restaurant').css('display', 'block'));
  init_google_map_restaurant();
  return false;
});
$('#choose_food_button').click(function(){
  $(this).blur();
  $('#trans_board').css('display', 'block');
  $('#white_board_2')
    .css('display', 'block')
    .animate({opacity: 1}, 700)
    .html($('#choose_food').css('display', 'block'));
  $('#select_food').change(function(){
    var func = $(this).val();
    var ary = [];
    $.each(healthy_food_ary, function(i, val){
      if(val.func.indexOf(func) > -1){
        ary.push(val);
      }
    });
    edit_food_table(ary);
  });
  $('#select_food_text').change(function(){
    var text = $('#select_food_text').val();
    if(text != ''){
      var ary = [];
      $.each(healthy_food_ary, function(i, val){
        if(val.name.indexOf(text) > -1){
          ary.push(val);
        }
      });
      edit_food_table(ary);
    }
  });
  return false;
});


var healthy_food_ary = [];
function add_healthy_food(name, func, func_long, company, warning, notice){
  healthy_food_ary.push({
    name: name,
    func: func,
    func_long: func_long,
    company: company,
    warning: warning,
    notice: notice
  });
}
<?php
foreach($healthy_foods_ruby_ary as $val){
  $func = '[';
  foreach($val[1] as $v){
    $func = $func."'$v', ";
  }
  $func = $func.']';
  echo "add_healthy_food('${val[0]}', $func, '${val[2]}', '${val[3]}', '${val[4]}', '${val[5]}');\n";
}

?>
function edit_food_table(ary){
  $('#healty_food_table').html($('<tr></tr>').append('<td class="choose_food_th_class">名稱</td><td class="choose_food_th_class">公司</td>'));
  $.each(ary, function(i, val){
    $('#healty_food_table').append(
      $('<tr></tr>').append(
        $('<td class="right_table_food_td_style"></td>').append(
          $('<a style="cursor: pointer;"></a>').html(val.name).click(function(){
            // $.getJSON(encodeURI('https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q='+val.name),
            //  function(data){
            //     console.log(data.responseData.results[0].url);
            //   });
            $('#select_food_info_div')
              .html($('<div style="align: center; font-size: 20px;"></div>').html(val.name))
              .append($('<div style="align: center; font-size: 16px;" class="biao_kai">功效：</div>').append(val.func=='' ? '無' : val.func))
              .append($('<div style="align: center; font-size: 16px;" class="biao_kai">注意事項：</div>').append(val.notice=='' ? '無' : val.notice))
              .append($('<div style="align: center; font-size: 16px;" class="biao_kai">搜尋更多商品資訊：</div>').append('<a target="_blank" href="http://ecshweb.pchome.com.tw/search/v3.2/?q='+encodeURI(val.name)+'"><img class="brand_icon" src="image/pchome.jpg"/></a>'));
            return false;
          })
        )
      ).append(
        $('<td class="right_table_food_td_style"></td>').append(val.company)
      )
    );
  });
}

var googleMapRestaurant = 0;
var googleMapMarkers = [];
function init_google_map_restaurant(){
  if(googleMapRestaurant == 0){
    googleMapRestaurant = new google.maps.Map(document.getElementById('google_map_restaurant'), {
      zoom: 11,
      center: new google.maps.LatLng(25.046337, 121.517444),
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      panControl: false,
      streetViewControl: false,
      mapTypeControl: false});
    <?php
      foreach($restaurant_ary as $val){
        echo "addMarkerToGoogleMap(\"${val[0]}\", \"${val[1]}\", \"${val[2]}\", \"${val[3]}\", ${val[4]}, ${val[5]});\n";
      }
    ?>
  }
  $('#select_county').change(function(){
    var cty = $(this).val();
    var type = $('#select_restaurant_type').val();
    $.each(county_latlng, function(i, val){
      if(val.name == cty){
        googleMapRestaurant.setCenter(new google.maps.LatLng(val.lat, val.lng));
      }
    });
    $.each(googleMapMarkers, function(i, m){
      if(m.county.substring(0,2) == cty && (type == 'all' || m.type == type)){
        m.marker.setMap(googleMapRestaurant);
      } else {
        m.marker.setMap(null);
      }
    });
  });
  $('#select_restaurant_type').change(function(){
    var type = $(this).val();
    var cty = $('#select_county').val()
    $.each(googleMapMarkers, function(i, m){
      if((type == 'all' || m.type == type) && (cty == 'null' || m.county.substring(0,2) == cty)){
        m.marker.setMap(googleMapRestaurant);
      } else {
        m.marker.setMap(null);
      }
    });
  });
  $('#select_restaurant_name_text').change(function(){
    $.each(googleMapMarkers, function(i, m){
      if(m.name.indexOf($('#select_restaurant_name_text').val()) > -1){
        m.marker.setMap(googleMapRestaurant);
      } else {
        m.marker.setMap(null);
      }
    });
    googleMapRestaurant.setCenter(new google.maps.LatLng(24.1556056, 121.4318678));
    googleMapRestaurant.setZoom(7);
  });
}


var county_latlng = [
<?php
foreach($county_latlng as $val){
  echo "{name: '${val[0]}', lat: ${val[2]}, lng: ${val[1]}},\n";
}
?>
];

function addMarkerToGoogleMap(county, addr, name, type, lat, lng){
  var marker = new google.maps.Marker({
    map: null,
    position: new google.maps.LatLng(lat, lng)
  });
  marker.infowindow = new google.maps.InfoWindow({
    content: '<div>地址：'+addr+'<br>名稱：'+name+'</div>'
  });
  google.maps.event.addListener(marker, 'click', function(){
    $('#restaurant_info_name').html($('<div class="biao_kai"></div>').append(name));
    $('#restaurant_info_addr').html($('<div class="biao_kai"></div>').append(addr));
    $('#restaurant_info_a1').html('<img src="image/google.jpg" class="brand_icon"/>').attr('href', encodeURI('https://www.google.com.tw/webhp?hl=zh-tw&gws_rd=ssl#hl=zh-tw&q='+name));
    $('#restaurant_info_a2').html('<img src="image/ipeen.jpg" class="brand_icon"/>').attr('href', encodeURI('http://www.ipeen.com.tw/search/taiwan/000/0-100-0-0/'+name));
  });
  googleMapMarkers.push({
    name: name,
    marker: marker,
    county: county,
    type: type
  });
}

});
</script>
