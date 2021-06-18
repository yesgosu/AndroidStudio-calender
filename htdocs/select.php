<?
$start = $_GET["start"];
$end = $_GET["end"];

$connect = mysql_connect("localhost:3306", "root", "apmsetup");
mysql_set_charset("utf8",$connect);
mysql_select_db("calender", $connect);

$sqlSelect = "select title,point,time from calenderlist where time >= $start and time < $end";

$selectResult = mysql_query($sqlSelect);
print mysql_error();

while($row = mysql_fetch_assoc($selectResult)){
echo $row["title"];
echo "/";
echo $row["point"];
echo "/";
echo $row["time"];
echo "\n";
}
?>

