<?php
function connect_to_database()
{
    $servername = "172.26.16.4";
    $username = "sdesdevi";
    $password = "postgres";
    $databasename = "sdvideoclub";

    return pg_connect("host=$servername dbname=$databasename user=$username password=$password");

}

function close_db($db)
{
    pg_close($db);
}

?>