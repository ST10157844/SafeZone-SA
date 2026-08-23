<?php

$conn = new mysqli(
    "localhost",
    "root",
    "",
    "safezone_db"
);

if($conn->connect_error){
    die("Database Connection Failed");
}

?>