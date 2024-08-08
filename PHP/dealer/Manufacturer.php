<?php


class Manufacturer implements JsonSerializable
{

    private string $port;
	private array $models;

    public function __construct(string $url, array $models)
    {
        $this->port = $url;
        $this->models = $models;
    }

    public function getPort(): string
    {
        return $this->port;
    }

    public function getModels(): array
    {
        return $this->models;
    }

    public function setModels(array $models): void
    {
        $this->models = $models;
    }

    public static function fromJson($json) : Manufacturer
    {

        $decode = json_decode($json, true);
        return new Manufacturer($decode['port'], $decode['models']);

    }

    public function jsonSerialize()
    {
        // TODO: Implement jsonSerialize() method.
    }
}