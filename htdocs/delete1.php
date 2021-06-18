<?
$key = $_POST["id"];

$connect = mysql_connect("localhost:3306", "root", "apmsetup");
mysql_select_db("calender", $connect);

$sqlDelete = "delete from membership where id='$key'";

mysql_query($sqlDelete,$connect);



?>

