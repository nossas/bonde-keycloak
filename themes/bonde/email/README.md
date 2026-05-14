# Template de E-mails Bonde

Este diretório contém os templates customizados de e-mail para o sistema Keycloak da Bonde.

## Estrutura

- **template.ftl** - Template base com layout padrão (logo, header, footer, responsividade)
- **html/** - Templates HTML para diferentes tipos de e-mail
  - `magic-link-email.tfl` - E-mail com link de acesso rápido
  - `password-reset.tfl` - E-mail para redefinição de senha
- **text/** - Versões em texto simples dos templates (fallback)
  - `magic-link-email.tfl`
  - `password-reset.tfl`
- **messages/** - Mensagens traduzidas (configurações de texto)
  - `messages_pt.properties` - Mensagens em português

## Características do Template

### Design
- ✅ Layout centralizado e responsivo
- ✅ CSS inline (compatível com clientes de e-mail)
- ✅ Logo da Bonde no header
- ✅ Cores corporativas (laranja #FF6B35)
- ✅ Suporte mobile (max-width: 600px)

### Componentes
- Header com logo
- Conteúdo centralizado
- Botão CTA (Call-to-Action) com hover
- Aviso de segurança destacado
- Footer com links e informações

### Variáveis Disponíveis

#### Magic Link Email
- `${realmName}` - Nome do realm/plataforma
- `${magicLink}` - URL do link de acesso

#### Password Reset Email
- `${realmName}` - Nome do realm/plataforma
- `${link}` - URL do link de reset
- `${linkExpirationFormatter(linkExpiration)}` - Tempo de expiração formatado

## Personalização

Para adicionar novos templates:

1. Crie o arquivo HTML em `html/novo-email.tfl`
2. Crie a versão texto em `text/novo-email.tfl`
3. Importe o template base: `<#import "template.ftl" as layout>`
4. Use a macro: `<@layout.emailLayout>` seu conteúdo `</@layout.emailLayout>`
5. Adicione mensagens em `messages/messages_pt.properties` se necessário

## Boas Práticas

- Sempre forneça uma versão em texto simples
- Use cores que contrastam bem (#333333 para texto escuro)
- Mantenha links com a cor corporativa (#FF6B35)
- Inclua informações de segurança quando apropriado
- Teste em diferentes clientes de e-mail
- Máximo de 600px de largura para boa visualização

## Cores Corporativas Bonde

- Laranja Principal: `#FF6B35`
- Laranja Hover: `#E55A2B`
- Texto Escuro: `#333333`
- Texto Cinza: `#666666` e `#999999`
- Fundo: `#f5f5f5`
- Aviso: `#fff3cd` (fundo), `#ffc107` (borda), `#856404` (texto)

## Site

https://bonde.org
