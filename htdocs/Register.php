<?php
    $con = mysqli_connect("localhost:3306", "root", "apmsetup","membership);
    mysqli_query($con,'SET NAMES utf8');

    $id = $_POST["id"];
    $password = $_POST["password"];


    $statement = mysqli_prepare($con, "INSERT INTO membership VALUES (?,?)");
    mysqli_stmt_bind_param($statement, "s", $id, $password);
    mysqli_stmt_execute($statement);

mysqli_commit($con);
    $response = array();
    $response["success"] = true;



    echo json_encode($response);

?>