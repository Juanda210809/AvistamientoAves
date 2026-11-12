#!/usr/bin/env bash
# Ejecuta la misma secuencia de peticiones contra la API y muestra código + cuerpo.
B=http://localhost:8080/avistamientos
H="Content-Type: application/json"
r() { echo "\$ $*"; out=$(curl -s -w '\nHTTP %{http_code}' "$@"); echo "$out"; echo; }

r -X POST $B -H "$H" -d '{"especie":"colibri","lugar":"Chingaza","fecha":"2026-09-29","observador":"Ana"}'
r -X POST $B -H "$H" -d '{"especie":"aguila","lugar":"Sumapaz","fecha":"2026-10-01","observador":"Luis"}'
r -X POST $B -H "$H" -d '{"especie":"ab","lugar":"Chingaza","fecha":"2026-09-29","observador":"Ana"}'
r -X POST $B -H "$H" -d '{"especie":"colibri","lugar":"Chingaza","fecha":"2099-01-01","observador":"Ana"}'
r $B
r $B/1
r $B/999
r -X PUT $B/1 -H "$H" -d '{"especie":"colibri","lugar":"Sumapaz","fecha":"2026-09-30","observador":"Ana"}'
r -X PUT $B/999 -H "$H" -d '{"especie":"colibri","lugar":"Sumapaz","fecha":"2026-09-30","observador":"Ana"}'
r -X PUT $B/1 -H "$H" -d '{"especie":"ab","lugar":"Sumapaz","fecha":"2026-09-30","observador":"Ana"}'
r $B/resumen
r -X DELETE $B/2
r -X DELETE $B/2
r $B
