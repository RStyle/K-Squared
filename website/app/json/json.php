<?php

$logJSONRequest = true;
$logJSONFile = 'logJSON.txt';

date_default_timezone_set('Europe/Berlin');

require_once('../secrets.php');
require_once '../functions.php';
require_once '../user.php';
$page = 'home';
require_once '../sets.php';

// echo "{ 'k1' : 'apple', 'k2' : 'orange' }";

$return = array();
$return['success'] = 'failure';

if($_GET['action'] == 'login' && !empty($_POST)) {
	$token = $user->jsonLogin($_POST['user'], $_POST['password']);
	if($token !== false) {
		$return['success'] = 'success';
		$return['token'] = $token;
		$return['user'] = $user->getUsername();
	}
} elseif($_GET['action'] == 'check_token' && !empty($_POST)) {
	$user->loginViaToken($_POST['token'], $_POST['user']);
	if($user->islogged()) {
		$return['success'] = 'success';
	}
} elseif($_GET['action'] == 'qr' && !empty($_POST)) {
	$user->loginViaToken($_POST['token'], $_POST['user']);
	if($user->islogged()) {	// token valid
		$return = $sets->qrCodeGetSetAndCards($_POST['id'], $_POST['user']);
		if(empty($return))
			$return['success'] = 'failure';
		else
			$return['success'] = 'success';
	}
}

if(!empty($_GET['test']))
	$return = $sets->qrCodeGetSetAndCards('tSu6Dn7n');


echo json_encode($return);

if($logJSONRequest) {
	if(empty($_POST)) {
		file_put_contents($logJSONFile, 'no arguments at '. date("Y/m/d H:i:s") . "\n" . file_get_contents($logJSONFile));
	} else {
		if(!empty($_POST['user']))
			$_POST['user'] = substr($_POST['user'], 0, 2);
		if(!empty($_POST['token']))
			$_POST['token'] = substr($_POST['token'], 0, 5);
		if(!empty($_POST['password']))
			$_POST['password'] = '123456';
		file_put_contents($logJSONFile, http_build_query($_POST) . "\n\t" . json_encode($return). ' at '. date("Y/m/d H:i:s") . "\n\n" . file_get_contents($logJSONFile));
	}
}








