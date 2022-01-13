#!/usr/bin/env bash
set -euo pipefail

curl -X POST localhost:8080/developer -d '{ "id": "e30b6082-7d1f-44e2-bef6-ecbf185ce74b", "firstName": "developer 1", "lastName": "developer 1" }' -H 'Content-Type: application/json'
curl -X POST localhost:8080/developer -d '{ "id": "d8367699-bee3-4331-8aae-2d84a3ef419e", "firstName": "developer 2", "lastName": "developer 2" }' -H 'Content-Type: application/json'

curl -X POST localhost:8080/project -d '{ "id": "f662abc2-0e73-49cd-b005-0f516314909b", "name": "project 1", "start": "2020-10-31" }' -H 'Content-Type: application/json'
curl -X POST localhost:8080/project -d '{ "id": "c73b7b2a-3ab8-4955-91aa-320b8a16d681", "name": "project 2", "start": "2021-01-01" }' -H 'Content-Type: application/json'
curl -X POST localhost:8080/project -d '{ "id": "a84d4c00-0e99-4b79-b63c-8c0b9a900634", "name": "project 3", "start": "2022-01-01" }' -H 'Content-Type: application/json'

curl -X GET localhost:8080/developers | jq .
curl -X GET localhost:8080/projects | jq .

curl -X POST localhost:8080/developer_project -d '{ "developerId": "e30b6082-7d1f-44e2-bef6-ecbf185ce74b", "projectId": "f662abc2-0e73-49cd-b005-0f516314909b", "role": "BACKEND", "created": "2020-10-25" }' -H 'Content-Type: application/json'
curl -X POST localhost:8080/developer_project -d '{ "developerId": "e30b6082-7d1f-44e2-bef6-ecbf185ce74b", "projectId": "c73b7b2a-3ab8-4955-91aa-320b8a16d681", "role": "FRONTEND", "created": "2020-10-25" }' -H 'Content-Type: application/json'

curl -X GET localhost:8080/developers | jq .
curl -X GET localhost:8080/projects | jq .
