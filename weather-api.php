<?php

/*functions*/


/*parses xml*/
function xml_string($link){

$xml = simplexml_load_file($link);
return $xml;

}
/************************************************************/

/*connect to mysql and execute query*/
function query_mysql($query){
mysql_connect("localhost","root","lg7838");
mysql_select_db("weather");
$sql=mysql_query($query);
while($row=mysql_fetch_assoc($sql))
$output[]=$row;
//print(json_encode($output));


return $output;
//mysql_close();
}
/************************************************************/


/*some initilizations*/

/************************************************************
here's the variables for the best guessed temperature and humidty.
************************************************************/
$best_temp = 0;
$best_humid = 0;

/***********************************************************

/*android app passes the user variable: $user_input, $lat, $long*/
$user_id = 1;
$user_temp = 70;
$user_humid = 50;
$lat =44.743999;
$long =-73.605117;
$date_time = date ('2012-09-10 12:12:00');
//print $date_time;
$threshold = 100;
/************************************************************/



/*query local database to get station name for noaa*/ 

$query = "SELECT station_id, state, station_name,
3956 * 2 * ASIN(SQRT( POWER(SIN(($lat - abs(latitude)) * pi()/180 / 2),2) + COS($lat * pi()/180 ) * COS(abs(latitude) *  pi()/180) * POWER(SIN(($long - longitude) * pi()/180 / 2), 2) )) as distance
FROM station having distance < $threshold
ORDER BY distance
limit 1;";

$noaa_local = query_mysql($query); 
//var_dump($noaa_local);
//print $noaa_local[0]["station_id"];



$noaa_station = $noaa_local[0]["station_id"];
$noaa_distance = round($noaa_local[0]["distance"], 3, PHP_ROUND_HALF_UP);
//echo $noaa_distance;
/**************************************************************/


/*Reverse lookup on Google's geocode to find zipcode based on $lat/$long. This is neccessary for weatherbug api */
/*the problem here is that if the coord are not in the us, then the below fails, gotta figure outsome way to handle this*/
$xml = xml_string('http://maps.googleapis.com/maps/api/geocode/xml?latlng='.$lat.','.$long.'&sensor=true');
$zip = $xml->children()->result->children()->address_component[7]->children()->short_name;
//print $zip;
//echo '<br>';

/**************************************************************/

/*This is just a work-around for the api*/
$var ='http://www.aws.com/aws';

/**************************************************************/

/*check local database*/
$query = "SELECT user_id, date,
3956 * 2 * ASIN(SQRT( POWER(SIN(($lat - abs(latitude)) * pi()/180 / 2),2) + COS($lat * pi()/180 ) * COS(abs(latitude) *  pi()/180) * POWER(SIN(($long - longitude) * pi()/180 / 2), 2) )) as distance
FROM user_data having distance < 1
ORDER BY distance
limit 1;";
$local = query_mysql($query);

var_dump($local);
echo "<br>";

/*******************************************************
so this is really messy and needs to be redone.
It just checks if someething is returned from the db
then it checks if it falls within a date range, and if it does, then sets
the best temperature to the returned value. 
*******************************************************/


if (!empty($local)){

//maybe need to do this part in query, but this will do for now
$post_date = $local[0]["date"];
$days = 0;
$hours = 1;
$hours2 = -1;


$low_date = date('Y-m-d H:i:s', strtotime("+$days days $hours2 hours", strtotime($post_date)));
$high_date = date('Y-m-d H:i:s', strtotime("+$days days $hours hours", strtotime($post_date)));
}

else{
$not_close = "nobody has reported data near you";


}


if($post_date >= $low_date && $post_date <= $high_date){

//coordinates are close, and within the hour, so these are the best possible results

$best_temp = $local[0]["temp"];
$best_humid = $local[0]["humidity"];

}



else{
$not_close = "nobody has reported data near you";


}


/**************************************************************/

/*get noaa data*/

//$xml = simplexml_load_file('http://www.weather.gov/xml/current_obs/'.$noaa_local.'.xml');
//$link = 'http://www.weather.gov/xml/current_obs/'.$noaa_local.'.xml';

$xml = xml_string('http://www.weather.gov/xml/current_obs/'.$noaa_station.'.xml');

   $noaa_temp = $xml->children()->temp_f;
   $noaa_humid = $xml->children()->relative_humidity;


/**************************************************************/


//$xml = simplexml_load_file('http://api.wunderground.com/api/1d165c1bb8bb4db2/conditions/q/'.$lat.','.$long.'.xml');

$xml = xml_string('http://api.wunderground.com/api/1d165c1bb8bb4db2/conditions/q/'.$lat.','.$long.'.xml');

$wg_temp=$xml->children()->current_observation->children()->temp_f;
$wg_humid=(int)$xml->children()->current_observation->children()->relative_humidity;


/**************************************************************/

//$xml = simplexml_load_file('http://api.wxbug.net/getLiveWeatherRSS.aspx?ACode=A5565943685&zipcode='.$zip.'&unittype=2');

$xml = xml_string('http://api.wxbug.net/getLiveWeatherRSS.aspx?ACode=A5565943685&zipcode='.$zip.'&unittype=2');

$wbug_temp = $xml->channel->children($var)->children($var)->ob->temp;
$wbug_humid=$xml->channel->children($var)->children($var)->ob->humidity;

//echo $argv[1];

$output = array();
exec("python approx-weather.py $user_temp $noaa_temp $wg_temp $wbug_temp", $output);
//var_dump($output);



/*store user data*/

$query = "INSERT INTO `user_data` (`user_id`, `latitude`, `longitude`, `temp`, `humidity`, `date`) VALUES($user_id, $lat, $long, $user_temp, $user_humid, '$date_time');";
//$query = "INSERT INTO user_data (date) VALUES(". mysql_real_escape_string($date_time). ");";
mysql_connect("localhost","root","lg7838");
mysql_select_db("weather");

mysql_query($query) or die(mysql_error());
//mysql_close();

/************************************************************/

/******Display results******************

echo 'noaa temp: ';
echo $noaa_temp;
echo '<br>';
echo 'noaa humid: ';
echo $noaa_humid;
echo '<br>';

echo 'wbug temp: ';
echo $wbug_temp;
echo '<br>';
echo 'wbug humid: ';
echo $wbug_humid;
echo '<br>';


echo 'wg temp: ';
echo $wg_temp;
echo '<br>';
echo 'wg humid: ';
echo $wg_humid;
echo '<br>';


echo (float)$output[0];

**************************************************************/

?>

