<?php


class Catalog implements JsonSerializable
{

	private array $models;

    public function __construct(array $models)
    {
        $this->models = $models;
    }

    public function getModels(): array
    {
        return $this->models;
    }

    public function setModels(array $models): void
    {
        $this->models = $models;
    }

    public static function fromJson($json) : Catalog
    {
        $decode = json_decode($json, true);
        return new Catalog($decode['models']);

    }

    public function jsonSerialize()
    {
        // TODO: Implement jsonSerialize() method.
    }
}