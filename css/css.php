<?php
header('Content-type: text/css');
// header ("cache-control: must-revalidate; max-age: 2592000");
// header ("expires: " . gmdate ("D, d M Y H:i:s", time() + 2592000) . " GMT");
ob_start("ob_gzhandler");
// ty http://phpperformance.de/performancegewinn-durch-virtuelles-javascript-file/ + http://www.phpgangsta.de/externe-javascript-dateien-zusammenfassen
// ty https://stackoverflow.com/questions/5389822/how-to-minify-js-or-css-on-the-fly

$cssFiles = array(
  "style.css"
);

$buffer = "";
foreach ($cssFiles as $cssFile) {
  $buffer .= file_get_contents($cssFile);
}


$buffer = preg_replace('!/\*[^*]*\*+([^/][^*]*\*+)*/!', '', $buffer);
$buffer = str_replace(': ', ':', $buffer);
$buffer = str_replace(array("\r\n", "\r", "\n", "\t", '  ', '    ', '    '), '', $buffer);


echo $buffer;
?>