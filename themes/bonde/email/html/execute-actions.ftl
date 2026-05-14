<#import "template.ftl" as layout>
<@layout.emailLayout>
    <h1>Atualizar sua conta</h1>
    
    <p>Sua conta precisa de atualização. Clique no botão abaixo para continuar:</p>
    
    <p style="text-align: center; margin: 30px 0;">
        <a href="${link}" class="cta-button">Atualizar Conta</a>
    </p>
    
    <p>Se você não solicitou essa ação, ignore este e-mail.</p>
</@layout.emailLayout>