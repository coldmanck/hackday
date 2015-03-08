<?php
	include 'mainfile.php';
	
	class stdb extends DB_class{
		var $Host     = db_host;
		var $Database = db_name;
		var $User     = db_user;
		var $Password = db_pass;
	}
	
?>