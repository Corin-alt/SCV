<?php

class Model implements JsonSerializable
{

    private string $brand;
    private string $modelName;

    public function __construct(string $brand, string $model)
    {
        $this->brand = $brand;
        $this->modelName = $model;
    }

    public function getBrand(): string
    {
        return $this->brand;
    }

    public function getModelName(): string
    {
        return $this->modelName;
    }

    public function __toString(): string
    {
         return "\nModel: \n-brand: " . $this->getBrand() . "\n-model: " . $this->getModelName();
    }


    public function jsonSerialize(): array
    {
        $json['brand'] = $this->getBrand();
        $json['model'] = $this->getModelName();
        return $json;
    }

    public static function fromJson($json): Model
    {
        $decode = json_decode($json, true);
        return new Model($decode["brand"], $decode["model"]);
    }
}