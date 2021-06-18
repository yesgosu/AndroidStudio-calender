<?php
    $con = mysqli_connect("localhost", "root", "apmsetup","calender");
    mysqli_query($con,'SET NAMES utf8');

    $id = $_POST["id"];
    $password = $_POST["password"];

    $statement = mysqli_prepare($con, "SELECT id,password FROM membership WHERE id = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $id, $password);
    mysqli_stmt_execute($statement);

    mysqli_stmt_bind_result($statement, $id, $password);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["id"] = $id;
        $response["password"] = $password;
    }

    echo json_encode($response);



?>