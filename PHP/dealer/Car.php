<?php


class Car implements JsonSerializable
{
    private string $vin;
    private Model $model;
    private Engine $engine;
    private string $color;
    private string $options;

    public function __construct(string $vin, Engine $engine, string $color, Model $model, string $options)
    {

        $this->vin = $vin;
        $this->engine = $engine;
        $this->color = $color;
        $this->model = $model;
        $this->options = $options;
    }

    public function getVin(): string
    {
        return $this->vin;
    }

    public function setVin(string $vin): void
    {
        $this->vin = $vin;
    }

    public function getModel(): Model
    {
        return $this->model;
    }

    public function getEngine(): Engine
    {
        return $this->engine;
    }

    public function setEngine(Engine $engine): void
    {
        $this->engine = $engine;
    }

    public function getColor(): string
    {
        return $this->color;
    }

    public function setColor(string $color): void
    {
        $this->color = $color;
    }

    public function getBrand(): string
    {
        return $this->brand;
    }

    public function setBrand(string $brand): void
    {
        $this->brand = $brand;
    }


    public function getOptions(): string
    {
        return $this->options;
    }

    public function __toString(): string
    {
        return "Car: \n-vin: " . $this->getVin()
            . "\n-color: " . $this->getColor()
            . "\n" . $this->getModel()->__toString()
            . "\n" . $this->getEngine()->__toString() . "\n-options: " . $this->getOptions();
    }


    public function jsonSerialize(): array
    {
        $json['vin'] = $this->getVin();
        $json['model'] = $this->getModel()->jsonSerialize();
        $json['engine'] = $this->getEngine()->jsonSerialize();
        $json['color'] = $this->getColor();
        $json['options'] = $this->getOptions();

        return $json;
    }

    public static function fromJson($json): Car
    {
        $decode = json_decode($json, true);
        $engine = Engine::fromJson(json_encode($decode["engine"]));
        $model = Model::fromJson(json_encode($decode["model"]));

        return new Car($decode["vin"], $engine, $decode["color"], $model, $decode["options"]);
    }
}

