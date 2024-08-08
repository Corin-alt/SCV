<?php

require '../Message.php';
require '../Car.php';

$order = $_POST['num_order'];
$key = $_POST['key'];
$port = $_POST['port'];

$arr = array('num_order' => $order, 'key' => $key);
$message = new Message("delivery", false, json_encode($arr));

$options = [
    'http' =>
        [
            'method' => 'POST',
            'header' => 'Content-type: application/x-www-formurlencoded',
            'content' => json_encode($message)
        ]
];

$URL = "http://localhost:" . $port . "/constructor";
$context = stream_context_create($options);

if (($jsonTexte = @file_get_contents($URL, false, $context)) !== false) {
    $res = json_decode($jsonTexte, true);
    if ($res['code'] == 200) {
        if ($res['idFunction'] === "delivery") {
            echo '<h1> Your ordered car </h1>';
            echo $res['content'];
        }
    } else {
        if ($res['content'] === "bad_key") {
            echo '<h1> 400 error, the input key is incorrect </h1>';
        } else {
            echo '<h1> 400 error, the input number order is incorrect </h1>';
        }
    }
}
