<?php

$brand = htmlspecialchars($_GET['brand']);
$model = htmlspecialchars($_GET['model']);
$port = htmlspecialchars($_GET['port']);

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
<h1>Order a <?= $brand ?> <?= $model ?></h1> <br><br><br>

<form action="processing.php" method="post">
    <input type="hidden" name="port" value=<?= $port ?>>
    <p>Client name : <input type="text" name="client" required/></p>
    <p>Brand : <input type="text" name="brand" value="<?= $brand ?>" readonly required/></p>
    <p>Model : <input type="text" name="model" value="<?= $model ?>" readonly required/></p>
    <label for="color">Color: </label>
    <select name="color">
        <option value="red">RED</option>
        <option value="green">GREEN</option>
        <option value="pink">PINK</option>
        <option value="blue">BLUE</option>
        <option value="gray">GRAY</option>
        <option value="white">WHITE</option>
        <option value="black">BLACK</option>
        <option value="orange">ORANGE</option>
        <option value="yellow">YELLOW</option>
        <option value="brown">BROWN</option>
    </select>
    <br><br>
    <label for="carburation">Carburation : </label>
    <select name="carburation">
        <option value="fuel">FUEL</option>
        <option value="diesel">DIESEL</option>
        <option value="electric">ELECTRIC</option>
    </select>
    <p>Power : <input type="text" name="power" required/></p>
    <p>Option(s) :</p>
    <p><strong>Separate your options with a comma (ex: Op1, Op2, ...)</strong></p>
    <label>
        <textarea name="options" rows=4 cols=40></textarea>
    </label>
    <p><input type="submit" value="Order"></p>
</form>
</body>
</html>
