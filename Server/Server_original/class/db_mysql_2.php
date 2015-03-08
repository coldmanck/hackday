<?php

/*****************************************************************************
 php抽象資料庫中介層: mysql
 ----------------------------------------------------------------------------
 本程式目的在於簡化資料庫的存取步驟, 並提供一套一致的存取介面, 讓應用程式在
 不同資料庫上都可以保持同樣的寫法


 介面:
	$Host, $Database, $User, $Password: 
		提供extend class用的連結資訊, 其他程式最好不要使用
	query( $SQL_command)
		送出SQL命令(不必加分號), 會自動建立連結
	 next() / seek( $pos = 0)
		將游標移動到 下一筆 / 第$pos筆 查詢結果, 預設為第一筆
	lock( $table, $mode='write') / unlock()
		lock/unlock某個資料表, 若要lock多個資料表, 則請用array輸入 array( 'tab1', 'tab2', ...)
	affected_rows()
		計算Insert, delete, update所影響的資料筆數
	nr() / num_rows() / num_records()
		計算查詢結果的資料筆數
	nc() / num_cols() / nf() / num_fields()
		計算查詢結果的欄位個數
	f( $FieldName) / p( $FieldName)
		傳回/輸出游標位置資料的 $FieldName 欄位內容
	struct( $table='', $phplib_compatible=false)
		傳回某個資料表的結構 (欄位名稱、型態、長度等)

 使用範例: 
	為了讓這段程式碼可以應用在不同地方, 建議您先建立一個新class, 內含連結資訊
	與密碼, 並保護好這個檔案, 之後您就可以直接使用SQL 命令了
 
	class some_ap_class extend DB_class
	{
		var $Host     = "localhost";
		var $Database = "some_ap_db";
		var $User     = "some_ap_user";
		var $Password = "some_ap_user_password";
	}
  
  在程式中如此使用:
	  include( '含有上面程式碼的檔案');
	  $res = new some_ap_class( 'SELECT * from some_table');
	  while ( $res->next() ) 
	{
	    // do some thing...
	}

----------------------------------------------------------------------------
 參考版本: db_mysql.inc (v1.2), by Boris Erdmann, Kristian Koehntopp (NetUSE AG)
 ****************************************************************************/

class DB_class 
{
	//
	//-- 本class中的變數都不建議使用, 您最好透過其他function存取 
	//

	//-- 資料庫連結資訊: 您最好針對不同的應用, 另外在extend class指定這些參數
	var $Host     = "";
	var $Database = "";
	var $User     = "";
	var $Password = "";

	//-- 查詢結果
	var $Result     = array(); // 儲存查詢結果的陣列
	var $Cursor     = -1;      // Result[]的游標; 目前所在的行數
	var $Errno      = 0;       // 錯誤編號
	var $Error      = "";      // 錯誤訊息

	//-- 設定資料: 這些預設值最好弄清楚作用後再修改
	var $Debug      = 0;       // 若為1, 則使用query()時, 會先顯示送出去的SQL命令
	var $When_Error = "halt";  // "halt": 停止並顯示錯誤, "warning": 僅顯示錯誤, 其他: 自行處理

	//-- 儲存connection和query結果的ID
	var $Connect_Id = 0;
	var $Query_Id   = 0;
	  
	//
	//-- 公用方法: 您可以直接使用這些方法, 而不必擔心日後版本相容的問題 
	//

	function DB_class($query_cmd = "")
	{
		//-- constructor: new這個class時會自動執行這個function
		$this->query($query_cmd);
	}	//--------//

	function connect($Database = "", $Host = "", $User = "", $Password = "")
	{
		//-- 連線處理: 以($User,$Password)連結到$Host主機上的$database資料庫
		if ( 0 != $this->Connect_Id ) 
			return $this->Connect_Id;
	    
	    //-- 若未指定連結參數, 則使用預設值
	    if ("" == $Database)
			$Database = $this->Database;
	    if ("" == $Host)
			$Host     = $this->Host;
	    if ("" == $User)
			$User     = $this->User;
	    if ("" == $Password)
			$Password = $this->Password;
	      
	    //-- 建立連結, 選擇資料庫
	    $this->Connect_Id = mysql_pconnect( $Host, $User, $Password);
	    if ( !$this->Connect_Id )
		{
			$this->halt("連接資料庫($Host, $User, $Password) 失敗.");
			return 0;
	    }

	    if ( !@mysql_select_db( $Database, $this->Connect_Id ) )
		{
			$this->halt( "無法使用資料庫: ".$this->Database );
			return 0;
	    }
		mysql_query("SET NAMES 'UTF8'"); 
        mysql_query("SET CHARACTER SET UTF8"); 
	    
		return $this->Connect_Id;
	}	//--------//

	function free()
	{
		//-- 釋放查詢結果所佔用的空間
		@mysql_free_result( $this->Query_Id);
		$this->Query_Id = 0;
	}	//--------//

	function query( $Query_String )
	{
		//-- 送出SQL命令
	  
	    //-- PHP4會抑制空白的SQL字串, 但new這個class時, 預設動作就是送出一個空白SQL字串
	    if ( '' == $Query_String )
			return 0;

	    if ( !$this->connect() ) // 錯誤訊息已經在connect()時送出了, 這裡直接return
			return 0; 

	    if ( $this->Query_Id )  // 如果查詢結果已經存在, 則清除上次查詢結果
			$this->free();

	    if ( $this->Debug )  // 若為除錯模式, 則顯示SQL字串
			printf( "Debug: query = %s<br>\n", $Query_String );

	    $this->Query_Id = @mysql_query( $Query_String, $this->Connect_Id );
	    $this->Cursor   = 0;
	    $this->Errno = mysql_errno();
	    $this->Error = mysql_error();
	    
		if (!$this->Query_Id)  // 如果查詢失敗, 則送出錯誤訊息
			$this->halt( "Invalid SQL: ".$Query_String );

	    return $this->Query_Id;  // 如果查詢失敗, 而且When_Error不是"halt", 則仍會return 0
	}	//==== query() ====//

	function next()
	{
		//-- 移動到下一筆查詢結果
	  
	    if ( !$this->Query_Id )
		{
			$this->halt( "next(): 尚未輸入查詢命令" );
			return 0;
	    }

	    $this->Result = @mysql_fetch_array( $this->Query_Id );
	    $this->Cursor   += 1;
	    $this->Errno  = mysql_errno();
	    $this->Error  = mysql_error();

	    $stat = is_array($this->Result);

	    return $stat;
	}	//--------//

	function seek( $pos = 0 )
	{
		//-- 將游標移到查詢結果中的某一個(絕對)位置

	    $status = @mysql_data_seek( $this->Query_Id, $pos );
	    if ( $status )
		{
	      $this->Cursor = $pos;
	      return 1;
	    }

	    $this->halt( "seek($pos) 錯誤: 結果只有 ".$this->num_Cursors()." 筆資料" );

		// 如果Halt_On_Error不是"yes", 則移到最後一筆, 並return 0
		@mysql_data_seek($this->Query_Id, $this->num_Cursors());
		$this->Cursor = $this->num_Cursors;
		return 0;
	}	//--------//

	function lock( $table_str, $mode="write" )
	{
		//-- lock資料表
	  
	    $this->connect();

	    // 組合出SQL命令字串:  "LOCK TABLES 資料表名稱 lock模式 [, 資料表名稱 lock模式, ...]"
		$query="lock tables ";
		$table = explode( ',', $table_str );
	    while ( list( $key,$value ) = each( $table ) ) 
			$query .= ( $key=="read" && $key!=0 ) ? "$value read, " : "$value $mode, ";
	    $query = substr( $query,0,-2 );

	    $res = @mysql_query( $query, $this->Connect_Id );
	    if ( !$res )
		{
			$this->halt( "lock($table, $mode) 失敗." );
			return 0;
	    }
	    return $res;
	}	//--------//
	  
	function unlock()
	{
		//-- unlock資料表
	    $this->connect();

	    $res = @mysql_query( "unlock tables" );
	    if ( !$res )
		{
			$this->halt( "unlock() 失敗." );
			return 0;
	    }
	    return $res;
	}	//--------//

	function affected_rows()
	{
		return @mysql_affected_rows( $this->Connect_Id );
	}	//--------//

	function nr()
	{
		return @mysql_num_Rows( $this->Query_Id );
	}	//--------//

	function num_rows()
	{
		return @mysql_num_Rows( $this->Query_Id );
	}	//--------//

	function num_records()
	{
		return @mysql_num_Rows( $this->Query_Id );
	}	//--------//

	function nc()
	{
		return @mysql_num_fields( $this->Query_Id );
	}	//--------//

	function num_cols()
	{
		return @mysql_num_fields( $this->Query_Id );
	}	//--------//

	function num_fields()
	{
		return @mysql_num_fields( $this->Query_Id );
	}	//--------//

	function f( $Name )
	{
		//-- 傳回Result[$Name]的內容
	    return $this->Result[$Name];
	}	//--------//

	function p( $Name )
	{
		//-- 輸出Result[$Name]的內容
	    print $this->Result[$Name];
	}	//--------//

	function struct( $table='', $phplib_compatible = false )
	{
	//  metadata($table='',$full=false) {
	//   struct( $table='', $phplib_compatible=false)

	  // 傳回某資料表的結構(欄位名稱、型態、長度)資訊

	    $count = 0;
	    $id    = 0;
	    $res   = array();

	    /*
	     * $result[]:
	     *   [0]["table"]  table name
	     *   [0]["name"]   field name
	     *   [0]["type"]   field type
	     *   [0]["len"]    field length
	     *   [0]["flags"]  field flags
	     */

	    // 若未指定table, 則預設為處理上次的查詢結果
	    if ($table) {
	      $this->connect();
	      $id = @mysql_list_fields($this->Database, $table);
	      if (!$id)
	        $this->halt("資料表結構 查詢失敗.");
	    } else {
	      $id = $this->Query_Id; 
	      if (!$id)
	        $this->halt("資料表結構 尚無查詢結果.");
	    }
	 
	    $count = @mysql_num_fields($id);

	    for ($i=0; $i<$count; $i++) {
	      $res[$i]["table"] = @mysql_field_table ($id, $i);
	      $res[$i]["name"]  = @mysql_field_name  ($id, $i);
	      $res[$i]["type"]  = @mysql_field_type  ($id, $i);
	      $res[$i]["len"]   = @mysql_field_len   ($id, $i);
	      $res[$i]["flags"] = @mysql_field_flags ($id, $i);
	    }
	    
	    // free the result only if we were called on a table
	    if ($table) @mysql_free_result($id);
	    return $res;
	  } //--------//

	//
	//-- 內部方法: 您最好不要直接使用這些方法, 其他程式應該看不到這些方法


	//

	  function halt($msg) {
	    if ($this->When_Error == "warning" || $this->When_Error == "halt") {
				$this->Error = @mysql_error($this->Connect_Id);
				$this->Errno = @mysql_errno($this->Connect_Id);
				$this->haltmsg($msg);
	    } 
	    if ($this->When_Error == "halt")
		  echo $msg;
	      die("DB_class: 中止執行.");
	  } //--------//

	  function haltmsg($msg) {
	    printf("<a href=javascript:history.go(-1)>回上一頁</a><br>");
	  } //--------//

	  function table_names() {
	  // 傳回資料庫中所有table的名稱
	  
	    $this->query("SHOW TABLES");
	    $i=0;
	    while ($info=mysql_fetch_Rows($this->Query_Id))
	     {
	      $return[$i]["table_name"]= $info[0];
	      $return[$i]["tablespace_name"]=$this->Database;
	      $return[$i]["database"]=$this->Database;
	      $i++;
	     }
	   return $return;
	  } //--------//

} //==== end of class ====//
?>