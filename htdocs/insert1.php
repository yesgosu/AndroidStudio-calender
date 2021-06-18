<?
$title = $_POST["title"];
$point = $_POST["point"];
$time = $_POST["time"];

$connect = mysql_connect("localhost:3306", "root", "apmsetup");
mysql_set_charset("utf-8",$connect);
mysql_select_db("calender", $connect);

$insertSQL = "insert into calenderlist(title,point,time) values('$title', '$point', $time)";
mysql_query($insertSQL);
mysql_close();
?>
