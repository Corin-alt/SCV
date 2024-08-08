<?php
require '../Message.php';

$port = $_POST['port'];
$message = new Message("informationDelivery", false, "");

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
        if ($res['idFunction'] === "informationDelivery") {
            $content = json_decode($res['content'], true);
            echo '<h1> Information to retrieve your car</h1>';
            echo '<p> Order number :<strong>' . $content ['order'] . '</strong></p>';
            echo '<p> The key to get the car :<strong>' . $content ['key'] . '</strong></p><br>';
            echo '<form action="../car/processing_car_delivery.php" method="post">';
            echo '<input type="hidden" name="port" value=' . $port . '>';
            echo '<p>Num order: <input type="text" name="num_order" required/></p>';
            echo '<p>Key: <input type="text" name="key" required/></p>';
            echo '<button type="submit">Get your car :)</button>';
            echo '</form>';
        }
    } else {
        echo '<h1> 400 error</h1>';
    }
}