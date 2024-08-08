<?php


class Engine implements JsonSerializable
{
    private string $carburation;
    private int $power;

    public function __construct(string $carburetion, int $power)
    {
        $this->carburation = $carburetion;
        $this->power = $power;
    }

    public function getCarburation(): string
    {
        return $this->carburation;
    }

    public function setCarburation(string $carburation): void
    {
        $this->carburation = $carburation;
    }

    public function getPower(): int
    {
        return $this->power;
    }

    public function setPower(int $power): void
    {
        $this->power = $power;
    }

    public function __toString(): string
    {
        return "\nEngine : \n-carburation: " . $this->getCarburation() . "\n-power: " . $this->getPower();
    }


    public function jsonSerialize(): array
    {
        $json['carburation'] = $this->getCarburation();
        $json['power'] =  $this->getPower();

        return $json;
    }

    public static function fromJson($json)
    {
        $decode = json_decode($json, true);
        return new Engine($decode["carburation"], $decode["power"]);
    }
}