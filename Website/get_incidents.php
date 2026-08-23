<?php

include "../db.php";

$result = $conn->query(

"SELECT * FROM incidents"

);

$incidents = array();

while($row = $result->fetch_assoc()){

$incidents[] = $row;

}

header("Content-Type: application/json");

echo json_encode($incidents);

?>