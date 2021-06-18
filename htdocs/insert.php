<?
$id = $_POST["id"];
$arr = explode("/", $id);

$connect = mysql_connect("localhost:3306", "root", "apmsetup");
mysql_select_db("calender", $connect);

$insertSQL = "insert into membership(id,password)
values('$arr[0]', '$arr[1]')";
mysql_query($insertSQL, $connect);
mysql_close();
?>
