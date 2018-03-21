<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);


require_once './vendor/autoload.php';
require_once './app/secrets.php';
require_once './app/functions.php';
require_once './app/user.php';

$page = 'home';
if(isset($_GET['page'])) {
	if(in_array($_GET['page'], array('home', 'register', 'login', 'privacy_policy', 'legal_notice', 'blog')))
		$page = $_GET['page'];
	elseif($user->islogged() && in_array($_GET['page'], array('profile', 'my_sets', 'edit_set', 'learn_set')))
		$page = $_GET['page'];
	elseif(in_array($_GET['page'], array('profile', 'my_sets', 'edit_set', 'learn_set')))
		$_GET['404'] = '1';
		
}
if(empty($_GET['subpage']))
	$_GET['subpage'] = '';

require_once './app/sets.php';


$loader = new Twig_Loader_Filesystem('./views');
$twig = new Twig_Environment($loader);


if($page == 'profile')
	require_once('./app/profile.php');

$properties = array(
    'name' => 'hello world',
    'page' => $page,
    'root' => ROOT,		// defined in secrets.php
    'success' => SUCCESS,
    'failure' => FAILURE,
    '_get' => $_GET,
    '_post' => $_POST,
    'lg_data' => $lg_data,
    'js_code' => $javascript_code,
    'user' => $user,
    'sets' => $sets
);

echo $twig->render('index.html', $properties);
