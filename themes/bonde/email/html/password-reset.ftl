<#import "template.ftl" as layout>
<@layout.emailLayout>
    <h1>Redefinir Sua Senha</h1>
    
    <p>Você solicitou para redefinir sua senha.</p>
    
    <p>Clique no botão abaixo para definir uma nova senha:</p>
    
    <p style="text-align: center; margin: 30px 0;">
        <a href="${link}" class="cta-button">Redefinir Senha</a>
    </p>
    
    <div class="security-note">
        <strong>Atenção:</strong> Este link é válido por ${linkExpirationFormatter(linkExpiration)}. Se você não solicitou esta redefinição, ignore este e-mail.
    </div>
</@layout.emailLayout>