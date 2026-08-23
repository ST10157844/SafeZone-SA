<?php

include "db.php";

if($_SERVER["REQUEST_METHOD"]=="POST"){

$type = $_POST["type"];
$description = $_POST["description"];

$sql = "INSERT INTO incidents
(type,description,status)

VALUES

('$type','$description','Pending')";

if($conn->query($sql)){
echo "<script>alert('Incident Submitted Successfully');</script>";
}
}
?>

<!DOCTYPE html>
<html>
<head>
<title>Submit Incident</title>
<link rel="stylesheet" href="style.css">
</head>

<body>

<header><img src="logo.png" width="100">
<h1>🛡️ SafeZone SA</h1>
</header>

<nav>
<a href="index.php">Home</a>
<a href="dashboard.php">Dashboard</a>
</nav>

<div class="container">

<h2>Submit Incident</h2>

<form method="POST">

<input
type="text"
name="type"
placeholder="Incident Type"
required>

<textarea
name="description"
placeholder="Describe the incident"
required>
</textarea>

<button type="submit">
Submit Incident
</button>

</form>

</div>

<footer>
SafeZone SA © 2026
</footer>

</body>
</html>