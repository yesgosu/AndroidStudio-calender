<?
$original_title = $_POST["original_title"];
$original_point = $_POST["original_point"];
$original_time = $_POST["original_time"];

$target_title = $_POST["target_title"];
$target_point = $_POST["target_point"];
$target_time = $_POST["target_time"];

$connect = mysql_connect("localhost:3306", "root", "apmsetup");
mysql_set_charset("utf8",$connect);
mysql_select_db("calender", $connect);

$sqlUpdate = "update calenderlist set title='$target_title', point='$target_point', time=$target_time where title='$original_title' and point='$original_point' and time=$original_time";

mysql_query($sqlUpdate);
mysql_close();
?>

