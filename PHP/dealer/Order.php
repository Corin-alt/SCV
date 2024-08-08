<?php


class Order implements JsonSerializable
{

    private int $num_order;
    private string $client;
    private string $date;
    private Car $car;

    public function __construct(int $num_order, string $client, string $date, Car $car)
    {
        $this->num_order = $num_order;
        $this->client = $client;
        $this->date = $date;
        $this->car = $car;
    }

    public function getNumOrder(): int
    {
        return $this->num_order;
    }


    public function getClient(): string
    {
        return $this->client;
    }


    public function getDate(): string
    {
        return $this->date;
    }

    public function getCar(): Car
    {
        return $this->car;
    }

    public function jsonSerialize(): array
    {
        $json['num_order'] = $this->getNumOrder();
        $json['client'] = $this->getClient();
        $json['car'] = $this->getCar();
        $json['date'] = $this->getDate();

        return $json;
    }

    public static function fromJson($json)
    {
        $decode = json_decode($json, true);
        $car = Car::fromJson(json_encode($decode["car"]));

        return new Order($decode["num_order"], $decode["client"], $decode["date"], $car);
    }
}