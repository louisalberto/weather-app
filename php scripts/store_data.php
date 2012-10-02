<?

/*user data taken from android*/
#$lat =44.743999;
#$long =-73.605117;
$user_id = 1;
$user_temp = 0;
$user_humid = 0;
$date_time = date ('2012-09-10 12:12:00');


$lat = $_POST["c_latitude"];
$long = $_POST["c_longitude"];
//$user_id = $_POST["c_user_id"];
//$user_temp = $_POST["c_user_temp"];
//$user_humid = $_POST["c_user_humid"];
//$date_time = $_POST["c_date_time"];


$query = "INSERT INTO `user_data` (`user_id`, `latitude`, `longitude`, `temp`, `humidity`, `date`) VALUES($user_id, $lat, $long, $user_temp, $user_humid, '$date_time');";
//$query = "INSERT INTO user_data (date) VALUES(". mysql_real_escape_string($date_time). ");";
mysql_connect("localhost","root","lg7838");
mysql_select_db("weather");

mysql_query($query) or die(mysql_error());
//mysql_close();
$response[0]["test"] = "Thanks, that was super helpful!";
echo json_encode($response);
?>
