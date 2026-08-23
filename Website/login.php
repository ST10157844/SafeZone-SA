<?php

include "db.php";
session_start();

if(isset($_GET['registered'])){
    echo "<p style='color:green;text-align:center;'>
    Registration successful. Please login.
    </p>";
}

if($_SERVER["REQUEST_METHOD"]=="POST"){

$email = $_POST["email"];

$sql = "SELECT * FROM users
WHERE email='$email'";

$result = $conn->query($sql);

if($result->num_rows > 0){

$_SESSION["user"] = $email;

header("Location: dashboard.php");

}else{

echo "<p style='color:red;text-align:center;'>Invalid Login</p>";

}
}
?>

<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<link rel="stylesheet" href="style.css">
</head>

<body>

<header><img src="logo.png" width="100">
<h1>🛡️ SafeZone SA</h1>
</header>

<nav>
<a href="index.php">Home</a>
<a href="register.php">Register</a>
<a href="login.php">Login</a>
</nav>

<div class="container">

<h2>Login</h2>

<form method="POST">

<input
type="email"
name="email"
placeholder="Enter Email"
required>

<button type="submit">
Login
</button>

</form>

</div>

<footer>
SafeZone SA © 2026
</footer>

</body>
</html>