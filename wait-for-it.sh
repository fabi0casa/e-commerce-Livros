#!/usr/bin/env bash

host="$1"
port="$2"
shift 2
cmd="$@"

echo "Aguardando o serviço $host:$port..."

while ! nc -z "$host" "$port"; do
  sleep 2
done

echo "$host:$port está pronto! Executando a aplicação..."
exec $cmd

