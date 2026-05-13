<#import "template.ftl" as layout>
<@layout.emailLayout>
    <h1>Acessar sua conta</h1>
    
    <p>Você recebeu um link para acessar sua conta no BONDE.</p>
    
    <p>Clique no botão abaixo para acessar sua conta agora:</p>
    
    <p style="text-align: center; margin: 30px 0;">
        <a href="${magicLink}" class="cta-button">Acessar minha conta BONDE</a>
    </p>
    
    <p>Se você não solicitou este acesso, ignore este e-mail. Este link é válido por 24 horas.</p>
</@layout.emailLayout>