<?
$title = $_POST["title"];
$point = $_POST["point"];
$time = $_POST["time"];

$connect = mysql_connect("localhost:3306", "root", "apmsetup");
mysql_set_charset("utf8",$connect);
mysql_select_db("calender", $connect);

$sqlDelete = "delete from calenderlist where title='$title' and point='$point' and time=$time";

mysql_query($sqlDelete);
mysql_close();

echo $title;
echo "\n";
echo $point;
echo "\n";
echo $time;
echo "\n";
?>

