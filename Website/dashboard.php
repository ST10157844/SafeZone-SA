<?php
session_start();

if(!isset($_SESSION["user"])){
    header("Location: login.php");
    exit();
}
?>

<!DOCTYPE html>
<html>
<head>
<title>Dashboard</title>
<link rel="stylesheet" href="style.css">
</head>

<body>

<header><img src="logo.png" width="100">
<h1>🛡️ SafeZone SA Dashboard</h1>
</header>

<nav>
<a href="index.php">Home</a>
<a href="submit_incident.php">Submit Incident</a>
</nav>

<div class="container">

<h2>Welcome <?php echo $_SESSION["user"]; ?></h2>

<div class="card">
<h3>📝 Report Incident</h3>
<p>Submit a new incident.</p>
</div>

<div class="card">
<h3>👥 Community Safety</h3>
<p>Promote safer communities.</p>
</div>

<div class="card">
<h3>🚨 Emergency Support</h3>
<p>Access emergency information.</p>
</div>

</div>

<footer>
SafeZone SA © 2026
</footer>

</body>
</html>