<?php

include "db.php";

if($_SERVER["REQUEST_METHOD"]=="POST"){

    $name = $_POST["name"];
    $email = $_POST["email"];

    $sql = "INSERT INTO users
    (name,email,role,status)
    VALUES
    ('$name','$email','Member','Active')";

    if($conn->query($sql)){
        header("Location: login.php?registered=1");
        exit();
    }
}
?>

<!DOCTYPE html>
<html>
<head>
<title>Register</title>
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

<h2>Register</h2>

<form method="POST">

<input
type="text"
name="name"
placeholder="Enter Name"
required>

<input
type="email"
name="email"
placeholder="Enter Email"
required>

<button type="submit">
Register
</button>

</form>

</div>

<footer>
SafeZone SA © 2026
</footer>

</body>
</html>