#!/bin/bash

# Script para disparar Magic Link no Keycloak
# Uso: ./send-magic-link.sh <USER_ID>
# Exemplo: ./send-magic-link.sh c9798161-d931-48f7-b5f7-4bb9864ff67b

if [ -z "$1" ]; then
    echo "❌ Erro: User ID é obrigatório"
    echo "Uso: ./send-magic-link.sh <USER_ID>"
    echo ""
    echo "Exemplo: ./send-magic-link.sh c9798161-d931-48f7-b5f7-4bb9864ff67b"
    exit 1
fi

USER_ID=$1

echo "🔐 Obtendo token de autenticação..."
TOKEN_RESPONSE_FILE=$(mktemp)
TOKEN_HTTP_CODE=$(curl -sS -o "$TOKEN_RESPONSE_FILE" -w "%{http_code}" -X POST http://localhost:8181/realms/master/protocol/openid-connect/token \
  -d 'client_id=admin-cli' \
  -d 'username=admin' \
  -d 'password=admin' \
  -d 'grant_type=password')

if [ "$TOKEN_HTTP_CODE" -ne 200 ]; then
    echo "❌ Erro ao obter token (HTTP $TOKEN_HTTP_CODE)"
    echo "📬 Resposta do servidor:"
    cat "$TOKEN_RESPONSE_FILE"
    rm -f "$TOKEN_RESPONSE_FILE"
    exit 1
fi

TOKEN=$(sed -n 's/.*"access_token":"\([^"]*\)".*/\1/p' "$TOKEN_RESPONSE_FILE")
rm -f "$TOKEN_RESPONSE_FILE"

if [ -z "$TOKEN" ]; then
    echo "❌ Erro: Não foi possível obter o token"
    exit 1
fi

echo "✅ Token obtido com sucesso"
echo ""
echo "📧 Disparando Magic Link (VERIFY_EMAIL) para usuário: $USER_ID"
echo ""
ACTION_RESPONSE_FILE=$(mktemp)
ACTION_HTTP_CODE=$(curl -sS -o "$ACTION_RESPONSE_FILE" -w "%{http_code}" -X PUT \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  "http://localhost:8181/admin/realms/master/users/$USER_ID/execute-actions-email" \
  -d '["VERIFY_EMAIL"]')

RESPONSE=$(cat "$ACTION_RESPONSE_FILE")
rm -f "$ACTION_RESPONSE_FILE"

if [ -n "$RESPONSE" ]; then
    echo "📬 Resposta do servidor:"
    echo "$RESPONSE"
    echo ""
fi

if [ "$ACTION_HTTP_CODE" -eq 204 ] || [ "$ACTION_HTTP_CODE" -eq 200 ]; then
    echo "✅ Magic Link disparado com sucesso!"
    echo ""
    echo "📍 Próximos passos:"
    echo "1. Acesse http://localhost:8025 (MailHog)"
    echo "2. Verifique o email recebido"
    echo "3. Copie o link de verificação"
    echo "4. Teste no navegador"
else
    echo "❌ Falha ao disparar Magic Link (HTTP $ACTION_HTTP_CODE)"
    if [ "$ACTION_HTTP_CODE" -eq 404 ]; then
        echo "🔎 Endpoint, realm ou usuário não encontrado."
    elif [ "$ACTION_HTTP_CODE" -eq 401 ] || [ "$ACTION_HTTP_CODE" -eq 403 ]; then
        echo "🔒 Token inválido ou sem permissão para executar ação de e-mail."
    elif [ "$ACTION_HTTP_CODE" -eq 400 ]; then
        echo "🧾 Requisição inválida. Verifique payload e configurações de email do realm."
    fi
    echo "⚠️  Verifique a resposta do servidor acima para mais detalhes."
    exit 1
fi
