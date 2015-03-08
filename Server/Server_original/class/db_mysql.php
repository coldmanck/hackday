<?php
  /**
   * DB_class
   *   Base class for connecting to mysql database.
   *   You should use db.class.php to new an object.
   *
   * function connect( $Database = "", $Host = "", $User = "", $Password = "" )
   *   Connect to the database.
   * function query( $query_string )
   *   Execute the sql string and return the result as data array
   * function halt( $msg ) 
   *   Die and show the Message
   * function execute( $query_string )
   *   Execute the sql string without return result. 
   * function struct( $table = '' )
   *   Get the fields name from tables.
   * function security_element( $sql_string ) 
   *   Security element to against sql injection.
   * 
   * @author Nagi Lin <johnny5581[at]gmail.com>
   */
  class DB_class
  {
      var $Host     = "";
      var $Database = "";
      var $User     = "";
      var $Password = "";


      var $Result     = array();
      var $Cursor     = -1;       
      var $Debug      = 0;       
      var $Connect    = 0;
      var $Query_Id   = 0;
    /**
     * Constructur
     */
    function DB_class($debug = 0)
    {
      $this->Debug = $debug;
      $this->connect();
    }

    /**
     * Connect to the database, main function.
     * @param  string $Database Database's name, the oracle 11g xe default is 'xe'.
     * @param  string $Host     Database's IP or Host, default is 'localhost'.
     * @param  string $User     Username
     * @param  string $Password Password
     * @return integer          Return the Connect.
     */
    function connect( $Database = "", $Host = "", $User = "", $Password = "" )
    {
      if (0 != $this->Connect) 
        return $this->Connect;

      if ("" == $Host)
        $Host = $this->Host;
      if ("" == $Database)
        $Database = $this->Database;
      if ("" == $User)
        $User = $this->User;
      if ("" == $Password)
        $Password = $this->Password;

      $this->Connect = mysql_pconnect( $Host, $User, $Password );
      if ( !$this->Connect ) {
        $this->halt ("[-] Connect database fail");
      }

      if ( !@mysql_select_db( $Database, $this->Connect ) )
      {
        $this->halt ("[-] Connect select database");
      }
      mysql_query("SET NAMES 'UTF8'"); 
      mysql_query("SET CHARACTER SET UTF8"); 
      return $this->Connect;
    }

    /**
     * Execute the sql string and return the result as data array. 
     * @param  string $query_string The query string.
     * @return array(row) It'll return the array of rows, or 0 if parameter fail, or die if query error.
     */
    function query( $query_string )
    {
      if ('' == $query_string)
        return 0;

      if (!$this->connect())
        return 0;

      if ($this->Query_Id)
        $this->free(); 

      $this->Cursor = -1;  //reset cursor

      //Take security element
      $query_string = $this->security_element($query_string);

      if ($this->Debug)
        printf ("[*] Debug sql_str=%s<br />", $query_string);

      $this->Query_Id = @mysql_query( $query_string, $this->Connect );


      $this->Result = array();

      while (($row = mysql_fetch_assoc($this->Query_Id)) != false) {
        $this->Result[] = $row;
      }
      return $this->Result;
    }

    function get_img() {
      if ('' == $query_string)
        return 0;

      if (!$this->connect())
        return 0;

      if ($this->Query_Id)
        $this->free(); 

      $this->Cursor = -1;  //reset cursor

      //Take security element
      $query_string = $this->security_element($query_string);

      if ($this->Debug)
        printf ("[*] Debug sql_str=%s<br />", $query_string);
      $this->Query_Id = mysql_query( $query_string, $this->Connect );
      $image = mysql_fetch_assoc($this->Query_Id);
      $image = $image['image'];
      return $image;
    }

    /**
     * Die and show the Message
     * @param  string $msg Message
     * @return void
     */
    function halt( $msg ) 
    {
      if ($this->Debug) 
        $msg .= "<br />".@oci_error($this->Query_Id)['message']."<br />";
      die("<br />".$msg."<br />");
    }

    /**
     * Execute the sql string without return result. 
     * @param  string $query_string The query string.
     * @return boolean Return oci_execute() result (True or False).
     */
    function execute( $query_string )
    {
      if ('' == $query_string)
        return 0;

      if (!$this->connect())
        return 0;
      
      if ($this->Query_Id)
        $this->free(); 

      //Take security element
      $query_string = $this->security_element($query_string);

      if ($this->Debug)
        printf("[*] Debug sql_str=%s<br />", $query_string);

      $this->Query_Id = mysql_query($query_string, $this->Connect);
      if (!$this->Query_Id) {
        $this->halt ("[-] SQL Invalid.");
      }

      return $this->Query_Id;
    }

    /**
     * Release the query resource.
     * @return void
     */
    function free()
    {
      @mysql_free_result($this->Query_Id);
      $this->Query_Id = 0;
    }


    /**
     * Security element to against sql injection.
     * @param  string $sql_string Original sql string
     * @return string             Filtered sql string 
     */
    function security_element( $sql_string ) 
    {
      // Filter sql injection
      // $sql_string = mysql_escape_string($sql_string);

      /**
       * @todo can put code here to protect the exception status when mysqli_real_esacpe_string not working.
       */
      
      return $sql_string;
    }

    function f( $field_name ) 
    {
      return $this->Result[$this->Cursor][$field_name];
    }

    function next(  ) 
    {
      if (!$this->Query_Id) {
        $this->halt("Not yet query.");
        return 0;
      }

      $this->Cursor += 1;

      return is_array($this->Result[$this->Cursor]);
    }
  }
?>