<?php
$order_information_json = json_decode(file_get_contents("informations_order.json", true), true);
$nb = $order_information_json['nb_order'] - 1;


$port = $_GET['port'];
?>

<html lang="EN">
<style>
    h1 {
        text-align: center;
    }

    form {
        text-align: center;
    }
</style>
<body>
<h1>Order accepted</h1>
<p>Your order has been accepted.</p>
<p>Your order number is: <strong><?= $nb ?></strong></p>
<form action="../delivery/processing_information_delivery.php" method="post">
    <input type="hidden" name="port" value=<?= $port?>>
    <button type="submit">Retrieve information for delivery</button>
</form>

</body>
</html>
