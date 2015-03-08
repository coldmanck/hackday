<?php
  include 'class/db.class.php';

  // if(isset($_GET['d'])) {
  //   $date = $_GET['d'];
  // } else {
  //   date_default_timezone_set('Asia/Tokyo');
  //   $date = date("Y-m-d H:i:s");
  // }


  $db = new stdb();
  $res = $db->query("select * from photo");
  // foreach ($res as $item) {
    // $json[] = base64_encode( $item['data'] );
  // }
  // echo json_encode($json);
  // header("Content-type: img/jpeg");
  var_dump($res) ;
?>