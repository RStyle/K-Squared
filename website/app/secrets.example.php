<?php

define('OPENSSL_KEY', 'Look into user.php to lookup how to create one.');
define('ROOT', 'https://savrasov.de/k-squared/');
define('RECAPTCHA_PRIVATE_KEY', 'your private google  reCaptchakey');
define('SUCCESS', 1);
define('FAILURE', 2);	// because of some annoying twig bug 0 is not recommended (by me)

try {
	$db_mysql = new PDO(
		'mysql:host=localhost;dbname=ksquared_db;charset=utf8mb4',
		'dbuser',
		'yourdbpassword'
	);
	$db_mysql->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
	die("MYSQL ERROR");
}
