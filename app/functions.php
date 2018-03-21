<?php

$lg_data = '';
function lg($data) {	//log function
	global $lg_data;
	$lg_data .= 'console.log(\''.$data.'\');';
}

$javascript_code = '';
function js_add($js) {	//log function
	global $javascript_code;
	$javascript_code .= $js;
}

function generateRandomString($length = 8) {
	// $characters = str_split('abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ');
	$characters = str_split('abcdefghijkmnpqrstuvwxyz23456789ABCDEFGHJKLMNPQRSTUVWXYZ');
	$return = '';
	for ($i = 0; $i < $length; $i++) {
		$return .= $characters[ rand(0, count($characters) - 1) ];
	}
	return $return;
}