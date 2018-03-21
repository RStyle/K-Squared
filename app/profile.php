<?php


if($page == 'profile' && $_GET['subpage'] == 'change_email' && $user->islogged() && !empty($_POST['email'])) {
	$user->changeEmail($_POST['email']);
	$_GET['change_email_success'] = SUCCESS;
} elseif($page == 'profile' && $_GET['subpage'] == 'change_password' && $user->islogged() && !empty($_POST['password1']) && !empty($_POST['password2']) && !empty($_POST['password_old'])) {
	$success = FAILURE;
	if($_POST['password1'] == $_POST['password2'])
		$success = $user->changePassword($_POST['password1'], $_POST['password_old']);
	$_GET['change_password_success'] = $success;
}


$months = array(
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July ',
    'August',
    'September',
    'October',
    'November',
    'December',
);

$current = date('F');
$start = array_search($current, $months);

$monthsTilNow = array();
$total = 0;
for($i = $start + 1; $total < 12; $i++)
{
    if($i == 12)
        $i = 0;
    $monthsTilNow[] = "'".$months[$i]."'";
    $total++;
}
$allMonthsTilNow = implode(', ', $monthsTilNow);

$last30days = array();
for($i = 29; $i >= 0; $i--) {
	$last30days[] = date('d', strtotime('today - '.$i.' days'));
}
$allLast30days = implode(', ', $last30days);

$last7days = array();
for($i = 6; $i >= 0; $i--) {
	$last7days[] = "'".date('d/m', strtotime('today - '.$i.' days'))."'";
}
$allLast7days = implode(', ', $last7days);
$js ="

window.chartColors = {
	red: 'rgb(255, 99, 132)',
	orange: 'rgb(255, 159, 64)',
	yellow: 'rgb(255, 205, 86)',
	green: 'rgb(75, 192, 192)',
	blue: 'rgb(54, 162, 235)',
	purple: 'rgb(153, 102, 255)',
	grey: 'rgb(201, 203, 207)'
};

(function(global) {
	var Months = [
		'January',
		'February',
		'March',
		'April',
		'May',
		'June',
		'July',
		'August',
		'September',
		'October',
		'November',
		'December'
	];

	var COLORS = [
		'#4dc9f6',
		'#f67019',
		'#f53794',
		'#537bc4',
		'#acc236',
		'#166a8f',
		'#00a950',
		'#58595b',
		'#8549ba'
	];

	var Samples = global.Samples || (global.Samples = {});
	var Color = global.Color;

	Samples.utils = {
		// Adapted from http://indiegamr.com/generate-repeatable-random-numbers-in-js/
		srand: function(seed) {
			this._seed = seed;
		},

		rand: function(min, max) {
			var seed = this._seed;
			min = min === undefined ? 0 : min;
			max = max === undefined ? 1 : max;
			this._seed = (seed * 9301 + 49297) % 233280;
			return min + (this._seed / 233280) * (max - min);
		},

		numbers: function(config) {
			var cfg = config || {};
			var min = cfg.min || 0;
			var max = cfg.max || 1;
			var from = cfg.from || [];
			var count = cfg.count || 8;
			var decimals = cfg.decimals || 8;
			var continuity = cfg.continuity || 1;
			var dfactor = Math.pow(10, decimals) || 0;
			var data = [];
			var i, value;

			for (i = 0; i < count; ++i) {
				value = (from[i] || 0) + this.rand(min, max);
				if (this.rand() <= continuity) {
					data.push(Math.round(dfactor * value) / dfactor);
				} else {
					data.push(null);
				}
			}

			return data;
		},

		labels: function(config) {
			var cfg = config || {};
			var min = cfg.min || 0;
			var max = cfg.max || 100;
			var count = cfg.count || 8;
			var step = (max - min) / count;
			var decimals = cfg.decimals || 8;
			var dfactor = Math.pow(10, decimals) || 0;
			var prefix = cfg.prefix || '';
			var values = [];
			var i;

			for (i = min; i < max; i += step) {
				values.push(prefix + Math.round(dfactor * i) / dfactor);
			}

			return values;
		},

		months: function(config) {
			var cfg = config || {};
			var count = cfg.count || 12;
			var section = cfg.section;
			var values = [];
			var i, value;

			for (i = 0; i < count; ++i) {
				value = Months[Math.ceil(i) % 12];
				values.push(value.substring(0, section));
			}

			return values;
		},

		color: function(index) {
			return COLORS[index % COLORS.length];
		},

		transparentize: function(color, opacity) {
			var alpha = opacity === undefined ? 0.5 : 1 - opacity;
			return Color(color).alpha(alpha).rgbString();
		}
	};

	// DEPRECATED
	window.randomScalingFactor = function() {
		return Math.round(Samples.utils.rand(0, 1000));
	};

	// INITIALIZATION

	Samples.utils.srand(Date.now());


}(this));
var config1 = {
    type: 'bar',
    data: {
        labels: [".$allLast7days."],
        datasets: [ {
            label: 'Last 7 Days',
            backgroundColor: window.chartColors.black,
            borderColor: window.chartColors.black,
            data: [
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor()
            ],
            fill: true,
        }]
    },
    options: {
        responsive: true,
        title:{
            display:true,
            text:''/* your progress */
        },
        tooltips: {
            mode: 'index',
            intersect: false,
        },
        hover: {
            mode: 'nearest',
            intersect: true
        },
        scales: {
            xAxes: [{
                display: true,
                scaleLabel: {
                    display: true,
                    labelString: 'Day'
                }
            }],
            yAxes: [{
                display: true,
                scaleLabel: {
                    display: true,
                    labelString: 'Value'
                }
            }]
        }
    }
};

var config2 = {
    type: 'bar',
    data: {
        labels: [".$allLast30days."],
        datasets: [ {
            label: 'Last 30 Days',
            backgroundColor: window.chartColors.black,
            borderColor: window.chartColors.black,
            data: [
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor()
            ],
            fill: true,
        }]
    },
    options: {
        responsive: true,
        title:{
            display:true,
            text:''
        },
        tooltips: {
            mode: 'index',
            intersect: false,
        },
        hover: {
            mode: 'nearest',
            intersect: true
        },
        scales: {
            xAxes: [{
                display: true,
                scaleLabel: {
                    display: true,
                    labelString: 'Day'
                }
            }],
            yAxes: [{
                display: true,
                scaleLabel: {
                    display: true,
                    labelString: 'Value'
                }
            }]
        }
    }
};

var config3 = {
    type: 'bar',
    data: {
        labels: [".$allMonthsTilNow."],
        datasets: [ {
            label: 'Last 12 Months',
            backgroundColor: window.chartColors.black,
            borderColor: window.chartColors.black,
            data: [
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor(),
                randomScalingFactor()
            ],
            fill: true,
        }]
    },
    options: {
        responsive: true,
        title:{
            display:true,
            text:''
        },
        tooltips: {
            mode: 'index',
            intersect: false,
        },
        hover: {
            mode: 'nearest',
            intersect: true
        },
        scales: {
            xAxes: [{
                display: true,
                scaleLabel: {
                    display: true,
                    labelString: 'Month'
                }
            }],
            yAxes: [{
                display: true,
                scaleLabel: {
                    display: true,
                    labelString: 'Value'
                }
            }]
        }
    }
};

window.onload = function() {
    var ctx1 = document.getElementById('canvas1').getContext('2d');
    window.myLine = new Chart(ctx1, config1);
    var ctx2 = document.getElementById('canvas2').getContext('2d');
    window.myLine = new Chart(ctx2, config2);
    var ctx3 = document.getElementById('canvas3').getContext('2d');
    window.myLine = new Chart(ctx3, config3);
};";

js_add($js);


js_add("
$( document ).ready(function() {
	$('#canvas1button').click(function() {
	  $('#canvas1').css('display', 'block');
	  $('#canvas2').css('display', 'none');
	  $('#canvas3').css('display', 'none');
	});
	$('#canvas2button').click(function() {
	  $('#canvas1').css('display', 'none');
	  $('#canvas2').css('display', 'block');
	  $('#canvas3').css('display', 'none');
	});
	$('#canvas3button').click(function() {
	  $('#canvas1').css('display', 'none');
	  $('#canvas2').css('display', 'none');
	  $('#canvas3').css('display', 'block');
	});
});
");






















