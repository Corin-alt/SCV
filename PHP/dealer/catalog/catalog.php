<?php
require "../Manufacturer.php";
$file_list = glob('*.json');
?>
<!DOCTYPE html>
<html>
<style>
    h1 {
        text-align: center;
    }
    form {
        text-align: center;
    }
</style>
<body>
<div class="container bootstrap snipets">
    <h1 class="text-center text-muted">Catalog of Cars</h1>
    <div class="row flow-offset-1">
        <div class="col-xs-6 col-md-4">
            <div class="caption">
                <?php foreach ($file_list  as $file_name) :
                    $file_data = file_get_contents($file_name, true);
                    $catalog = Manufacturer::fromJson($file_data);
                    $models = $catalog->getModels();
                    $port = $catalog->getPort();
                    foreach ($models as $m) :
                        ?>
                        <form action="../orders/order_page.php" method="get">
                            <input type="hidden" name="port" value=<?= $port?>>
                            <input type="hidden" name="brand" value=<?= basename($file_name, ".json")?>>
                            <input type="hidden" name="model" value=<?= $m['name'] ?>>
                            <button name="submit" value="1"
                                    type="submit"><?= basename($file_name, ".json") ?> <?= $m['name'] ?></button>
                        </form>
                        <br><br>
                    <?php endforeach; ?>
                <?php endforeach; ?>
            </div>
        </div>
    </div>
</div>
</body>
</html>



