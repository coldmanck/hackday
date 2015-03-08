<?php 
  include 'class/db.class.php';
  if(isset($_GET['edison_id'])) {

    $device_id  = $_GET['edison_id'];  // 00-00-00-00-00-00
    $conn_type  = $_GET['con'];  // 1 is connect, 0 is disconnect
    $patient_id = $_GET['target_id'];  // 00-00-00-00-00-00

    $db = new stdb(1);
    $db->execute("insert into notify (device_id, patient_id, conn_type) values ('$device_id', '$patient_id', '$conn_type')");

    
    $subtitle = "Warning Mail: ".$device_id;    
    $message = $patient_id." is now ".(($conn_type == 0)?"go out! ":"go in! ");
    // $mailto = "johnny5581@gmail.com";
    $mailto = $_GET['m']."@gmail.com";
    $mailto_label = $_GET['m'];

    $headers  = 'MIME-Version: 1.0' . "\r\n";
    $headers .= 'Content-type: text/html; charset=utf8' . "\r\n";
    $headers .= 'To: '.$mailto_label.' <'.$mailto.'>' . "\r\n";
    $headers .= 'From: HelloTaiwan<support@hellotaiwan.com.tw>'."\r\n";

    echo mail( $mailto, $subtitle, $message, $headers );
    
  } else {
    echo 'No insert<br />';
    $db = new stdb();
    $s = $db->query('select * from notify;');
    var_dump($s);
  }



  
?>
