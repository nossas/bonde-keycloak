<#import "template.ftl" as layout>
<@layout.emailLayout>
    <h1>Confirme Seu E-mail</h1>
    
    <p>Obrigado por criar sua conta!</p>
    
    <p>Clique no botão abaixo para confirmar seu e-mail e completar seu cadastro:</p>
    
    <p style="text-align: center; margin: 30px 0;">
        <a href="${link}" class="cta-button">Confirmar E-mail</a>
    </p>
    
    <p>Se você não criou esta conta, ignore este e-mail.</p>
</@layout.emailLayout>