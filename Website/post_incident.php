<?php

include "../db.php";

$data = json_decode(
file_get_contents("php://input"),
true
);

$type = $data["type"];
$description = $data["description"];

$sql = "

INSERT INTO incidents

(type,description,status)

VALUES

('$type','$description','Pending')

";

if($conn->query($sql)){

echo json_encode(

array(

"success"=>true

)

);

}else{

echo json_encode(

array(

"success"=>false

)

);

}

?>