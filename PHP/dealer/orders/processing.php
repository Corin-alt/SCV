<?php
require '../Engine.php';
require '../Car.php';
require '../Order.php';
require '../Model.php';
require '../Message.php';

$client = $_POST['client'];
$carbu = $_POST['carburation'];
$brand = $_POST['brand'];
$power = $_POST['power'];
$color = $_POST['color'];
$model = $_POST['model'];
$options = $_POST['options'];
$port = $_POST['port'];


$model_obj = new Model($brand, $model);
$engine = new Engine($carbu, (int)$power);
$car = new Car("", $engine, $color, $model_obj, $options);

$order_information_json = json_decode(file_get_contents("informations_order.json", true), true);
$nb = $order_information_json['nb_order'];

$order = new Order($nb, $client, date("d/m/Y"), $car);
$order_encode = json_encode($order);

$message = new Message("order", false, $order_encode);

$fp = fopen('logs/order_' . $nb . '.json', 'w');
fwrite($fp, $order_encode);

$order_information_json['nb_order'] = $nb + 1;

$fp = fopen('informations_order.json', 'w');
fwrite($fp, json_encode($order_information_json));
fclose($fp);


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
        if ($res['idFunction'] === "orderPassed") {
            header('Location: confirm_order.php');
        } else if ($res['idFunction'] === "orderPassedAndDelivery") {
            header('Location: confirm_order_delivery.php?port=' . $port);
        }
    } else {
        echo '<h1> 400 error</h1>';
    }
}
