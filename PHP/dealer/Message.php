<?php

class Message implements JsonSerializable
{

    private string $id;
    private bool $bool;
    private string $content;


    public function __construct(string $id, bool $bool, string $content)
    {
        $this->id = $id;
        $this->bool = $bool;
        $this->content = $content;
    }

    public function getId(): string
    {
        return $this->id;
    }

    public function getBool(): bool
    {
        return $this->bool;
    }


    public function getContent(): string
    {
        return $this->content;
    }


    public function jsonSerialize(): array
    {
        $json['id'] = $this->getId();
        $json['bool'] = $this->getBool();
        $json['content'] = $this->getContent();
        return $json;
    }

    public static function fromJson($json): Message
    {
        $decode = json_decode($json, true);
        return new Message($decode["id"], $decode["bool"], $decode["content"]);
    }
}